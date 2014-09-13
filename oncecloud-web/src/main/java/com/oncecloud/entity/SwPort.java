package com.oncecloud.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cyh
 * @version 2014/03/28 交换机网口表
 */
@Entity
@Table(name = "oc_port")
public class SwPort {
	private String portUuid;
	private String portId;
	private Integer portNum;
	private Integer portState;
	private Integer portUsing;
	private String serverportUuid;
	private String portType;
	private String vlanUuid;

	public SwPort() {
	}

	public SwPort(String portUuid, String portId, Integer portNum,
			Integer portState, Integer portUsing, String serverportUuid,
			String portType, String vlanUuid) {
		super();
		this.portUuid = portUuid;
		this.portId = portId;
		this.portNum = portNum;
		this.portState = portState;
		this.portUsing = portUsing;
		this.serverportUuid = serverportUuid;
		this.portType = portType;
		this.vlanUuid = vlanUuid;
	}

	@Override
	public String toString() {
		return "SwPort [portUuid=" + portUuid + ", portId=" + portId
				+ ", portNum=" + portNum + ", portState=" + portState
				+ ", portUsing=" + portUsing + ", serverportUuid="
				+ serverportUuid + ", portType=" + portType + ", vlanUuid="
				+ vlanUuid + "]";
	}

	@Id
	@Column(name = "port_uuid")
	public String getPortUuid() {
		return portUuid;
	}

	public void setPortUuid(String portUuid) {
		this.portUuid = portUuid;
	}

	@Column(name = "port_id")
	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	@Column(name = "port_num")
	public Integer getPortNum() {
		return portNum;
	}

	public void setPortNum(Integer portNum) {
		this.portNum = portNum;
	}

	@Column(name = "port_state")
	public Integer getPortState() {
		return portState;
	}

	public void setPortState(Integer portState) {
		this.portState = portState;
	}

	@Column(name = "port_using")
	public Integer getPortUsing() {
		return portUsing;
	}

	public void setPortUsing(Integer portUsing) {
		this.portUsing = portUsing;
	}

	@Column(name = "serverport_uuid")
	public String getServerportUuid() {
		return serverportUuid;
	}

	public void setServerportUuid(String serverportUuid) {
		this.serverportUuid = serverportUuid;
	}

	@Column(name = "port_type")
	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	@Column(name = "vlan_uuid")
	public String getVlanUuid() {
		return vlanUuid;
	}

	public void setVlanUuid(String vlanUuid) {
		this.vlanUuid = vlanUuid;
	}

	public String toJsonString() {
		try {
			return "{'portUuid':'" + portUuid + "', 'portId':'"
					+ URLEncoder.encode(portId, "utf-8") + "', 'portNum':'"
					+ portNum + "', 'portState':'" + portState
					+ "', 'portUsing':'" + portUsing + "', 'serverportUuid':'"
					+ serverportUuid + "', 'portType':'" + portType
					+ "', 'vlanUuid':'" + vlanUuid + "'}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
