/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.veraxsystems.vxipmi.api.async.ConnectionHandle;
import com.veraxsystems.vxipmi.api.sync.IpmiConnector;
import com.veraxsystems.vxipmi.coding.commands.IpmiVersion;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.chassis.ChassisControl;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatus;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatusResponseData;
import com.veraxsystems.vxipmi.coding.commands.chassis.PowerCommand;
import com.veraxsystems.vxipmi.coding.commands.session.SetSessionPrivilegeLevel;
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;

/**
 * @author henry
 * @date 2014年9月19日
 * 
 *      硬件相关的操作
 */
public class HWUtils {

	private final static Logger m_logger = Logger.getLogger(HWUtils.class);
	
	/****
	 * 
	 *  判定物理服务器是否处于开机状态
	 *  
	 * @param Ip        IP地址
	 * @param UdpPort   任意udp端口 
	 * @param user      用户名
	 * @param pwd       密码
	 * @return
	 * @throws Exception
	 */
	public synchronized static boolean isPowerOn(String Ip, int UdpPort,
			String user, String pwd) throws Exception {
		IpmiConnector connector = new IpmiConnector(UdpPort);

		ConnectionHandle handle = connector.createConnection(InetAddress
				.getByName(Ip));

		CipherSuite cs = connector.getAvailableCipherSuites(handle).get(0);

		connector.getChannelAuthenticationCapabilities(handle, cs,
				PrivilegeLevel.Operator);

		connector.openSession(handle, user, pwd, null);

		GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector
				.sendMessage(handle, new GetChassisStatus(IpmiVersion.V20, cs,
						AuthenticationType.RMCPPlus));

		connector.closeSession(handle);

		connector.tearDown();

		boolean powerOn = rd.isPowerOn();
		
		m_logger.info("Server with IP " + Ip + " is " + (powerOn ? "power on" : "power off"));
		
		return powerOn;
	}

	/****
	 * 
	 *  对指定物理服务器执行开机操作
	 *  
	 * @param Ip        IP地址
	 * @param UdpPort   任意udp端口 
	 * @param user      用户名
	 * @param pwd       密码
	 * @return
	 * @throws Exception
	 */
	public synchronized static void Shutdown(String Ip, int UdpPort,
			String user, String pwd) throws Exception {
		
		m_logger.info("Shut down server with IP " + Ip);
		
		IpmiConnector connector = new IpmiConnector(UdpPort);

		ConnectionHandle handle = connector.createConnection(InetAddress
				.getByName(Ip));

		CipherSuite cs = connector.getAvailableCipherSuites(handle).get(0);
		connector.getChannelAuthenticationCapabilities(handle, cs,
				PrivilegeLevel.Operator);

		connector.openSession(handle, user, pwd, null);

		connector.sendMessage(handle, new SetSessionPrivilegeLevel(
				IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
				PrivilegeLevel.Operator));

		ChassisControl chassisControl = new ChassisControl(IpmiVersion.V20, cs,
				AuthenticationType.RMCPPlus, PowerCommand.PowerDown);

		connector.sendMessage(handle, chassisControl);

		connector.closeSession(handle);
		connector.tearDown();
	}

	/****
	 * 
	 *  对指定物理服务器执行关机操作
	 *  
	 * @param Ip        IP地址
	 * @param UdpPort   任意udp端口 
	 * @param user      用户名
	 * @param pwd       密码
	 * @return
	 * @throws Exception
	 */
	public synchronized static void Startup(String Ip, int UdpPort, String user,
			String pwd) throws Exception {
		
		m_logger.info("Start server with IP " + Ip);
		
		IpmiConnector connector = new IpmiConnector(UdpPort);
		ConnectionHandle handle = connector.createConnection(InetAddress
				.getByName(Ip));

		CipherSuite cs = connector.getAvailableCipherSuites(handle).get(0);
		connector.getChannelAuthenticationCapabilities(handle, cs,
				PrivilegeLevel.Operator);
		connector.openSession(handle, user, pwd, null);

		connector.sendMessage(handle, new SetSessionPrivilegeLevel(
				IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
				PrivilegeLevel.Operator));

		ChassisControl chassisControl = new ChassisControl(IpmiVersion.V20, cs,
				AuthenticationType.RMCPPlus, PowerCommand.PowerUp);

		connector.sendMessage(handle, chassisControl);

		connector.closeSession(handle);
		connector.tearDown();
	}
}
