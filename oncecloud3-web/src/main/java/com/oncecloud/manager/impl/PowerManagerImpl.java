package com.oncecloud.manager.impl;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.PowerDAO;
import com.oncecloud.entity.Power;
import com.oncecloud.manager.PowerManager;
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

@Component("PowerManager")
public class PowerManagerImpl implements PowerManager {

	private PowerDAO powerDAO;
	
	
	public PowerDAO getPowerDAO() {
		return powerDAO;
	}

	@Autowired
	public void setPowerDAO(PowerDAO powerDAO) {
		this.powerDAO = powerDAO;
	}

	public boolean savePower(String powerUuid, String hostUuid,
			String motherboardIP, int powerPort, String powerUsername,
			String powerPassword, Integer powerValid) {
		// TODO Auto-generated method stub
		Power power =new Power(powerUuid,hostUuid,motherboardIP,powerPort,powerUsername,powerPassword,powerValid);
		if(this.getPowerDAO().editPower(power))
		{
			return true;
		}
		return false;
	}

	//// 返回值 1 是已经开启， 0 是关闭 -1 验证不通过
	public int getStatusOfPower(String powerIP, int powerPort,
			String userName, String passWord) {
		// TODO Auto-generated method stub
		try{
			IpmiConnector connector = new IpmiConnector(powerPort);
	
			ConnectionHandle handle = connector.createConnection(InetAddress
					.getByName(powerIP));
	
			CipherSuite cs = connector.getAvailableCipherSuites(handle).get(0);
	
			connector.getChannelAuthenticationCapabilities(handle, cs,
					PrivilegeLevel.Operator);
	
			connector.openSession(handle, userName, passWord, null);
	
			GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector
					.sendMessage(handle, new GetChassisStatus(IpmiVersion.V20, cs,
							AuthenticationType.RMCPPlus));
	
			connector.closeSession(handle);
	
			connector.tearDown();
	
			boolean powerOn = rd.isPowerOn();
			if(powerOn)
				return 1;
			
			return 0;
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	public boolean startPower(String powerIP, int powerPort,
			String userName, String passWord) {
		// TODO Auto-generated method stub
		try{
			IpmiConnector connector = new IpmiConnector(powerPort);
			ConnectionHandle handle = connector.createConnection(InetAddress
					.getByName(powerIP));

			CipherSuite cs = connector.getAvailableCipherSuites(handle).get(0);
			connector.getChannelAuthenticationCapabilities(handle, cs,
					PrivilegeLevel.Operator);
			connector.openSession(handle, userName, passWord, null);

			connector.sendMessage(handle, new SetSessionPrivilegeLevel(
					IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
					PrivilegeLevel.Operator));

			ChassisControl chassisControl = new ChassisControl(IpmiVersion.V20, cs,
					AuthenticationType.RMCPPlus, PowerCommand.PowerUp);

			connector.sendMessage(handle, chassisControl);

			connector.closeSession(handle);
			connector.tearDown();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean stopPower(String powerIP, int powerPort, String userName,
			String passWord) {
		// TODO Auto-generated method stub
		try{
			IpmiConnector connector = new IpmiConnector(powerPort);

			ConnectionHandle handle = connector.createConnection(InetAddress
					.getByName(powerIP));

			CipherSuite cs = connector.getAvailableCipherSuites(handle).get(0);
			connector.getChannelAuthenticationCapabilities(handle, cs,
					PrivilegeLevel.Operator);

			connector.openSession(handle, userName, passWord, null);

			connector.sendMessage(handle, new SetSessionPrivilegeLevel(
					IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
					PrivilegeLevel.Operator));

			ChassisControl chassisControl = new ChassisControl(IpmiVersion.V20, cs,
					AuthenticationType.RMCPPlus, PowerCommand.PowerDown);

			connector.sendMessage(handle, chassisControl);

			connector.closeSession(handle);
			connector.tearDown();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean deletePower(String hostUuid) {
		// TODO Auto-generated method stub
		Power power =this.getPower(hostUuid);
		if(this.getPowerDAO().removePower(power))
		{
			return true;
		}
		return false;
	}

	public Power getPower(String hostUuid) {
		// TODO Auto-generated method stub
		Power power =this.getPowerDAO().getPowerByHostID(hostUuid);
		return power;
	}

}
