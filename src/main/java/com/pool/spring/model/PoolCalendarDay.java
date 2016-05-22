package com.pool.spring.model;

public class PoolCalendarDay implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long poolCalendarId;
	private Long carPoolId;
	private Long date;
	private Integer isHoliday = 0;
	private Integer noOfAvblSeats;
	private Integer noOfTravellers;
	private Carpool carPool;

	public PoolCalendarDay() {
	}

	public Carpool getCarPool() {
		return carPool;
	}

	public void setCarPool(Carpool carPool) {
		this.carPool = carPool;
	}

	public Long getPoolCalendarId() {
		return poolCalendarId;
	}

	public void setPoolCalendarId(Long poolCalendarId) {
		this.poolCalendarId = poolCalendarId;
	}

	public Long getCarPoolId() {
		return carPoolId;
	}

	public void setCarPoolId(Long carPoolId) {
		this.carPoolId = carPoolId;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public Integer getIsHoliday() {
		return isHoliday;
	}

	public void setIsHoliday(Integer isHoliday) {
		this.isHoliday = isHoliday;
	}

	public Integer getNoOfAvblSeats() {
		return noOfAvblSeats;
	}

	public void setNoOfAvblSeats(Integer noOfAvblSeats) {
		this.noOfAvblSeats = noOfAvblSeats;
	}

	public Integer getNoOfTravellers() {
		return noOfTravellers;
	}

	public void setNoOfTravellers(Integer noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carPool == null) ? 0 : carPool.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		PoolCalendarDay other = (PoolCalendarDay) obj;
		if (carPool == null) {
			if (other.carPool != null)
				return false;
		} else if (!carPool.equals(other.carPool))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}
}
