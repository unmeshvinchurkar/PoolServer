package com.pool.service;

import java.util.ArrayList;
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
import com.pool.spring.model.Vehicle;
import com.run.GoogleConstants;

public class CarPoolService {

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

	public List findPoolsByUserId(String userId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		return poolDao.findPoolsByUserId(userId);

	}

	public Carpool findPoolById(String carPoolId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		return poolDao.findPoolById(carPoolId);

	}

	public List<Point> convertRouteToPoints(String routeStr) {
		List<Point> points = new ArrayList<Point>();

		try {
			JSONObject route = new JSONObject(routeStr);
			//
			// JSONArray legs = route.getJSONArray(GoogleConstants.LEGS);
			//
			// for (int i = 0; i < legs.length(); i++) {
			//
			// JSONObject leg = legs.getJSONObject(i);
			//
			// JSONArray steps = leg.getJSONArray(GoogleConstants.STEPS);
			//
			// for (int j = 0; j < steps.length(); j++) {
			// JSONObject step = steps.getJSONObject(j);
			// JSONObject location = step
			// .getJSONObject(GoogleConstants.START_LOCATION);
			// points.add(new Point(location.getString("G"/*
			// * GoogleConstants.
			// * LONGITUDE
			// */),
			//
			// location.getString("K"/* GoogleConstants.LONGITUDE */)));
			// }
			// }
			//
			// JSONObject lastLeg = legs.getJSONObject(legs.length() - 1);
			//
			// JSONObject endLocation = lastLeg
			// .getJSONObject(GoogleConstants.END_LOCATION);
			// points.add(new Point(endLocation.getString("G"/*
			// * GoogleConstants.LONGITUDE
			// */),
			//
			// endLocation.getString("K"/* GoogleConstants.LONGITUDE */)));

			JSONArray path = route.getJSONArray("overview_path");

			for (int j = 0; j < path.length(); j++) {
				JSONObject location = path.getJSONObject(j);
				
				points.add(new Point(location.getString("G"),
				location.getString("K")));
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

	public Vehicle getVehicleByOwnerId(String ownerId) {
		VehicleDao vDao = (VehicleDao) SpringBeanProvider.getBean("vehicleDao");
		return vDao.getVehicleByOwnerId(ownerId);
	}

	public void subscribeToPool(String carPoolId, String travellerId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		poolDao.subscribeToPool(carPoolId, travellerId);
	}

	public Carpool createCarPool(Carpool pool, List<Point> points) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		return poolDao.createCarPool(pool, points);
	}

	public void updatePool(Carpool pool, List<Point> points) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		pool.setDestLattitude(points.get(points.size() - 1).getLattitude());
		pool.setDestLongitude(points.get(points.size() - 1).getLongitude());
		pool.setSrcLattitude(points.get(0).getLattitude());
		pool.setSrcLongitude(points.get(0).getLongitude());
		poolDao.updatePool(pool, points);
	}

	public List<Long> findNearestPools2(Point srcPoint, Point destPoint) {

		String lattitude = srcPoint.getLattitude();
		String longitude = srcPoint.getLongitude();

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

	public List<Long> findNearestPools(Point srcPoint, Point destPoint) {

		String lattitude = srcPoint.getLattitude();
		String longitude = srcPoint.getLongitude();

		List<Long> carPools = new ArrayList<Long>();

		Double initFixedDistance = 400.0;
		DeltaLatLong screenDelta = PoolUtils.findDelta(initFixedDistance,
				lattitude, longitude);

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		// Search Car Pools with the radius of 400km around user location
		List<Long> carPoolIds = poolDao.searchPools(lattitude, longitude,
				screenDelta);

		DeltaLatLong delta = PoolUtils.findDelta(3.0, lattitude, longitude);

		// For these pools select points on car pool within 3kms
		List<GeoPoint> points = poolDao.findNearestPoints(delta, carPoolIds);

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

		if (carPools.size() > 0 && destPoint != null) {
			Collection<Long> pools = findPoolsGngToDestination(carPools,
					destPoint);
			carPools.clear();
			carPools.addAll(pools);
		}

		return carPools;
	}

	private static Collection<Long> findPoolsGngToDestination(
			List<Long> carPoolIds, Point point) {
		DeltaLatLong delta = PoolUtils.findDelta(2.0, point.getLattitude(),
				point.getLongitude());

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Set<Long> carPoolIdSet = new HashSet<Long>();

		// For these pools select points on car pool within 2kms
		List<GeoPoint> points = poolDao.findNearestPoints(delta, carPoolIds);

		for (GeoPoint p : points) {
			carPoolIdSet.add(p.getCarPoolId());
		}

		for (Long poolId : carPoolIds) {
			if (!carPoolIdSet.contains(poolId)) {
				carPoolIds.remove(poolId);
			}
		}

		return carPoolIds;
	}

}
