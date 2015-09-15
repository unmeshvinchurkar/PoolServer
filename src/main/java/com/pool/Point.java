package com.pool;

public class Point {

	private Double lattitude = null;
	private Double longitude = null;

	public Point(Double lattitude, Double longitude) {
		super();
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public Double getLattitude() {
		return lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return this.lattitude + "," + this.longitude;
	}

}