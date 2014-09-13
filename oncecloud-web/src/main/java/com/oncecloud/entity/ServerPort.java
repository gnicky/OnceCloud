package com.oncecloud.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cyh
 * @version 2014/03/28 服务器网口表
 */
@Entity
@Table(name = "oc_serverport")
public class ServerPort {
	private String serverportUuid;
	private String serverportName;
	private String hostUuid;
	private String protType;

	public ServerPort() {
	}

	@Override
	public String toString() {
		return "ServerPort [serverportUuid=" + serverportUuid
				+ ", serverportName=" + serverportName + ", hostUuid="
				+ hostUuid + ", protType=" + protType + "]";
	}

	@Id
	@Column(name = "serverport_uuid")
	public String getServerportUuid() {
		return serverportUuid;
	}

	public void setServerportUuid(String serverportUuid) {
		this.serverportUuid = serverportUuid;
	}

	@Column(name = "serverport_name")
	public String getServerportName() {
		return serverportName;
	}

	public void setServerportName(String serverportName) {
		this.serverportName = serverportName;
	}

	@Column(name = "host_uuid")
	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

	@Column(name = "prot_type")
	public String getProtType() {
		return protType;
	}

	public void setProtType(String protType) {
		this.protType = protType;
	}

	public ServerPort(String serverportUuid, String serverportName,
			String hostUuid, String protType) {
		super();
		this.serverportUuid = serverportUuid;
		this.serverportName = serverportName;
		this.hostUuid = hostUuid;
		this.protType = protType;
	}

	public String toJsonString() {
		try {
			return "{'serverportUuid':'" + serverportUuid
					+ "', 'serverportName':'"
					+ URLEncoder.encode(serverportName, "utf-8")
					+ "', 'hostUuid':'" + hostUuid + "', 'protType':'"
					+ protType + "'}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
