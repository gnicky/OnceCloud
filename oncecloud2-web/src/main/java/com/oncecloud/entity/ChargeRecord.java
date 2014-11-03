package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "charge_record")
public class ChargeRecord {

	private String recordId;
	private Double recordBill;
	private Integer recordType;
	private Date recordDate;
	private Integer recordUID;
	private Integer recordState;

	public ChargeRecord() {
	}

	public ChargeRecord(String recordId, Double recordBill, Integer recordType,
			Date recordDate, Integer recordUID) {
		this.recordId = recordId;
		this.recordBill = recordBill;
		this.recordType = recordType;
		this.recordDate = recordDate;
		this.recordUID = recordUID;
	}

	@Id
	@Column(name = "record_id")
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Column(name = "record_state")
	public Integer getRecordState() {
		return recordState;
	}

	public void setRecordState(Integer recordState) {
		this.recordState = recordState;
	}

	@Column(name = "record_bill")
	public Double getRecordBill() {
		return recordBill;
	}

	public void setRecordBill(Double recordBill) {
		this.recordBill = recordBill;
	}

	@Column(name = "record_type")
	public Integer getRecordType() {
		return recordType;
	}

	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}

	@Column(name = "record_date")
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	@Column(name = "record_uid")
	public Integer getRecordUID() {
		return recordUID;
	}

	public void setRecordUID(Integer recordUID) {
		this.recordUID = recordUID;
	}

	@Override
	public String toString() {
		return "ChargeRecord [recordId=" + recordId + ", recordBill="
				+ recordBill + ", recordType=" + recordType + ", recordDate="
				+ recordDate + ", recordUID=" + recordUID + ", recordState="
				+ recordState + "]";
	}
}
