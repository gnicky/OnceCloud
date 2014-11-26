package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;


public interface RouterManager {

	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;
	
//	public abstract HostDAO getHostDAO();
//
//	public abstract void setHostDAO(HostDAO hostDAO);
//
//	public abstract UserDAO getUserDAO();
//
//	public abstract void setUserDAO(UserDAO userDAO);
//
//	public abstract PPTPUserDAO getPptpUserDAO();
//
//	public abstract void setPptpUserDAO(PPTPUserDAO pptpUserDAO);
//
//	public abstract ForwardPortDAO getForwardPortDAO();
//
//	public abstract void setForwardPortDAO(ForwardPortDAO forwardPortDAO);

	public abstract void createRouter(String uuid, int userId, String name,
			int capacity, String fwuuid, String poolUuid);

	public abstract void deleteRouter(String uuid, int userId, String poolUuid);

	public abstract void startRouter(String uuid, int userId, String poolUuid);

	public abstract void shutdownRouter(String uuid, String force, int userId,
			String poolUuid);

//	public abstract JSONObject doUnlinkRouter(String vnetUuid, int userId);
//
//	public abstract JSONObject doLinkRouter(int userId, String vnetUuid,
//			String routerUuid, int net, int gate, int start, int end,
//			int dhcpState);
//
//	public abstract JSONObject enableDHCP(String vnetUuid, int userId);
//
//	public abstract JSONObject disableDHCP(String vnetUuid, int userId);
//
//	/**
//	 * @author hty
//	 * @param rtuuid
//	 * @return
//	 * @throws JSONException
//	 * @time 2014/08/14
//	 */
//	public abstract JSONArray getVxnets(String rtuuid);

	/**
	 * 获取用户路由器列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getRouterList(int userId, int page, int limit,
			String search);

//	public abstract JSONArray getAdminRouterList(int page, int limit,
//			String host, int importance, String type);
//
//	public abstract JSONArray getRoutersOfUser(int userId, int page, int limit,
//			String search);

	/**
	 * 获取路由器详细信息
	 * 
	 * @param routerUuid
	 * @return
	 */
	public abstract JSONObject getRouterDetail(String routerUuid);

//	public abstract void routerAdminStartUp(String uuid, int userId);
//
//	public abstract void routerAdminShutDown(String uuid, String force,
//			int userId);

	public abstract JSONArray routerQuota(int userId);

//	public abstract void updateImportance(String uuid, int importance);
//
//	public abstract JSONArray getRoutersOfUser(int userId);
//
//	public abstract JSONObject addPortForwarding(int userId, String allocate,
//			String protocol, String srcIP, String srcPort, String destIP,
//			String destPort, String pfName, String routerUuid);
//
//	public abstract JSONObject delPortForwarding(int userId, String allocate,
//			String protocol, String srcIP, String srcPort, String destIP,
//			String destPort, String uuid);
//
//	public abstract JSONArray getpfList(String routerUuid);
//
//	public abstract JSONObject checkPortForwarding(String routerUuid,
//			String destIP, int destPort, int srcPort);
//
//	public abstract JSONObject savePPTPUser(String name, String pwd,
//			String routerUuid);
//
//	public abstract JSONArray getPPTPList(String routerUuid);
//
//	public abstract JSONObject deletePPTP(int pptpid);
//
//	public abstract JSONObject updatePPTP(String pwd, int pptpid);
//
//	public abstract boolean openPPTP(String routerUuid, User user);
//
//	public abstract boolean closePPTP(String routerUuid, User user);
}
