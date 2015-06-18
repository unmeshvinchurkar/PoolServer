package com.pool;

import java.util.Comparator;

import com.pool.spring.model.GeoPoint;

public class DistanceComparator implements Comparator<GeoPoint> {

	private Double lattitude = null;
	private Double longitude = null;

	public DistanceComparator(Double longitude, Double lattitude) {
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public int compare(GeoPoint a, GeoPoint b) {

		double d1 = PoolUtils.calculateDistance(this.longitude,
				this.lattitude, a.getLongitude(), a.getLatitude());
		double d2 = PoolUtils.calculateDistance(this.longitude,
				this.lattitude, b.getLongitude(), b.getLatitude());

		if (d1 >= d2) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}