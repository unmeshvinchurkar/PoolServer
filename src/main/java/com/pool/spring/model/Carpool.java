package com.pool.spring.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Carpool implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long carPoolId;
	private String vehicleId;
	private Long ownerId;
	private String carpoolName;
	private Double srcLongitude;
	private Double srcLattitude;
	private Double destLongitude;
	private Double destLattitude;
	private String path;
	private Long startTime;
	private Long exptdEndTime;
	private Long createDate;
	private Long startDate;
	private Long endDate;
	private List geoPoints = new ArrayList();;
	private Integer deleted = null;
	private String srcArea;
	private String destArea;
	private Integer noOfAvblSeats;
	private Integer bucksPerKm;
	private Integer noOfRemainingSeats;
	private Set<PoolCalendarDay> calendarDays = null;
	private String excludedDays;

	public Carpool() {
	}

	public String getExcludedDays() {
		return excludedDays;
	}

	public void setExcludedDays(String excludedDays) {
		this.excludedDays = excludedDays;
	}

	public Integer getBucksPerKm() {
		return bucksPerKm;
	}

	public void setBucksPerKm(Integer bucksPerKm) {
		this.bucksPerKm = bucksPerKm;
	}

	public Integer getNoOfRemainingSeats() {
		return noOfRemainingSeats;
	}

	public void setNoOfRemainingSeats(Integer noOfRemainingSeats) {
		this.noOfRemainingSeats = noOfRemainingSeats;
	}

	public Set<PoolCalendarDay> getCalendarDays() {
		return calendarDays;
	}

	public void setCalendarDays(Set<PoolCalendarDay> calendarDays) {
		this.calendarDays = calendarDays;
	}

	public String getSrcArea() {
		return srcArea;
	}

	public void setSrcArea(String srcArea) {
		this.srcArea = srcArea;
	}

	public String getDestArea() {
		return destArea;
	}

	public void setDestArea(String destArea) {
		this.destArea = destArea;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public List getGeoPoints() {
		return geoPoints;
	}

	public void setGeoPoints(List geoPoints) {
		this.geoPoints = geoPoints;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Long getCarPoolId() {
		return carPoolId;
	}

	public void setCarPoolId(Long carPoolId) {
		this.carPoolId = carPoolId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getCarpoolName() {
		return carpoolName;
	}

	public void setCarpoolName(String carpoolName) {
		this.carpoolName = carpoolName;
	}

	public Double getSrcLongitude() {
		return srcLongitude;
	}

	public void setSrcLongitude(Double srcLongitude) {
		this.srcLongitude = srcLongitude;
	}

	public Double getSrcLattitude() {
		return srcLattitude;
	}

	public void setSrcLattitude(Double srcLattitude) {
		this.srcLattitude = srcLattitude;
	}

	public Double getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(Double destLongitude) {
		this.destLongitude = destLongitude;
	}

	public Double getDestLattitude() {
		return destLattitude;
	}

	public void setDestLattitude(Double destLattitude) {
		this.destLattitude = destLattitude;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getExptdEndTime() {
		return exptdEndTime;
	}

	public void setExptdEndTime(Long exptdEndTime) {
		this.exptdEndTime = exptdEndTime;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Integer getNoOfAvblSeats() {
		return noOfAvblSeats;
	}

	public void setNoOfAvblSeats(Integer noOfAvblSeats) {
		this.noOfAvblSeats = noOfAvblSeats;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destLattitude == null) ? 0 : destLattitude.hashCode());
		result = prime * result
				+ ((destLongitude == null) ? 0 : destLongitude.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result
				+ ((srcLattitude == null) ? 0 : srcLattitude.hashCode());
		result = prime * result
				+ ((srcLongitude == null) ? 0 : srcLongitude.hashCode());
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
		Carpool other = (Carpool) obj;
		if (destLattitude == null) {
			if (other.destLattitude != null)
				return false;
		} else if (!destLattitude.equals(other.destLattitude))
			return false;
		if (destLongitude == null) {
			if (other.destLongitude != null)
				return false;
		} else if (!destLongitude.equals(other.destLongitude))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (srcLattitude == null) {
			if (other.srcLattitude != null)
				return false;
		} else if (!srcLattitude.equals(other.srcLattitude))
			return false;
		if (srcLongitude == null) {
			if (other.srcLongitude != null)
				return false;
		} else if (!srcLongitude.equals(other.srcLongitude))
			return false;
		return true;
	}

}
