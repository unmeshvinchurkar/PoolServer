package com.pool.spring.model;

public class Request implements java.io.Serializable {

	private Long requestId;
	private Long requestTypeId;
	private Long fromUserId;
	private Long toUserId;
	private Long carPoolId;
	private Long createDate;
	private Integer seen;
	private Integer processed;
	private Integer status;
	private Double srcLattitude;
	private Double srcLongitude;
	private Double destLattitude;
	private Double destLongitude;
	private Long startTime;
	private Float tripCost;
	private Float pickupDistance;

	public Double getDestLattitude() {
		return destLattitude;
	}

	public void setDestLattitude(Double destLattitude) {
		this.destLattitude = destLattitude;
	}

	public Double getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(Double destLongitude) {
		this.destLongitude = destLongitude;
	}

	public Float getTripCost() {
		return tripCost;
	}

	public void setTripCost(Float tripCost) {
		this.tripCost = tripCost;
	}

	public Float getPickupDistance() {
		return pickupDistance;
	}

	public void setPickupDistance(Float pickupDistance) {
		this.pickupDistance = pickupDistance;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Long requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public Long getCarPoolId() {
		return carPoolId;
	}

	public void setCarPoolId(Long carPoolId) {
		this.carPoolId = carPoolId;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Integer getSeen() {
		return seen;
	}

	public void setSeen(Integer seen) {
		this.seen = seen;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getSrcLattitude() {
		return srcLattitude;
	}

	public void setSrcLattitude(Double srcLattitude) {
		this.srcLattitude = srcLattitude;
	}

	public Double getSrcLongitude() {
		return srcLongitude;
	}

	public void setSrcLongitude(Double srcLongitude) {
		this.srcLongitude = srcLongitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
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
		Request other = (Request) obj;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		return true;
	}

}
