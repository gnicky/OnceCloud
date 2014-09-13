package com.oncecloud.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cyh
 * @version 2014/03/28 交换机表
 */
@Entity
@Table(name = "oc_switch")
public class Switch {
	private String swUuid;
	private String swName;
	private String rackUuid;
	private Integer swPortCount;
	private String swType;
	private Integer swState;

	private Date createDate;

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Switch() {
	}

	@Id
	@Column(name = "sw_uuid")
	public String getSwUuid() {
		return swUuid;
	}

	public void setSwUuid(String swUuid) {
		this.swUuid = swUuid;
	}

	@Column(name = "switch_state")
	public Integer getSwState() {
		return swState;
	}

	public void setSwState(Integer swState) {
		this.swState = swState;
	}

	@Column(name = "switch_name")
	public String getSwName() {
		return swName;
	}

	public void setSwName(String swName) {
		this.swName = swName;
	}

	@Column(name = "rack_uuid")
	public String getRackUuid() {
		return rackUuid;
	}

	public void setRackUuid(String rackUuid) {
		this.rackUuid = rackUuid;
	}

	@Column(name = "switch_portcount")
	public Integer getSwPortCount() {
		return swPortCount;
	}

	public void setSwPortCount(Integer swPortCount) {
		this.swPortCount = swPortCount;
	}

	@Column(name = "switch_type")
	public String getSwType() {
		return swType;
	}

	public void setSwType(String swType) {
		this.swType = swType;
	}

	@Override
	public String toString() {
		return "Switch [swUuid=" + swUuid + ", swName=" + swName
				+ ", rackUuid=" + rackUuid + ", swPortCount=" + swPortCount
				+ ", swType=" + swType + ", swState=" + swState
				+ ", createDate=" + createDate + "]";
	}

	public Switch(String swUuid, String swName, String rackUuid,
			Integer swPortCount, String swType, Integer swState, Date createDate) {
		super();
		this.swUuid = swUuid;
		this.swName = swName;
		this.rackUuid = rackUuid;
		this.swPortCount = swPortCount;
		this.swType = swType;
		this.swState = swState;
		this.createDate = createDate;
	}

	public String toJsonString() {
		try {
			return "{'swUuid':'" + swUuid + "', 'swName':'"
					+ URLEncoder.encode(swName, "utf-8") + "', 'rackUuid':'"
					+ rackUuid + "', 'swPortCount':'" + swPortCount
					+ "', 'swType':'" + swType + "', 'swState':'" + swState
					+ "', 'createDate':'" + createDate + "'}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
