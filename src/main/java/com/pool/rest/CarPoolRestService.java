package com.pool.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.esapi.errors.AuthenticationCredentialsException;

import nl.captcha.Captcha;

import com.pool.DistanceComparator;
import com.pool.Point;
import com.pool.PoolConstants;
import com.pool.esapi.EsapiUtils;
import com.pool.esapi.FieldValidationException;
import com.pool.esapi.IntrusionDetectedException;
import com.pool.esapi.Validator;
import com.pool.service.CarPoolService;
import com.pool.service.NotificationService;
import com.pool.service.UserService;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.GeoPoint;
import com.pool.spring.model.Notification;
import com.pool.spring.model.PoolCalendarDay;
import com.pool.spring.model.PoolSubscription;
import com.pool.spring.model.Request;
import com.pool.spring.model.User;
import com.pool.spring.model.UserCalendarDay;
import com.pool.spring.model.Vehicle;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

//import com.sun.jersey.core.header.FormDataContentDisposition;

@Path("/carpool")
public class CarPoolRestService {

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/getSentRequests")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSentRequests() {
		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		NotificationService service = new NotificationService();

		List sentRequestList = service.getSentRequests(user.getUserId());
		List recvRequestList = service.getReceivedRequests(user.getUserId());

		JSONArray sentRequests = new JSONArray();
		JSONArray recvRequests = new JSONArray();

		if (sentRequestList != null) {
			for (Object obj : sentRequestList) {
				Map map = (Map) obj;
				JSONObject jsonObj = new JSONObject();

				for (Object key : map.keySet()) {
					try {
						jsonObj.put((String) key, map.get(key));
					} catch (JSONException e) {
					}
				}
				sentRequests.put(jsonObj);
			}
		}

		if (recvRequestList != null) {
			for (Object obj : recvRequestList) {
				Map map = (Map) obj;
				JSONObject jsonObj = new JSONObject();

				for (Object key : map.keySet()) {
					try {
						jsonObj.put((String) key, map.get(key));
					} catch (JSONException e) {
					}
				}
				recvRequests.put(jsonObj);
			}
		}

		JSONObject responseData = new JSONObject();
		try {
			responseData.put("receivedRequests", recvRequests);
			responseData.put("sentRequests", sentRequests);
		} catch (JSONException e) {
		}

		return Response.status(Response.Status.OK)
				.entity(responseData.toString()).build();
	}

	@GET
	@Path("/getReceivedRequests")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReceivedRequests() {
		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");

		NotificationService service = new NotificationService();

		List requestList = service.getReceivedRequests(user.getUserId());
		JSONArray requests = new JSONArray();

		if (requestList != null) {
			for (Object req : requestList) {
				requests.put(new JSONObject(req));
			}
		}
		return Response.status(Response.Status.OK).entity(requests).build();
	}

	@GET
	@Path("/getNotifications")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getNotifications() {
		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		NotificationService service = new NotificationService();

		List notifications = service.fetchNotifications(user.getUserId());
		JSONArray arr = new JSONArray();

		if (notifications != null) {
			for (Object obj : notifications) {
				Map map = (Map) obj;
				JSONObject jsonObj = new JSONObject();

				for (Object key : map.keySet()) {
					try {
						jsonObj.put((String) key, map.get(key));
					} catch (JSONException e) {
					}
				}
				arr.put(jsonObj);
			}
		}

		return Response.status(Response.Status.OK).entity(arr.toString())
				.build();
	}

	@POST
	@Path("/raiseJoinRequest")
	public Response raiseJoinRequest(@FormParam("carPoolId") String carPoolId,
			@FormParam("srcLattitude") String srcLattitude,
			@FormParam("srcLongitude") String srcLongitude,
			@FormParam("destLattitude") String destLattitude,
			@FormParam("destLongitude") String destLongitude,
			@FormParam("tripCost") String tripCost,
			@FormParam("pickupTime") String pickupTime,
			@FormParam("pickupDistance") String pickupDistance) {

		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		CarPoolService service = new CarPoolService();
		Carpool carPool = service.findPoolById(carPoolId);

		Request request = new Request();
		request.setToUserId(carPool.getOwnerId());
		request.setFromUserId(user.getUserId());
		request.setCarPoolId(Long.parseLong(carPoolId));
		request.setCreateDate(new Date().getTime() / 1000);
		request.setRequestTypeId(PoolConstants.REQUEST_JOIN_POOL_REQUEST_ID);
		request.setSrcLattitude(Double.parseDouble(srcLattitude));
		request.setSrcLongitude(Double.parseDouble(srcLongitude));
		request.setDestLattitude(Double.parseDouble(destLattitude));
		request.setDestLongitude(Double.parseDouble(destLongitude));
		request.setTripCost(Float.valueOf(tripCost));
		request.setPickupDistance(Float.valueOf(pickupDistance));
		request.setStartTime(Long.valueOf(pickupTime));
		service.saveOrUpdate(request);

		Notification note = new Notification();
		note.setCarPoolId(request.getCarPoolId());
		note.setCreateDate((new Date()).getTime() / 1000);
		note.setFromUserId(user.getUserId());
		note.setToUserId(carPool.getOwnerId());
		note.setNotificationTypeId(PoolConstants.NOTI_JOIN_REQUEST_RECEIVED_ID);
		note.setRequestId(request.getRequestId());
		service.saveOrUpdate(note);
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/acceptJoinRequest")
	public Response acceptJoinRequest(@FormParam("requestId") String requestId) {
		_validateSession();
		CarPoolService cService = new CarPoolService();
		NotificationService service = new NotificationService();

		Request req = service.fetchRequestById(Long.parseLong(requestId));
		req.setSeen(1);
		req.setProcessed(1);
		req.setStatus(1);

		Carpool carPool = cService.findPoolById(req.getCarPoolId().toString());

		Notification note = new Notification();
		note.setCarPoolId(req.getCarPoolId());
		note.setCreateDate((new Date()).getTime() / 1000);
		note.setFromUserId(req.getToUserId());
		note.setNotificationTypeId(PoolConstants.NOTI_JOIN_REQUEST_ACCEPTED_ID);
		note.setRequestId(req.getRequestId());
		service.saveObjects(req, note);

		PoolSubscription subs = new PoolSubscription();
		subs.setCarPoolId(req.getCarPoolId());
		subs.setTravellerId(req.getFromUserId());
		subs.setPickupLattitude(req.getSrcLattitude());
		subs.setPickupLongitute(req.getSrcLongitude());
		subs.setPickupTime(req.getStartTime());
		subs.setDestLattitude(req.getDestLattitude());
		subs.setDestLongitude(req.getDestLongitude());
		subs.setTripCost(req.getTripCost());
		subs.setPickupDistance(req.getPickupDistance());

		carPool.setNoOfRemainingSeats(carPool.getNoOfRemainingSeats() - 1);
		service.saveObjects(subs, carPool);
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/rejectJoinRequest")
	public Response rejectJoinRequest(@FormParam("requestId") String requestId) {
		_validateSession();
		NotificationService service = new NotificationService();
		Request req = service.fetchRequestById(Long.parseLong(requestId));
		req.setSeen(1);
		req.setProcessed(1);
		req.setStatus(0);

		Notification note = new Notification();
		note.setCarPoolId(req.getCarPoolId());
		note.setCreateDate((new Date()).getTime() / 1000);
		note.setFromUserId(req.getToUserId());
		note.setNotificationTypeId(PoolConstants.NOTI_JOIN_REQUEST_REJECTED_ID);
		note.setRequestId(req.getRequestId());
		note.setToUserId(req.getFromUserId());
		service.saveObjects(req, note);

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/leavePool/{carPoolId}")
	public Response leavePool(@PathParam("carPoolId") String carPoolId) {
		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");

		CarPoolService service = new CarPoolService();
		NotificationService nService = new NotificationService();

		Carpool carPool = service.findPoolById(carPoolId);
		nService.removeTraveller(user.getUserId(), Long.parseLong(carPoolId));
		Notification note = new Notification();
		note.setCarPoolId(Long.parseLong(carPoolId));
		note.setCreateDate((new Date()).getTime() / 1000);
		note.setFromUserId(user.getUserId());
		note.setNotificationTypeId(PoolConstants.NOTI_TRAVELLER_OPTED_OUT_ID);
		note.setToUserId(carPool.getOwnerId());

		carPool.setNoOfRemainingSeats(carPool.getNoOfRemainingSeats() + 1);
		nService.saveObjects(carPool, note);

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/removeTraveller")
	public Response removeTraveller(@FormParam("carPoolId") String carPoolId,
			@FormParam("travellerId") String travellerId) {

		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");

		NotificationService service = new NotificationService();
		CarPoolService cService = new CarPoolService();

		Carpool carPool = cService.findPoolById(carPoolId);

		service.removeTraveller(Long.parseLong(travellerId),
				Long.parseLong(carPoolId));
		Notification note = new Notification();
		note.setCarPoolId(Long.parseLong(carPoolId));
		note.setCreateDate((new Date()).getTime() / 1000);
		note.setFromUserId(user.getUserId());
		note.setNotificationTypeId(PoolConstants.NOTI_TRAVELLER_REMOVED_ID);
		note.setToUserId(Long.parseLong(travellerId));
		carPool.setNoOfRemainingSeats(carPool.getNoOfRemainingSeats() + 1);
		service.saveObjects(carPool, note);

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/changePassword")
	public Response changePassword(
			@FormParam("oldPassword") String oldPassword,
			@FormParam("newPassword") String newPassword) {

		_validateSession();
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute("USER");
		CarPoolService service = new CarPoolService();

		try {

			if (user.getPasswd().equals(oldPassword)) {
				EsapiUtils.verifyPasswordStrength(newPassword,
						user.getUsername());

				user.setPasswd(newPassword);
				service.saveOrUpdate(user);
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (AuthenticationCredentialsException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/getCalendar")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCalendar(@QueryParam("carPoolId") String carPoolId,
			@QueryParam("year") String year, @QueryParam("month") String month) {

		_validateSession();
		HttpSession session = request.getSession(false);

		CarPoolService service = new CarPoolService();
		User user = (User) session.getAttribute("USER");
		Long userId = user.getUserId();

		boolean isOwner = service.isOwner(userId, Long.valueOf(carPoolId));
		Integer yearInt = Integer.parseInt(year);
		Integer monthInt = Integer.parseInt(month);

		List<UserCalendarDay> userHolidays = null;
		List<PoolCalendarDay> poolHolidays = service.getPoolHolidays(
				Long.valueOf(carPoolId), yearInt, monthInt);
		userHolidays = service.getUserHolidays(userId, Long.valueOf(carPoolId),
				yearInt, monthInt);

		JSONArray poolHolidaysArray = new JSONArray();
		JSONArray userHolidaysArray = new JSONArray();
		try {
			if (poolHolidays != null) {
				int i = 0;
				for (PoolCalendarDay day : poolHolidays) {

					JSONObject obj = new JSONObject();
					obj.put("date", day.getDate());
					obj.put("isHoliday", day.getIsHoliday());
					obj.put("carPoolId", day.getCarPoolId());

					poolHolidaysArray.put(i++, obj);
				}
			}

			if (userHolidays != null) {
				int i = 0;
				for (UserCalendarDay day : userHolidays) {

					JSONObject obj = new JSONObject();
					obj.put("date", day.getCalendarDay());
					obj.put("userId", day.getUserId());
					obj.put("carPoolId", day.getCarPoolId());

					userHolidaysArray.put(i++, obj);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JSONObject map = null;
		try {
			map = new JSONObject();
			map.put("isOwner", isOwner);
			map.put("currentUserId", user.getUserId());
			map.put("userHolidays", userHolidaysArray);
			map.put("poolHolidays", poolHolidaysArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return Response.status(Response.Status.OK).entity(map.toString())
				.build();
	}

	@POST
	@Path("/markHoliday")
	public Response markHoliday(@FormParam("carPoolId") String carPoolId,
			@FormParam("timeInSec") String timeInSec) {

		_validateSession();
		HttpSession session = request.getSession(false);

		CarPoolService service = new CarPoolService();
		User user = (User) session.getAttribute("USER");
		Long userId = user.getUserId();

		service.markHoliday(userId, Long.valueOf(carPoolId),
				Long.valueOf(timeInSec));

		Carpool carPool = service.findPoolById(carPoolId);
		boolean isOwner = carPool.getOwnerId().equals(user.getUserId());

		if (!isOwner) {
			Notification note = new Notification();
			note.setCarPoolId(carPool.getCarPoolId());
			note.setCreateDate((new Date()).getTime() / 1000);
			note.setFromUserId(user.getUserId());
			note.setToUserId(carPool.getOwnerId());
			note.setNotificationTypeId(PoolConstants.NOTI_TRAVELLER_HOLIDAY_ID);
			note.setHolidayDate(Long.valueOf(timeInSec));
			service.saveOrUpdate(note);
		} else {

			List travellers = service.fetchSubscribedTravellers(carPool
					.getCarPoolId());

			if (travellers != null) {
				for (Object travellerId : travellers) {
					Notification note = new Notification();
					note.setCarPoolId(carPool.getCarPoolId());
					note.setCreateDate((new Date()).getTime() / 1000);
					note.setFromUserId(user.getUserId());
					note.setToUserId((Long) travellerId);
					note.setNotificationTypeId(PoolConstants.NOTI_POOL_HOLIDAY_ID);
					note.setHolidayDate(Long.valueOf(timeInSec));
					service.saveOrUpdate(note);
				}
			}
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/unMarkHoliday")
	public Response unMarkHoliday(@FormParam("carPoolId") String carPoolId,
			@FormParam("timeInSec") String timeInSec) {

		_validateSession();
		HttpSession session = request.getSession(false);

		CarPoolService service = new CarPoolService();
		User user = (User) session.getAttribute("USER");
		Long userId = user.getUserId();

		service.unMarkHoliday(userId, Long.valueOf(carPoolId),
				Long.valueOf(timeInSec));

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/edituser")
	public Response editUser(@FormParam("state") String state,
			@FormParam("streetAddress") String streetAddress,
			@FormParam("city") String city, @FormParam("pin") String pin,
			@FormParam("country") String country,
			@FormParam("contactNo") String contactNo) {

		try {
			streetAddress = Validator.validateString("streetAddress",
					streetAddress);
			state = Validator.validateName("state", state);
			city = Validator.validateName("city", city);
			pin = Validator.validatePin("pin", pin);
			Long.parseLong(contactNo);

			_validateSession();
			HttpSession session = request.getSession(false);

			CarPoolService service = new CarPoolService();
			User usr = (User) session.getAttribute("USER");

			usr.setCity(city);
			usr.setPin(Integer.valueOf(pin));
			usr.setState(state);
			usr.setAddress(streetAddress);
			usr.setCountry(country);
			usr.setContactNo(contactNo);
			service.saveOrUpdate(usr);

		} catch (FieldValidationException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		} catch (IntrusionDetectedException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN)
					.entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/signup")
	public Response signup(@FormParam("username") String username,

	@FormParam("password") String password,

	@FormParam("firstName") String firstName,

	@FormParam("lastName") String lastName,

	@FormParam("email") String email, @FormParam("state") String state,

	@FormParam("gender") String gender,

	@FormParam("streetAddress") String streetAddress,

	@FormParam("city") String city, @FormParam("pin") String pin,

	@FormParam("country") String country, @FormParam("answer") String answer,

	@FormParam("birthDate") String birthDate) {

		Captcha captcha = (Captcha) request.getSession().getAttribute(
				Captcha.NAME);

		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}

		if (!captcha.isCorrect(answer)) {

			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity("Incorrect capcha").build();
		}

		try {
			
			username = username.trim();
			lastName = lastName.trim();
			email = email.trim();
			
			EsapiUtils.verifyPasswordStrength(password, username);

			username = Validator.validateEmail("username", username);
			firstName = Validator.validateName("firstName", firstName);
			lastName = Validator.validateName("lastName", lastName);
			email = Validator.validateEmail("email", email);
			streetAddress = Validator.validateString("streetAddress",
					streetAddress);
			state = Validator.validateName("state", state);
			city = Validator.validateName("city", city);
			gender = Validator.validateGender("gender", gender);
			answer = Validator.validateString("answer", answer);
			pin = Validator.validatePin("pin", pin);

			User usr = new User();
			usr.setUsername(username);
			usr.setPasswd(password);
			usr.setCity(city);
			usr.setEmail(email);
			usr.setPin(Integer.valueOf(pin));
			usr.setFirstName(firstName);
			usr.setLastName(lastName);
			usr.setState(state);
			usr.setGender(gender);
			usr.setAddress(streetAddress);
			usr.setPasswd(password);
			usr.setBirthDate(Long.valueOf(birthDate) / 1000);
			usr.setCountry(country);
			UserService service = new UserService();
			service.createUser(usr);

		} catch (FieldValidationException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		} catch (IntrusionDetectedException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN)
					.entity(e.getMessage()).build();
		} catch (AuthenticationCredentialsException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/login")
	public Response login(@FormParam("username") String username,
			@FormParam("password") String password) {

		try {

			System.out.println("********************* Login: " + username);
			System.out.println("********************* password: " + password);

			username = Validator.validateEmail("username", username);

			UserService service = new UserService();
			User usr = service.getUser(username, password);
			if (usr != null) {
				HttpSession session = request.getSession(true);
				session.setAttribute(PoolConstants.USER_SESSION_ATTR, usr);
			} else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		}

		catch (FieldValidationException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		} catch (IntrusionDetectedException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN)
					.entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/logout")
	public Response logout() {

		_validateSession();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/deletepool/{poolId}")
	public Response deletePool(@PathParam("poolId") String poolId) {
		_validateSession();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		CarPoolService service = new CarPoolService();
		service.deletePool(poolId);

		List travellers = service.fetchSubscribedTravellers(Long
				.valueOf(poolId));

		if (travellers != null) {
			for (Object travellerId : travellers) {
				Notification note = new Notification();
				note.setCarPoolId(Long.valueOf(poolId));
				note.setCreateDate((new Date()).getTime() / 1000);
				note.setFromUserId(user.getUserId());
				note.setToUserId((Long) travellerId);
				note.setNotificationTypeId(PoolConstants.NOTI_POOL_DISSOLVED_ID);
				service.saveOrUpdate(note);
			}
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/remove")
	public Response removeUser() {
		_validateSession();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		UserService service = new UserService();
		service.removeUser(user);
		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/getVehicle")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getVehicle() {
		_validateSession();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		UserService service = new UserService();
		Vehicle v = service.getVehicle(user.getUserId());

		if (v != null) {
			JSONObject jsonObj = new JSONObject(v);
			v.setDrivingLicense(user.getDrivingLicense());
			try {
				jsonObj.put("drivingLicense", user.getDrivingLicense());
			} catch (JSONException e) {
			}
			return Response.status(Response.Status.OK)
					.entity(jsonObj.toString()).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();

		}
	}

	@POST
	@Path("/addVehicle")
	public Response addVehicle(@FormParam("manufacturer") String manufacturer,
			@FormParam("model") String model,
			@FormParam("fuelType") String fuelType,
			@FormParam("color") String color,
			@FormParam("registrationNumber") String registrationNumber,
			@FormParam("drivingLicense") String drivingLicense) {

		_validateSession();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");

		UserService service = new UserService();
		Vehicle vh = service.getVehicle(user.getUserId());

		if (vh == null) {
			vh = new Vehicle();
		}

		vh.setRegistrationNo(registrationNumber);
		vh.setOwnerId(user.getUserId());
		vh.setDrivingLicense(drivingLicense);
		vh.setManufacturer(manufacturer);
		vh.setModel(model);
		vh.setFuelType(fuelType);
		vh.setColor(color);

		user.setDrivingLicense(drivingLicense);
		CarPoolService carService = new CarPoolService();

		carService.saveOrUpdate(user);
		carService.saveOrUpdate(vh);

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/subscribepool")
	public Response subscribeToPool(
			@FormParam("travellerId") String travellerId,
			@FormParam("carPoolId") String carPoolId) {
		try {
			_validateSession();
			CarPoolService service = new CarPoolService();
			service.subscribeToPool(Long.parseLong(carPoolId),
					Long.parseLong(travellerId));
		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/createPool")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createOrUpdatePool(
			@FormParam("carPoolId") String carPoolId,
			@FormParam("route") String route,
			@FormParam("vehicleId") String vehicleId,
			@FormParam("startDate") String startDateInMilis,
			@FormParam("endDate") String endDateInMilis,
			@FormParam("startTime") String startTimeInSec,
			@FormParam("srcArea") String srcArea,
			@FormParam("destArea") String destArea,
			@FormParam("totalSeats") String totalSeats,
			@FormParam("bucksPerKm") String bucksPerKm,
			@FormParam("excludeWeekend") String excludeWeekend,
			@FormParam("oddEven") String oddEven) {
		_validateSession();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");
		CarPoolService service = new CarPoolService();

		boolean doExcludeWeekend = (excludeWeekend != null && excludeWeekend
				.equals("true")) ? true : false;
		boolean isOddEven = (oddEven != null && oddEven.equals("true")) ? true
				: false;

		List<Point> pointList = null;
		Carpool carPool = null;
		try {

			pointList = route != null ? service.convertRouteToPoints(route,
					Long.parseLong(startTimeInSec)) : null;

			if (vehicleId == null) {
				Vehicle vh = service.getVehicleByOwnerId(user.getUserId());
				vehicleId = vh.getVehicleId().toString();
			}

			if (carPoolId == null) {
				carPool = new Carpool();
				carPool.setOwnerId(user.getUserId());
				carPool.setCarpoolName(user.getFirstName());
				carPool.setPath(pointList.toString());
				carPool.setStartDate(Long.valueOf(startDateInMilis) / 1000);
				carPool.setEndDate(Long.valueOf(endDateInMilis) / 1000);
				carPool.setStartTime(Long.parseLong(startTimeInSec));
				carPool.setVehicleId(vehicleId);
				carPool.setSrcArea(srcArea);
				carPool.setDestArea(destArea);
				carPool.setNoOfAvblSeats(Integer.valueOf(totalSeats));
				carPool.setNoOfRemainingSeats(Integer.valueOf(totalSeats));
				carPool.setBucksPerKm(Integer.valueOf(bucksPerKm));

				carPool = service.createCarPool(carPool, pointList,
						doExcludeWeekend, isOddEven);

				System.err.println("############### Src area: " + srcArea);
				System.err.println("############### Dest area: " + destArea);
			} else {
				carPool = service.findPoolById(carPoolId);
				carPool.setSrcArea(srcArea);
				carPool.setDestArea(destArea);
				carPool.setVehicleId(vehicleId);
				carPool.setPath(pointList.toString());
				carPool.setStartDate(Long.valueOf(startDateInMilis) / 1000);
				carPool.setEndDate(Long.valueOf(endDateInMilis) / 1000);
				carPool.setStartTime(Long.parseLong(startTimeInSec));

				if (pointList != null && pointList.size() > 0) {
					carPool.setDestLattitude(pointList
							.get(pointList.size() - 1).getLattitude());
					carPool.setDestLongitude(pointList
							.get(pointList.size() - 1).getLongitude());
					carPool.setSrcLattitude(pointList.get(0).getLattitude());
					carPool.setSrcLongitude(pointList.get(0).getLongitude());
				}

				service.updatePool(carPool, pointList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}

		System.err.println(carPool.getCarPoolId());
		return Response.status(Response.Status.OK)
				.entity(carPool.getCarPoolId()).build();
	}

	@GET
	@Path("/getCurrentUserDetails")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCurrentUserDetails() {
		_validateSession();

		HttpSession session = request.getSession(false);
		User usr = (User) session.getAttribute("USER");

		return getUserDetails(usr.getUserId().toString());

	}

	@GET
	@Path("/getUserDetails/{userid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUserDetails(@PathParam("userid") String userid) {
		_validateSession();
		CarPoolService service = new CarPoolService();
		File profileImgFile = null;

		final User usr = service.fetchUserDetails(Long.valueOf(userid));

		usr.setCarpools(null);
		usr.setPasswd(null);
		usr.setVehicles(null);

		String path = request.getSession().getServletContext()
				.getRealPath("/images/");

		if (usr.getProfileImagePath() != null) {
			profileImgFile = new File(path + usr.getProfileImagePath());
		}

		JSONObject jsonObj = new JSONObject(usr);

		try {
			if (profileImgFile != null && profileImgFile.exists()) {
				jsonObj.put("profileImagePath", usr.getProfileImagePath());
			} else {
				jsonObj.remove("profileImagePath");
			}
		} catch (JSONException e) {
		}

		return Response.status(Response.Status.OK).entity(jsonObj.toString())
				.build();
	}

	@GET
	@Path("{poolId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPoolById(@PathParam("poolId") String poolId) {

		CarPoolService service = new CarPoolService();
		Carpool carPool = service.findPoolById(poolId);
		carPool.setCalendarDays(null);

		JSONObject jsonObj = new JSONObject(carPool);
		JSONArray array = new JSONArray();

		List subDetails = service.fetchSubscribedTravellersDetails(carPool
				.getCarPoolId());

		if (subDetails != null) {
			for (Object obj : subDetails) {
				Map usrSubDetails = (Map) obj;

				if (!_checkProfileImage((String) usrSubDetails
						.get("profileImagePath"))) {
					usrSubDetails.remove("profileImagePath");
				}

				array.put(usrSubDetails);
			}
		}
		try {
			jsonObj.put("subscriptionDetails", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return Response.status(Response.Status.OK).entity(jsonObj.toString())
				.build();
	}

	private boolean _checkProfileImage(String profileImagePath) {
		String path = request.getSession().getServletContext()
				.getRealPath("/images/");
		File profileImgFile = new File(path + profileImagePath);
		return profileImgFile != null && profileImgFile.exists();
	}

	@GET
	@Path("/searchPools")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response searchNearestPools(@QueryParam("srcLat") String srcLat,
			@QueryParam("srcLng") String srcLng,
			@QueryParam("destLat") String destLat,
			@QueryParam("destLng") String destLng,
			@QueryParam("startTime") String startTime) {

		_validateSession();

		HttpSession session = request.getSession(false);
		User usr = (User) session.getAttribute("USER");

		Point srcPoint = new Point(Double.parseDouble(srcLat),
				Double.parseDouble(srcLng));
		Point destPoint = new Point(Double.parseDouble(destLat),
				Double.parseDouble(destLng));

		CarPoolService service = new CarPoolService();

		System.err.println("Before searching pools ***********************");

		Map<Long, GeoPoint> poolIdPointMap = service.findNearestPools(srcPoint,
				destPoint, Long.parseLong(startTime), usr.getUserId());

		if (poolIdPointMap != null) {

			List list = service.fetchPoolDetailsById(poolIdPointMap.keySet());

			NotificationService nService = new NotificationService();
			List poolIdsForSentReqs = nService.getPoolIdsForSentRequests(
					usr.getUserId(), poolIdPointMap.keySet());
			List subsPoolIds = nService.getSubscribedPoolIds(usr.getUserId());
			JSONArray array = new JSONArray();

			try {
				for (int i = 0; i < list.size(); i++) {
					Object result[] = (Object[]) list.get(i);

					Carpool pool = (Carpool) result[0];

					if (!subsPoolIds.contains(pool.getCarPoolId())) {
						User user = (User) result[1];
						user.setCarpools(null);
						user.setVehicles(null);
						pool.setCalendarDays(null);
						// pool.setGeoPoints(null);

						// Get the nearest drop point
						DistanceComparator destComparator = new DistanceComparator(
								destPoint.getLongitude(),
								destPoint.getLattitude());
						List<GeoPoint> allPoints = service
								.fetchGeoPointsByPoolId(pool.getCarPoolId());

						GeoPoint startPoint = allPoints.get(0);
						Collections.sort(allPoints, destComparator);
						GeoPoint dropPoint = allPoints.get(0);

						pool.setGeoPoints(null);

						JSONObject map = new JSONObject();
						JSONObject poolJson = new JSONObject(pool);
						GeoPoint geoPoint = poolIdPointMap.get(pool
								.getCarPoolId());

						poolJson.put("pickupTime", startTime);
						poolJson.put("pickupLattitude", geoPoint.getLatitude());
						poolJson.put("pickupLongitude", geoPoint.getLongitude());
						poolJson.put("dropLattitude", dropPoint.getLatitude());
						poolJson.put("dropLongitude", dropPoint.getLongitude());
						poolJson.put("pickupDistance", ((float) (geoPoint
								
								.getDistanceToReach() - startPoint
								.getDistanceToReach())) / 1000.00);
						poolJson.put(
								"tripCost",
								((float) (dropPoint.getDistanceToReach() - geoPoint
										.getDistanceToReach()) * pool
										.getBucksPerKm()) / 1000.0);

						if (poolIdsForSentReqs.contains(pool.getCarPoolId())) {
							poolJson.put("requestReceived", true);
						}

						map.put("owner", new JSONObject(user));
						map.put("carpool", poolJson);
						array.put(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.build();
			}

			return Response.status(Response.Status.OK).entity(array.toString())
					.build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@Path("/myPools")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getMyPools() {

		_validateSession();

		HttpSession session = request.getSession(false);
		User usr = (User) session.getAttribute(PoolConstants.USER_SESSION_ATTR);

		CarPoolService service = new CarPoolService();
		List<Carpool> carPools = service.findPoolsByUserId(usr.getUserId());
		JSONArray array = new JSONArray();
		List<Long> carPoolIds = new ArrayList<Long>();

		for (Carpool pool : carPools) {
			carPoolIds.add(pool.getCarPoolId());
		}

		Map<Long, PoolSubscription> subs = null;

		if (carPoolIds != null && carPoolIds.size() > 0) {
			subs = service.fetchTravellerSubscriptions(carPoolIds,
					usr.getUserId());

		}

		for (Carpool pool : carPools) {
			pool.setCalendarDays(null);

			JSONObject poolJson = new JSONObject(pool);
			array.put(poolJson);

			try {
				if (pool.getOwnerId().equals(usr.getUserId())) {
					poolJson.put("isOwner", true);
					poolJson.put("pickupTime", pool.getStartTime());
					poolJson.put("cost",
							service.getPerTripCollection(pool.getCarPoolId()));

				} else {
					PoolSubscription sub = subs.get(pool.getCarPoolId());
					poolJson.put("isOwner", false);
					poolJson.put("pickupTime", sub.getPickupTime());
					poolJson.put("cost", sub.getTripCost());
				}
			} catch (JSONException e) {
			}
		}
		return Response.status(Response.Status.OK).entity(array.toString())
				.build();
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		_validateSession();
		HttpSession session = request.getSession(false);
		User usr = (User) session.getAttribute("USER");

		CarPoolService service = new CarPoolService();

		String fileName = fileDetail.getFileName();
		String fileType = ".jpg";
		File profileImagePath = null;

		if (fileName.indexOf(".") > 0) {
			fileType = fileName.substring(fileName.indexOf("."));
		}

		String path = request.getSession().getServletContext()
				.getRealPath("/images/");
		String newFileName = usr.getUsername() + fileType;

		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}

		if (usr.getProfileImagePath() != null) {
			profileImagePath = new File(path + usr.getProfileImagePath());

			if (profileImagePath.exists()) {
				profileImagePath.delete();
			}
		}

		// save it
		_writeToFile(uploadedInputStream, path + newFileName);

		usr.setProfileImagePath(newFileName);

		service.saveOrUpdate(usr);

		String output = "File uploaded successfully";

		JSONObject json = new JSONObject();
		try {
			json.put("msg", output);
		} catch (JSONException e) {
		}

		return Response.status(200).entity(json.toString()).build();
	}

	// save uploaded file
	private void _writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	private void _validateSession() {
		HttpSession session = request.getSession(false);

		if (session == null) {
			throw new UserSessionException("User not loggedin");
		}
	}

}