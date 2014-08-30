package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yly
 * @version 2014/04/04
 */
@Entity
@Table(name = "oc_snapshot")
public class Snapshot {

	private String snapshotId;
	private String snapshotName;
	private int snapshotSize;
	private Date backupDate;
	private String snapshotVm;
	private String snapshotVolume;

	public Snapshot() {
	}

	public Snapshot(String snapshotId, String snapshotName, int snapshotSize,
			Date backupDate, String snapshotVm, String snapshotVolume) {
		this.snapshotId = snapshotId;
		this.snapshotName = snapshotName;
		this.snapshotSize = snapshotSize;
		this.backupDate = backupDate;
		this.snapshotVm = snapshotVm;
		this.snapshotVolume = snapshotVolume;
	}

	@Id
	@Column(name = "snapshot_id")
	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	@Column(name = "snapshot_name")
	public String getSnapshotName() {
		return snapshotName;
	}

	public void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}

	@Column(name = "snapshot_size")
	public int getSnapshotSize() {
		return snapshotSize;
	}

	public void setSnapshotSize(int snapshotSize) {
		this.snapshotSize = snapshotSize;
	}

	@Column(name = "backup_date")
	public Date getBackupDate() {
		return backupDate;
	}

	public void setBackupDate(Date backupDate) {
		this.backupDate = backupDate;
	}

	@Column(name = "snapshot_vm")
	public String getSnapshotVm() {
		return snapshotVm;
	}

	public void setSnapshotVm(String snapshotVm) {
		this.snapshotVm = snapshotVm;
	}

	@Column(name = "snapshot_volume")
	public String getSnapshotVolume() {
		return snapshotVolume;
	}

	public void setSnapshotVolume(String snapshotVolume) {
		this.snapshotVolume = snapshotVolume;
	}
}
