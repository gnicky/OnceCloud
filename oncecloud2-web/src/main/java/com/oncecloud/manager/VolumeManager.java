package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolumeManager {
	/**
	 * 获取用户硬盘列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getVolumeList(int userId, int page, int limit,
			String search);

	public abstract void createVolume(int userId, String volUuid,
			String volName, int volSize);

	public abstract void deleteVolume(int userId, String volUuid);

	public abstract void bindVolume(int userId, String volUuid, String vmUuid);

	public abstract void unbindVolume(int userId, String volUuid);

	public abstract String getQuota(int userId, int count, int size);

	/**
	 * 获取硬盘详细信息
	 * 
	 * @param volUuid
	 * @return
	 */
	public abstract JSONObject getVolumeDetail(String volUuid);

	public abstract JSONArray getAvailableVolumes(int userId);

	public abstract JSONArray getVolumesOfVM(String vmUuid);
}
