package com.pool.spring.model;

public class GeoPoint implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long pointId;
	private Double latitude;
	private Double longitude;
	private int pointOrder;
	private Long carPoolId;

	public GeoPoint() {
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public int getPointOrder() {
		return pointOrder;
	}

	public void setPointOrder(int pointOrder) {
		this.pointOrder = pointOrder;
	}

	public Long getCarPoolId() {
		return carPoolId;
	}

	public void setCarPoolId(Long carPoolId) {
		this.carPoolId = carPoolId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carPoolId == null) ? 0 : carPoolId.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
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
		GeoPoint other = (GeoPoint) obj;
		if (carPoolId == null) {
			if (other.carPoolId != null)
				return false;
		} else if (!carPoolId.equals(other.carPoolId))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}

}
