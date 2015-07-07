package com.pool.spring.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.pool.DeltaLatLong;
import com.pool.Point;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.GeoPoint;
import com.pool.spring.model.PoolSubscription;
import com.pool.spring.model.User;

@Repository("carPoolDao")
public class CarPoolDao extends AbstractDao {

	public void subscribeToPool(String carPoolId, String travellerId) {

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
			Query q = session
					.createQuery("delete from GeoPoint geoPoint where geoPoint.carPoolId = :carPoolId");
			q.setParameter("carPoolId", pool.getCarPoolId());
			q.executeUpdate();

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
			pool.setExptdEndTime(new Date());
			pool.setCreateDate(new Date());
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
			pool.setCreateDate(new Date());
			pool.setDestLattitude(points.get(points.size() - 1).getLattitude());
			pool.setDestLongitude(points.get(points.size() - 1).getLongitude());
			pool.setSrcLattitude(points.get(0).getLattitude());
			pool.setSrcLongitude(points.get(0).getLongitude());
			pool.setStartDate(new Date());
			pool.setStartTime(new Date());
			pool.setOwnerId(userId);
			pool.setExptdEndTime(new Date());
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
		try {
			session = this.openSession();
			pool = (Carpool) session.get(Carpool.class, carPoolId);

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
			com.pool.DeltaLatLong delta) {

		List<Long> carpoolIds = null;
		Session session = null;
		try {

			session = this.openSession();
			Query queryPool = session
					.createQuery("select pool.carPoolId from Carpool pool where (pool.srcLongitude < :maxLongitude and pool.srcLongitude > :minLongitude and pool.srcLattitude > :minLattitude and  pool.srcLattitude < :maxLattitude) or "
							+ " (pool.destLongitude < :maxLongitude and  pool.destLongitude < :minLongitude "
							+ "  and pool.destLattitude < :maxLattitude and  pool.destLattitude > :minLattitude)");

			queryPool.setParameter("minLongitude", delta.getMinLongitude()
					.toString());
			queryPool.setParameter("maxLongitude", delta.getMaxLongitude()
					.toString());
			queryPool.setParameter("minLattitude", delta.getMinLattitude()
					.toString());
			queryPool.setParameter("maxLattitude", delta.getMaxLattitude()
					.toString());
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
	public List<GeoPoint> findNearestPoints(DeltaLatLong delta,
			List<Long> carpoolIds) {

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
