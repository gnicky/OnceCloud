package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Entity
@Table(name = "oc_lb")
public class LB {
	private String lbUuid;
	private String lbPWD;
	private Integer lbUID;
	private String lbName;
	private String lbDesc;
	private String lbIP;
	private String lbMac;
	private Integer lbCapacity;
	private Integer lbPower;
	// lbStatus 0=伪删除 1=正常使用 2=修改后尚未提交
	private Integer lbStatus;
	private Integer lbImportance;
	private String hostUuid;
	private String firewallUuid;
	private Date createDate;
	private String alarmUuid;

	public LB() {
	}

	public LB(String uuid, String pwd, Integer userId, String name, String mac,
			Integer capacity, Integer power, Integer status, Date createDate) {
		this.lbUuid = uuid;
		this.lbPWD = pwd;
		this.lbUID = userId;
		this.lbName = name;
		this.lbMac = mac;
		this.lbCapacity = capacity;
		this.lbPower = power;
		this.lbStatus = status;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "lb_uuid")
	public String getLbUuid() {
		return lbUuid;
	}

	public void setLbUuid(String lbUuid) {
		this.lbUuid = lbUuid;
	}

	@Column(name = "lb_pwd")
	public String getLbPWD() {
		return lbPWD;
	}

	public void setLbPWD(String lbPWD) {
		this.lbPWD = lbPWD;
	}

	@Column(name = "lb_uid")
	public Integer getLbUID() {
		return lbUID;
	}

	public void setLbUID(Integer lbUID) {
		this.lbUID = lbUID;
	}

	@Column(name = "lb_name")
	public String getLbName() {
		return lbName;
	}

	public void setLbName(String lbName) {
		this.lbName = lbName;
	}

	@Column(name = "lb_desc")
	public String getLbDesc() {
		return lbDesc;
	}

	public void setLbDesc(String lbDesc) {
		this.lbDesc = lbDesc;
	}

	@Column(name = "lb_ip")
	public String getLbIP() {
		return lbIP;
	}

	public void setLbIP(String lbIP) {
		this.lbIP = lbIP;
	}

	@Column(name = "lb_mac")
	public String getLbMac() {
		return lbMac;
	}

	public void setLbMac(String lbMac) {
		this.lbMac = lbMac;
	}

	@Column(name = "lb_capacity")
	public Integer getLbCapacity() {
		return lbCapacity;
	}

	public void setLbCapacity(Integer lbCapacity) {
		this.lbCapacity = lbCapacity;
	}

	@Column(name = "lb_power")
	public Integer getLbPower() {
		return lbPower;
	}

	public void setLbPower(Integer lbPower) {
		this.lbPower = lbPower;
	}

	@Column(name = "lb_status")
	public Integer getLbStatus() {
		return lbStatus;
	}

	public void setLbStatus(Integer lbStatus) {
		this.lbStatus = lbStatus;
	}

	@Column(name = "host_uuid")
	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

	@Column(name = "lb_firewall")
	public String getFirewallUuid() {
		return firewallUuid;
	}

	public void setFirewallUuid(String firewallUuid) {
		this.firewallUuid = firewallUuid;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "alarm_uuid")
	public String getAlarmUuid() {
		return alarmUuid;
	}

	public void setAlarmUuid(String alarmUuid) {
		this.alarmUuid = alarmUuid;
	}

	@Column(name = "lb_importance")
	public Integer getLbImportance() {
		return lbImportance;
	}

	public void setLbImportance(Integer lbImportance) {
		this.lbImportance = lbImportance;
	}

	@Override
	public String toString() {
		return "LB [lbUuid=" + lbUuid + ", lbPWD=" + lbPWD + ", lbUID=" + lbUID
				+ ", lbName=" + lbName + ", lbDesc=" + lbDesc + ", lbIP="
				+ lbIP + ", lbMac=" + lbMac + ", lbCapacity=" + lbCapacity
				+ ", lbPower=" + lbPower + ", lbStatus=" + lbStatus
				+ ", hostUuid=" + hostUuid + ", firewallUuid=" + firewallUuid
				+ ", createDate=" + createDate + "alarmUuid=" + alarmUuid + "]";
	}
}
