package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xpx
 * @version 2014/08/23
 */
@Entity
@Table(name = "oc_router")
public class Router {
	private String routerUuid;
	private String routerPWD;
	private Integer routerUID;
	private String routerName;
	private String routerDesc;
	private String routerIP;
	private String routerMac;
	private Integer routerCapacity;
	private Integer routerImportance;
	private Integer routerPower;
	// routerStatus 0=伪删除 1=正常使用 2=修改后尚未提交
	private Integer routerStatus;
	private String hostUuid;
	private String firewallUuid;
	private Date createDate;
	private String alarmUuid;
	private String innerFirewallUuid;
	private Integer pptp;

	public Router() {
	}

	public Router(String uuid, String pwd, Integer userId, String name, String mac,
			Integer capacity, Integer power, Integer status, String firewallUuid,
			Date createDate) {
		this.routerUuid = uuid;
		this.routerPWD = pwd;
		this.routerUID = userId;
		this.routerName = name;
		this.routerMac = mac;
		this.routerCapacity = capacity;
		this.routerPower = power;
		this.routerStatus = status;
		this.firewallUuid = firewallUuid;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "rt_uuid")
	public String getRouterUuid() {
		return routerUuid;
	}

	public void setRouterUuid(String routerUuid) {
		this.routerUuid = routerUuid;
	}

	@Column(name = "rt_pwd")
	public String getRouterPWD() {
		return routerPWD;
	}

	public void setRouterPWD(String routerPWD) {
		this.routerPWD = routerPWD;
	}

	@Column(name = "rt_uid")
	public Integer getRouterUID() {
		return routerUID;
	}

	public void setRouterUID(Integer routerUID) {
		this.routerUID = routerUID;
	}

	@Column(name = "rt_name")
	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	@Column(name = "rt_desc")
	public String getRouterDesc() {
		return routerDesc;
	}

	public void setRouterDesc(String routerDesc) {
		this.routerDesc = routerDesc;
	}

	@Column(name = "rt_ip")
	public String getRouterIP() {
		return routerIP;
	}

	public void setRouterIP(String routerIP) {
		this.routerIP = routerIP;
	}

	@Column(name = "rt_mac")
	public String getRouterMac() {
		return routerMac;
	}

	public void setRouterMac(String routerMac) {
		this.routerMac = routerMac;
	}

	@Column(name = "rt_capacity")
	public Integer getRouterCapacity() {
		return routerCapacity;
	}

	public void setRouterCapacity(Integer routerCapacity) {
		this.routerCapacity = routerCapacity;
	}

	@Column(name = "rt_power")
	public Integer getRouterPower() {
		return routerPower;
	}

	public void setRouterPower(Integer routerPower) {
		this.routerPower = routerPower;
	}

	@Column(name = "rt_status")
	public Integer getRouterStatus() {
		return routerStatus;
	}

	public void setRouterStatus(Integer routerStatus) {
		this.routerStatus = routerStatus;
	}

	@Column(name = "host_uuid")
	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

	@Column(name = "rt_firewall")
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

	@Column(name = "rt_importance")
	public Integer getRouterImportance() {
		return routerImportance;
	}
	
	public void setRouterImportance(Integer routerImportance) {
		this.routerImportance = routerImportance;
	}

	@Column(name = "rt_innerfirewall")
	public String getInnerFirewallUuid() {
		return innerFirewallUuid;
	}

	public void setInnerFirewallUuid(String innerFirewallUuid) {
		this.innerFirewallUuid = innerFirewallUuid;
	}
	
	@Column(name = "rt_pptp")
	public Integer getPptp() {
		return pptp;
	}

	public void setPptp(Integer pptp) {
		this.pptp = pptp;
	}

	@Override
	public String toString() {
		return "RT [routerUuid=" + routerUuid + ", routerPWD=" + routerPWD
				+ ", routerUID=" + routerUID + ", routerName=" + routerName
				+ ", routerDesc=" + routerDesc + ", routerIP=" + routerIP
				+ ", routerMac=" + routerMac + ", routerCapacity="
				+ routerCapacity + ", routerPower=" + routerPower
				+ ", routerStatus=" + routerStatus + ", hostUuid=" + hostUuid
				+ ", firewallUuid=" + firewallUuid + ", createDate="
				+ createDate + "alarmUuid=" + alarmUuid + "]";
	}
}
