package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface EIPManager {

	public abstract JSONObject eipApply(String eipName, int uid,
			int eipBandwidth, String eipUuid);

	/**
	 * 获取公网IP列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getEIPList(int userId, int page, int limit,
			String search);

	public abstract void eipDelete(String eipIp, int uid);

	public abstract JSONObject eipBind(int uid, String vmUuid, String eipIp,
			String bindtype);

	public abstract JSONObject eipUnbind(int uid, String vmUuid, String eipIp,
			String bindtype);

//	public abstract JSONObject bindElasticIp(int userId, String uuid,
//			String eipIp, String type);
//
//	public abstract boolean changeBandwidth(int userId, int size, String ip);
//
//	public abstract JSONObject unbindElasticIp(int userId, String uuid,
//			String eipIp, String type);

	public abstract JSONArray getAvailableVMs(int page, int limit,
			String searchStr, String bindtype, int uid);

	public abstract JSONObject eipBandwidth(String eip, int size, int userId);

	public abstract JSONObject getBasicList(String eipIp);

	public abstract JSONArray getAvailableEIPs(int uid);

	public abstract String getQuota(int userId, int count, int size);

}
