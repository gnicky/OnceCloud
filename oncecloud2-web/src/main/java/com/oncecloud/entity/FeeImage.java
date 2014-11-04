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
@Table(name = "fee_image")
public class FeeImage {

	private Integer feeId;
	private Integer imageUID;
	private Date startDate;
	private Date endDate;
	private Double imagePrice;
	private Integer imageState;
	private String imageUuid;
	private Double imageExpense;
	private String imageName;

	public FeeImage() {
	}

	public FeeImage(Integer imageUID, Date startDate, Date endDate,
			Double imagePrice, Integer imageState, String imageUuid,
			String imageName) {
		this.imageUID = imageUID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.imagePrice = imagePrice;
		this.imageState = imageState;
		this.imageUuid = imageUuid;
		this.imageName = imageName;
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

	@Column(name = "image_uid")
	public Integer getImageUID() {
		return imageUID;
	}

	public void setImageUID(Integer imageUID) {
		this.imageUID = imageUID;
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

	@Column(name = "image_price")
	public Double getImagePrice() {
		return imagePrice;
	}

	public void setImagePrice(Double imagePrice) {
		this.imagePrice = imagePrice;
	}

	@Column(name = "image_state")
	public Integer getImageState() {
		return imageState;
	}

	public void setImageState(Integer imageState) {
		this.imageState = imageState;
	}

	@Column(name = "image_uuid")
	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	@Column(name = "image_expense")
	public Double getImageExpense() {
		return imageExpense;
	}

	public void setImageExpense(Double imageExpense) {
		this.imageExpense = imageExpense;
	}

	@Column(name = "image_name")
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImageExpense() {
		long miliDis = this.endDate.getTime() - this.startDate.getTime();
		Double perHour = miliDis / (1000 * 60 * 60.0);
		Double imageExpense = Helper.roundTwo(this.imagePrice * perHour);
		this.imageExpense = imageExpense;
	}
}
