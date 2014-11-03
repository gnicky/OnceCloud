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
@Table(name = "fee_vm")
public class FeeVM {
	private Integer feeId;
	private Integer vmUID;
	private Date startDate;
	private Date endDate;
	private Double vmPrice;
	private Integer vmState;
	private String vmUuid;
	private Double vmExpense;
	private String vmName;

	public FeeVM() {

	}

	public FeeVM(Integer vmUID, Date startDate, Date endDate, Double vmPrice,
			Integer vmState, String vmUuid, String vmName) {
		this.vmUID = vmUID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.vmPrice = vmPrice;
		this.vmState = vmState;
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

	@Column(name = "vm_uid")
	public Integer getVmUID() {
		return vmUID;
	}

	public void setVmUID(Integer vmUID) {
		this.vmUID = vmUID;
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

	@Column(name = "vm_price")
	public Double getVmPrice() {
		return vmPrice;
	}

	public void setVmPrice(Double vmPrice) {
		this.vmPrice = vmPrice;
	}

	@Column(name = "vm_state")
	public Integer getVmState() {
		return vmState;
	}

	public void setVmState(Integer vmState) {
		this.vmState = vmState;
	}

	@Column(name = "vm_uuid")
	public String getVmUuid() {
		return vmUuid;
	}

	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}

	@Column(name = "vm_expense")
	public Double getVmExpense() {
		return vmExpense;
	}

	public void setVmExpense(Double vmExpense) {
		this.vmExpense = vmExpense;
	}

	@Column(name = "vm_name")
	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public void setVmExpense() {
		long miliDis = this.endDate.getTime() - this.startDate.getTime();
		Double perHour = miliDis / (1000 * 60 * 60.0);
		Double vmExpense = Helper.roundTwo(this.vmPrice * perHour);
		this.vmExpense = vmExpense;
	}

}
