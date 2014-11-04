package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oncecloud.common.main.Utilities;

@Entity
@Table(name = "oc_eip")
public class EIP {
	private String eipIp;
	private String eipUuid;
	private String eipName;
	private Integer eipUID;
	private String eipDependency;
	/**
	 * 绑定类型 0/vm虚拟机 1/lb负载均衡 2/数据库 3/路由器……
	 */
	private Integer depenType;
	private Integer eipBandwidth;
	private String eipInterface;
	private Date createDate;
	private String eipDescription;
	private Integer eipType;
	private String alarmUuid;

	public EIP() {
	}

	@Column(name = "eip_type")
	public Integer getEipType() {
		return eipType;
	}

	public void setEipType(Integer eipType) {
		this.eipType = eipType;
	}

	@Id
	@Column(name = "eip_ip")
	public String getEipIp() {
		return eipIp;
	}

	public void setEipIp(String eipIp) {
		this.eipIp = eipIp;
	}

	@Column(name = "eip_uuid")
	public String getEipUuid() {
		return eipUuid;
	}

	public void setEipUuid(String eipUuid) {
		this.eipUuid = eipUuid;
	}

	@Column(name = "eip_name")
	public String getEipName() {
		return eipName;
	}

	public void setEipName(String eipName) {
		this.eipName = eipName;
	}

	@Column(name = "eip_uid")
	public Integer getEipUID() {
		return eipUID;
	}

	public void setEipUID(Integer eipUID) {
		this.eipUID = eipUID;
	}

	@Column(name = "eip_dependency")
	public String getEipDependency() {
		return eipDependency;
	}

	public void setEipDependency(String eipDependency) {
		this.eipDependency = eipDependency;
	}

	@Column(name = "depen_type")
	public Integer getDepenType() {
		return depenType;
	}

	public void setDepenType(Integer depenType) {
		this.depenType = depenType;
	}

	@Column(name = "eip_bandwidth")
	public Integer getEipBandwidth() {
		return eipBandwidth;
	}

	public void setEipBandwidth(Integer eipBandwidth) {
		this.eipBandwidth = eipBandwidth;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "eip_description")
	public String getEipDescription() {
		return eipDescription;
	}

	public void setEipDescription(String eipDescription) {
		this.eipDescription = eipDescription;
	}

	@Column(name = "eip_interface")
	public String getEipInterface() {
		return eipInterface;
	}

	public void setEipInterface(String eipInterface) {
		this.eipInterface = eipInterface;
	}

	@Column(name = "alarm_uuid")
	public String getAlarmUuid() {
		return alarmUuid;
	}

	public void setAlarmUuid(String alarmUuid) {
		this.alarmUuid = alarmUuid;
	}

	@Override
	public String toString() {
		return "EIP [eipIp=" + eipIp + ", eipUuid=" + eipUuid + ", eipName="
				+ eipName + ", eipUID=" + eipUID + ", eipDependency="
				+ eipDependency + ", depenType=" + depenType
				+ ", eipBandwidth=" + eipBandwidth + ", eipInterface="
				+ eipInterface + ", createDate=" + createDate
				+ ", eipDescription=" + eipDescription + ", eipType=" + eipType
				+ "alarmUuid=" + alarmUuid + "]";
	}

	public EIP(String eipIp, String eipUuid, String eipName, Integer eipUID,
			String eipDependency, Integer depenType, Integer eipBandwidth,
			Date createDate, String eipDescription, Integer eipType) {
		super();
		this.eipIp = eipIp;
		this.eipUuid = eipUuid;
		this.eipName = eipName;
		this.eipUID = eipUID;
		this.eipDependency = eipDependency;
		this.depenType = depenType;
		this.eipBandwidth = eipBandwidth;
		this.createDate = createDate;
		this.eipDescription = eipDescription;
		this.eipType = eipType;
	}
	
	public String toJsonString() {
		return "{'eipIp':'" + eipIp + "', 'eipUuid':'" + eipUuid + "', 'eipName':'" + Utilities.encodeText(eipName) + "', 'eipUID':'" + eipUID
				+ "', 'eipDependency':'" + Utilities.encodeText(eipDependency) + "', 'depenType':'" + depenType + "', 'eipBandwidth':'" + eipBandwidth
				+ "', 'eipInterface':'" + eipInterface + "', 'createDate':'" + Utilities.formatTime(createDate) + "', 'eipDescription':'"
				+ Utilities.encodeText(eipDescription) + "', 'eipType':'" + eipType + "'}";
	}
}
