package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Entity
@Table(name = "oc_image")
public class Image {
	private String imageUuid;
	private String imageName;
	private String imagePwd;
	private Integer imageUID;
	private Integer imageDisk;
	private Integer imagePlatform;
	private Integer imageStatus;
	private String poolUuid;
	private String imageDesc;
	private Date createDate;
	private Integer preAllocate;

	public Image() {
	}

	public Image(String imageUuid, String imageName, String imagePwd,
			Integer imageUID, Integer imageDisk, Integer imagePlatform,
			Integer imageStatus, String poolUuid, String imageDesc,
			Date createDate) {
		super();
		this.imageUuid = imageUuid;
		this.imageName = imageName;
		this.imagePwd = imagePwd;
		this.imageUID = imageUID;
		this.imageDisk = imageDisk;
		this.imagePlatform = imagePlatform;
		this.imageStatus = imageStatus;
		this.poolUuid = poolUuid;
		this.imageDesc = imageDesc;
		this.createDate = createDate;
	}

	@Column(name = "image_pwd")
	public String getImagePwd() {
		return imagePwd;
	}

	public void setImagePwd(String imagePwd) {
		this.imagePwd = imagePwd;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "image_desc")
	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	@Id
	@Column(name = "image_uuid")
	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	@Column(name = "image_name")
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Column(name = "image_uid")
	public Integer getImageUID() {
		return imageUID;
	}

	public void setImageUID(Integer imageUID) {
		this.imageUID = imageUID;
	}

	@Column(name = "image_disk")
	public Integer getImageDisk() {
		return imageDisk;
	}

	public void setImageDisk(Integer imageDisk) {
		this.imageDisk = imageDisk;
	}

	@Column(name = "image_platform")
	public Integer getImagePlatform() {
		return imagePlatform;
	}

	public void setImagePlatform(Integer imagePlatform) {
		this.imagePlatform = imagePlatform;
	}

	@Column(name = "image_status")
	public Integer getImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(Integer imageStatus) {
		this.imageStatus = imageStatus;
	}

	@Column(name = "pool_uuid")
	public String getPoolUuid() {
		return poolUuid;
	}

	public void setPoolUuid(String poolUuid) {
		this.poolUuid = poolUuid;
	}

	@Column(name = "pre_allocate")
	public Integer getPreAllocate() {
		return preAllocate;
	}

	public void setPreAllocate(Integer preAllocate) {
		this.preAllocate = preAllocate;
	}

	@Override
	public String toString() {
		return "Image [imageUuid=" + imageUuid + ", imageName=" + imageName
				+ ", imagePwd=" + imagePwd + ", imageUID=" + imageUID
				+ ", imageDisk=" + imageDisk + ", imagePlatform="
				+ imagePlatform + ", imageStatus=" + imageStatus
				+ ", poolUuid=" + poolUuid + ", imageDesc=" + imageDesc
				+ ", createDate=" + createDate + ", preAllocate=" + preAllocate
				+ "]";
	}
}
