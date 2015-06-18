package com.pool.spring.model;

public class PoolSubscription implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String subscriptionId;
	private String carPoolId;
	private String travellerId;

	public PoolSubscription() {
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getCarPoolId() {
		return carPoolId;
	}

	public void setCarPoolId(String carPoolId) {
		this.carPoolId = carPoolId;
	}

	public String getTravellerId() {
		return travellerId;
	}

	public void setTravellerId(String travellerId) {
		this.travellerId = travellerId;
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
