/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.oncecloud.ha.core.entities.IPMIEntity;
import com.oncecloud.ha.core.refs.HostRef;
import com.oncecloud.ha.core.refs.VMRef;

/**
 * @author henry
 * @date 2014年9月23日 和数据库操作相关的操作
 */
public class DBUtils {

	private final static Logger m_logger = Logger.getLogger(DBUtils.class);

	/**
	 * 得到指定资源池的所有服务器
	 * 
	 * @param conn
	 * @param poolUUID
	 * @return
	 */
	public synchronized static List<HostRef> getAllServersOnPool(
			java.sql.Connection conn, String poolUUID) {
		List<HostRef> servers = new ArrayList<HostRef>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT host_uuid, host_ip FROM oc_host WHERE pool_uuid='"
							+ poolUUID + "'");
			while (rs.next()) {
				HostRef server = new HostRef();
				// String hostUUID = rs.getString(1);
				// String hostIP = rs.getString(2);
				server.setHostUUID(rs.getString(1));
				server.setVMHostIP(rs.getString(2));
//				server.setStoreBondIP(rs.getString(3));
//				server.setMotherboardIP(rs.getString(4));
				// server.setStoreBondIP(getStoreBondIP(hostIP));
				// server.setMotherboardIP(getMotherboardIPFromConfig(hostIP));
				servers.add(server);
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
		return servers;
	}

	/**
	 * 得到指定服务器的所有虚拟机
	 * 
	 * @param conn
	 * @param hostUUID
	 * @return
	 */
	public static List<VMRef> getAllVMsOnHost(java.sql.Connection conn,
			String hostUUID) {
		List<VMRef> VMs = new ArrayList<VMRef>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT vm_uuid,vm_power FROM oc_vm WHERE host_uuid='"
							+ hostUUID + "'");
			while (rs.next()) {
				VMRef vm = new VMRef();
				String vmUUID = rs.getString(1);
				int status = rs.getInt(2);
				vm.setVmUUID(vmUUID);
				vm.setHostUUID(hostUUID);
				vm.setStartup(isStart(status));
				VMs.add(vm);

			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());

		}
		return VMs;
	}

	public static String getPoolHA(java.sql.Connection conn, String poolUUID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ha_path FROM oc_pool WHERE pool_uuid='"
							+ poolUUID + "'");
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());

		}
		return null;
	}

	/**
	 * 更新
	 * 
	 * @param conn
	 * @param vm
	 * @return
	 */
	public static boolean updateVMOnHostInfo(java.sql.Connection conn, VMRef vm) {
		try {
			Statement stmt = conn.createStatement();
			boolean sucessful = stmt.execute("UPDATE oc_vm SET host_uuid = '"
					+ vm.getHostUUID() + "' WHERE vm_uuid = '" + vm.getVmUUID()
					+ "' ");
			m_logger.info("Update vm " + vm.getVmUUID() + " for new host "
					+ vm.getHostUUID());
			return sucessful;
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
		return false;
	}

	public synchronized static boolean updateVMStatus(java.sql.Connection conn,
			VMRef vm) {
		try {
			int status = (vm.needStartup()) ? 1 : 0;
			Statement stmt = conn.createStatement();
			return stmt.execute("UPDATE oc_vm SET vm_uuid = '" + vm.getVmUUID()
					+ "' WHERE vm_power = '" + status + "' ");
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
		return false;
	}

	// private static String getStoreBondIP(String hostIP) {
	// return
	// SSHUtils.getOvsIP(SSHUtils.createSSHChannel(EntityUtils.createSSHEntity(hostIP)),
	// "ovs0");
	// }
	//
	// private static String getMotherboardIPFromConfig(String hostIP) {
	// return IPMIManager.createIPMIManager().get(hostIP).getUrl();
	// }

	private static boolean isStart(int status) {
		return (status == 1) ? true : false;
	}

	public static boolean updatePoolStatus(java.sql.Connection conn,
			String masterUUID, String poolUUID) {
		try {
			Statement stmt = conn.createStatement();
			return stmt.execute("UPDATE oc_pool SET pool_master = '"
					+ masterUUID + "' WHERE pool_uuid = '" + poolUUID + "' ");
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
		return false;
	}

	public static boolean updateServerStatus(java.sql.Connection conn,
			String hostUUID, String poolUUID) {
		try {
			Statement stmt = conn.createStatement();
			return stmt.execute("UPDATE oc_host SET host_uuid = '" + hostUUID
					+ "' WHERE pool_uuid = '" + poolUUID + "' ");
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
		return false;
	}
	
	
	public static IPMIEntity getIPMI(java.sql.Connection conn,
			String hostUUID) {
		IPMIEntity ipmi = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT power_username,power_password,power_port,motherboard_ip FROM power WHERE host_uuid='"
							+ hostUUID + "'");
			while (rs.next()) {
				ipmi.setUser(rs.getString(1));
				ipmi.setPwd(rs.getString(2));
				ipmi.setPort(rs.getInt(3));
				ipmi.setUrl(rs.getString(4));
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());
			return null;

		}
		return ipmi;
	}
}
