package com.pool.spring.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Carpool implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long carPoolId;
	private String vehicleId;
	private String ownerId;
	private String carpoolName;
	private String srcLongitude;
	private String srcLattitude;
	private String destLongitude;
	private String destLattitude;
	private String path;
	private Date startTime;
	private Date exptdEndTime;
	private Date createDate;
	private Date startDate;
	private Date endDate;
	private List geoPoints = new ArrayList();;
	private Integer deleted = null;
	private String srcArea;
	private String destArea;

	public Carpool() {
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getCarpoolName() {
		return carpoolName;
	}

	public void setCarpoolName(String carpoolName) {
		this.carpoolName = carpoolName;
	}

	public String getSrcLongitude() {
		return srcLongitude;
	}

	public void setSrcLongitude(String srcLongitude) {
		this.srcLongitude = srcLongitude;
	}

	public String getSrcLattitude() {
		return srcLattitude;
	}

	public void setSrcLattitude(String srcLattitude) {
		this.srcLattitude = srcLattitude;
	}

	public String getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(String destLongitude) {
		this.destLongitude = destLongitude;
	}

	public String getDestLattitude() {
		return destLattitude;
	}

	public void setDestLattitude(String destLattitude) {
		this.destLattitude = destLattitude;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExptdEndTime() {
		return exptdEndTime;
	}

	public void setExptdEndTime(Date exptdEndTime) {
		this.exptdEndTime = exptdEndTime;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
