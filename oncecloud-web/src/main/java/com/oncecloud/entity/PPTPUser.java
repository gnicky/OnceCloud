package com.oncecloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_pptp_user")
public class PPTPUser {

	private Integer pptpId;
	private String pptpName;
	private String pptpPwd;
	private String routerUuid;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "pptp_id")
	public Integer getPptpId() {
		return pptpId;
	}

	public void setPptpId(Integer pptpId) {
		this.pptpId = pptpId;
	}

	@Column(name = "pptp_name")
	public String getPptpName() {
		return pptpName;
	}

	public void setPptpName(String pptpName) {
		this.pptpName = pptpName;
	}

	@Column(name = "pptp_pwd")
	public String getPptpPwd() {
		return pptpPwd;
	}

	public void setPptpPwd(String pptpPwd) {
		this.pptpPwd = pptpPwd;
	}

	@Column(name = "router_uuid")
	public String getRouterUuid() {
		return routerUuid;
	}

	public void setRouterUuid(String routerUuid) {
		this.routerUuid = routerUuid;
	}

}
