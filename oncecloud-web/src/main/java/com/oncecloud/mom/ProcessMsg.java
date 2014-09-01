package com.oncecloud.mom;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.OCVM;
import com.oncecloud.main.NoVNC;
import com.oncecloud.manager.VMManager;
import com.oncecloud.message.MessagePush;

@Component
public class ProcessMsg {
	private VMDAO vmDAO;
	private HostDAO hostDAO;
	private VMManager vmManager;
	private MessagePush messagePush;

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public void ProcessSync(String message) {
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(message);
		JsonObject hostjo = je.getAsJsonObject();
		for (Map.Entry<String, JsonElement> hostentry : hostjo.entrySet()) {
			String hostUuid = hostentry.getKey();
			JsonObject opjo = hostentry.getValue().getAsJsonObject();
			for (Map.Entry<String, JsonElement> opentry : opjo.entrySet()) {
				String optype = opentry.getKey();
				JsonObject resourcejo = opentry.getValue().getAsJsonObject();
				for (Map.Entry<String, JsonElement> resourceentry : resourcejo
						.entrySet()) {
					String resourcetype = resourceentry.getKey();
					if (resourcetype.equals("VM")) {
						JsonObject vmjo = resourceentry.getValue()
								.getAsJsonObject();
						for (Map.Entry<String, JsonElement> vmentry : vmjo
								.entrySet()) {
							String vmUuid = vmentry.getKey();
							if (optype.equals("ADD")) {
								JsonObject attrjo = vmentry.getValue()
										.getAsJsonObject();
								int powerAttrvalue = 1;
								for (Map.Entry<String, JsonElement> attrentry : attrjo
										.entrySet()) {
									String attr = attrentry.getKey();
									if (attr.equals("POWER")) {
										powerAttrvalue = attrentry.getValue()
												.getAsInt();
									}
								}
								this.getVmManager().syncAddVMOperate(hostUuid,
										vmUuid, powerAttrvalue);
							} else if (optype.equals("DEL")) {
								this.getVmDAO().syncDelVMOperate(hostUuid,
										vmUuid);
							} else if (optype.equals("UPDATE")) {
								JsonObject attrjo = vmentry.getValue()
										.getAsJsonObject();
								int powerAttrvalue = 1;
								for (Map.Entry<String, JsonElement> attrentry : attrjo
										.entrySet()) {
									String attr = attrentry.getKey();
									if (attr.equals("POWER")) {
										powerAttrvalue = attrentry.getValue()
												.getAsInt();
									}
								}
								OCVM ocvm = this.getVmDAO().getVM(vmUuid);
								if (ocvm != null) {
									int userId = ocvm.getVmUID();
									String poolUuid = this.getHostDAO()
											.getHost(ocvm.getHostUuid())
											.getPoolUuid();
									if (powerAttrvalue == 1
											&& ocvm.getVmPower() == 0) {
										this.getVmDAO().updatePowerStatus(
												vmUuid, powerAttrvalue);
										String hostAddress = this
												.getVmManager().getHostAddress(
														ocvm.getHostUuid());
										int port = this.getVmManager()
												.getVNCPort(vmUuid, poolUuid);
										NoVNC.createToken(
												vmUuid.substring(0, 8),
												hostAddress, port);
										this.getMessagePush().editRowStatus(
												userId, vmUuid, "running",
												"正常运行");
										this.getMessagePush().editRowConsole(
												userId, vmUuid, "add");
									} else if (powerAttrvalue == 0
											&& ocvm.getVmPower() == 1) {
										this.getVmDAO().updatePowerStatus(
												vmUuid, powerAttrvalue);
										NoVNC.deleteToken(vmUuid
												.substring(0, 8));
										this.getMessagePush().editRowStatus(
												userId, vmUuid, "stopped",
												"已关机");
										this.getMessagePush().editRowConsole(
												userId, vmUuid, "del");
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
