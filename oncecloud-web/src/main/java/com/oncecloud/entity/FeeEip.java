package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oncecloud.helper.Helper;

@Entity
@Table(name = "fee_eip")
public class FeeEip {

	private Integer feeId;
	private Integer eipUID;
	private Date startDate;
	private Date endDate;
	private Double eipPrice;
	private Integer eipState;
	private String eipUuid;
	private Double eipExpense;
	private String eipName;

	public FeeEip() {
	}

	public FeeEip(Integer eipUID, Date startDate, Date endDate,
			Double eipPrice, Integer eipState, String eipUuid, String eipName) {
		this.eipUID = eipUID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.eipPrice = eipPrice;
		this.eipState = eipState;
		this.eipUuid = eipUuid;
		this.eipName = eipName;
	}

	@Id
	@Column(name = "fee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	@Column(name = "eip_uid")
	public Integer getEipUID() {
		return eipUID;
	}

	public void setEipUID(Integer eipUID) {
		this.eipUID = eipUID;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "eip_price")
	public Double getEipPrice() {
		return eipPrice;
	}

	public void setEipPrice(Double eipPrice) {
		this.eipPrice = eipPrice;
	}

	@Column(name = "eip_state")
	public Integer getEipState() {
		return eipState;
	}

	public void setEipState(Integer eipState) {
		this.eipState = eipState;
	}

	@Column(name = "eip_uuid")
	public String getEipUuid() {
		return eipUuid;
	}

	public void setEipUuid(String eipUuid) {
		this.eipUuid = eipUuid;
	}

	@Column(name = "eip_expense")
	public Double getEipExpense() {
		return eipExpense;
	}

	public void setEipExpense(Double eipExpense) {
		this.eipExpense = eipExpense;
	}

	@Column(name = "eip_name")
	public String getEipName() {
		return eipName;
	}

	public void setEipName(String eipName) {
		this.eipName = eipName;
	}

	public void setEipExpense() {
		long miliDis = this.endDate.getTime() - this.startDate.getTime();
		Double perHour = miliDis / (1000 * 60 * 60.0);
		Double eipExpense = Helper.roundTwo(this.eipPrice * perHour);
		this.eipExpense = eipExpense;
	}
}
