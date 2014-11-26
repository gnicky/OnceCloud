package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VnetManager {
//	public abstract JSONObject checkNet(int userId, String routerid, Integer net);
//
//	public abstract JSONArray getVMs(String vnUuid);
//
//	public abstract boolean bindVnetToVM(int userId, String vmuuid,
//			String vnId, String poolUuid);
//
//	/**
//	 * 添加主机到私有网络
//	 */
//	public abstract boolean bindVnetToVMs(int userId, String vmArray,
//			String vnetId, String poolUuid, String content, String conid);
//
//	public abstract String deleteVnet(int userId, String vnetUuid);

	/**
	 * 获取用户私有网络列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getVnetList(int userId, int page, int limit,
			String search);

//	public abstract void vnetCreate(String name, String uuid, String desc,
//			int userId);
//
//	public abstract JSONArray vnetQuota(int userId);

	public abstract JSONObject getVnetDetail(String vnetUuid);

//	public abstract JSONObject linkRouter(int userId, String vnetUuid,
//			String routerId, Integer net, Integer gate, Integer start,
//			Integer end, Integer dhcpState, String content, String conid);
//
//	public abstract JSONObject unlinkRouter(String vnetUuid, int userId,
//			String content, String conid);

	public abstract JSONObject vnetAddvm(int userId, String vmuuidStr,
			String vnId, String poolUuid, String content, String conid);

	public abstract JSONArray getVnetsOfUser(int userId);

//	public abstract JSONObject bindVM(String vnId, String vmuuid, int userId,
//			String poolUuid);

	public abstract boolean isRouterHasVnets(String routerUuid, int userId);

//	public abstract JSONArray getAvailableVnet(Integer userId);
}
