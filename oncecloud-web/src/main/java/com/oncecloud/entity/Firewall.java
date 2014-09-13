package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_firewall")
public class Firewall {

	private String firewallId;
	private String firewallName;
	private Integer firewallUID;
	private Date createDate;
	private Integer isConfirm;
	private Integer isDefault;

	public Firewall() {
	}

	public Firewall(String firewallId, String firewallName,
			Integer firewallUID, Date createDate, Integer isConfirm,
			Integer isDefault) {
		this.firewallId = firewallId;
		this.firewallName = firewallName;
		this.firewallUID = firewallUID;
		this.createDate = createDate;
		this.isConfirm = isConfirm;
		this.isDefault = isDefault;
	}

	@Id
	@Column(name = "firewall_id")
	public String getFirewallId() {
		return firewallId;
	}

	public void setFirewallId(String firewallId) {
		this.firewallId = firewallId;
	}

	@Column(name = "firewall_name")
	public String getFirewallName() {
		return firewallName;
	}

	public void setFirewallName(String firewallName) {
		this.firewallName = firewallName;
	}

	@Column(name = "firewall_uid")
	public Integer getFirewallUID() {
		return firewallUID;
	}

	public void setFirewallUID(Integer firewallUID) {
		this.firewallUID = firewallUID;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "firewall_confirm")
	public Integer getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}

	@Column(name = "is_default")
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

}
