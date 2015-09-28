package com.pool.spring.model;

public class PoolSubscription implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long subscriptionId;
	private Long carPoolId;
	private Long travellerId;
	private Double pickupLongitute;
	private Double pickupLattitude;
	private Long pickupTime;
	private String pickupAddress;
	private String dropAddress;

	public PoolSubscription() {
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Long getCarPoolId() {
		return carPoolId;
	}

	public void setCarPoolId(Long carPoolId) {
		this.carPoolId = carPoolId;
	}

	public Long getTravellerId() {
		return travellerId;
	}

	public void setTravellerId(Long travellerId) {
		this.travellerId = travellerId;
	}

	public Double getPickupLongitute() {
		return pickupLongitute;
	}

	public void setPickupLongitute(Double pickupLongitute) {
		this.pickupLongitute = pickupLongitute;
	}

	public Double getPickupLattitude() {
		return pickupLattitude;
	}

	public void setPickupLattitude(Double pickupLattitude) {
		this.pickupLattitude = pickupLattitude;
	}

	public Long getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(Long pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public String getDropAddress() {
		return dropAddress;
	}

	public void setDropAddress(String dropAddress) {
		this.dropAddress = dropAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carPoolId == null) ? 0 : carPoolId.hashCode());
		result = prime * result
				+ ((travellerId == null) ? 0 : travellerId.hashCode());
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
		PoolSubscription other = (PoolSubscription) obj;
		if (carPoolId == null) {
			if (other.carPoolId != null)
				return false;
		} else if (!carPoolId.equals(other.carPoolId))
			return false;
		if (travellerId == null) {
			if (other.travellerId != null)
				return false;
		} else if (!travellerId.equals(other.travellerId))
			return false;
		return true;
	}
}
