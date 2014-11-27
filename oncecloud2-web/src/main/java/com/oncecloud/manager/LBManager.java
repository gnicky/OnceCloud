package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.oncecloud.dao.UserDAO;

public interface LBManager {

	public static int[] capacity = { 5000, 20000, 40000, 100000 };
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;

//	public abstract void createLB(String name, String uuid, int capacity,
//			int userId, String poolUuid);
//
//	public abstract void startLB(String uuid, int userId, String poolUuid);
//
//	public abstract void shutdownLB(String uuid, String force, int userId,
//			String poolUuid);
//
//	public abstract JSONArray getFEListByLB(String lbUuid);
//
//	public abstract boolean checkFore(String lbUuid, int port);
//
//	public abstract boolean deleteLB(int userId, String uuid);
//
//	public abstract boolean applyLB(int userId, String lbUuid);

	public abstract JSONArray getLBList(int userId, int page, int limit,
			String search);

//	public abstract JSONArray getLBsOfUser(int userId, int page, int limit,
//			String search);
//
//	public abstract void lbCreateFore(String name, String foreuuid,
//			String lbuuid, String protocol, int port, int policy, int userId);
//
//	public abstract void lbCreateBack(String name, String lbuuid,
//			String backuuid, String vmuuid, String vmip, int port, int weight,
//			String feuuid, int userId);
//
//	public abstract JSONObject lbCheckBack(String beuuid, int port);
//
//	public abstract JSONObject lbDeletefore(String foreuuid, String lbuuid,
//			int userId);
//
//	public abstract JSONObject lbDeleteBack(String backuuid, String lbuuid,
//			int userId);
//
//	public abstract JSONObject lbForbidfore(String foreUuid, int state,
//			String lbuuid, int userId);
//
//	public abstract JSONObject lbForbidback(String backuuid, int state,
//			String lbuuid, int userId);
//
//	public abstract void lbUpdatefore(String foreName, String foreUuid,
//			int forePolicy, String lbuuid, int userId);
//
//	public abstract JSONObject lbApplylb(String lbuuid, int userId);
//
//	public abstract void lbAdminShutUp(String uuid, int userId);
//
//	public abstract void lbAdminShutDown(String uuid, String force, int userId);
//
//	public abstract void lbDelete(String uuid, int userId, String poolUuid);
//
//	public abstract JSONArray lbQuota(int userId);
//
//	public abstract JSONObject getLBDetail(String lbUuid);
//
//	public abstract JSONArray getAdminLBList(int page, int limit, String host,
//			int importance, String type);
//
//	public abstract void updateImportance(String uuid, int importance);

}
