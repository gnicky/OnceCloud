package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Types;
import com.once.xenapi.VM;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCLog;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.LBManager;
import com.oncecloud.manager.RouterManager;
import com.oncecloud.message.MessagePush;

@Component("LBManager")
public class LBManagerImpl implements LBManager {
	private final static Logger logger = Logger.getLogger(LBManager.class);
	private final static long MB = 1024 * 1024;
	
	private LBDAO lbDAO;
	private HostDAO hostDAO;
	private LogDAO logDAO;
	
	private MessagePush messagePush;
	
	private Constant constant;

	public LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	public void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	public HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	public void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	public LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	public void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public Constant getConstant() {
		return constant;
	}

	@Autowired
	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	public void lbAdminShutUp(String uuid, int userId) {
		LB lb = this.getLbDAO().getLB(uuid);
		String poolUuid = this.getHostDAO().getHost(lb.getHostUuid())
				.getPoolUuid();
		this.startLB(uuid, userId, poolUuid);
	}
	
	private void startLB(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doStartLB(uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.负载均衡.toString(),
				"lb-" + uuid.substring(0, 8)));
		if (result == true) {
			this.getMessagePush().editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			this.getMessagePush().editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doStartLB(String uuid, String poolUuid) {
		boolean result = false;
		String hostUuid = null;
		String powerState = null;
		Connection c = null;
		try {
			LB currentLB = this.getLbDAO().getLB(uuid);
			if (currentLB != null) {
				boolean preStartLB = this.getLbDAO().updateLBPowerStatus(uuid,
						LBManager.POWER_BOOT);
				if (preStartLB == true) {
					c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					if (!powerState.equals("Running")) {
						hostUuid = getAllocateHost(c, 1024);
						Host allocateHost = Types.toHost(hostUuid);
						thisVM.startOn(c, allocateHost, false, true);
					} else {
						hostUuid = thisVM.getResidentOn(c).toWireString();
					}
					this.getLbDAO().updateLBHostUuid(uuid, hostUuid);
					this.getLbDAO().updateLBPowerStatus(uuid,
							LBManager.POWER_RUNNING);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getLbDAO().updateLBPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getLbDAO().updateLBPowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getLbDAO().updateLBPowerStatus(uuid,
						RouterManager.POWER_HALTED);
			}
		}
		return result;
	}

	private String getAllocateHost(Connection conn, int memory) {
		String host = null;
		try {
			Map<Host, Host.Record> hostMap = Host.getAllRecords(conn);
			long maxFree = 0;
			for (Host thisHost : hostMap.keySet()) {
				Host.Record hostRecord = hostMap.get(thisHost);
				long memoryFree = hostRecord.memoryFree;
				if (memoryFree > maxFree) {
					maxFree = memoryFree;
					host = thisHost.toWireString();
				}
			}
			if ((int) (maxFree / MB) >= memory) {
				return host;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public void lbAdminShutDown(String uuid, String force, int userId) {
		LB lb = this.getLbDAO().getLB(uuid);
		String poolUuid = this.getHostDAO().getHost(lb.getHostUuid())
				.getPoolUuid();
		this.shutdownLB(uuid, force, userId, poolUuid);
	}
	
	private void shutdownLB(String uuid, String force, int userId,
			String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doShutdownLB(uuid, force, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.负载均衡.toString(),
				"lb-" + uuid.substring(0, 8)));
		if (result == true) {
			this.getMessagePush().editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			this.getMessagePush().editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doShutdownLB(String uuid, String force, String poolUuid) {
		boolean result = false;
		String powerState = null;
		String hostUuid = null;
		try {
			LB currentLB = this.getLbDAO().getLB(uuid);
			if (currentLB != null) {
				boolean preShutdownLB = this.getLbDAO().updateLBPowerStatus(uuid,
						LBManager.POWER_SHUTDOWN);
				if (preShutdownLB == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					hostUuid = thisVM.getResidentOn(c).toWireString();
					if (powerState.equals("Running")) {
						if (force.equals("true")) {
							thisVM.hardShutdown(c);
						} else {
							if (thisVM.cleanShutdown(c)) {
							} else {
								thisVM.hardShutdown(c);
							}
						}
					}
					this.getLbDAO().updateLBHostUuid(uuid, hostUuid);
					this.getLbDAO().updateLBPowerStatus(uuid,
							LBManager.POWER_HALTED);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getLbDAO().updateLBPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getLbDAO().updateLBPowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getLbDAO().updateLBPowerStatus(uuid,
						RouterManager.POWER_RUNNING);
			}
		}
		return result;
	}
	
	public void updateImportance(String uuid, int importance) {
		this.getLbDAO().updateLBImportance(uuid, importance);
	}
	
	public JSONArray getAdminLBList(int page, int limit, String host,
			int importance, String type) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getLbDAO().countAllAdminVMList(host, importance);
		List<LB> lbList = this.getLbDAO().getOnePageAdminVmList(page, limit,
				host, importance);
		ja.put(totalNum);
		if (lbList != null) {
			for (int i = 0; i < lbList.size(); i++) {
				JSONObject jo = new JSONObject();
				LB lb = lbList.get(i);
				jo.put("vmid", lb.getLbUuid());
				jo.put("vmname", Utilities.encodeText(lb.getLbName()));
				jo.put("state", lb.getLbPower());
				jo.put("cpu", "1");
				jo.put("memory", "1024");
				String ip = lb.getLbIP();
				if (ip == null) {
					jo.put("ip", "null");
				} else {
					jo.put("ip", ip);
				}
				String timeUsed = Utilities.encodeText(Utilities.dateToUsed(lb
						.getCreateDate()));
				jo.put("createdate", timeUsed);
				jo.put("importance", lb.getLbImportance());
				jo.put("userName",
						Utilities.encodeText(this.getUserDAO()
								.getUser(lb.getLbUID()).getUserName()));
				ja.put(jo);
			}
		}
		return ja;
	}
	
}
