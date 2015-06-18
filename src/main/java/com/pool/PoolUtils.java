package com.pool;

import java.security.Key;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.pool.spring.model.GeoPoint;

public class PoolUtils {

	// Radius of earth in Kms
	private static final double R = 6371;

	public static DeltaLatLong findDelta(double fixedDistance,
			String lattitude, String longitude) {

		double Rs = R * Math.cos(Double.valueOf(lattitude) * Math.PI / 360.0);
		double deltaLong = (fixedDistance / Rs) * 180 / (Math.PI);

		// Max-min Longitude calculations
		double minLong = Double.valueOf(longitude) - deltaLong;
		double maxLong = Double.valueOf(longitude) + deltaLong;

		if (minLong < -180) {
			minLong = minLong + 360;
		}
		if (maxLong > 180) {
			maxLong = 360 - maxLong;
		}

		// Max-min Lattitude calculations
		double deltaLat = (fixedDistance / Rs) * 180 / (Math.PI);

		double minLat = Double.valueOf(lattitude) - deltaLat;
		double maxLat = Double.valueOf(lattitude) + deltaLat;

		DeltaLatLong delta = new DeltaLatLong();
		delta.setMinLattitude(minLat);
		delta.setMaxLattitude(maxLat);
		delta.setMinLongitude(minLong);
		delta.setMaxLongitude(maxLong);

		return delta;
	}

	public static GeoPoint findNearestPoint(String lattitude, String longitude,
			List<GeoPoint> points) {
		Double minDistance = null;
		GeoPoint point = null;

		for (GeoPoint p : points) {
			double d = calculateDistance(Double.valueOf(longitude),
					Double.valueOf(lattitude), p.getLongitude(),
					p.getLatitude());

			if (minDistance == null) {
				minDistance = d;
				point = p;
			} else if (d < minDistance.doubleValue()) {
				point = p;
				minDistance = d;
			}
		}

		return point;
	}

	public static double calculateDistance(Double srcLng, Double srcLat,
			Double destLng, Double destLat) {

		double srcLatRad = srcLat * (Math.PI / 180.0);
		double destLatRad = destLat * (Math.PI / 180.0);
		double deltaLat = (destLat - srcLatRad) * (Math.PI / 180.0);
		double deltaLng = (destLng - srcLng) * (Math.PI / 180.0);

		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
				+ Math.cos(srcLatRad) * Math.cos(destLatRad)
				* Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;
		return distance;
	}

	private static final String key = "Bar12345Bar12345"; // 128 bit key

	public static String encrypt(String text) {

		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		byte[] encrypted = null;

		try {
			Cipher cipher = Cipher.getInstance("AES");

			// encrypt the text
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			encrypted = cipher.doFinal(text.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(encrypted);
	}

	public static String decrypt(String encryptedText) {

		String decrypted = null;

		try {
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES");
			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);

			decrypted = new String(cipher.doFinal(encryptedText.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decrypted;
	}

}
