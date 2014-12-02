/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.refs;

/**
 * @author henry
 * @date   2014年8月25日
 * 
 *  用于记录指定虚拟机标识，其所属物理机标识，以及其开机关机状态
 */
public class VMRef {

	/**
	 * 虚拟机UUID
	 */
	private String m_vmUUID;
	
	/**
	 * 主机UUID
	 */
	private String m_hostUUID;
	
	/**
	 * 开机关机状态
	 */
	private boolean m_startup;
	
	

	public String getVmUUID() {
		return m_vmUUID;
	}

	public void setVmUUID(String vmUUID) {
		this.m_vmUUID = vmUUID;
	}

	public String getHostUUID() {
		return m_hostUUID;
	}

	public void setHostUUID(String hostUUID) {
		this.m_hostUUID = hostUUID;
	}

	public boolean needStartup() {
		return m_startup;
	}

	public void setStartup(boolean startup) {
		this.m_startup = startup;
	}

}
