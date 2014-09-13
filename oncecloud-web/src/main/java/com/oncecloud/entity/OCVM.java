package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oncecloud.main.Utilities;

@Entity
@Table(name = "oc_vm")
public class OCVM {
	private String vmUuid;
	private String vmPWD;
	private Integer vmUID;
	private String vmName;
	private String vmDesc;
	private Integer vmPlatform;
	private String vmIP;
	private String vmVlan;
	private String vmMac;
	private Integer vmMem;
	private Integer vmCpu;
	private Integer vmDisk;
	private Integer vmPower;
	private Integer vmStatus;
	private Integer vmImportance;
	private String hostUuid;
	private Date createDate;
	private Date backupDate;
	private String vmFirewall;
	private String alarmUuid;
	private String tplUuid;

	public OCVM() {
	}

	// this method is used to precreate vm
	public OCVM(String vmUuid, String vmPWD, Integer vmUID, String vmName,
			Integer vmPlatform, String vmMac, Integer vmMem, Integer vmCpu,
			Integer vmPower, Integer vmStatus, Date createDate) {
		this.vmUuid = vmUuid;
		this.vmPWD = vmPWD;
		this.vmUID = vmUID;
		this.vmName = vmName;
		this.vmPlatform = vmPlatform;
		this.vmMac = vmMac;
		this.vmMem = vmMem;
		this.vmCpu = vmCpu;
		this.vmPower = vmPower;
		this.vmStatus = vmStatus;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "vm_uuid")
	public String getVmUuid() {
		return vmUuid;
	}

	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}

	@Column(name = "vm_pwd")
	public String getVmPWD() {
		return vmPWD;
	}

	public void setVmPWD(String vmPWD) {
		this.vmPWD = vmPWD;
	}

	@Column(name = "vm_uid")
	public Integer getVmUID() {
		return vmUID;
	}

	public void setVmUID(Integer vmUID) {
		this.vmUID = vmUID;
	}

	@Column(name = "vm_name")
	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	@Column(name = "vm_desc")
	public String getVmDesc() {
		return vmDesc;
	}

	public void setVmDesc(String vmDesc) {
		this.vmDesc = vmDesc;
	}

	@Column(name = "vm_platform")
	public Integer getVmPlatform() {
		return vmPlatform;
	}

	public void setVmPlatform(Integer vmPlatform) {
		this.vmPlatform = vmPlatform;
	}

	@Column(name = "vm_ip")
	public String getVmIP() {
		return vmIP;
	}

	public void setVmIP(String vmIP) {
		this.vmIP = vmIP;
	}

	@Column(name = "vm_vlan")
	public String getVmVlan() {
		return vmVlan;
	}

	public void setVmVlan(String vmVlan) {
		this.vmVlan = vmVlan;
	}

	@Column(name = "vm_mac")
	public String getVmMac() {
		return vmMac;
	}

	public void setVmMac(String vmMac) {
		this.vmMac = vmMac;
	}

	@Column(name = "vm_mem")
	public Integer getVmMem() {
		return vmMem;
	}

	public void setVmMem(Integer vmMem) {
		this.vmMem = vmMem;
	}

	@Column(name = "vm_cpu")
	public Integer getVmCpu() {
		return vmCpu;
	}

	public void setVmCpu(Integer vmCpu) {
		this.vmCpu = vmCpu;
	}

	@Column(name = "vm_disk")
	public Integer getVmDisk() {
		return vmDisk;
	}

	public void setVmDisk(Integer vmDisk) {
		this.vmDisk = vmDisk;
	}

	@Column(name = "vm_power")
	public Integer getVmPower() {
		return vmPower;
	}

	public void setVmPower(Integer vmPower) {
		this.vmPower = vmPower;
	}

	@Column(name = "vm_status")
	public Integer getVmStatus() {
		return vmStatus;
	}

	public void setVmStatus(Integer vmStatus) {
		this.vmStatus = vmStatus;
	}

	@Column(name = "host_uuid")
	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "backup_date")
	public Date getBackupDate() {
		return backupDate;
	}

	public void setBackupDate(Date backupDate) {
		this.backupDate = backupDate;
	}

	@Column(name = "vm_firewall")
	public String getVmFirewall() {
		return vmFirewall;
	}

	public void setVmFirewall(String vmFirewall) {
		this.vmFirewall = vmFirewall;
	}

	@Column(name = "alarm_uuid")
	public String getAlarmUuid() {
		return alarmUuid;
	}

	public void setAlarmUuid(String alarmUuid) {
		this.alarmUuid = alarmUuid;
	}

	@Column(name = "tpl_uuid")
	public String getTplUuid() {
		return tplUuid;
	}

	public void setTplUuid(String tplUuid) {
		this.tplUuid = tplUuid;
	}

	@Column(name = "vm_importance")
	public Integer getVmImportance() {
		return vmImportance;
	}

	public void setVmImportance(Integer vmImportance) {
		this.vmImportance = vmImportance;
	}

	@Override
	public String toString() {
		return "OCVM [vmUuid=" + vmUuid + ", vmPWD=" + vmPWD + ", vmUID="
				+ vmUID + ", vmName=" + vmName + ", vmDesc=" + vmDesc
				+ ", vmPlatform=" + vmPlatform + ", vmIP=" + vmIP + ", vmVlan="
				+ vmVlan + ", vmMac=" + vmMac + ", vmMem=" + vmMem + ", vmCpu="
				+ vmCpu + ", vmDisk=" + vmDisk + ", vmPower=" + vmPower
				+ ", vmStatus=" + vmStatus + ", hostUuid=" + hostUuid
				+ ", createDate=" + createDate + ", backupDate=" + backupDate
				+ ", vmFirewall=" + vmFirewall + ", tplUuid=" + tplUuid
				+ "alarmUuid=" + alarmUuid + "]";

	}
	
	public String toJsonString() {
		return "{'vmUuid':'" + vmUuid + "', 'vmPWD':'" + vmPWD + "', 'vmUID':'" + vmUID + "', 'vmName':'" + Utilities.encodeText(vmName) + "', 'vmDesc':'"
				+ Utilities.encodeText(vmDesc) + "', 'vmPlatform':'" + vmPlatform + "', 'vmIP':'" + vmIP + "', 'vmVlan':'" + vmVlan + "', 'vmMac':'" + vmMac
				+ "', 'vmMem':'" + vmMem + "', 'vmCpu':'" + vmCpu + "', 'vmDisk':'" + vmDisk + "', 'vmPower':'" + vmPower
				+ "', 'vmStatus':'" + vmStatus + "', 'hostUuid':'" + hostUuid + "', 'createDate':'" + Utilities.formatTime(createDate) + "'}";
	}
}
