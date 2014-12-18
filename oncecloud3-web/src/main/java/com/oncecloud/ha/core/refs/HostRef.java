/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.refs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author henry
 * @date   2014年8月25日
 *
 * 用于记录服务器标识，IP以及可达状态，以及已迁移虚拟机信息，只是记录迁移过来的虚拟机信息，它是该物理机上所有虚拟机的子集
 */
public class HostRef {

	/**
	 * 主机UUID
	 */
	private String m_hostUUID;

	/**
	 * 所属资源池 UUID
	 */
	private String m_poolUUID;

	/**
	 * 物理服务器给虚拟机桥接使用的IP, 比如ovs0
	 */
	private String m_vbIP;

	/**
	 * 物理服务器给虚拟机桥接使用的IP连续不可达次数, 比如ovs0
	 */
	private int vbUnavaiableTimes = 0;
	
	/**
	 * 物理服务器使用分布式存储的IP,如分布式存储如Glusterfs
	 */
	private String m_sbIP;
	
	/**
	 * 物理服务器使用分布式存储的IP连续不可达次数，如分布式存储如Glusterfs
	 */
//	private int sbUnavaiableTimes = 0;
	
	/**
	 * 物理服务器主板IP,可以网络唤醒
	 */
//	private String m_mbIP;
	
	
	/**
	 * 迁移过来的虚拟机全集
	 */
	private List<VMRef> m_failoverVMs = new ArrayList<VMRef>();
	

	public String getHostUUID() {
		return m_hostUUID;
	}

	public void setHostUUID(String hostUUID) {
		this.m_hostUUID = hostUUID;
	}


	public String getPoolUUID() {
		return m_poolUUID;
	}

	public void setPoolUUID(String poolUUID) {
		this.m_poolUUID = poolUUID;
	}

	/**
	 * 物理服务器给虚拟机桥接使用的IP, 比如ovs0
	 */
	public String getVMHostIP() {
		return m_vbIP;
	}

	/**
	 * 物理服务器给虚拟机桥接使用的IP, 比如ovs0
	 */
	public void setVMHostIP(String vbIP) {
		this.m_vbIP = vbIP;
	}

	/**
	 * 物理服务器使用分布式存储的IP,如分布式存储如Glusterfs
	 */
	public String getStoreBondIP() {
		return m_sbIP;
	}

	/**
	 * 物理服务器使用分布式存储的IP,如分布式存储如Glusterfs
	 */
	public void setStoreBondIP(String storeManagerIP) {
		this.m_sbIP = storeManagerIP;
	}

//	public String getMotherboardIP() {
//		return m_mbIP;
//	}
//
//	public void setMotherboardIP(String motherboardIP) {
//		this.m_mbIP = motherboardIP;
//	}

	/**
	 * 获取物理服务器给虚拟机桥接使用的IP连续不可达次数
	 * 
	 */
	public int getVmUnavaiableTimes() {
		return vbUnavaiableTimes;
	}

	/**
	 * 获取物理服务器使用分布式存储的IP连续不可达次数
	 * 
	 */
//	public int getStorageUnavaiableTimes() {
//		return sbUnavaiableTimes;
//	}
//	
	/**
	 * 增加物理服务器给虚拟机桥接使用的IP连续不可达次数
	 */
	public void incrementVmUnavaiableTimes() {
		vbUnavaiableTimes++;
	}
	
	/**
	 * 增加物理服务器使用分布式存储的IP连续不可达次数
	 */
//	public void incrementStorageUnavaiableTimes() {
//		sbUnavaiableTimes++;
//	}
	
	public void resetVmUnavaiableTimes() {
		vbUnavaiableTimes = 0;
	}
	
//	public void resetStorageUnavaiableTimes() {
//		sbUnavaiableTimes = 0;
//	}

	/**
	 * 不存在返回Null的场景，只会返回数据数为0的List
	 * 
	 * @return
	 */
	public List<VMRef> getFailoverVMs() {
		return m_failoverVMs;
	}
	
	public void registerFailoverVM(VMRef vm) {
		m_failoverVMs.add(vm);
	}
	
	public void markVMFailoverSucessful(VMRef vm) {
		m_failoverVMs.remove(vm);
	}
	
	public void markAllVMsFailoverSucessful() {
		m_failoverVMs.clear();;
	}

}
