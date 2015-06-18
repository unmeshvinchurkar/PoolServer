package com.pool.spring.model;

// Generated May 28, 2015 5:38:28 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Vehicle generated by hbm2java
 */
@Entity
@Table(name = "vehicle", catalog = "carpool")
public class Vehicle implements java.io.Serializable {

	private String vehicleId;
	private Users users;
	private String vehicleNo;
	private String vehicleType;
	private String licenseNo;

	public Vehicle() {
	}

	@Id
	@Column(name = "VehicleId", unique = true, nullable = false, length = 100)
	public String getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "Vehicle_no")
	public String getVehicleNo() {
		return this.vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	@Column(name = "vehicle_type", length = 50)
	public String getVehicleType() {
		return this.vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	@Column(name = "license_no", length = 50)
	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

}
