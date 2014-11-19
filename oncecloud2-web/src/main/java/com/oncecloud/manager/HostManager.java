package com.oncecloud.manager;

import org.json.JSONArray;

public interface HostManager {

	public final static String DEFAULT_USER = "root";
	public final static String DEFAULT_PORT = "9363";

	public abstract JSONArray createHost(String hostName, String hostPwd,
			String hostDesc, String hostIp, String rackUuid, String rackName,
			int userid);

	public abstract JSONArray getHostList(int page, int limit, String search);

	public abstract JSONArray getHostLoadList(int page, int limit,
			String searchStr, String srUuid);

//	public abstract void bindHost(String srUuid, String hostUuid, int userid);

	public abstract JSONArray getSrOfHost(String hostUuid);

	public abstract void unbindSr(String hostUuid, String srUuid, int userid);

	public abstract JSONArray isSameSr(String uuidJsonStr);

	public abstract JSONArray getTablePool(String uuidJsonStr);

	public abstract JSONArray deleteHost(String hostId, String hostName,
			int userid);

	public abstract JSONArray queryAddress(String address);

	public abstract JSONArray add2Pool(String uuidJsonStr, String hasMaster,
			String poolUuid, int userid);

	public abstract JSONArray r4Pool(String hostUuid, int userid);

//	public abstract JSONArray getHostForImage();

	public abstract JSONArray getOneHost(String hostid);

	public abstract boolean updateHost(String hostId, String hostName,
			String hostDesc, String rackUuid);

	public abstract JSONArray getAllList();
	
	public abstract boolean recover(int userId, String ip, String username,
			String password, String content, String conid);
	
	public abstract JSONArray getHostListForMigration(String vmuuid);
}
