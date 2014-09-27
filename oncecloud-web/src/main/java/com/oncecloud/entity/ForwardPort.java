package com.oncecloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_pf")
public class ForwardPort {

	private String pfUuid;
	private String pfName;
	private String pfProtocal;
	private Integer pfSourcePort;
	private String pfInteranlIP;
	private Integer pfInternalPort;
	private String	routerUuid;

	@Id
	@Column(name = "pf_uuid")
	public String getPfUuid() {
		return pfUuid;
	}

	public void setPfUuid(String pfUuid) {
		this.pfUuid = pfUuid;
	}

	@Column(name = "pf_name")
	public String getPfName() {
		return pfName;
	}

	public void setPfName(String pfName) {
		this.pfName = pfName;
	}

	@Column(name = "pf_protocal")
	public String getPfProtocal() {
		return pfProtocal;
	}

	public void setPfProtocal(String pfProtocal) {
		this.pfProtocal = pfProtocal;
	}

	@Column(name = "pf_sourceport")
	public Integer getPfSourcePort() {
		return pfSourcePort;
	}

	public void setPfSourcePort(Integer pfSourcePort) {
		this.pfSourcePort = pfSourcePort;
	}

	@Column(name = "pf_internalIP")
	public String getPfInteranlIP() {
		return pfInteranlIP;
	}

	public void setPfInteranlIP(String pfInteranlIP) {
		this.pfInteranlIP = pfInteranlIP;
	}

	@Column(name = "pf_internalport")
	public Integer getPfInternalPort() {
		return pfInternalPort;
	}

	public void setPfInternalPort(Integer pfInternalPort) {
		this.pfInternalPort = pfInternalPort;
	}

	@Column(name = "router_uuid")
	public String getRouterUuid() {
		return routerUuid;
	}

	public void setRouterUuid(String routerUuid) {
		this.routerUuid = routerUuid;
	}

}
