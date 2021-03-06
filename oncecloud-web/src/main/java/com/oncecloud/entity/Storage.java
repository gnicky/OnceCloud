package com.oncecloud.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_sr")
public class Storage {
	private String srUuid;
	private String srName;
	private String srDesc;
	private String srAddress;
	private Date createDate;
	private Integer srstatus;
	private String srdir;
	private String srtype;
	private String diskuuid;
	private String isouuid;
	private String hauuid;
	private String rackuuid;

	public Storage() {
	}

	@Column(name = "rack_uuid")
	public String getRackuuid() {
		return rackuuid;
	}

	public void setRackuuid(String rackuuid) {
		this.rackuuid = rackuuid;
	}

	@Id
	@Column(name = "sr_uuid")
	public String getSrUuid() {
		return srUuid;
	}

	public void setSrUuid(String srUuid) {
		this.srUuid = srUuid;
	}

	@Column(name = "sr_name")
	public String getSrName() {
		return srName;
	}

	public void setSrName(String srName) {
		this.srName = srName;
	}

	@Column(name = "sr_desc")
	public String getSrDesc() {
		return srDesc;
	}

	public void setSrDesc(String srDesc) {
		this.srDesc = srDesc;
	}

	@Column(name = "sr_address")
	public String getSrAddress() {
		return srAddress;
	}

	public void setSrAddress(String srAddress) {
		this.srAddress = srAddress;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "sr_status")
	public Integer getSrstatus() {
		return srstatus;
	}

	public void setSrstatus(Integer srstatus) {
		this.srstatus = srstatus;
	}

	@Column(name = "sr_dir")
	public String getSrdir() {
		return srdir;
	}

	public void setSrdir(String srdir) {
		this.srdir = srdir;
	}

	@Column(name = "sr_type")
	public String getSrtype() {
		return srtype;
	}

	public void setSrtype(String srtype) {
		this.srtype = srtype;
	}

	@Column(name = "disk_uuid")
	public String getDiskuuid() {
		return diskuuid;
	}

	public void setDiskuuid(String diskuuid) {
		this.diskuuid = diskuuid;
	}

	@Column(name = "iso_uuid")
	public String getIsouuid() {
		return isouuid;
	}

	public void setIsouuid(String isouuid) {
		this.isouuid = isouuid;
	}

	@Column(name = "ha_uuid")
	public String getHauuid() {
		return hauuid;
	}

	public void setHauuid(String hauuid) {
		this.hauuid = hauuid;
	}

	@Override
	public String toString() {
		return "Storage [srUuid=" + srUuid + ", srName=" + srName + ", srDesc="
				+ srDesc + ", srAddress=" + srAddress + ", createDate="
				+ createDate + ", srstatus=" + srstatus + ", srdir=" + srdir
				+ ", srtype=" + srtype + ", diskuuid=" + diskuuid
				+ ", isouuid=" + isouuid + ", hauuid=" + hauuid + ", rackuuid="
				+ rackuuid + "]";
	}

	public Storage(String srUuid, String srName, String srDesc,
			String srAddress, String srpod, Date createDate, Integer srstatus,
			String srdir, String srtype) {
		super();
		this.srUuid = srUuid;
		this.srName = srName;
		this.srDesc = srDesc;
		this.srAddress = srAddress;
		this.createDate = createDate;
		this.srstatus = srstatus;
		this.srdir = srdir;
		this.srtype = srtype;
	}

	public Storage(String srUuid, String srName, String srDesc,
			String srAddress, String srpod, Date createDate, Integer srstatus,
			String srdir, String srtype, String rackuuid) {
		super();
		this.srUuid = srUuid;
		this.srName = srName;
		this.srDesc = srDesc;
		this.srAddress = srAddress;
		this.createDate = createDate;
		this.srstatus = srstatus;
		this.srdir = srdir;
		this.srtype = srtype;
		this.rackuuid = rackuuid;
	}

	public String toJsonString() {
		try {
			return "{'srUuid':'" + srUuid + "', 'srName':'"
					+ URLEncoder.encode(srName, "utf-8") + "', 'srDesc':'"
					+ URLEncoder.encode(srDesc, "utf-8") + "', 'srAddress':'"
					+ srAddress + "', 'createDate':'" + createDate
					+ "', 'srstatus':'" + srstatus + "', 'srdir':'" + srdir
					+ "', 'srtype':'" + srtype + "'}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
