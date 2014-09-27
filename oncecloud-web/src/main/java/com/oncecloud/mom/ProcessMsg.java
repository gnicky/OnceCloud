package com.oncecloud.mom;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oncecloud.manager.VMManager;

@Component
public class ProcessMsg {
	private VMManager vmManager;

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	public void ProcessSync(String message) {
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(message);
		JsonObject hostjo = je.getAsJsonObject();
		for (Map.Entry<String, JsonElement> hostentry : hostjo.entrySet()) {
//			String hostUuid = hostentry.getKey();
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
//								JsonObject attrjo = vmentry.getValue()
//										.getAsJsonObject();
//								int powerAttrvalue = 1;
//								for (Map.Entry<String, JsonElement> attrentry : attrjo
//										.entrySet()) {
//									String attr = attrentry.getKey();
//									if (attr.equals("POWER")) {
//										powerAttrvalue = attrentry.getValue()
//												.getAsInt();
//									}
//								}
//								this.getVmManager().syncAddVMOperate(hostUuid,
//										vmUuid, powerAttrvalue);
							} else if (optype.equals("DEL")) {
//								this.getVmManager().syncDelVMOperate(vmUuid, hostUuid);
							} else if (optype.equals("UPDATE")) {
								JsonObject attrjo = vmentry.getValue()
										.getAsJsonObject();
								int powerAttrvalue = 0;
								for (Map.Entry<String, JsonElement> attrentry : attrjo
										.entrySet()) {
									String attr = attrentry.getKey();
									if (attr.equals("POWER")) {
										powerAttrvalue = attrentry.getValue()
												.getAsInt();
									}
								}
								this.getVmManager().syncUpdateVM(vmUuid, powerAttrvalue);
							}
						}
					}
				}
			}
		}
	}
}
