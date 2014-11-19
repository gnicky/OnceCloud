package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.LB;

public interface LBDAO {

	public abstract int countAllAdminVMList(String host, int importance);

//	public abstract int countAllLBList(String search, int lbUID);
//
//	public abstract int countAllLBListAlarm(String search, int lbUID);
//
//	public abstract int countLBsWithoutEIP(String search, int userId);

	public abstract LB getAliveLB(String lbUuid);

//	/**
//	 * @author hty
//	 * @param alarmUuid
//	 * @param uid
//	 * @return
//	 */
//	public abstract List<LB> getAllListAlarm(int lbUID, String alarmUuid);

	public abstract LB getLB(String lbUuid);

//	public abstract String getLBName(String lbuuid);

	public abstract List<LB> getOnePageAdminVmList(int page, int limit,
			String host, int importance);

//	public abstract List<LB> getOnePageLBList(int userId, int page, int limit,
//			String search);
//
//	/**
//	 * @author hty
//	 * @param page
//	 * @param limit
//	 * @param search
//	 * @param uid
//	 * @return
//	 */
//	public abstract List<LB> getOnePageLBListAlarm(int page, int limit,
//			String search, int lbUID);
//
//	public abstract List<LB> getOnePageLBsWithoutEip(int page, int limit,
//			String search, int userId);
//
//	/**
//	 * @author hty
//	 * @param alarmUuid
//	 * @return
//	 */
//	public abstract boolean isLNotExistAlarm(String alarmUuid);
//
//	public abstract boolean preCreateLB(String uuid, String pwd, int userId,
//			String name, String mac, int capacity, int power, int status,
//			Date createDate);
//
//	public abstract void removeLB(int userId, String uuid);

	public abstract boolean updateLBHostUuid(String uuid, String hostUuid);

	public abstract boolean updateLBPowerStatus(String uuid, int powerStatus);

	public abstract boolean updateLBImportance(String uuid, int lbImportance);

//	public abstract boolean updateLBStatus(String lbUuid, int state);
//
//	/**
//	 * @author hty
//	 * @param lbUuid
//	 * @param alarmUuid
//	 */
//	public abstract void updateAlarm(String lbUuid, String alarmUuid);
//
//	public abstract void updateFirewall(String lbUuid, String firewallId);
//
//	public abstract void updateLB(int userId, String uuid, String pwd,
//			int power, String hostUuid, String ip);
//
//	public abstract void updateName(String lbuuid, String newName,
//			String description);

	/**
	 * 更新负载均衡电源状态和所在服务器
	 * 
	 * @param session
	 * @param uuid
	 * @param power
	 * @param hostUuid
	 */
	public abstract void updatePowerAndHost(String uuid,
			int power, String hostUuid);

}
