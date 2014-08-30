package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yly hehai
 * @version 2014/08/23
 */
@Entity
@Table(name = "oc_volume")
public class Volume {
	private String volumeUuid;
	private String volumeName;
	private Integer volumeUID;
	private Integer volumeSize;
	private String volumeDependency;
	private String volumeDescription;
	private Date createDate;
	private Date backupDate;
	private Integer volumeStatus;

	public Volume() {
	}

	public Volume(String volumeUuid, String volumeName, Integer volumeUID,
			Integer volumeSize, Date createDate, Integer volumeStatus) {
		this.volumeUuid = volumeUuid;
		this.volumeName = volumeName;
		this.volumeUID = volumeUID;
		this.volumeSize = volumeSize;
		this.createDate = createDate;
		this.volumeStatus = volumeStatus;
	}

	@Id
	@Column(name = "volume_uuid")
	public String getVolumeUuid() {
		return volumeUuid;
	}

	public void setVolumeUuid(String volumeUuid) {
		this.volumeUuid = volumeUuid;
	}

	@Column(name = "volume_name")
	public String getVolumeName() {
		return volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	@Column(name = "volume_uid")
	public Integer getVolumeUID() {
		return volumeUID;
	}

	public void setVolumeUID(Integer volumeUID) {
		this.volumeUID = volumeUID;
	}

	@Column(name = "volume_size")
	public Integer getVolumeSize() {
		return volumeSize;
	}

	public void setVolumeSize(Integer volumeSize) {
		this.volumeSize = volumeSize;
	}

	@Column(name = "volume_dependency")
	public String getVolumeDependency() {
		return volumeDependency;
	}

	public void setVolumeDependency(String volumeDependency) {
		this.volumeDependency = volumeDependency;
	}

	@Column(name = "volume_description")
	public String getVolumeDescription() {
		return volumeDescription;
	}

	public void setVolumeDescription(String volumeDescription) {
		this.volumeDescription = volumeDescription;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "backup_date")
	public Date getBackupDate() {
		return backupDate;
	}

	public void setBackupDate(Date backupDate) {
		this.backupDate = backupDate;
	}

	@Column(name = "volume_status")
	public Integer getVolumeStatus() {
		return volumeStatus;
	}

	public void setVolumeStatus(Integer volumeStatus) {
		this.volumeStatus = volumeStatus;
	}

	@Override
	public String toString() {
		return "Volume [volumeUuid=" + volumeUuid + ", volumeName="
				+ volumeName + ", volumeUID=" + volumeUID + ", volumeSize="
				+ volumeSize + ", volumeDependency=" + volumeDependency
				+ ", volumeDescription=" + volumeDescription + ", createDate="
				+ createDate + ", backupDate=" + backupDate + "]";
	}
}
