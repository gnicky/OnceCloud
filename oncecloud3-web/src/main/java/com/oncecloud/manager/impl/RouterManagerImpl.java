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
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.RouterManager;
import com.oncecloud.message.MessagePush;

@Component("RouterManager")
public class RouterManagerImpl implements RouterManager {
	private final static Logger logger = Logger.getLogger(RouterManager.class);
	private final static long MB = 1024 * 1024;
	
	private RouterDAO routerDAO;
	private UserDAO userDAO;
	private HostDAO hostDAO;
	private LogDAO logDAO;
	private VnetDAO vnetDAO;
	
	private MessagePush messagePush;
	
	private Constant constant;

	public RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	public void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
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

	public VnetDAO getVnetDAO() {
		return vnetDAO;
	}

	@Autowired
	public void setVnetDAO(VnetDAO vnetDAO) {
		this.vnetDAO = vnetDAO;
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

	public JSONArray getAdminRouterList(int page, int limit, String host,
			int importance, String type) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getRouterDAO()
				.countRoutersOfAdmin(host, importance);
		List<Router> rtList = this.getRouterDAO().getOnePageRoutersOfAdmin(
				page, limit, host, importance);
		ja.put(totalNum);
		if (rtList != null) {
			for (int i = 0; i < rtList.size(); i++) {
				JSONObject jo = new JSONObject();
				Router router = rtList.get(i);
				jo.put("vmid", router.getRouterUuid());
				jo.put("vmname", Utilities.encodeText(router.getRouterName()));
				jo.put("state", router.getRouterPower());
				jo.put("cpu", "1");
				jo.put("memory", "1024");
				String ip = router.getRouterIP();
				if (ip == null) {
					jo.put("ip", "null");
				} else {
					jo.put("ip", ip);
				}
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed(router.getCreateDate()));
				jo.put("createdate", timeUsed);
				jo.put("importance", router.getRouterImportance());
				jo.put("userName",
						Utilities.encodeText(this.getUserDAO()
								.getUser(router.getRouterUID()).getUserName()));
				ja.put(jo);
			}
		}
		return ja;
	}

	public void routerAdminStartUp(String uuid, int userId) {
		Router router = this.getRouterDAO().getRouter(uuid);
		String poolUuid = this.getHostDAO().getHost(router.getHostUuid())
				.getPoolUuid();
		this.startRouter(uuid, userId, poolUuid);
	}
	
	private void startRouter(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doStartRouter(uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + uuid.substring(0, 8)));
		if (result == true) {
			this.getMessagePush().editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			this.getMessagePush().editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doStartRouter(String uuid, String poolUuid) {
		boolean result = false;
		String hostUuid = null;
		String powerState = null;
		Connection c = null;
		try {
			Router currentRT = this.getRouterDAO().getRouter(uuid);
			if (currentRT != null) {
				boolean preStartRouter = this.getRouterDAO().updatePowerStatus(
						uuid, RouterManager.POWER_BOOT);
				if (preStartRouter == true) {
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
					this.getRouterDAO().updateHostUuid(uuid, hostUuid);
					this.getRouterDAO().updatePowerStatus(uuid,
							RouterManager.POWER_RUNNING);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getRouterDAO().updatePowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getRouterDAO().updatePowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getRouterDAO().updatePowerStatus(uuid,
						RouterManager.POWER_HALTED);
			}
		} finally {
			if (result = true) {
				List<Vnet> vnetList = this.getVnetDAO().getVnetsOfRouter(uuid);
				if (vnetList != null && vnetList.size() > 0) {
					for (Vnet vnet : vnetList) {
						bindVlan(uuid, vnet.getVifUuid(), vnet.getVnetID(),
								c);
						logger.debug("Update Router Vlan: MAC ["
								+ vnet.getVifMac() + "] Vlan ["
								+ vnet.getVnetID() + "]");
					}
				}
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
	
	private void bindVlan(String routerUuid, String vifUuid, int vnetId,
			Connection c) {
		try {
			VM router = VM.getByUuid(c, routerUuid);
			router.setTag(c, Types.toVIF(vifUuid), String.valueOf(vnetId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void routerAdminShutDown(String uuid, String force, int userId) {
		Router router = this.getRouterDAO().getRouter(uuid);
		String poolUuid = this.getHostDAO().getHost(router.getHostUuid())
				.getPoolUuid();
		this.shutdownRouter(uuid, force, userId, poolUuid);
	}
	
	public void shutdownRouter(String uuid, String force, int userId,
			String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doShutdownRouter(uuid, force, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + uuid.substring(0, 8)));
		if (result == true) {
			this.getMessagePush().editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			this.getMessagePush().editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doShutdownRouter(String uuid, String force, String poolUuid) {
		boolean result = false;
		String powerState = null;
		String hostUuid = null;
		try {
			Router currentRT = this.getRouterDAO().getRouter(uuid);
			if (currentRT != null) {
				boolean preShutdownRouter = this.getRouterDAO()
						.updatePowerStatus(uuid, RouterManager.POWER_SHUTDOWN);
				if (preShutdownRouter == true) {
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
					this.getRouterDAO().updateHostUuid(uuid, hostUuid);
					this.getRouterDAO().updatePowerStatus(uuid,
							RouterManager.POWER_HALTED);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getRouterDAO().updatePowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getRouterDAO().updatePowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getRouterDAO().updatePowerStatus(uuid,
						RouterManager.POWER_RUNNING);
			}
		}
		return result;
	}
	
	public void updateImportance(String uuid, int importance) {
		this.getRouterDAO().updateImportance(uuid, importance);
	}
	
}
