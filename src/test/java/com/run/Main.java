package com.run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pool.DeltaLatLong;
import com.pool.Point;
import com.pool.spring.SpringBeanProvider;
import com.pool.spring.model.Carpool;
import com.pool.spring.model.GeoPoint;

public class Main {
	
//	public static void searchPools(String lattitude, String longitude) {
//
//		Double initFixedDistance = 400.0;
//		DeltaLatLong screenDelta = findDelta(initFixedDistance, lattitude,
//				longitude);
//		Session session = null;
//		Transaction tx = null;
//
//		try {
//			SessionFactory sessionFactory = SpringBeanProvider
//					.getSessionFactory();
//			session = sessionFactory.openSession();
//			tx = session.beginTransaction();
//
//			Query queryPool = session
//					.createQuery("select pool.carPoolId from Carpool pool where (pool.srcLongitude < :maxLongitude and pool.srcLongitude > :minLongitude and pool.srcLattitude > :minLattitude and  pool.srcLattitude < :maxLattitude) or "
//							+ " (pool.destLongitude < :maxLongitude and  pool.destLongitude < :minLongitude "
//							+ "  and pool.destLattitude < :maxLattitude and  pool.destLattitude > :minLattitude)");
//
//			queryPool.setParameter("minLongitude", screenDelta
//					.getMinLongitude().toString());
//			queryPool.setParameter("maxLongitude", screenDelta
//					.getMaxLongitude().toString());
//			queryPool.setParameter("minLattitude", screenDelta
//					.getMinLattitude().toString());
//			queryPool.setParameter("maxLattitude", screenDelta
//					.getMaxLattitude().toString());
//			List carpoolIds = queryPool.list();
//
//			DeltaLatLong delta = findDelta(4, lattitude, longitude);
//			
//			Query queryPoints = session.getNamedQuery("fetchPoints");
//			queryPoints.setParameter("minLongitude", delta.getMinLongitude());
//			queryPoints.setParameter("maxLongitude", delta.getMaxLongitude());
//			queryPoints.setParameter("minLattitude", delta.getMinLattitude());
//			queryPoints.setParameter("maxLattitude", delta.getMaxLattitude());
//			queryPoints.setParameterList("carPoolIds", carpoolIds);
//
//			List points = queryPoints.list();
//
//			tx.commit();
//			System.out.println(delta);
//			System.out.println(points);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//		} finally {
//			session.close();
//		}
//	}
//
//	public static DeltaLatLong findDelta(double fixedDistance,
//			String lattitude, String longitude) {
//
//		double R = 6371;
//		double Rs = R * Math.cos(Double.valueOf(lattitude) * Math.PI / 360.0);
//		double deltaLong = (fixedDistance / Rs) * 180 / (Math.PI);
//
//		// Max-min Longitude calculations
//		double minLong = Double.valueOf(longitude) - deltaLong;
//		double maxLong = Double.valueOf(longitude) + deltaLong;
//
//		if (minLong < -180) {
//			minLong = minLong + 360;
//		}
//		if (maxLong > 180) {
//			maxLong = 360 - maxLong;
//		}
//
//		// Max-min Lattitude calculations
//		double deltaLat = (fixedDistance / Rs) * 180 / (Math.PI);
//
//		double minLat = Double.valueOf(lattitude) - deltaLat;
//		double maxLat = Double.valueOf(lattitude) + deltaLat;
//
//		DeltaLatLong delta = new DeltaLatLong();
//		delta.setMinLattitude(minLat);
//		delta.setMaxLattitude(maxLat);
//		delta.setMinLongitude(minLong);
//		delta.setMaxLongitude(maxLong);
//
//		return delta;
//	}
//
//	public static void main(String[] args) throws Exception {
//		searchPools("43.6533101", "-79.382766");
//
//	}
//
//	public static void main1(String[] args) throws Exception {
//
//		StringBuffer b = new StringBuffer(100);
//		List<Point> points = new ArrayList<Point>();
//
//		URL oracle = new URL(
//				"https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyDM9TTxSkYXKz6F1XtOod-Nr8Q_wlRaNs4");
//
//		URLConnection yc = oracle.openConnection();
//		BufferedReader in = new BufferedReader(new InputStreamReader(
//				yc.getInputStream()));
//		String inputLine;
//		while ((inputLine = in.readLine()) != null) {
//			System.out.println(inputLine);
//			b.append(inputLine);
//
//		}
//		in.close();
//
//		JSONObject jObject = new JSONObject(b.toString());
//
//		JSONObject route = jObject.getJSONArray(GoogleConstants.ROUTES)
//				.getJSONObject(0);
//
//		JSONArray legs = route.getJSONArray(GoogleConstants.LEGS);
//
//		for (int i = 0; i < legs.length(); i++) {
//
//			JSONObject leg = legs.getJSONObject(i);
//
//			JSONArray steps = leg.getJSONArray(GoogleConstants.STEPS);
//
//			for (int j = 0; j < steps.length(); j++) {
//				JSONObject step = steps.getJSONObject(j);
//				JSONObject location = step
//						.getJSONObject(GoogleConstants.START_LOCATION);
//				points.add(new Point(location
//						.getString(GoogleConstants.LATTITUDE), location
//						.getString(GoogleConstants.LONGITUDE)));
//			}
//		}
//
//		JSONObject lastLeg = legs.getJSONObject(legs.length() - 1);
//
//		JSONObject endLocation = lastLeg
//				.getJSONObject(GoogleConstants.END_LOCATION);
//		points.add(new Point(endLocation.getString(GoogleConstants.LATTITUDE),
//				endLocation.getString(GoogleConstants.LONGITUDE)));
//		System.out.println(b);
//
//		SessionFactory sessionFactory = SpringBeanProvider.getSessionFactory();
//		Session session = sessionFactory.openSession();
//		Transaction tx = null;
//
//		try {
//			tx = session.beginTransaction();
//			Carpool pool = new Carpool();
//			pool.setCarpoolName("Test");
//			pool.setCreateDate(new Date());
//			pool.setDestLattitude(points.get(points.size() - 1).getLattitude());
//			pool.setDestLongitude(points.get(points.size() - 1).getLongitude());
//			pool.setSrcLattitude(points.get(0).getLattitude());
//			pool.setSrcLongitude(points.get(0).getLongitude());
//			pool.setStartDate(new Date());
//			pool.setStartTime(new Date());
//			pool.setOwnerId("1");
//			pool.setExptdEndTime(new Date());
//			session.save(pool);
//			int i = 0;
//			for (; i < points.size(); i++) {
//				GeoPoint point = new GeoPoint();
//				point.setCarPoolId(pool.getCarPoolId());
//				point.setLatitude(Double.valueOf(points.get(i).getLattitude()));
//				point.setLongitude(Double.valueOf(points.get(i).getLongitude()));
//				point.setPointOrder(i);
//				session.save(point);
//			}
//			tx.commit();
//
//		} catch (Exception e) {
//			tx.rollback();
//			e.printStackTrace();
//		} finally {
//			session.close();
//		}
//
//	}
}
