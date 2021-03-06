package com.pool.spring.model;

// Generated May 28, 2015 5:38:28 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Carpool generated by hbm2java
 */
@Entity
@Table(name = "carpool", catalog = "carpool")
public class Carpool implements java.io.Serializable {

	@Id
	@Column(name = "carPoolId", unique = true, nullable = false, length = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String carPoolId;
	private Users users;
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

	public Carpool() {
	}

	public String getCarPoolId() {
		return this.carPoolId;
	}

	public void setCarPoolId(String carPoolId) {
		this.carPoolId = carPoolId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "carpool_name", length = 100)
	public String getCarpoolName() {
		return this.carpoolName;
	}

	public void setCarpoolName(String carpoolName) {
		this.carpoolName = carpoolName;
	}

	@Column(name = "src_longitude", nullable = false, length = 10)
	public String getSrcLongitude() {
		return this.srcLongitude;
	}

	public void setSrcLongitude(String srcLongitude) {
		this.srcLongitude = srcLongitude;
	}

	@Column(name = "src_lattitude", nullable = false, length = 10)
	public String getSrcLattitude() {
		return this.srcLattitude;
	}

	public void setSrcLattitude(String srcLattitude) {
		this.srcLattitude = srcLattitude;
	}

	@Column(name = "dest_longitude", nullable = false, length = 10)
	public String getDestLongitude() {
		return this.destLongitude;
	}

	public void setDestLongitude(String destLongitude) {
		this.destLongitude = destLongitude;
	}

	@Column(name = "dest_lattitude", nullable = false, length = 10)
	public String getDestLattitude() {
		return this.destLattitude;
	}

	public void setDestLattitude(String destLattitude) {
		this.destLattitude = destLattitude;
	}

	@Column(name = "path", nullable = false)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = false, length = 0)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "exptd_end_time", nullable = false, length = 0)
	public Date getExptdEndTime() {
		return this.exptdEndTime;
	}

	public void setExptdEndTime(Date exptdEndTime) {
		this.exptdEndTime = exptdEndTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date", nullable = false, length = 0)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", nullable = false, length = 0)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", length = 0)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
