package com.pool.spring.model;

public class UserCalendarDay implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long userCalendarDayId;
	private Long userId;
	private Long calendarDay;
	private Long carPoolId;

	public UserCalendarDay() {
	}

	public Long getUserCalendarDayId() {
		return userCalendarDayId;
	}

	public void setUserCalendarDayId(Long userCalendarDayId) {
		this.userCalendarDayId = userCalendarDayId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCalendarDay() {
		return calendarDay;
	}

	public void setCalendarDay(Long calendarDay) {
		this.calendarDay = calendarDay;
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
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserCalendarDay other = (UserCalendarDay) obj;
		if (carPoolId == null) {
			if (other.carPoolId != null)
				return false;
		} else if (!carPoolId.equals(other.carPoolId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
