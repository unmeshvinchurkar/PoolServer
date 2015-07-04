package com.pool.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;

import com.pool.Point;
import com.pool.service.CarPoolService;
import com.pool.service.UserService;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.User;
import com.pool.spring.model.Vehicle;

@Path("/carpool")
public class CarPoolRestService {

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/test")
	public Response getMsg() {

		String output = "Jersey say : ";

		return Response.status(200).entity(output).build();

	}

	@POST
	// @Consumes("application/x-www-form-urlencoded")
	@Path("/signup")
	public Response signup(@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("repassword") String repassword,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("pincode") String pincode,
			@FormParam("email") String email, @FormParam("city") String city,
			@FormParam("state") String state,
			@FormParam("gender") String gender,
			@FormParam("address") String address,
			@FormParam("officeAddress") String officeAddress) {

		try {

			String vPassword = ESAPI.validator().getValidInput("password",
					password, "Password", 25, false);
			String vRepassword = ESAPI.validator().getValidInput("repassword",
					repassword, "Password", 25, false);
			String vUsername = ESAPI.validator().getValidInput("username",
					username, "UserName", 25, false);
			String vFirstName = ESAPI.validator().getValidInput("firstName",
					firstName, "Name", 20, false);
			String vLastName = ESAPI.validator().getValidInput("lastName",
					lastName, "Name", 20, false);
			String vEmail = ESAPI.validator().getValidInput("email", email,
					"Email", 100, false);
			String vAddress = ESAPI.validator().getValidInput("address",
					address, "SafeString", 200, false);
			String vOffAddress = ESAPI.validator().getValidInput(
					"officeAddress", officeAddress, "SafeString", 200, false);
			String vGender = ESAPI.validator().getValidInput("gender", gender,
					"Gender", 1, false);
			String vPin = ESAPI.validator().getValidInput("pincode", pincode,
					"Pincode", 6, false);
			String vState = ESAPI.validator().getValidInput("state", state,
					"Name", 20, false);
			String vCity = ESAPI.validator().getValidInput("state", state,
					"Name", 20, false);

			if (vPassword != null && vRepassword != null
					&& vRepassword.equals(vPassword)) {
				User usr = new User();
				usr.setUsername(vUsername);
				usr.setPasswd(vPassword);
				usr.setCity(vCity);
				usr.setEmail(vEmail);
				usr.setPin(Integer.valueOf(vPin));
				usr.setFirstName(vFirstName);
				usr.setLastName(vLastName);
				usr.setState(vState);
				usr.setGender(vGender);
				usr.setAddress(vAddress);
				usr.setOfficeAddress(vOffAddress);

				UserService service = new UserService();
				service.createUser(usr);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IntrusionException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/login")
	public Response login(@FormParam("username") String username,
			@FormParam("password") String password) {

		try {
			String vPassword = ESAPI.validator().getValidInput("password",
					password, "Password", 25, false);
			String vUsername = ESAPI.validator().getValidInput("username",
					username, "UserName", 25, false);

			UserService service = new UserService();
			User usr = service.getUser(vUsername, vPassword);
			if (usr != null) {
				HttpSession session = request.getSession(true);
				session.setAttribute("USER", usr);
			}

		} catch (ValidationException e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (IntrusionException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/logout")
	public Response logout() {
		HttpSession session = request.getSession(false);
		session.invalidate();
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

	@POST
	// @Consumes("application/x-www-form-urlencoded")
	@Path("/addVehicle")
	public Response addVehicle(@FormParam("vehicleNo") String vehicleNo,
			@FormParam("licenseNo") String licenseNo,
			@FormParam("vehicleType") String vehicleType) {

		_validateSession();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");

		Vehicle vh = new Vehicle();
		vh.setVehicleNo(vehicleNo);
		vh.setVehicleType(vehicleType);
		vh.setLicenseNo(licenseNo);
		vh.setOwnerId(user.getUserId());

		CarPoolService service = new CarPoolService();
		service.addVehicle(vh);
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
			service.subscribeToPool(carPoolId, travellerId);
		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/createpool")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createPool(@FormParam("points") String points,
			@FormParam("vehicleId") String vehicleId) {
		_validateSession();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("USER");

		List<Point> pointList = null;
		Carpool carPool = null;
		try {
			pointList = new ArrayList<Point>();
			JSONArray array = new JSONArray(points);
			for (int i = 0; i < array.length(); i++) {

				JSONObject jsonpoint = array.getJSONObject(i);
				Point point = new Point(jsonpoint.getString("lattitude"),
						jsonpoint.getString("longitude"));
				pointList.add(point);
			}

			CarPoolService service = new CarPoolService();

			if (vehicleId == null) {
				Vehicle vh = service.getVehicleByOwnerId(user.getUserId());
				vehicleId = vh.getVehicleId();
			}

			carPool = service.createCarPool(user.getUserId().toString(),
					vehicleId, pointList);
		} catch (JSONException e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/search")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findNearestPools(@FormParam("srcLat") String srcLat,
			@FormParam("srcLng") String srcLng,
			@FormParam("destLat") String destLat,
			@FormParam("destLng") String destLng) {

		_validateSession();

		Point srcPoint = new Point(srcLat, srcLng);
		Point destPoint = new Point(destLat, destLng);

		CarPoolService service = new CarPoolService();
		List<Long> poolIds = service.findNearestPools(srcPoint, destPoint);

		return Response.status(Response.Status.OK).entity(poolIds).build();
	}

	private void _validateSession() {
		HttpSession session = request.getSession(false);

		if (session == null) {
			throw new UserSessionException("User not loggedin");
		}
	}

}