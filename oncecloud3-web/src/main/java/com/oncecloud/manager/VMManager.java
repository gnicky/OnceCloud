package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.once.xenapi.Connection;
import com.once.xenapi.VM.Record;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.User;

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

	public abstract void adminDeleteVM(int userId, String uuid);

//	public abstract void deleteVM(int userId, String uuid, String poolUuid);
//
//	public abstract void restartVM(int userId, String uuid, String poolUuid);

	public abstract void doAdminStartVm(int userId, String uuid);

	public abstract void doAdminShutDown(int userId, String uuid, String force);

//	public abstract void startVM(int userId, String uuid, String poolUuid);
//
//	public abstract void shutdownVM(int userId, String uuid, String force,
//			String poolUuid);
//
//	/**
//	 * 获取用户主机列表
//	 * 
//	 * @param userId
//	 * @param page
//	 * @param limit
//	 * @param search
//	 * @return
//	 */
//	public abstract JSONArray getVMList(int userId, int page, int limit,
//			String search);

	public abstract JSONArray getAdminVMList(int page, int limit, String host,
			int importance, String type);

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
//
//	public abstract String getQuota(int userId, int userLevel, int count);
//
//	public abstract boolean syncAddVMOperate(String hostUuid, String vmUuid,
//			int power);
//
//	public abstract void syncDelVMOperate(String vmUuid, String hostUuid);
//
//	public abstract void syncUpdateVM(String vmUuid, int powerAttrvalue);

	public abstract void updateImportance(String uuid, int importance);

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

	public abstract JSONArray getISOList(String poolUuid);

//	public abstract void createVM(String vmUuid, String tplUuid, int userId,
//			String vmName, int cpuCore, double memorySize, String pwd,
//			String poolUuid, String vnetuuid);

	public abstract void createVMByISO(String vmUuid, String isoUuid,
			String srUuid, String name, int cpu, int memory, int volumeSize,
			String poolUuid, int userId);

	public abstract boolean addMac(String uuid, String type, String physical,
			String vnetid);

	public abstract boolean modifyVnet(String uuid, String type, String vnetid,
			String vifUuid);

	public abstract boolean modifyPhysical(String uuid, String type,
			String physical, String vifUuid);

	public abstract boolean deleteMac(String uuid, String type, String vifUuid);

	public abstract JSONArray getMacs(String uuid, String type);

	public abstract JSONArray getNets(String uuid, String type);

	public abstract void saveToDataBase(String vmUuid, String vmPWD, int vmUID,
			int vmPlatform, String vmName, String vmIP);

	public abstract boolean templateToVM(String uuid);

	public abstract boolean vmToTemplate(String uuid);

//	public abstract int getCount(int userId);
//
//	public abstract boolean adjustMemAndCPU(String uuid, int userId, int cpu,
//			int mem, String content, String conid);
}
