package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_back_end")
public class Backend {
	private String backUuid;
	private String backName;
	/**
	 * vmUuid 对应主机id
	 */
	private String vmUuid;
	/**
	 * vmIp 对应主机的ip
	 */
	private String vmIp;
	private Integer vmPort;
	/**
	 * 权重
	 */
	private Integer backWeight;

	/**
	 * backStatus 后端的状态 0/禁用 1/可用
	 */
	private Integer backStatus;
	/**
	 * foreUuid 对应前端的id
	 */
	private String foreUuid;
	private Date createDate;

	public Backend() {
	}

	public Backend(String backUuid, String backName, String vmUuid,
			String vmIp, Integer vmPort, Integer backWeight,
			Integer backStatus, String foreUuid, Date createDate) {
		super();
		this.backUuid = backUuid;
		this.backName = backName;
		this.vmUuid = vmUuid;
		this.vmIp = vmIp;
		this.vmPort = vmPort;
		this.backWeight = backWeight;
		this.backStatus = backStatus;
		this.foreUuid = foreUuid;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "back_uuid")
	public String getBackUuid() {
		return backUuid;
	}

	public void setBackUuid(String backUuid) {
		this.backUuid = backUuid;
	}

	@Column(name = "back_name")
	public String getBackName() {
		return backName;
	}

	public void setBackName(String backName) {
		this.backName = backName;
	}

	@Column(name = "vm_uuid")
	public String getVmUuid() {
		return vmUuid;
	}

	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}

	@Column(name = "vm_ip")
	public String getVmIp() {
		return vmIp;
	}

	public void setVmIp(String vmIp) {
		this.vmIp = vmIp;
	}

	@Column(name = "vm_port")
	public Integer getVmPort() {
		return vmPort;
	}

	public void setVmPort(Integer vmPort) {
		this.vmPort = vmPort;
	}

	@Column(name = "back_weight")
	public Integer getBackWeight() {
		return backWeight;
	}

	public void setBackWeight(Integer backWeight) {
		this.backWeight = backWeight;
	}

	@Column(name = "back_status")
	public Integer getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}

	@Column(name = "fore_uuid")
	public String getForeUuid() {
		return foreUuid;
	}

	public void setForeUuid(String foreUuid) {
		this.foreUuid = foreUuid;
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
		return "Backend [backUuid=" + backUuid + ", backName=" + backName
				+ ", vmUuid=" + vmUuid + ", vmIp=" + vmIp + ", vmPort="
				+ vmPort + ", backWeight=" + backWeight + ", backStatus="
				+ backStatus + ", foreUuid=" + foreUuid + ", createDate="
				+ createDate + "]";
	}

}
