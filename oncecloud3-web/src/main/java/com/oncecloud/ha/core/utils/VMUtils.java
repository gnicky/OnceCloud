/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.net.URL;

import org.apache.log4j.Logger;

import com.once.xenapi.Connection;
import com.once.xenapi.Pool;
import com.once.xenapi.VM;
import com.oncecloud.ha.core.entities.XenEntity;

/**
 * @author henry
 * @date 2014年9月24日
 *
 * 虚拟机操作相关
 */
public class VMUtils {

	private final static Logger m_logger = Logger.getLogger(VMUtils.class);
	
	public static Connection createXenConnection(XenEntity config) {
		return (config == null) ? null : createXenConnection(config.getUrl(),
				config.getUser(), config.getPwd(), null);
	}

	private static Connection createXenConnection(URL url, String usr,
			String pwd, String version) {
		Connection conn = new Connection(url);
		try {
			com.once.xenapi.Session.loginWithPassword(conn, usr, pwd, version);
		} catch (Exception e) {
			return null;
		}
		return conn;
	}

	public synchronized static boolean startVM(Connection conn, String uuid) {
		try {
			VM vm = VM.getByUuid(conn, uuid);
			vm.start(conn, false, true);
		} catch (Exception e) {
			m_logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean createPool(Connection conn, String poolUudi) {
		try {
			Pool.create(conn, poolUudi);
		} catch (Exception e) {
			m_logger.error(e.getMessage());
			return false;
		}
		m_logger.info("Recover pool " + poolUudi + " sucessful.");
		return true;
	}

	public static boolean joinPool(Connection conn, String IP) {
		try {
			Pool.join(conn, IP, "root", "onceas");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
