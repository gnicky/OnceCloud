package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_dhcp")
public class DHCP {
	private String dhcpMac;
	private String dhcpIp;
	private String tenantUuid;
	/**
	 * 租用者角色 0/vm虚拟机 1/lb负载均衡 2/rt路由器
	 */
	private Integer depenType;
	private Date createDate;

	public DHCP() {
	}

	@Id
	@Column(name = "dhcp_mac")
	public String getDhcpMac() {
		return dhcpMac;
	}

	public void setDhcpMac(String dhcpMac) {
		this.dhcpMac = dhcpMac;
	}

	@Column(name = "dhcp_ip")
	public String getDhcpIp() {
		return dhcpIp;
	}

	public void setDhcpIp(String dhcpIp) {
		this.dhcpIp = dhcpIp;
	}

	@Column(name = "tenant_uuid")
	public String getTenantUuid() {
		return tenantUuid;
	}

	public void setTenantUuid(String tenantUuid) {
		this.tenantUuid = tenantUuid;
	}

	@Column(name = "depen_type")
	public Integer getDepenType() {
		return depenType;
	}

	public void setDepenType(Integer depenType) {
		this.depenType = depenType;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "DHCP [dhcpMac=" + dhcpMac + ", dhcpIp=" + dhcpIp
				+ ", tenantUuid=" + tenantUuid + ", depenType=" + depenType
				+ ", createDate=" + createDate + "]";
	}

}
