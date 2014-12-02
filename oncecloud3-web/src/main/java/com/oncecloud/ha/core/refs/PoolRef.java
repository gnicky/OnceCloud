/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.refs;

/**
 * @author henry
 * @date   2014年8月22日
 *  
 *  用于记录资源资源的关键信息
 */
public class PoolRef {

	private String m_poolUUID = null;

	private String m_HAPath = null;
	
	private String m_noVNCServer = null;
	
	private java.sql.Connection m_mysqlConn = null;
	
	private com.once.xenapi.Connection m_xenConn= null;
	
	private String m_master;
	
	public String getNovncServer() {
		return m_noVNCServer;
	}

	public void setNovncServer(String novncServer) {
		this.m_noVNCServer = novncServer;
	}

	public String getPoolUUID() {
		return m_poolUUID;
	}

	public void setPoolUUID(String poolUUID) {
		this.m_poolUUID = poolUUID;
	}

	public String getHAPath() {
		return m_HAPath;
	}

	public void setHAPath(String HAPath) {
		m_HAPath = HAPath;
	}

	public String getMaster() {
		return m_master;
	}

	public void setMaster(String master) {
		this.m_master = master;
	}

	public java.sql.Connection getMysqlConn() {
		return m_mysqlConn;
	}

	public void setMysqlConn(java.sql.Connection mysqlConn) {
		this.m_mysqlConn = mysqlConn;
	}

	public com.once.xenapi.Connection getXenConn() {
		return m_xenConn;
	}

	public void setXenConn(com.once.xenapi.Connection xenConn) {
		this.m_xenConn = xenConn;
	}

}
