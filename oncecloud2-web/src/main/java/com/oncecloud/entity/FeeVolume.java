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
@Table(name = "fee_volume")
public class FeeVolume {
	private Integer feeId;
	private Integer volumeUID;
	private Date startDate;
	private Date endDate;
	private Double volumePrice;
	private Integer volumeState;
	private String volumeUuid;
	private Double volumeExpense;
	private String volumeName;

	public FeeVolume() {
	}

	public FeeVolume(Integer volumeUID, Date startDate, Date endDate,
			Double volumePrice, Integer volumeState, String volumeUuid,
			String volumeName) {
		this.volumeUID = volumeUID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.volumePrice = volumePrice;
		this.volumeState = volumeState;
		this.volumeUuid = volumeUuid;
		this.volumeName = volumeName;
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

	@Column(name = "volume_uid")
	public Integer getVolumeUID() {
		return volumeUID;
	}

	public void setVolumeUID(Integer volumeUID) {
		this.volumeUID = volumeUID;
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

	@Column(name = "volume_price")
	public Double getVolumePrice() {
		return volumePrice;
	}

	public void setVolumePrice(Double volumePrice) {
		this.volumePrice = volumePrice;
	}

	@Column(name = "volume_state")
	public Integer getVolumeState() {
		return volumeState;
	}

	public void setVolumeState(Integer volumeState) {
		this.volumeState = volumeState;
	}

	@Column(name = "volume_uuid")
	public String getVolumeUuid() {
		return volumeUuid;
	}

	public void setVolumeUuid(String volumeUuid) {
		this.volumeUuid = volumeUuid;
	}

	@Column(name = "volume_expense")
	public Double getVolumeExpense() {
		return volumeExpense;
	}

	public void setVolumeExpense(Double volumeExpense) {
		this.volumeExpense = volumeExpense;
	}

	@Column(name = "volume_name")
	public String getVolumeName() {
		return volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	public void setVolumeExpense() {
		long miliDis = this.endDate.getTime() - this.startDate.getTime();
		Double perHour = miliDis / (1000 * 60 * 60.0);
		Double volumeExpense = Helper.roundTwo(this.volumePrice * perHour);
		this.volumeExpense = volumeExpense;
	}
}
