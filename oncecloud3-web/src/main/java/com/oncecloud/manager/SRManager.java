package com.oncecloud.manager;

import org.json.JSONArray;

import com.oncecloud.main.Constant;

public interface SRManager {

	public abstract boolean checkSREquals(String masterHost, String targetHost);

	public abstract JSONArray addStorage(int userId, String srname,
			String srAddress, String srDesc, String srType, String srDir,
			String rackId, String rackName);

	/**
	 * 获取存储列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getStorageList(int page, int limit, String search);

	public abstract JSONArray deleteStorage(int userId, String srId,
			String srName);

	public abstract JSONArray load2Server(int userId, String srUuid,
			String hostUuid);

	public abstract JSONArray getStorageByAddress(String address);

	public abstract void updateStorage(int userId, String srId, String srName,
			String srDesc, String rackId);

//	public abstract JSONArray getRealSRList(String poolUuid);
}
