package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.Volume;

public interface VolumeDAO {
//	/**
//	 * 获取硬盘
//	 * 
//	 * @param volumeUuid
//	 * @return
//	 */
//	public abstract Volume getVolume(String volumeUuid);

	/**
	 * 获取一页用户硬盘列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract List<Volume> getOnePageVolumes(int userId, int page,
			int limit, String search);

	/**
	 * 获取主机硬盘列表
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract List<String> getVolumesOfVM(String vmUuid);

//	public abstract List<Volume> getVolListByVM(String vmUuid);
//
//	/**
//	 * 获取用户硬盘总数
//	 * 
//	 * @param userId
//	 * @param search
//	 * @return
//	 */
//	public abstract int countVolumes(int userId, String search);
//
//	/**
//	 * 预创建硬盘
//	 * 
//	 * @param volumeUuid
//	 * @param volumeName
//	 * @param volumeUID
//	 * @param volumeSize
//	 * @param createDate
//	 * @param status
//	 * @return
//	 */
//	public abstract boolean preCreateVolume(String volumeUuid,
//			String volumeName, Integer volumeUID, Integer volumeSize,
//			Date createDate, Integer status);
//
//	/**
//	 * 删除硬盘
//	 * 
//	 * @param userId
//	 * @param volumeUuid
//	 */
//	public abstract void deleteVolume(int userId, String volumeUuid);
//
//	/**
//	 * 硬盘是否存在
//	 * 
//	 * @param volumeUuid
//	 * @return
//	 */
//	public abstract boolean isExist(String volumeUuid);
//
//	public abstract void addDependency(String volumeUuid, String vmUuid);

	public abstract void emptyDependency(String volumeUuid);

//	public abstract void updateBackupDate(String volumeUuid, Date backupDate);
//
//	public abstract void updateName(String volumeUuid, String newName,
//			String description);
//
//	public abstract boolean updateVolumeStatus(String volUuid, int status);
//
//	public abstract List<Volume> getAbledVolumes(int userId);
}
