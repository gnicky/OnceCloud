package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oncecloud.common.helper.Helper;

@Entity
@Table(name = "fee_snapshot")
public class FeeSnapshot {

	private Integer feeId;
	private Integer snapshotUID;
	private Date startDate;
	private Date endDate;
	private Double snapshotPrice;
	private Integer snapshotState;
	private String vmUuid;
	private Double snapshotExpense;
	private String vmName;

	public FeeSnapshot() {
	}

	public FeeSnapshot(Integer snapshotUID, Date startDate, Date endDate,
			Double snapshotPrice, Integer snapshotState, String vmUuid,
			String vmName) {
		this.snapshotUID = snapshotUID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.snapshotPrice = snapshotPrice;
		this.snapshotState = snapshotState;
		this.vmUuid = vmUuid;
		this.vmName = vmName;
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

	@Column(name = "snapshot_uid")
	public Integer getSnapshotUID() {
		return snapshotUID;
	}

	public void setSnapshotUID(Integer snapshotUID) {
		this.snapshotUID = snapshotUID;
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

	@Column(name = "snapshot_price")
	public Double getSnapshotPrice() {
		return snapshotPrice;
	}

	public void setSnapshotPrice(Double snapshotPrice) {
		this.snapshotPrice = snapshotPrice;
	}

	@Column(name = "snapshot_state")
	public Integer getSnapshotState() {
		return snapshotState;
	}

	public void setSnapshotState(Integer snapshotState) {
		this.snapshotState = snapshotState;
	}

	@Column(name = "vm_uuid")
	public String getVmUuid() {
		return vmUuid;
	}

	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}

	@Column(name = "snapshot_expense")
	public Double getSnapshotExpense() {
		return snapshotExpense;
	}

	public void setSnapshotExpense(Double snapshotExpense) {
		this.snapshotExpense = snapshotExpense;
	}

	@Column(name = "vm_name")
	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public void setSnapshotExpense() {
		long miliDis = this.endDate.getTime() - this.startDate.getTime();
		Double perHour = miliDis / (1000 * 60 * 60.0);
		Double snapshotExpense = Helper.roundTwo(this.snapshotPrice * perHour);
		this.snapshotExpense = snapshotExpense;
	}
}
