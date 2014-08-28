package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oc_db")
public class Database {

	private String databaseUuid;
	private String databaseName;
	private Integer databaseUID;
	private String databaseUser;
	private String databasePwd;
	private String databaseType;
	private Integer databaseThroughout;
	private String databaseMac;
	private String databaseIp;
	private Integer databasePort;
	private Integer databasePower;
	private Integer databaseStatus;
	private Date createDate;
	private String hostUuid;

	public Database() {
	}

	public Database(String databaseUuid, String databaseName,
			Integer databaseUID, String databaseUser, String databasePwd,
			String databaseType, Integer databaseThroughout,
			String databaseMac, String databaseIp, Integer databasePort,
			Integer databasePower, Integer databaseStatus, Date createDate) {
		this.databaseUuid = databaseUuid;
		this.databaseName = databaseName;
		this.databaseUID = databaseUID;
		this.databaseUser = databaseUser;
		this.databasePwd = databasePwd;
		this.databaseType = databaseType;
		this.databaseThroughout = databaseThroughout;
		this.databaseMac = databaseMac;
		this.databaseIp = databaseIp;
		this.databasePort = databasePort;
		this.databasePower = databasePower;
		this.databaseStatus = databaseStatus;
		this.createDate = createDate;
	}

	@Column(name = "database_uuid")
	public String getDatabaseUuid() {
		return databaseUuid;
	}

	public void setDatabaseUuid(String databaseUuid) {
		this.databaseUuid = databaseUuid;
	}

	@Column(name = "database_uid")
	public Integer getDatabaseUID() {
		return databaseUID;
	}

	public void setDatabaseUID(Integer databaseUID) {
		this.databaseUID = databaseUID;
	}

	@Column(name = "database_user")
	public String getDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	@Column(name = "database_pwd")
	public String getDatabasePwd() {
		return databasePwd;
	}

	public void setDatabasePwd(String databasePwd) {
		this.databasePwd = databasePwd;
	}

	@Column(name = "database_type")
	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	@Column(name = "database_throughout")
	public Integer getDatabaseThroughout() {
		return databaseThroughout;
	}

	public void setDatabaseThroughout(Integer databaseThroughout) {
		this.databaseThroughout = databaseThroughout;
	}

	@Column(name = "database_ip")
	public String getDatabaseIp() {
		return databaseIp;
	}

	public void setDatabaseIp(String databaseIp) {
		this.databaseIp = databaseIp;
	}

	@Column(name = "database_power")
	public Integer getDatabasePower() {
		return databasePower;
	}

	public void setDatabasePower(Integer databasePower) {
		this.databasePower = databasePower;
	}

	@Column(name = "database_status")
	public Integer getDatabaseStatus() {
		return databaseStatus;
	}

	public void setDatabaseStatus(Integer databaseStatus) {
		this.databaseStatus = databaseStatus;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "host_uuid")
	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

	@Column(name = "database_name")
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Column(name = "database_mac")
	public String getDatabaseMac() {
		return databaseMac;
	}

	public void setDatabaseMac(String databaseMac) {
		this.databaseMac = databaseMac;
	}

	@Column(name = "database_port")
	public Integer getDatabasePort() {
		return databasePort;
	}

	public void setDatabasePort(Integer databasePort) {
		this.databasePort = databasePort;
	}

	@Override
	public String toString() {
		return "Database [databaseUuid=" + databaseUuid + ", databaseName="
				+ databaseName + ", databaseUID=" + databaseUID
				+ ", databaseUser=" + databaseUser + ", databasePwd="
				+ databasePwd + ", databaseType=" + databaseType
				+ ", databaseThroughout=" + databaseThroughout
				+ ", databaseMac=" + databaseMac + ", databaseIp=" + databaseIp
				+ ", databasePort=" + databasePort + ", databasePower="
				+ databasePower + ", databaseStatus=" + databaseStatus
				+ ", createDate=" + createDate + ", hostUuid=" + hostUuid + "]";
	}
}
