package com.pool.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pool.DeltaLatLong;
import com.pool.DistanceComparator;
import com.pool.Point;
import com.pool.PoolUtils;
import com.pool.spring.SpringBeanProvider;
import com.pool.spring.dao.CarPoolDao;
import com.pool.spring.dao.VehicleDao;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.GeoPoint;
import com.pool.spring.model.PoolCalendarDay;
import com.pool.spring.model.PoolSubscription;
import com.pool.spring.model.User;
import com.pool.spring.model.UserCalendarDay;
import com.pool.spring.model.Vehicle;

public class CarPoolService {

	public Float getPerTripCollection(Long carPoolId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.getPerTripCollection(carPoolId);
	}

	public List fetchGeoPointsByPoolId(Long poolId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.fetchGeoPointsByPoolId(poolId);
	}

	public User fetchUserDetails(Long userId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.fetchUsersDetails(userId);
	}

	public Map<Long, PoolSubscription> fetchTravellerSubscriptions(
			Collection carPoolIds, Long travellerId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.fetchTravellerSubscriptions(carPoolIds, travellerId);
	}

	public List fetchSubscribedTravellersDetails(Long carPoolId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.fetchSubscribedTravellersDetails(carPoolId);
	}

	public List fetchSubscribedTravellers(Long carPoolId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.fetchSubscribedTravellers(carPoolId);
	}

	public List fetchPoolDetailsById(Collection<Long> carPoolIds) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return poolDao.fetchPoolDetailsById(carPoolIds);
	}

	public void unMarkHoliday(Long userId, Long carPoolId, Long timeInSec) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Carpool carpool = poolDao.findPoolById(carPoolId.toString());
		boolean isOwner = (carpool.getOwnerId().equals(userId)) ? true : false;

		if (isOwner) {
			PoolCalendarDay calDay = poolDao.fetchPoolCalendarDay(carPoolId,
					timeInSec);

			if (calDay == null) {
				calDay = new PoolCalendarDay();
			}
			calDay.setCarPool(carpool);
			calDay.setDate(timeInSec);
			calDay.setIsHoliday(0);
			poolDao.saveOrUpdate(calDay);

		} else {
			UserCalendarDay usrCalDay = poolDao.fetchUserCalendarDay(carPoolId,
					userId, timeInSec);

			if (usrCalDay != null) {
				poolDao.delete(usrCalDay);
			}
		}
	}

	public void markHoliday(Long userId, Long carPoolId, Long timeInSec) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Carpool carpool = poolDao.findPoolById(carPoolId.toString());
		boolean isOwner = (carpool.getOwnerId().equals(userId)) ? true : false;

		if (isOwner) {
			PoolCalendarDay calDay = poolDao.fetchPoolCalendarDay(carPoolId,
					timeInSec);

			if (calDay == null) {
				calDay = new PoolCalendarDay();
			}
			calDay.setCarPool(carpool);
			calDay.setDate(timeInSec);
			calDay.setIsHoliday(1);
			calDay.setNoOfAvblSeats(carpool.getNoOfAvblSeats());
			calDay.setNoOfTravellers(carpool.getNoOfAvblSeats());

			if (calDay.getPoolCalendarId() != null) {
				poolDao.saveOrUpdate(calDay);
			} else {
				poolDao.save(calDay);

			}

		} else {
			UserCalendarDay usrCalDay = poolDao.fetchUserCalendarDay(carPoolId,
					userId, timeInSec);

			if (usrCalDay == null) {
				usrCalDay = new UserCalendarDay();
			}

			usrCalDay.setCalendarDay(timeInSec);
			usrCalDay.setCarPoolId(carPoolId);
			usrCalDay.setUserId(userId);
			poolDao.saveOrUpdate(usrCalDay);
		}
	}

	public List<PoolCalendarDay> getPoolHolidays(Long carPoolId, Integer year,
			Integer month) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Calendar cal = Calendar.getInstance();

		int currentMonth = month - 1;
		int currentYear1 = year;
		int currentYear2 = (currentMonth == 11) ? (currentYear1 + 1)
				: currentYear1;
		int nextMonth = (currentMonth == 11) ? 1 : (currentMonth + 1);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		Long startTime = cal.getTimeInMillis() / 1000;

		cal.set(Calendar.YEAR, currentYear2);
		cal.set(Calendar.MONTH, nextMonth);

		Long endTime = cal.getTimeInMillis() / 1000;

		return poolDao.fetchPoolHolidays(carPoolId, startTime, endTime);
	}

	public List<UserCalendarDay> getUserHolidays(Long userId, Long carPoolId,
			Integer year, Integer month) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		Calendar cal = Calendar.getInstance();

		int currentMonth = month - 1;
		int currentYear1 = year;
		int currentYear2 = (currentMonth == 11) ? (currentYear1 + 1)
				: currentYear1;
		int nextMonth = (currentMonth == 11) ? 1 : (currentMonth + 1);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		cal.set(Calendar.DAY_OF_MONTH, 1);

		Long startTime = cal.getTimeInMillis() / 1000;

		cal.set(Calendar.YEAR, currentYear2);
		cal.set(Calendar.MONTH, nextMonth);

		Long endTime = cal.getTimeInMillis() / 1000;

		return poolDao.fetchUserHolidays(userId, carPoolId, startTime, endTime);
	}

	public boolean isOwner(Long userId, Long carPoolId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		return poolDao.isOwner(userId, carPoolId);
	}

	public void saveOrUpdate(Object obj) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		poolDao.saveOrUpdate(obj);

	}

	public void deletePool(String carPoolId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		poolDao.deletePool(carPoolId);

	}

	public List<Carpool> findPoolsByUserId(Long userId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		return poolDao.findPoolsByUserId(userId);
	}

	public Carpool findPoolById(String carPoolId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		return poolDao.findPoolById(carPoolId);

	}

	public List<Point> convertRouteToPoints1(String routeStr) {
		List<Point> points = new ArrayList<Point>();

		try {
			JSONObject route = new JSONObject(routeStr);

			JSONArray path = route.getJSONArray("overview_path");

			for (int j = 0; j < path.length(); j++) {
				JSONObject location = path.getJSONObject(j);

				points.add(new Point(
						Double.parseDouble(location.getString("H")), Double
								.parseDouble(location.getString("L"))));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return points;
	}

	public List<Point> convertRouteToPoints(String routeStr, Long startTimeInSec) {
		List<Point> points = new ArrayList<Point>();

		try {

			JSONObject route = new JSONObject(routeStr);

			JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
			JSONArray steps = leg.getJSONArray("steps");

			long duration = startTimeInSec.longValue();
			long totalDistance = 0;

			for (int i = 0; i < steps.length(); i++) {

				JSONObject step = steps.getJSONObject(i);

				JSONObject startPoint = step.getJSONObject("start_point");
				JSONArray path = step.getJSONArray("path");
				int timeToCrossInSecs = step.getJSONObject("duration").getInt(
						"value");
				int distanceInMeters = step.getJSONObject("distance").getInt(
						"value");

				if (startPoint.has("lat")) {
					points.add(new Point(Double.parseDouble(startPoint
							.getString("lat")), Double.parseDouble(startPoint
							.getString("lng")), duration));
				}

				if (path.length() > 0) {

					int timePerStep = timeToCrossInSecs / path.length() - 1;
					int distancePerStep = distanceInMeters / path.length() - 1;

					for (int j = 0; j < path.length(); j++) {

						JSONObject point = path.getJSONObject(j);

						if (startPoint.has("lat")) {
							points.add(new Point(Double.parseDouble(point
									.getString("lat")), Double
									.parseDouble(point.getString("lng")),
									(duration + timePerStep * j),
									(totalDistance + distancePerStep * j)));
						}
					}
				}

				duration = duration + timeToCrossInSecs;
				totalDistance = totalDistance + distanceInMeters;

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return points;
	}

	public void addVehicle(Vehicle vh) {
		VehicleDao vDao = (VehicleDao) SpringBeanProvider.getBean("vehicleDao");
		vDao.addvehicle(vh);
	}

	public Vehicle getVehicleByOwnerId(Long ownerId) {
		VehicleDao vDao = (VehicleDao) SpringBeanProvider.getBean("vehicleDao");
		return vDao.getVehicleByOwnerId(ownerId);
	}

	public void subscribeToPool(Long carPoolId, Long travellerId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		poolDao.subscribeToPool(carPoolId, travellerId);
	}

	public Carpool createCarPool(Carpool pool, List<Point> points,
			boolean excludeWeekend, boolean oddEven) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		long noOfSecsInDay = 24 * 60 * 60;

		Long startDate = pool.getStartDate();
		Long endDate = pool.getEndDate();

		if (pool.getNoOfAvblSeats() == null) {
			pool.setNoOfAvblSeats(1);
		}

		Set<PoolCalendarDay> calendarDays = new HashSet<PoolCalendarDay>();

		Vehicle v = null;
		boolean isEven = true;

		if (oddEven) {
			v = (Vehicle) poolDao.get(Vehicle.class,
					Long.valueOf(pool.getVehicleId()));
			String regNo = v.getRegistrationNo().trim();
			isEven = Integer.valueOf(regNo.substring(regNo.length() - 1)) % 2 == 0;
		}

		for (long day = startDate; day <= endDate; day = day + noOfSecsInDay) {

			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(day * 1000);

			PoolCalendarDay calendarDay = new PoolCalendarDay();
			
			if (oddEven || excludeWeekend) {
				if (excludeWeekend
						&& (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || date
								.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
					calendarDay.setIsHoliday(1);
				}

				if (oddEven && v != null) {
					if (isEven && date.get(Calendar.DATE) % 2 == 0) {
						calendarDay.setIsHoliday(1);
					} else {
						calendarDay.setIsHoliday(1);
					}
				}

			} else {
				calendarDay.setIsHoliday(0);
			}

			calendarDay.setNoOfTravellers(1);// Including owner
			calendarDay.setDate(day);
			calendarDay.setNoOfAvblSeats(pool.getNoOfAvblSeats());
			calendarDay.setCarPool(pool);
			calendarDays.add(calendarDay);
		}

		pool.setCalendarDays(calendarDays);

		return poolDao.createCarPool(pool, points);
	}

	public void updatePool(Carpool pool, List<Point> points) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		poolDao.updatePool(pool, points);
	}

	public Map<Long, GeoPoint> findNearestPools(Point srcPoint,
			Point destPoint, Long startTime, Long userId) {

		String lattitude = srcPoint.getLattitude().toString();
		String longitude = srcPoint.getLongitude().toString();
		Map<Long, GeoPoint> poolPointMap = new HashMap<Long, GeoPoint>();

		Double initFixedDistance = 200.0;
		DeltaLatLong screenDelta = PoolUtils.findDelta(initFixedDistance,
				lattitude, longitude);

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		// Search Car Pools with the radius of 400km around user location
		List<Long> carPoolIds = poolDao.searchPools(lattitude, longitude,
				screenDelta, startTime, userId);

		if (carPoolIds == null || carPoolIds.size() == 0) {
			return null;
		}

		DeltaLatLong delta = PoolUtils.findDelta(3.0, lattitude, longitude);

		// For these pools select points on car pool within 3kms
		List<GeoPoint> points = poolDao.findNearestPoints(delta, carPoolIds,
				startTime);

		// Creat a Map of carpoolId and list of points fetched
		Map<Long, List<GeoPoint>> poolId_PointMap = new HashMap<Long, List<GeoPoint>>();

		for (GeoPoint p : points) {
			if (poolId_PointMap.get(p.getCarPoolId()) == null) {
				poolId_PointMap
						.put(p.getCarPoolId(), new ArrayList<GeoPoint>());
			}
			poolId_PointMap.get(p.getCarPoolId()).add(p);
		}

		if (poolId_PointMap.size() > 0) {

			DeltaLatLong deltaDest = PoolUtils.findDelta(3.0, destPoint
					.getLattitude().toString(), destPoint.getLongitude()
					.toString());

			// For these pools select points on car pool within 3kms
			Set<Long> destPoolIds = poolDao.findDestinationPools(deltaDest,
					carPoolIds);

			for (Long poolId : poolId_PointMap.keySet()) {
				if (!destPoolIds.contains(poolId)) {
					poolId_PointMap.remove(poolId);
				}
			}

			if (poolId_PointMap.size() > 0) {

				// For each pool find the nearest source point. The pool with
				// nearest
				// point will get the priority
				DistanceComparator comparator = new DistanceComparator(
						Double.valueOf(longitude), Double.valueOf(lattitude));

				List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

				for (Long poolId : poolId_PointMap.keySet()) {

					GeoPoint point = PoolUtils.findNearestPoint(lattitude,
							longitude, poolId_PointMap.get(poolId));
					geoPoints.add(point);
				}

				Collections.sort(geoPoints, comparator);

				for (GeoPoint point : geoPoints) {
					poolPointMap.put(point.getCarPoolId(), point);
				}
			}
		}
		return poolPointMap;
	}
}
