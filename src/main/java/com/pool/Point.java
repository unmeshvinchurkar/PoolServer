package com.pool;

public class Point {

	private Double lattitude = null;
	private Double longitude = null;
	private Long timeToReach = null;
	private Long distanceFromStart = null;

	public Point(Double lattitude, Double longitude) {
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public Point(Double lattitude, Double longitude, Long timeToReach) {
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.timeToReach = timeToReach;
	}
	public Point(Double lattitude, Double longitude, Long timeToReach, Long distanceFromStart) {
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.timeToReach = timeToReach;
		this.distanceFromStart = distanceFromStart;
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

	public Long getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(Long distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}

	@Override
	public String toString() {
		return this.lattitude + "," + this.longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lattitude == null) ? 0 : lattitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (lattitude == null) {
			if (other.lattitude != null)
				return false;
		} else if (!lattitude.equals(other.lattitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}

}