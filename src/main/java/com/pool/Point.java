package com.pool;

public class Point {

	private String lattitude = null;
	private String longitude = null;

	public Point(String lattitude, String longitude) {
		super();
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public String getLattitude() {
		return lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return this.lattitude + "," + this.longitude;
	}

}