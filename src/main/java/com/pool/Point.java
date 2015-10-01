package com.pool;

public class Point {

	private Double lattitude = null;
	private Double longitude = null;
	private Long timeToReach = null;
	
	public Point(Double lattitude, Double longitude) {
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public Point(Double lattitude, Double longitude, Long timeToReach) {
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.timeToReach = timeToReach;
	}

	public Long getTimeToReach() {
		return timeToReach;
	}

	public void setTimeToReach(Long timeToReach) {
		this.timeToReach = timeToReach;
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