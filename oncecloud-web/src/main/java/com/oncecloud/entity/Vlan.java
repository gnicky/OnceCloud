package com.oncecloud.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cyh
 * @version 2014/03/28 Vlan表
 */
@Entity
@Table(name = "oc_vlan")
public class Vlan {
	private String vlanUuid;
	private String vlanName;
	private String vlanDesc;
	private String swUuid;

	public Vlan() {
	}

	@Override
	public String toString() {
		return "Vlan [vlanUuid=" + vlanUuid + ", valnName=" + vlanName
				+ ", vlanDesc=" + vlanDesc + ", swUuid=" + swUuid + "]";
	}

	@Id
	@Column(name = "vlan_uuid")
	public String getVlanUuid() {
		return vlanUuid;
	}

	public void setVlanUuid(String vlanUuid) {
		this.vlanUuid = vlanUuid;
	}

	@Column(name = "vlan_name")
	public String getVlanName() {
		return vlanName;
	}

	public void setVlanName(String vlanName) {
		this.vlanName = vlanName;
	}

	@Column(name = "vlan_desc")
	public String getVlanDesc() {
		return vlanDesc;
	}

	public void setVlanDesc(String vlanDesc) {
		this.vlanDesc = vlanDesc;
	}

	@Column(name = "switch_uuid")
	public String getSwUuid() {
		return swUuid;
	}

	public void setSwUuid(String swUuid) {
		this.swUuid = swUuid;
	}

	public Vlan(String vlanUuid, String vlanName, String vlanDesc, String swUuid) {
		super();
		this.vlanUuid = vlanUuid;
		this.vlanName = vlanName;
		this.vlanDesc = vlanDesc;
		this.swUuid = swUuid;
	}

	public String toJsonString() {
		try {
			return "{'vlanUuid':'" + vlanUuid + "', 'valnName':'"
					+ URLEncoder.encode(vlanName, "utf-8") + "', 'vlanDesc':'"
					+ URLEncoder.encode(vlanDesc, "utf-8") + "', 'swUuid':'"
					+ swUuid + "'}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
