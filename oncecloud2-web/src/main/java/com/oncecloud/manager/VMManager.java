package com.oncecloud.manager;

import org.json.JSONArray;

public interface VMManager {
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;
	public final static int POWER_RESTART = 6;

//	public abstract Record createVMOnHost(Connection c, String vmUuid,
//			String tplUuid, String loginName, String loginPwd, long cpuCore,
//			long memoryCapacity, String mac, String ip, String OS,
//			String hostUuid, String imagePwd, String vmName, boolean ping);
//
//	public abstract Record createVMFromVDI(Connection c, String vmUuid,
//			String vdiUuid, String tplUuid, String loginName, String loginPwd,
//			long cpuCore, long memoryCapacity, String mac, String ip,
//			String OS, String hostUuid, String imagePwd, String vmName,
//			boolean ping);
//
//	public abstract String getHostAddress(String hostUuid);
//
//	public abstract int getVNCPort(String uuid, String poolUuid);
//
//	public abstract boolean setVlan(String uuid, int vlanId, String poolUuid);
//
//	public abstract boolean changeBandwidth(Integer userId, String uuid,
//			int size);
//
//	public abstract String getAllocateHost(String poolUuid, int memory);

	public abstract void deleteVM(int userId, String uuid, String poolUuid);

	public abstract void restartVM(int userId, String uuid, String poolUuid);

	public abstract void startVM(int userId, String uuid, String poolUuid);

	public abstract void shutdownVM(int userId, String uuid, String force,
			String poolUuid);

	/**
	 * 获取用户主机列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getVMList(int userId, int page, int limit,
			String search);

//	public abstract JSONArray getVMsOfUser(int userId, int page, int limit,
//			String search);
//
//	public abstract JSONArray getBasicNetworkList(int userId);
//
//	/**
//	 * 获取主机详细信息
//	 * 
//	 * @param vmUuid
//	 * @return
//	 */
//	public abstract JSONObject getVMDetail(String vmUuid);

	public abstract String getQuota(int userId, int userLevel, int count);

//	public abstract boolean syncAddVMOperate(String hostUuid, String vmUuid,
//			int power);
//
//	public abstract void syncDelVMOperate(String vmUuid, String hostUuid);
//
//	public abstract void syncUpdateVM(String vmUuid, int powerAttrvalue);

//	public abstract void assginIpAddress(Connection c, String url,
//			String subnet, String vnUuid);
//
//	public abstract boolean assginIpAddressToVM(Connection c, String url,
//			String subnet, OCVM vm);
//
//	public abstract void unAssginIpAddress(Connection c, String vnUuid);
//
//	public abstract JSONObject unbindNet(String uuid, User user,
//			String content, String conid);

	public abstract void createVM(String vmUuid, String tplUuid, int userId,
			String vmName, int cpuCore, double memorySize, String pwd,
			String poolUuid, String vnetuuid);

//	public abstract int getCount(int userId);
//
//	public abstract boolean adjustMemAndCPU(String uuid, int userId, int cpu,
//			int mem, String content, String conid);
	
}
