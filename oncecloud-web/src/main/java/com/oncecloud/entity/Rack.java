package com.oncecloud.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hehai
 * @version 2014/03/28
 */
@Entity
@Table(name = "oc_rack")
public class Rack {
	private String rackUuid;
	private String rackName;
	private String rackDesc;
	private Integer rackStatus;
	private String dcUuid;
	private Date createDate;

	@Id
	@Column(name = "rack_uuid")
	public String getRackUuid() {
		return rackUuid;
	}

	public void setRackUuid(String rackUuid) {
		this.rackUuid = rackUuid;
	}

	@Column(name = "rack_name")
	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	@Column(name = "rack_desc")
	public String getRackDesc() {
		return rackDesc;
	}

	public void setRackDesc(String rackDesc) {
		this.rackDesc = rackDesc;
	}

	@Column(name = "rack_status")
	public Integer getRackStatus() {
		return rackStatus;
	}

	public void setRackStatus(Integer rackStatus) {
		this.rackStatus = rackStatus;
	}

	@Column(name = "dc_uuid")
	public String getDcUuid() {
		return dcUuid;
	}

	public void setDcUuid(String dcUuid) {
		this.dcUuid = dcUuid;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Rack() {
		super();
	}

	public Rack(String rackUuid, String rackName, String rackDesc,
			Integer rackStatus, String dcUuid, Date createDate) {
		super();
		this.rackUuid = rackUuid;
		this.rackName = rackName;
		this.rackDesc = rackDesc;
		this.rackStatus = rackStatus;
		this.dcUuid = dcUuid;
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Rack [rackUuid=" + rackUuid + ", rackName=" + rackName
				+ ", rackDesc=" + rackDesc + ", rackStatus=" + rackStatus
				+ ", dcUuid=" + dcUuid + ", createDate=" + createDate + "]";
	}

	public String toJsonString() {
		try {
			return "{'rackUuid':'" + rackUuid + "', 'rackName':'"
					+ URLEncoder.encode(rackName, "utf-8") + "', 'rackDesc':'"
					+ rackDesc + "', 'rackStatus':'" + rackStatus
					+ "', 'dcUuid':'" + dcUuid + "', 'createDate':'"
					+ createDate + "'}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
