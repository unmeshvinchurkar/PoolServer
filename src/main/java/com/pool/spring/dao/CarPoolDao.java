package com.pool.spring.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.pool.DeltaLatLong;
import com.pool.Point;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.GeoPoint;
import com.pool.spring.model.PoolCalendarDay;
import com.pool.spring.model.PoolSubscription;
import com.pool.spring.model.UserCalendarDay;

@Repository("carPoolDao")
public class CarPoolDao extends AbstractDao {

	public List fetchPoolIdsForSentRequests(Long userId, Collection carpoolIds) {
		Session session = null;
		List result = null;
		try {
			session = this.openSession();
			Query q = session
					.createQuery("select req.carPoolId from Request req where req.fromUserId =(:userId) "
							+ "  and req.carPoolId in (:carpoolIds) and (req.processed is NULL or req.processed=0) ");
			q.setParameter("userId", userId);
			q.setParameterList("carpoolIds", carpoolIds);
			result = q.list();
		} finally {
			session.close();
		}
		return result;
	}

	public List fetchSubscribedTravellers(Long carPoolId) {
		Session session = null;
		List result = null;
		try {
			session = this.openSession();
			Query q = session
					.createQuery("select sub.travellerId from PoolSubscription sub where sub.carPoolId=:carPoolId");
			q.setParameter("carPoolId", carPoolId);
			result = q.list();
		} finally {
			session.close();
		}
		return result;
	}

	public List fetchNotifications(Long userId, Long dateInSec) {
		Session session = null;
		List result = null;

		try {
			session = this.openSession();
			Query q = session
					.createQuery("from Notification not where not.toUserId =(:userId) and not.createDate> = (:createDate) ORDER BY not.createDate DESC ");
			q.setParameter("userId", userId);
			q.setParameter("createDate", dateInSec);
			result = q.list();
		} finally {
			session.close();
		}
		return result;
	}

	public List fetchSentRequests(Long userId) {
		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("select req.carPoolId, req.createDate, req.status, req.startTime, usr.firstName, usr.lastName, usr.userId, req.requestId, req.srcLattitude, req.srcLongitude   "
							+ " from Request req, User usr where req.fromUserId =(:userId) and (req.processed !=1 or req.processed is NULL ) and usr.userId = req.toUserId ORDER BY req.createDate DESC  ");
			q.setParameter("userId", userId);
			result = q.list();
		} finally {
			session.close();
		}
		List resultSet = new ArrayList();

		if (result != null) {
			for (Object obj : result) {
				Map map = new HashMap();
				Object values[] = (Object[]) obj;
				map.put("carPoolId", values[0]);
				map.put("createDate", values[1]);
				map.put("status", values[2]);
				map.put("startTime", values[3]);
				map.put("ownerName", values[4] + " " + values[5]);
				map.put("userId", values[6]);
				map.put("requestId", values[7]);
				map.put("pickupLattitude", values[8]);
				map.put("pickupLongitude", values[9]);
				resultSet.add(map);
			}
		}
		return resultSet;
	}

	public List fetchReceivedRequests(Long userId) {
		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("select req.carPoolId, req.createDate, req.startTime, usr.firstName, usr.lastName, usr.userId, req.requestId, req.srcLattitude, req.srcLongitude  "
							+ " from Request req, User usr  where req.toUserId =(:userId) and (req.processed = 0 or req.processed is NULL ) and usr.userId = req.fromUserId ORDER BY req.createDate DESC  ");
			q.setParameter("userId", userId);
			result = q.list();
		} finally {
			session.close();
		}
		
		List resultSet = new ArrayList();

		if (result != null) {
			for (Object obj : result) {
				Map map = new HashMap();
				Object values[] = (Object[]) obj;
				map.put("carPoolId", values[0]);
				map.put("createDate", values[1]);
				map.put("startTime", values[2]);
				map.put("fullName", values[3] + " " + values[4]);
				map.put("userId", values[5]);
				map.put("requestId", values[6]);
				map.put("pickupLattitude", values[7]);
				map.put("pickupLongitude", values[8]);
				resultSet.add(map);
			}
		}		
		return resultSet;
	}

	public void removeTraveller(Long travellerId, Long carPoolId) {

		Session session = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("delete PoolSubscription where travellerId=(:travellerId) and carPoolId =(:carPoolId)");
			q.setParameter("travellerId", travellerId);
			q.setParameter("carPoolId", carPoolId);
			q.executeUpdate();
		} finally {
			session.close();
		}
	}

	public List fetchSubscribedPoolIds(Long userId) {
		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("select sub.carPoolId from  PoolSubscription sub where sub.travellerId=(:userId)");
			q.setParameter("userId", userId);
			result = q.list();

		} finally {
			session.close();
		}

		return result;
	}

	public List fetchPoolDetailsById(Collection<Long> carPoolIds) {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("select pool, usr  from Carpool pool, User usr where pool.carPoolId in (:carPoolIds) and pool.ownerId = usr.userId");
			q.setParameterList("carPoolIds", carPoolIds);
			result = q.list();

		} finally {
			session.close();
		}

		return result;
	}

	public PoolCalendarDay fetchPoolCalendarDay(Long carPoolId, Long timeInSec) {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery(" from PoolCalendarDay day where day.carPool.carPoolId =(:carPoolId) and day.date=:timeInSec ");
			q.setParameter("timeInSec", timeInSec);
			q.setParameter("carPoolId", carPoolId);
			result = q.list();
		} finally {
			session.close();
		}

		if (result != null && result.size() > 0) {
			return (PoolCalendarDay) result.get(0);
		}

		return null;
	}

	public List fetchPoolHolidays(Long carPoolId, Long startTime, Long endTime) {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery(" from PoolCalendarDay day where day.carPool.carPoolId =(:carPoolId) and day.isHoliday=1 and day.date>=:startDay and day.date<=:endDay");
			q.setParameter("startDay", startTime);
			q.setParameter("endDay", endTime);
			q.setParameter("carPoolId", carPoolId);
			result = q.list();
		} finally {
			session.close();
		}

		return result;
	}

	public UserCalendarDay fetchUserCalendarDay(Long carPoolId, Long userId,
			Long timeInSec) {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery(" from UserCalendarDay day where day.carPoolId =(:carPoolId) and day.userId =(:userId) and day.calendarDay=:timeInSec ");
			q.setParameter("timeInSec", timeInSec);
			q.setParameter("carPoolId", carPoolId);
			q.setParameter("userId", userId);
			result = q.list();
		} finally {
			session.close();
		}

		if (result != null && result.size() > 0) {
			return (UserCalendarDay) result.get(0);
		}

		return null;
	}

	public List fetchUserHolidays(Long userId, Long carPoolId, Long startTime,
			Long endTime) {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery(" from UserCalendarDay day where day.carPoolId =(:carPoolId)  and day.calendarDay>=:startDay and day.calendarDay<=:endDay");
			q.setParameter("startDay", startTime);
			q.setParameter("endDay", endTime);
			q.setParameter("carPoolId", carPoolId);
			// q.setParameter("userId", userId);
			result = q.list();
		} finally {
			session.close();
		}

		return result;
	}

	public boolean isOwner(Long userId, Long carPoolId) {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("select pool.ownerId from Carpool pool where pool.carPoolId=:carPoolId");
			q.setParameter("carPoolId", carPoolId);
			result = q.list();

			if (result != null && result.size() > 0) {
				Object ownerId = result.get(0);
				if (ownerId.equals(userId)) {
					return true;
				}
			}
		} finally {
			session.close();
		}

		return false;
	}

	public void deletePool(String carPoolId) {
		Session session = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("update Carpool pool set pool.deleted=1 where pool.carPoolId=:carPoolId");
			q.setParameter("carPoolId", Long.valueOf(carPoolId));
			q.executeUpdate();

		} finally {
			session.close();
		}
	}

	public List findPoolsByUserId(Long userId) {

		Session session = null;
		List carPoolList = null;
		try {
			session = this.openSession();

			Query q = session
					.createQuery("select carpool from Carpool carpool where carpool.ownerId =(:userId) and (carpool.deleted = 0 or carpool.deleted is Null) union "
							+ " select pool from Carpool pool, PoolSubscription subs where pool.carPoolId = subs.carPoolId and subs.travellerId=:userId");
			q.setParameter("userId", userId);
			carPoolList = q.list();

		} finally {
			session.close();
		}

		return carPoolList;
	}

	public void subscribeToPool(Long carPoolId, Long travellerId) {

		PoolSubscription subs = new PoolSubscription();
		subs.setCarPoolId(carPoolId);
		subs.setTravellerId(travellerId);

		Session session = null;
		try {
			session = this.openSession();
			session.saveOrUpdate(subs);
		} finally {
			session.close();
		}

	}

	public void updatePool(Carpool pool, List<Point> points) {

		Transaction tx = null;
		Session session = null;
		try {
			session = this.openSession();
			tx = session.beginTransaction();

			if (points != null && points.size() > 0) {

				Query q = session
						.createQuery("delete from GeoPoint geoPoint where geoPoint.carPoolId = :carPoolId");
				q.setParameter("carPoolId", pool.getCarPoolId());
				q.executeUpdate();

				for (int i = 0; i < points.size(); i++) {
					GeoPoint point = new GeoPoint();
					point.setCarPoolId(pool.getCarPoolId());
					point.setLatitude(Double.valueOf(points.get(i)
							.getLattitude()));
					point.setLongitude(Double.valueOf(points.get(i)
							.getLongitude()));
					point.setPointOrder(i);
					session.save(point);
				}
			}

			session.saveOrUpdate(pool);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public Carpool createCarPool(Carpool pool, List<Point> points) {
		Transaction tx = null;
		Session session = null;
		try {
			session = this.openSession();
			tx = session.beginTransaction();

			pool.setDestLattitude(points.get(points.size() - 1).getLattitude());
			pool.setDestLongitude(points.get(points.size() - 1).getLongitude());
			pool.setSrcLattitude(points.get(0).getLattitude());
			pool.setSrcLongitude(points.get(0).getLongitude());
			pool.setExptdEndTime(new Date().getTime() / 1000);
			pool.setCreateDate(new Date().getTime() / 1000);
			session.save(pool);
			session.flush();

			for (int i = 0; i < points.size(); i++) {
				GeoPoint point = new GeoPoint();
				point.setCarPoolId(pool.getCarPoolId());
				point.setLatitude(points.get(i).getLattitude());
				point.setLongitude(points.get(i).getLongitude());
				point.setPointOrder(i);
				point.setApproxTimeToReach(points.get(i).getTimeToReach());
				session.save(point);
			}
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return pool;
	}

	public Carpool createCarPool(String userId, String vehicleId,
			List<Point> points) {
		Transaction tx = null;
		Session session = null;
		Carpool pool = null;
		try {
			session = this.openSession();
			tx = session.beginTransaction();

			pool = new Carpool();
			pool.setCarpoolName("Test");
			pool.setCreateDate(new Date().getTime());
			pool.setDestLattitude(points.get(points.size() - 1).getLattitude());
			pool.setDestLongitude(points.get(points.size() - 1).getLongitude());
			pool.setSrcLattitude(points.get(0).getLattitude());
			pool.setSrcLongitude(points.get(0).getLongitude());
			pool.setStartDate(new Date().getTime());
			pool.setStartTime(11111l);
			pool.setOwnerId(Long.valueOf(userId));
			pool.setExptdEndTime(new Date().getTime() / 1000);
			pool.setVehicleId(vehicleId);
			session.save(pool);
			session.flush();

			for (int i = 0; i < points.size(); i++) {
				GeoPoint point = new GeoPoint();
				point.setCarPoolId(pool.getCarPoolId());
				point.setLatitude(Double.valueOf(points.get(i).getLattitude()));
				point.setLongitude(Double.valueOf(points.get(i).getLongitude()));
				point.setPointOrder(i);
				session.save(point);
			}
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return pool;
	}

	public Carpool findPoolById(String carPoolId) {
		Session session = null;
		Carpool pool = null;
		List points = null;

		try {
			session = this.openSession();
			pool = (Carpool) session
					.get(Carpool.class, Long.valueOf(carPoolId));

			if (pool != null) {
				Query queryPool = session
						.createQuery("from GeoPoint point where point.carPoolId =:carPoolId order by point.pointOrder");
				queryPool.setParameter("carPoolId", Long.valueOf(carPoolId));
				points = queryPool.list();
				pool.setGeoPoints(points);
			}

		} finally {
			session.close();
		}
		return pool;
	}

	public List<Carpool> findCarPoolByIds(Collection<Long> poolIds) {
		Session session = null;
		List<Carpool> pools = null;
		try {
			session = this.openSession();
			Query queryPool = session
					.createQuery("from Carpool pool where pool.carPoolId in (:poolIds)");
			queryPool.setParameterList("poolIds", poolIds);
			pools = queryPool.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pools;
	}

	public List<GeoPoint> findPointsByPoolIds(List<Long> carPoolIds) {

		Session session = null;
		List<GeoPoint> points = null;
		try {
			session = this.openSession();
			Query queryPoints = session
					.createQuery("from GeoPoint point where point.carPoolId in (:poolIds) order by point.carPoolId, point.pointOrder");
			queryPoints.setParameterList("poolIds", carPoolIds);
			points = queryPoints.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return points;
	}

	/**
	 * This method seaches for those car pools whose source or destination
	 * points are located with a particular distance(defined by delta)
	 * 
	 * The point is defined by (lattitude, longitude)
	 * 
	 * @param lattitude
	 * @param longitude
	 * @param delta
	 * @return Long
	 */
	public List<Long> searchPools(String lattitude, String longitude,
			com.pool.DeltaLatLong delta, Long startTime, Long userId) {

		List<Long> carpoolIds = null;
		Session session = null;
		try {

			session = this.openSession();
			Query queryPool = session
					.createQuery("select pool.carPoolId from Carpool pool where pool.ownerId!=(:userId) and ((pool.srcLongitude <(:maxLongitude) and pool.srcLongitude > (:minLongitude) and pool.srcLattitude > (:minLattitude) and  pool.srcLattitude < (:maxLattitude)) or "
							+ " (pool.destLongitude < (:maxLongitude) and  pool.destLongitude > (:minLongitude) "
							+ "  and pool.destLattitude < (:maxLattitude) and  pool.destLattitude > (:minLattitude))) and pool.startTime <= (:startTime) and (pool.deleted != :deleted or pool.deleted is NULL)and pool.noOfAvblSeats>=(:noOfAvblSeats) and pool.endDate > (:endDate)  ");

			queryPool.setParameter("minLongitude", delta.getMinLongitude());
			queryPool.setParameter("maxLongitude", delta.getMaxLongitude());
			queryPool.setParameter("minLattitude", delta.getMinLattitude());
			queryPool.setParameter("maxLattitude", delta.getMaxLattitude());
			queryPool.setParameter("deleted", Integer.valueOf(1));
			queryPool.setParameter("noOfAvblSeats", Integer.valueOf("1"));
			queryPool.setParameter("userId", userId);

			queryPool.setParameter("endDate",
					Long.valueOf((new Date().getTime()) / 1000));
			queryPool.setParameter("startTime", startTime);
			carpoolIds = queryPool.list();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
		}
		return carpoolIds;
	}

	/**
	 * This method fetches all points for given carpoolIds which come under
	 * geographical distance define by delta.
	 * 
	 * @param delta
	 * @param carpoolIds
	 * @return List<Points>
	 */
	public Set<Long> findDestinationPools(DeltaLatLong delta,
			Collection<Long> carpoolIds) {

		Session session = null;
		List<GeoPoint> points = null;
		Set<Long> carPoolIds = new HashSet<Long>();
		try {
			session = this.openSession();
			Query queryPoints = session
					.createQuery("from com.pool.spring.model.GeoPoint point1 where "
							+ " (point1.longitude < (:maxLongitude) "
							+ " and point1.longitude > (:minLongitude) "
							+ " and point1.latitude > (:minLattitude) "
							+ "and point1.latitude <(:maxLattitude) "
							+ " and point1.carPoolId in (:carPoolIds))");
			queryPoints.setParameter("minLongitude", delta.getMinLongitude());
			queryPoints.setParameter("maxLongitude", delta.getMaxLongitude());
			queryPoints.setParameter("minLattitude", delta.getMinLattitude());
			queryPoints.setParameter("maxLattitude", delta.getMaxLattitude());
			queryPoints.setParameterList("carPoolIds", carpoolIds);
			points = queryPoints.list();
		} finally {
			session.close();
		}

		if (points != null) {

			for (GeoPoint p : points) {

				carPoolIds.add(p.getCarPoolId());

			}

		}
		return carPoolIds;
	}

	/**
	 * This method fetches all points for given carpoolIds which come under
	 * geographical distance define by delta.
	 * 
	 * @param delta
	 * @param carpoolIds
	 * @return List<Points>
	 */
	public List<GeoPoint> findNearestPoints(DeltaLatLong delta,
			List<Long> carpoolIds, Long startTime) {

		Session session = null;
		List<GeoPoint> points = null;
		try {
			session = this.openSession();
			Query queryPoints = session.getNamedQuery("fetchPoints");
			queryPoints.setParameter("minLongitude", delta.getMinLongitude());
			queryPoints.setParameter("maxLongitude", delta.getMaxLongitude());
			queryPoints.setParameter("minLattitude", delta.getMinLattitude());
			queryPoints.setParameter("maxLattitude", delta.getMaxLattitude());
			queryPoints.setParameterList("carPoolIds", carpoolIds);
			queryPoints.setParameter("minPickUpTime", startTime - 10 * 60);
			queryPoints.setParameter("maxPickUpTime", startTime + 20 * 60);
			points = queryPoints.list();
		} finally {
			session.close();

		}

		return points;
	}

	public List<GeoPoint> findNearestPoints(DeltaLatLong delta) {

		Session session = null;
		List<GeoPoint> points = null;
		try {
			session = this.openSession();
			Query queryPoints = session.getNamedQuery("fetchPoints");
			queryPoints.setParameter("minLongitude", delta.getMinLongitude());
			queryPoints.setParameter("maxLongitude", delta.getMaxLongitude());
			queryPoints.setParameter("minLattitude", delta.getMinLattitude());
			queryPoints.setParameter("maxLattitude", delta.getMaxLattitude());
			points = queryPoints.list();
		} finally {
			session.close();

		}

		return points;
	}

}
