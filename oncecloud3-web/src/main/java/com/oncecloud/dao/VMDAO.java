package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.OCVM;

public interface VMDAO {
	/**
	 * 获取主机
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract OCVM getVM(String vmUuid);

	/**
	 * 获取主机名称
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract String getVmName(String vmUuid);

	/**
	 * 获取使用中的主机
	 * 
	 * @param vmUuid
	 * @return
	 */
	public abstract OCVM getAliveVM(String vmUuid);

	/**
	 * 获取一页用户主机列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract List<OCVM> getOnePageVMs(int userId, int page, int limit,
			String search);

	/**
	 * 获取一页管理员主机列表
	 * 
	 * @param page
	 * @param limit
	 * @param host
	 * @param importance
	 * @return
	 */
	public abstract List<OCVM> getOnePageVMsOfAdmin(int page, int limit,
			String host, int importance);

	/**
	 * 获取一页未设置监控警告的主机列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param userId
	 * @return
	 */
	public abstract List<OCVM> getOnePageVMsWithoutAlarm(int page, int limit,
			String search, int userId);

	/**
	 * 获取一页没有绑定公网IP的主机列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param userId
	 * @return
	 */
	public abstract List<OCVM> getOnePageVMsWithoutEIP(int page, int limit,
			String search, int userId);

	/**
	 * 获取对应监控警告的主机列表
	 * 
	 * @param vmUID
	 * @param alarmUuid
	 * @return
	 */
	public abstract List<OCVM> getVMsOfAlarm(int vmUID, String alarmUuid);

	/**
	 * 获取对应私有网络的主机列表
	 * 
	 * @param vmVlan
	 * @return
	 */
	public abstract List<OCVM> getVMsOfVnet(String vmVlan);

	/**
	 * 获取简单用户主机列表
	 * 
	 * @param userId
	 * @return
	 */
	public abstract List<Object[]> getBasicNetworkList(int userId);

	/**
	 * 获取用户主机总数
	 * 
	 * @param search
	 * @param vmUID
	 * @return
	 */
	public abstract int countVMs(int userId, String search);

	/**
	 * 获取管理员主机总数
	 * 
	 * @param host
	 * @param importance
	 * @return
	 */
	public abstract int countVMsOfAdmin(String host, int importance);

	/**
	 * 获取用户的主机总数
	 * 
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public abstract int countVMsOfUser(int userId);

	/**
	 * 获取未设置监控警告的主机总数
	 * 
	 * @param search
	 * @param userId
	 * @return
	 */
	public abstract int countVMsWithoutAlarm(String search, int userId);

	/**
	 * 获取未绑定公网IP的主机总数
	 * 
	 * @param search
	 * @param userId
	 * @return
	 */
	public abstract int countVMsWithoutEIP(String search, int userId);

	/**
	 * 获取指定私有网络的主机总数
	 * 
	 * @param vnetUuid
	 * @return
	 */
	public abstract int countVMsOfVnet(String vnetUuid);

	/**
	 * 获取指定服务器的主机总数
	 * 
	 * @param hostUuid
	 * @return
	 */
	public abstract int countVMsOfHost(String hostUuid);

	/**
	 * 预创建虚拟机
	 * 
	 * @param vmUuid
	 * @param vmPWD
	 * @param vmUID
	 * @param vmName
	 * @param vmPlatform
	 * @param vmMac
	 * @param vmMem
	 * @param vmCpu
	 * @param vmPower
	 * @param vmStatus
	 * @param createDate
	 * @return
	 */
	public abstract boolean preCreateVM(String vmUuid, String vmPWD,
			Integer vmUID, String vmName, Integer vmPlatform, String vmMac,
			Integer vmMem, Integer vmCpu, Integer vmPower, Integer vmStatus,
			Date createDate);

	/**
	 * 删除主机
	 * 
	 * @param userId
	 * @param vmUuid
	 */
	public abstract void removeVM(int userId, String vmUuid);

	/**
	 * 更新主机
	 * 
	 * @param vm
	 */
	public abstract void updateVM(OCVM vm);

	public abstract void saveVM(OCVM vm);

	public abstract void deleteVM(OCVM vm);

	/**
	 * 更新主机
	 * 
	 * @param userId
	 * @param vmUuid
	 * @param vmPWD
	 * @param vmPower
	 * @param hostUuid
	 * @param ip
	 * @return
	 */
	public abstract boolean updateVM(int userId, String vmUuid, String vmPWD,
			int vmPower, String hostUuid, String ip);

	/**
	 * 更新主机备份时间
	 * 
	 * @param vmUuid
	 * @param date
	 */
	public abstract void updateBackupDate(String vmUuid, Date date);

	/**
	 * 更新主机防火墙
	 * 
	 * @param vmUuid
	 * @param firewallId
	 */
	public abstract void updateFirewall(String vmUuid, String firewallId);

	/**
	 * 更新主机监控警告
	 * 
	 * @param vmUuid
	 * @param alarmUuid
	 */
	public abstract boolean updateAlarm(String vmUuid, String alarmUuid);

	/**
	 * 更新主机名称和描述
	 * 
	 * @param vmUuid
	 * @param vmName
	 * @param vmDesc
	 */
	public abstract void updateName(String vmUuid, String vmName, String vmDesc);

	/**
	 * 更新主机状态
	 * 
	 * @param vmUuid
	 * @param hostUuid
	 * @param power
	 * @return
	 */
	public abstract boolean updateVMStatus(String vmUuid, String hostUuid,
			int power);

	public abstract boolean updateVMImportance(String vmUuid, int vmImportance);

	/**
	 * 更新主机电源状态和所在服务器
	 * 
	 * @param session
	 * @param uuid
	 * @param power
	 * @param hostUuid
	 */
	public abstract void updatePowerAndHostNoTransaction(String uuid,
			int power, String hostUuid);

	/**
	 * 更新主机私有网络
	 * 
	 * @param vmuuid
	 * @param vnetid
	 * @return
	 */
	public abstract boolean updateVMVlan(String vmuuid, String vnetid);

	/**
	 * 更新主机电源状态
	 * 
	 * @param uuid
	 * @param powerStatus
	 * @return
	 */
	public abstract boolean updatePowerStatus(String uuid, int powerStatus);

	public abstract void unbindNet(String uuid);

	/**
	 * 更新主机所在服务器
	 * 
	 * @param uuid
	 * @param hostUuid
	 * @return
	 */
	public abstract boolean updateHostUuid(String uuid, String hostUuid);

	/**
	 * 主机一致性删除
	 * 
	 * @param hostUuid
	 * @param vmUuid
	 */
	public abstract void syncDelVMOperate(String hostUuid, String vmUuid);

	/**
	 * 主机回到基础网络
	 * 
	 * @param vmuuid
	 * @param ip
	 * @return
	 */
	public abstract boolean returnToBasicNetwork(String vmuuid, String ip);

	/**
	 * 是否有主机具有该监控警告
	 * 
	 * @param alarmUuid
	 * @return
	 */
	public abstract boolean isNotExistAlarm(String alarmUuid);
}
