package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.Snapshot;

public interface SnapshotDAO {
	/**
	 * 获取快照
	 * 
	 * @param snapshotId
	 * @return
	 */
	public abstract Snapshot getSnapshot(String snapshotId);

	/**
	 * 获取主机的备份链
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract Object getOneVmSnapshot(String vmUuid);

	/**
	 * 获取硬盘的备份链
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public abstract Object getOneVolumeSnapshot(String volumeUuid);

	/**
	 * 获取一页主机备份链列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @return
	 */
	public abstract List<Object> getOnePageVMSnapshotList(int userId, int page,
			int limit, String search);

	/**
	 * 获取一页硬盘备份链列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @param offside
	 * @return
	 */
	public abstract List<Object> getOnePageVolumeSnapshotList(int userId,
			int page, int limit, String search, int offside);

	/**
	 * 获取虚拟机快照列表
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract List<Snapshot> getVmSnapshotList(String vmUuid);

	/**
	 * 获取硬盘快照列表
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public abstract List<Snapshot> getVolumeSnapshotList(String volumeUuid);

	/**
	 * 获取最近的主机快照时间
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract Date getRecentVmSnapshotDate(String vmUuid);

	/**
	 * 获取最近的硬盘快照时间
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public abstract Date getRecentVolumeSnapshotDate(String volumeUuid);

	/**
	 * 获取主机快照数目
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract int getVmSnapshotSize(String vmUuid);

	/**
	 * 获取硬盘快照数目
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public abstract int getVolumeSnapshotSize(String volumeUuid);

	/**
	 * 获取主机备份链总数
	 * 
	 * @param userId
	 * @param search
	 * @return
	 */
	public abstract int countVMSnapshotList(int userId, String search);

	/**
	 * 获取备份链总数
	 * 
	 * @param search
	 * @param uid
	 * @return
	 */
	public abstract int countAllSnapshotList(int userId, String search);

	/**
	 * 判断是否是新的备份链
	 * 
	 * @param uuid
	 * @return
	 */
	public abstract boolean ifNewChain(String uuid);

	/**
	 * 创建新的快照
	 * 
	 * @param snapshotId
	 * @param snapshotName
	 * @param snapshotSize
	 * @param backupDate
	 * @param snapshotVm
	 * @param snapshotVolume
	 * @param newChain
	 * @param userId
	 */
	public abstract void insertSnapshot(String snapshotId, String snapshotName,
			int snapshotSize, Date backupDate, String snapshotVm,
			String snapshotVolume, int userId);

	/**
	 * 删除快照
	 * 
	 * @param userId
	 * @param snapshotId
	 */
	public abstract Snapshot deleteOneSnapshot(Snapshot ss);

	/**
	 * 删除主机的全部快照
	 * 
	 * @param vmUuid
	 * @param userId
	 */
	public abstract void deleteVmSnapshot(String vmUuid, int userId);

	/**
	 * 删除硬盘的全部快照
	 * 
	 * @param volumeUuid
	 * @param userId
	 */
	public abstract void deleteVolumeSnapshot(String volumeUuid, int userId);

}
