package com.pool;

public class DeltaLatLong {

	Double minLongitude;
	Double maxLongitude;
	Double minLattitude;
	Double maxLattitude;

	public DeltaLatLong() {

	}

	public DeltaLatLong(Double minLongitude, Double maxLongitude,
			Double minLattitude, Double maxLattitude) {
		super();
		this.minLongitude = minLongitude;
		this.maxLongitude = maxLongitude;
		this.minLattitude = minLattitude;
		this.maxLattitude = maxLattitude;
	}

	public Double getMaxLongitude() {
		return maxLongitude;
	}

	public Double getMinLongitude() {
		return minLongitude;
	}

	public Double getMinLattitude() {
		return minLattitude;
	}

	public Double getMaxLattitude() {
		return maxLattitude;
	}

	public void setMaxLongitude(Double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public void setMinLongitude(Double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public void setMinLattitude(Double minLattitude) {
		this.minLattitude = minLattitude;
	}

	public void setMaxLattitude(Double maxLattitude) {
		this.maxLattitude = maxLattitude;
	}

	@Override
	public String toString() {
		return "DeltaLatLong [minLongitude=" + minLongitude + ", maxLongitude="
				+ maxLongitude + ", minLattitude=" + minLattitude
				+ ", maxLattitude=" + maxLattitude + "]";
	}
	

}
