/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.once.xenapi.Connection;
import com.once.xenapi.Types;
import com.oncecloud.ha.core.entities.IPMIEntity;
import com.oncecloud.ha.core.entities.SSHEntity;
import com.oncecloud.ha.core.refs.HostRef;
import com.oncecloud.ha.core.refs.PoolRef;
import com.oncecloud.ha.core.refs.VMRef;
import com.oncecloud.ha.core.utils.DBUtils;
import com.oncecloud.ha.core.utils.EntityUtils;
import com.oncecloud.ha.core.utils.HWUtils;
import com.oncecloud.ha.core.utils.IPUtils;
import com.oncecloud.ha.core.utils.NoVNCUtils;
import com.oncecloud.ha.core.utils.ObjectUtils;
import com.oncecloud.ha.core.utils.SSHUtils;
import com.oncecloud.ha.core.utils.VMUtils;

/**
 * @author henry
 * @email wuheng09@gmail.com
 * @date 2014年8月20日
 *
 *       一个资源池对应一个Worker，负责其故障恢复
 */
public class HAPoolWorker implements Runnable {

	private final static Logger m_logger = Logger.getLogger(HAPoolWorker.class);

	private PoolRef m_pool;
	
	private boolean m_started = true;

	/***************************************************************************************************
	 * 
	 * 初始化相关操作
	 * 
	 ****************************************************************************************************/
	public HAPoolWorker(PoolRef pool) {
		super();
		this.m_pool = pool;
	}

	/***************************************************************************************************
	 * 
	 * 具体执行操作
	 * 
	 ****************************************************************************************************/
	public void run() {

		// 正常的主机
		List<HostRef> normalHosts = DBUtils.getAllServersOnPool(
				m_pool.getMysqlConn(), m_pool.getPoolUUID());

		// 确认宕机的主机
		Set<HostRef> crashHosts = new HashSet<HostRef>();

		// 可能宕机的主机
		Set<HostRef> suspectedHosts = new HashSet<HostRef>();

		while (m_started) {
			for (HostRef nServer : normalHosts) {
				Inet4Address vmmIP = IPUtils.getInet4Address(nServer
						.getVMHostIP());
				// 服务器处于正常状态
				if (IPUtils.avaiable(vmmIP)) {
					// 可ping通，说明服务器处于正常状态，此时查看是否有迁移过来的虚拟机需要启动
					m_logger.info("Server with IP (" + vmmIP + ") is avaiable.");
					markServerAvaiable(nServer);
					failoverVMsOnThisHostIfHad(nServer);
				} else {
					m_logger.info("Server with IP (" + vmmIP + ") is unavaiable.");
					markServerUnavaiable(nServer);
					suspectedHosts.add(nServer);
				}
			}

			// // 三次不可达，说明服务器宕机
			// // 强制关机****!!!
			for (HostRef nServer : suspectedHosts) {
				if (checkIfCrash(nServer)) {
					if(forceServerReboot(nServer)) {
						crashHosts.add(nServer);
					}
				} else {
					m_logger.error("Motherboard IP is null, HA is stop");
					this.m_started = false;
				}
			}

			int expectedNormalSize = normalHosts.size();
			normalHosts.removeAll(crashHosts);
			int actualNormalSize = normalHosts.size();
			migrateVMsToNormalServers(normalHosts, crashHosts);
			
			if (expectedNormalSize != actualNormalSize) {
				updatePoolInfo(normalHosts);
			}

			crashServersJoinBackPoolIfRecover(normalHosts, crashHosts);

			waitForNextPeriod();
		}

	}

	private void crashServersJoinBackPoolIfRecover(List<HostRef> normalHosts,
			Set<HostRef> crashHosts) {
		List<HostRef> recoverHosts = new ArrayList<HostRef>();
		for (HostRef crashserver : crashHosts) {
			Inet4Address vmmIP = IPUtils.getInet4Address(crashserver
					.getVMHostIP());
			
			if (IPUtils.avaiable(vmmIP, 3000)) {

				SSHEntity ssh = EntityUtils.createSSHEntity(crashserver
						.getVMHostIP());
				if (!SSHUtils.mountAll(ssh) || !SSHUtils.startSRagent(ssh)
						|| !SSHUtils.startPasswd(ssh)) {
					continue;
				}

				SSHUtils.removeAllVMsConfig(ssh);
				SSHUtils.restartXend(ssh);
				DBUtils.updateServerStatus(m_pool.getMysqlConn(),
						crashserver.getHostUUID(), null);
				Connection newConn = VMUtils.createXenConnection(EntityUtils
						.createXenEntity(crashserver.getVMHostIP()));
				VMUtils.joinPool(newConn, m_pool.getMaster());
				DBUtils.updateServerStatus(m_pool.getMysqlConn(),
						crashserver.getHostUUID(), m_pool.getPoolUUID());
				markServerAvaiable(crashserver);
				recoverHosts.add(crashserver);
			}
		}
		crashHosts.removeAll(recoverHosts);
		normalHosts.addAll(recoverHosts);
	}

	private void waitForNextPeriod() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

	private void updatePoolInfo(List<HostRef> normalHosts) {
		boolean isMaster = true;
		for (HostRef server : normalHosts) {
			SSHUtils.restartXend(EntityUtils.createSSHEntity(server
					.getVMHostIP()));

			Connection newConn = VMUtils.createXenConnection(EntityUtils
					.createXenEntity(server.getVMHostIP()));
			if (isMaster) {
				DBUtils.updatePoolStatus(m_pool.getMysqlConn(),
						server.getHostUUID(), m_pool.getPoolUUID());
				VMUtils.createPool(newConn, m_pool.getPoolUUID());
				isMaster = false;
				m_pool.setMaster(server.getVMHostIP());
				m_pool.setXenConn(VMUtils.createXenConnection(EntityUtils
						.createXenEntity(server.getVMHostIP())));
				m_logger.info("Recover pool with UUID " + m_pool.getPoolUUID()
						+ " and new master is " + server.getVMHostIP());
			} else {
				m_logger.error("Server " + server.getVMHostIP()
						+ " join to pool " + m_pool.getPoolUUID());
				VMUtils.joinPool(newConn, m_pool.getMaster());
			}

		}
	}

	private void migrateVMsToNormalServers(List<HostRef> normalHosts,
			Set<HostRef> crashHosts) {

		for(HostRef nServer : crashHosts) {
			List<VMRef> crashVMs = DBUtils.getAllVMsOnHost(m_pool.getMysqlConn(),
					nServer.getHostUUID());
			int serverNum = normalHosts.size();
			int vmNum = crashVMs.size();
			if (serverNum == 0) {
				return;
			}
			int vmPerServer = (vmNum % serverNum == 0) ? vmNum / serverNum : vmNum
					/ serverNum + 1;
			for (int i = 0; i < crashVMs.size(); i++) {
				HostRef newServer = normalHosts.get(i / vmPerServer);
				VMRef vm = crashVMs.get(i);
				vm.setHostUUID(newServer.getHostUUID());
				newServer.registerFailoverVM(vm);
				boolean sucessful = SSHUtils.copyVMConfig(
						EntityUtils.createSSHEntity(newServer.getVMHostIP()),
						m_pool.getHAPath() + "/" + vm.getVmUUID(),
						"/var/lib/xend/domains/" + vm.getVmUUID());
				if(sucessful) {
					DBUtils.updateVMOnHostInfo(m_pool.getMysqlConn(), vm);
					m_logger.info("VM configuration with UUID " + vm.getVmUUID()
							+ " is sucessful");
					m_logger.info("VM with UUID " + vm.getVmUUID()
							+ " migrate to Server " + newServer.getVMHostIP() + " sucessful");
				} else {
					m_logger.info("VM configuration with UUID " + vm.getVmUUID()
							+ " is failed");
					m_logger.info("VM with UUID " + vm.getVmUUID()
							+ " migrate to Server " + newServer.getVMHostIP() + " failed");
				}
				
			}
			DBUtils.updateServerStatus(m_pool.getMysqlConn(),
					nServer.getHostUUID(), null);
		}
	}
	

	private boolean forceServerReboot(HostRef nServer) {
		m_logger.info("Server with IP " + nServer.getVMHostIP()
				+ " is rebooting");
		IPMIEntity ipmi = DBUtils.getIPMI(m_pool.getMysqlConn(), nServer.getHostUUID());
		try {
			if (!ObjectUtils.invalid(ipmi)) {
				if (HWUtils.isPowerOn(ipmi.getUrl(), ipmi.getPort(),
						ipmi.getUser(), ipmi.getPwd())) {
					HWUtils.Shutdown(ipmi.getUrl(), ipmi.getPort(),
							ipmi.getUser(), ipmi.getPwd());
					while (HWUtils.isPowerOn(ipmi.getUrl(), ipmi.getPort(),
							ipmi.getUser(), ipmi.getPwd())) {
						Thread.sleep(10000);
					}

					HWUtils.Startup(ipmi.getUrl(), ipmi.getPort(),
							ipmi.getUser(), ipmi.getPwd());
				} else {
					HWUtils.Startup(ipmi.getUrl(), ipmi.getPort(),
							ipmi.getUser(), ipmi.getPwd());
				}

			} else {
				return false;
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());
			m_logger.error("No motherborad IP for server " + nServer.getVMHostIP());
			return false;
		}
		return true;
	}
	
	/***********************************************************************************
	 * 
	 * Core Functions
	 * 
	 ************************************************************************************/
	private void failoverVMsOnThisHostIfHad(HostRef nServer) {
		for (VMRef vm : nServer.getFailoverVMs()) {
			startFailoverVMIfNeed(vm);
		}
		nServer.markAllVMsFailoverSucessful();
	}

	private void startFailoverVMIfNeed(VMRef vm) {
		if (vm.needStartup()) {
			boolean sucessful = VMUtils.startVM(m_pool.getXenConn(),
					vm.getVmUUID());
			if (!sucessful) {
				m_logger.info("Start VM " + vm.getVmUUID() + " failed.");
				vm.setStartup(false);
				DBUtils.updateVMStatus(m_pool.getMysqlConn(), vm);
			} else {
				m_logger.info("Start VM " + vm.getVmUUID() + " sucessful.");
				updateNoVNCConsoleForVM(vm);
			}
		}
	}

	/**
	 * NoVNC无法启动，会被忽略，因为虚拟机已经正常运行，还可以通过远程桌面进行访问
	 * 
	 * @param vm
	 */
	private void updateNoVNCConsoleForVM(VMRef vm) {
		try {
			String hostIP = Types.toHost(vm.getHostUUID()).getAddress(
					m_pool.getXenConn());
			int port = NoVNCUtils.getVNCPort(vm.getVmUUID(),
					m_pool.getXenConn());
			NoVNCUtils.updateToken(m_pool.getNovncServer(), vm.getVmUUID()
					.substring(0, 8), hostIP, port);
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
	}

	/********************************************************************************
	 * 
	 * Others
	 * 
	 *********************************************************************************/
	private void markServerAvaiable(HostRef nServer) {
//		nServer.resetStorageUnavaiableTimes();
		nServer.resetVmUnavaiableTimes();
	}

	private void markServerUnavaiable(HostRef nServer) {
		nServer.incrementVmUnavaiableTimes();
//		nServer.incrementStorageUnavaiableTimes();
	}

	private boolean checkIfCrash(HostRef vmm) {
		return (vmm.getVmUnavaiableTimes() >= 3) ? true : false;
//		return (vmm.getStorageUnavaiableTimes() >= 3 || vmm
//				.getVmUnavaiableTimes() >= 3) ? true : false;
	}

	public void stop() {
		this.m_started = false;
	}

}
