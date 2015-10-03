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
import com.pool.spring.dao.UserDao;
import com.pool.spring.dao.VehicleDao;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.GeoPoint;
import com.pool.spring.model.PoolCalendarDay;
import com.pool.spring.model.UserCalendarDay;
import com.pool.spring.model.Vehicle;
import com.run.GoogleConstants;

public class CarPoolService {

	public List<PoolCalendarDay> getPoolHolidays(Long carPoolId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Calendar cal = Calendar.getInstance();

		int currentMonth = cal.get(Calendar.MONTH);
		int currentYear1 = cal.get(Calendar.MONTH);
		int currentYear2 = (currentMonth == 12) ? (currentYear1 + 1)
				: currentYear1;
		int nextMonth = (currentMonth == 12) ? 2 : (currentMonth + 2);

		cal.set(Calendar.DAY_OF_MONTH, 1);

		Long startTime = cal.getTimeInMillis() / 1000;

		cal.set(Calendar.YEAR, currentYear2);
		cal.set(Calendar.MONTH, nextMonth);

		Long endTime = cal.getTimeInMillis() / 1000;

		return poolDao.fetchPoolHolidays(carPoolId, startTime, endTime);
	}

	public List<UserCalendarDay> getUserHolidays(Long userId, Long carPoolId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		Calendar cal = Calendar.getInstance();

		int currentMonth = cal.get(Calendar.MONTH);
		int currentYear1 = cal.get(Calendar.MONTH);
		int currentYear2 = (currentMonth == 12) ? (currentYear1 + 1)
				: currentYear1;
		int nextMonth = (currentMonth == 12) ? 2 : (currentMonth + 2);

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
		poolDao.save(obj);

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

			for (int i = 0; i < steps.length(); i++) {

				JSONObject step = steps.getJSONObject(i);

				JSONObject startPoint = step.getJSONObject("start_point");
				JSONArray path = step.getJSONArray("path");
				int timeToCross = step.getJSONObject("duration")
						.getInt("value");

				points.add(new Point(Double.parseDouble(startPoint
						.getString("H")), Double.parseDouble(startPoint
						.getString("L")), duration));

				if (path.length() > 0) {

					int timePerStep = timeToCross / path.length();

					for (int j = 0; j < path.length(); j++) {

						JSONObject point = path.getJSONObject(j);
						points.add(new Point(Double.parseDouble(point
								.getString("H")), Double.parseDouble(point
								.getString("L")), (duration + timePerStep * j)));
					}
				}

				duration = duration + timeToCross;

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

	public Carpool createCarPool(Carpool pool, List<Point> points) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		long noOfSecsInDay = 24 * 60 * 60;

		Long startDate = pool.getStartDate();
		Long endDate = pool.getEndDate();

		if (pool.getNoOfAvblSeats() == null) {
			pool.setNoOfAvblSeats(1);
		}

		Set<PoolCalendarDay> calendarDays = new HashSet<PoolCalendarDay>();

		for (long day = startDate; day <= endDate; day = day + noOfSecsInDay) {
			PoolCalendarDay calendarDay = new PoolCalendarDay();
			calendarDay.setNoOfTravellers(1);// Including owner
			calendarDay.setIsHoliday(0);
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

	public List<Long> findNearestPools2(Point srcPoint, Point destPoint) {

		String lattitude = srcPoint.getLattitude().toString();
		String longitude = srcPoint.getLongitude().toString();

		List<Long> carPools = new ArrayList<Long>();
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		DeltaLatLong delta = PoolUtils.findDelta(3.0, lattitude, longitude);

		// Find all points within 3kms
		List<GeoPoint> points = poolDao.findNearestPoints(delta);

		// Creat a Map of carpoolId and list of points fetched
		Map<Long, List<GeoPoint>> poolId_PointMap = new HashMap<Long, List<GeoPoint>>();

		for (GeoPoint p : points) {
			if (poolId_PointMap.get(p.getCarPoolId()) == null) {
				poolId_PointMap
						.put(p.getCarPoolId(), new ArrayList<GeoPoint>());
			}
			poolId_PointMap.get(p.getCarPoolId()).add(p);
		}

		if (poolId_PointMap.keySet().size() > 0) {

			// For each pool find the nearest point. The pool with nearst point
			// will get the priority
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
				carPools.add(point.getCarPoolId());
			}
		}

		if (carPools.size() > 0) {
			Collection<Long> pools = findPoolsGngToDestination(carPools,
					destPoint);
			carPools.clear();
			carPools.addAll(pools);
		}

		return carPools;
	}

	public List<Long> findNearestPools(Point srcPoint, Point destPoint,
			Long startTime) {

		String lattitude = srcPoint.getLattitude().toString();
		String longitude = srcPoint.getLongitude().toString();

		List<Long> carPools = new ArrayList<Long>();

		Double initFixedDistance = 200.0;
		DeltaLatLong screenDelta = PoolUtils.findDelta(initFixedDistance,
				lattitude, longitude);

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		// Search Car Pools with the radius of 400km around user location
		List<Long> carPoolIds = poolDao.searchPools(lattitude, longitude,
				screenDelta, startTime);

		if (carPoolIds == null || carPoolIds.size() == 0) {
			return carPools;
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

			Collection<Long> pools = findPoolsGngToDestination(
					poolId_PointMap.keySet(), destPoint);

			for (Long poolId : poolId_PointMap.keySet()) {
				if (!pools.contains(poolId)) {
					poolId_PointMap.remove(poolId);
				}
			}

			if (poolId_PointMap.size() > 0) {

				// For each pool find the nearest point. The pool with nearst
				// point
				// will get the priority
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
					carPools.add(point.getCarPoolId());
				}
			}
		}

		return carPools;
	}

	private static Collection<Long> findPoolsGngToDestination(
			Collection<Long> carPoolIds, Point point) {
		DeltaLatLong delta = PoolUtils.findDelta(2.0, point.getLattitude()
				.toString(), point.getLongitude().toString());

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Set<Long> carPoolIdSet = new HashSet<Long>();

		// For these pools select points on car pool within 2kms
		List<GeoPoint> points = poolDao.findNearestDestinationPoints(delta,
				carPoolIds);

		for (GeoPoint p : points) {
			carPoolIdSet.add(p.getCarPoolId());
		}

		return carPoolIds;
	}

}
