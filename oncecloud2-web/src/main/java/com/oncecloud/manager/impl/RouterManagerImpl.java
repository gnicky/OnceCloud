package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Types;
import com.once.xenapi.VM;
import com.once.xenapi.Types.BadServerResponse;
import com.once.xenapi.Types.XenAPIException;
import com.once.xenapi.VM.Record;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.ForwardPortDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.PPTPUserDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.ForwardPort;
import com.oncecloud.entity.Image;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.PPTPUser;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.User;
import com.oncecloud.entity.Vnet;
import com.oncecloud.helper.HashHelper;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.RouterManager;
import com.oncecloud.message.MessagePush;

@Component("RouterManager")
public class RouterManagerImpl implements RouterManager {
	private final static Logger logger = Logger.getLogger(RouterManager.class);
	private final static long MB = 1024 * 1024;
	public static int[] capacity = { 250, 500, 1000 };

	private RouterDAO routerDAO;
	private EIPDAO eipDAO;
	private FirewallDAO firewallDAO;
	private VnetDAO vnetDAO;
	private VMDAO vmDAO;
	private LogDAO logDAO;
	private ImageDAO imageDAO;
	private DHCPDAO dhcpDAO;
	private QuotaDAO quotaDAO;
	private HostDAO hostDAO;
	private UserDAO userDAO;
	private ForwardPortDAO forwardPortDAO;
	private PPTPUserDAO pptpUserDAO;

	private MessagePush messagePush;
	private HashHelper hashHelper;
	private Constant constant;

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private DHCPDAO getDhcpDAO() {
		return dhcpDAO;
	}

	@Autowired
	private void setDhcpDAO(DHCPDAO dhcpDAO) {
		this.dhcpDAO = dhcpDAO;
	}

	private VnetDAO getVnetDAO() {
		return vnetDAO;
	}

	@Autowired
	private void setVnetDAO(VnetDAO vnetDAO) {
		this.vnetDAO = vnetDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	public void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public PPTPUserDAO getPptpUserDAO() {
		return pptpUserDAO;
	}

	@Autowired
	public void setPptpUserDAO(PPTPUserDAO pptpUserDAO) {
		this.pptpUserDAO = pptpUserDAO;
	}

	private Constant getConstant() {
		return constant;
	}

	private HashHelper getHashHelper() {
		return hashHelper;
	}

	@Autowired
	private void setHashHelper(HashHelper hashHelper) {
		this.hashHelper = hashHelper;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	public ForwardPortDAO getForwardPortDAO() {
		return forwardPortDAO;
	}

	@Autowired
	public void setForwardPortDAO(ForwardPortDAO forwardPortDAO) {
		this.forwardPortDAO = forwardPortDAO;
	}

	public void createRouter(String uuid, int userId, String name,
			int capacity, String fwuuid, String poolUuid) {
		Date startTime = new Date();
		JSONObject result = this.doCreateRouter(uuid, userId, name, capacity,
				fwuuid, poolUuid);
		// Begin to push message and write log
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + uuid.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo("峰值带宽", capacity + "&nbsp;Mbps"));
		if (result.getBoolean("isSuccess") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().editRowStatus(userId, uuid, "running", "活跃");
			this.getMessagePush().editRowIP(userId, uuid, "基础网络",
					result.getString("ip"));
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			infoArray.put(Utilities.createLogInfo("原因",
					result.has("error") ? result.getString("error") : "未知"));
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().deleteRow(userId, uuid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private JSONObject doCreateRouter(String uuid, int userId, String name,
			int capacity, String fwuuid, String poolUuid) {
		Connection c = null;
		String allocateHost = null;
		String ip = null;
		String mac = null;
		JSONObject jo = new JSONObject();
		String OS = null;
		String imagePwd = null;
		String pwd = "onceas";
		String tplUuid = null;
		String backendName = "rt-" + uuid.substring(0, 8);
		Date createDate = new Date();
		try {
			Image rtImage = this.getImageDAO().getRTImage(userId, poolUuid);
			OS = "linux";
			imagePwd = rtImage.getImagePwd();
			tplUuid = rtImage.getImageUuid();
			DHCP dhcp = this.getDhcpDAO().getFreeDHCP(uuid, 3);
			ip = dhcp.getDhcpIp();
			mac = dhcp.getDhcpMac();
			c = this.getConstant().getConnectionFromPool(poolUuid);
			allocateHost = getAllocateHost(c, 1024);
			logger.info("Router [" + backendName + "] allocated to Host ["
					+ allocateHost + "]");
		} catch (Exception e) {
			e.printStackTrace();
			if (mac != null) {
				try {
					this.getDhcpDAO().returnDHCP(mac);
				} catch (Exception e1) {
					e.printStackTrace();
				}
				jo.put("isSuccess", false);
			}
			return jo;
		}
		if (allocateHost == null) {
			jo.put("isSuccess", false);
		} else {
			boolean dhcpRolrtack = false;
			boolean dbRolrtack = false;
			try {
				boolean preCreate = this.getRouterDAO().preCreateRouter(uuid,
						pwd, userId, name, mac, capacity, fwuuid,
						RouterManager.POWER_CREATE, 1, createDate);
				this.getQuotaDAO().updateQuota(userId, "quotaRoute", 1, true);
				Date preEndDate = new Date();
				int elapse = Utilities.timeElapse(createDate, preEndDate);
				logger.info("Router [" + backendName + "] Pre Create Time ["
						+ elapse + "]");
				if (preCreate == true) {
					Record rtrecord = createVMOnHost(c, uuid, tplUuid, "root",
							pwd, 1, 1024, mac, ip, OS, allocateHost, imagePwd,
							backendName, true);
					Date createEndDate = new Date();
					int elapse1 = Utilities.timeElapse(createDate,
							createEndDate);
					logger.info("Router [" + backendName + "] Create Time ["
							+ elapse1 + "]");
					if (rtrecord != null) {
						String hostuuid = rtrecord.residentOn.toWireString();
						if (hostuuid.equals(allocateHost)) {
							if (!rtrecord.setpasswd) {
								pwd = imagePwd;
							}
							jo.put("ip", ip);
							this.getRouterDAO().updateRouter(userId, uuid, pwd,
									RouterManager.POWER_RUNNING, fwuuid,
									hostuuid, ip);
							jo.put("isSuccess", true);
						} else {
							jo.put("error", "路由器后台启动位置错误");
							dhcpRolrtack = true;
							dbRolrtack = true;
						}
					} else {
						jo.put("error", "路由器后台创建失败");
						dhcpRolrtack = true;
						dbRolrtack = true;
					}
				} else {
					jo.put("error", "路由器预创建失败");
					dhcpRolrtack = true;
				}
			} catch (Exception e) {
				jo.put("error", "路由器创建异常");
				e.printStackTrace();
				dhcpRolrtack = true;
				dbRolrtack = true;
			}
			if (dhcpRolrtack == true) {
				try {
					this.getDhcpDAO().returnDHCP(mac);
				} catch (Exception e) {
					e.printStackTrace();
				}
				jo.put("isSuccess", false);
			}
			if (dbRolrtack == true) {
				this.getRouterDAO().removeRouter(userId, uuid);
				jo.put("isSuccess", false);
			}
		}

		// /cyh 绑定内部防火墙
		if (jo.getBoolean("isSuccess") == true) {
			// /1 创建内部防火墙，2 绑定到当前路由器
			String firewallinner = UUID.randomUUID().toString();
			if (this.getFirewallDAO().insertFirewallForinnerRoute(
					firewallinner, name, userId, new Date())) {
				this.getRouterDAO().updateInnerFirewall(uuid, firewallinner);
			}
		}
		return jo;
	}

	private Record createVMOnHost(Connection c, String vmUuid, String tplUuid,
			String loginName, String loginPwd, long cpuCore,
			long memoryCapacity, String mac, String ip, String OS,
			String hostUuid, String imagePwd, String vmName, boolean ping) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cpuNumber", cpuCore);
		map.put("memoryValue", memoryCapacity);
		map.put("newUuid", vmUuid);
		map.put("MAC", mac);
		map.put("IP", ip);
		map.put("type", OS);
		map.put("passwd", loginPwd);
		map.put("origin_passwd", imagePwd);
		Host host = Types.toHost(hostUuid);
		Record vmrecord = VM.createOnFromTemplate(c, host, tplUuid, vmName,
				map, ping);
		return vmrecord;
	}

	public void deleteRouter(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doDeleteRouter(userId, uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + uuid.substring(0, 8)));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().deleteRow(userId, uuid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doDeleteRouter(int userId, String uuid, String poolUuid) {
		boolean result = false;
		Connection c = null;
		try {
			c = this.getConstant().getConnectionFromPool(poolUuid);
			boolean preDeleteRouter = this.getRouterDAO().updatePowerStatus(
					uuid, RouterManager.POWER_DESTROY);
			if (preDeleteRouter == true) {
				VM thisRouter = VM.getByUuid(c, uuid);
				thisRouter.hardShutdown(c);
				thisRouter.destroy(c, true);
				Router rt = this.getRouterDAO().getRouter(uuid);
				String ip = rt.getRouterIP();
				String mac = rt.getRouterMac();
				if (ip != null) {
					this.getDhcpDAO().returnDHCP(mac);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				String publicip = this.getEipDAO().getEipIp(uuid);
				if (publicip != null) {
					unbindElasticIp(c, uuid, publicip);
				}
				this.getRouterDAO().removeRouter(userId, uuid);
				this.getQuotaDAO().updateQuota(userId, "quotaRoute", 1, false);
				String firewallId = this.getRouterDAO().getRouter(uuid)
						.getInnerFirewallUuid();
				this.getFirewallDAO().deleteFirewall(userId, firewallId);
				this.getFirewallDAO().deleteAllRuleOfFirewall(firewallId);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private JSONObject unbindElasticIp(Connection c, String uuid, String eipIp) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			String ip = null;
			String eif = this.getEipDAO().getEip(eipIp).getEipInterface();
			Router rt = this.getRouterDAO().getRouter(uuid);
			ip = rt.getRouterIP();
			boolean deActiveResult = deActiveFirewall(c, ip);
			if (deActiveResult) {
				if (Host.unbindOuterIp(c, ip, eipIp, eif)) {
					this.getEipDAO().unBindEip(eipIp);
					result.put("result", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean deActiveFirewall(Connection c, String ip) {
		boolean result = false;
		try {
			JSONObject total = new JSONObject();
			JSONArray ipArray = new JSONArray();
			ipArray.put(ip);
			JSONArray ruleArray = new JSONArray();
			total.put("IP", ipArray);
			total.put("rules", ruleArray);
			result = Host.firewallApplyRule(c, total.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void startRouter(String uuid, int userId, String poolUuid) {
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
					c = this.getConstant().getConnectionFromPool(poolUuid);
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
						bindVlan(uuid, vnet.getVifUuid(), vnet.getVnetID(), c);
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
	
	/**
	 * 获取用户路由器列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getRouterList(int userId, int page, int limit,
			String search) {
		JSONArray ja = new JSONArray();
		int total = this.getRouterDAO().countRouters(userId, search);
		List<Router> routerList = this.getRouterDAO().getOnePageRouters(userId,
				page, limit, search);
		ja.put(total);
		if (routerList != null) {
			for (Router router : routerList) {
				JSONObject obj = new JSONObject();
				String routerUuid = router.getRouterUuid();
				obj.put("uuid", routerUuid);
				obj.put("name", Utilities.encodeText(router.getRouterName()));
				obj.put("ip",
						null == router.getRouterIP() ? "null" : router
								.getRouterIP());
				obj.put("power", router.getRouterPower());
				obj.put("capacity", router.getRouterCapacity());
				String eip = this.getEipDAO().getEipIp(routerUuid);
				String ip = router.getRouterIP();
				if (ip == null) {
					obj.put("ip", "");
				} else {
					obj.put("ip", ip);
				}
				if (eip == null) {
					obj.put("eip", "");
				} else {
					obj.put("eip", eip);
				}
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed(router.getCreateDate()));
				obj.put("createdate", timeUsed);
				ja.put(obj);
			}
		}
		return ja;
	}

	/**
	 * 获取路由器详细信息
	 * 
	 * @param routerUuid
	 * @return
	 */
	public JSONObject getRouterDetail(String routerUuid) {
		JSONObject jo = new JSONObject();
		Router router = this.getRouterDAO().getRouter(routerUuid);
		if (router != null) {
			jo.put("routerName", Utilities.encodeText(router.getRouterName()));
			jo.put("routerDesc", (null == router.getRouterDesc()) ? "&nbsp;"
					: Utilities.encodeText(router.getRouterDesc()));
			jo.put("routerIp", router.getRouterIP());
			String eip = this.getEipDAO().getEipIp(routerUuid);
			String eipUuid = this.getEipDAO().getEipId(eip);
			if (eip == null) {
				jo.put("eip", "");
				jo.put("eipUuid", "");
			} else {
				jo.put("eip", eip);
				jo.put("eipUuid", eipUuid);
			}
			jo.put("routerUID", router.getRouterUID());
			jo.put("routerMac", router.getRouterMac());
			jo.put("routerStatus", router.getRouterStatus());
			jo.put("routerCapacity", router.getRouterCapacity());
			jo.put("routerPower", router.getRouterPower());
			String fw = router.getFirewallUuid();
			if (fw == null) {
				jo.put("routerFirewall", "");
				jo.put("routerFirewallName", "");
			} else {
				jo.put("routerFirewall", fw);
				jo.put("routerFirewallName",
						Utilities.encodeText(this.getFirewallDAO()
								.getFirewall(fw).getFirewallName()));
			}
			jo.put("createDate", Utilities.formatTime(router.getCreateDate()));
			String timeUsed = Utilities.encodeText(Utilities.dateToUsed(router
					.getCreateDate()));
			jo.put("useDate", timeUsed);
			jo.put("routerInnerFirewall", router.getInnerFirewallUuid());
			jo.put("pptpStatus", router.getPptp());
		}
		return jo;
	}

	public JSONArray routerQuota(int userId) {
		JSONArray ja = new JSONArray();
		int free = this.getQuotaDAO().getQuotaTotal(userId).getQuotaRoute()
				- this.getQuotaDAO().getQuotaUsed(userId).getQuotaRoute();
		JSONObject jo = new JSONObject();
		jo.put("free", free);
		if (free < 1) {
			jo.put("result", false);
		} else {
			jo.put("result", true);
		}
		ja.put(jo);
		return ja;
	}
	
	/**
	 * @author hty
	 * @param rtuuid
	 * @return
	 * @throws JSONException
	 * @time 2014/08/14
	 */
	public JSONArray getVxnets(String rtuuid) {
		JSONArray ja = new JSONArray();
		List<Vnet> vxnetsList = this.getVnetDAO().getVnetsOfRouter(rtuuid);
		if (vxnetsList != null) {
			for (Vnet vnet : vxnetsList) {
				JSONObject jo = new JSONObject();
				List<OCVM> ocvmList = this.getVmDAO().getVMsOfVnet(
						vnet.getVnetUuid());
				if (ocvmList.size() == 0) {
					jo.put("ocvm", "null");
				} else {
					JSONArray javm = new JSONArray();
					for (OCVM ocvm : ocvmList) {
						JSONObject jovm = new JSONObject();
						jovm.put("hostid", ocvm.getVmUuid());
						jovm.put("hostname",
								Utilities.encodeText(ocvm.getVmName()));
						jovm.put("hoststatus", ocvm.getVmPower());
						jovm.put("hostip", null == ocvm.getVmIP() ? "null"
								: ocvm.getVmIP());
						javm.put(jovm);
					}
					jo.put("ocvm", javm);
				}
				jo.put("vn_dhcp", vnet.getDhcpStatus());
				jo.put("vn_uuid", vnet.getVnetUuid());
				jo.put("vn_net", vnet.getVnetNet());
				jo.put("vn_gate", vnet.getVnetGate());
				jo.put("vn_dhcp_start", vnet.getVnetStart());
				jo.put("vn_dhcp_end", vnet.getVnetEnd());
				jo.put("vn_name", vnet.getVnetName());
				jo.put("vn_uuid", vnet.getVnetUuid());
				ja.put(jo);
			}
		}
		return ja;
	}
	
	public JSONArray getRoutersOfUser(int userId, int page, int limit,
			String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getRouterDAO().countRouters(userId, search);
		ja.put(totalNum);
		List<Router> rtList = this.getRouterDAO().getOnePageRouters(userId,
				page, limit, search);
		if (rtList != null) {
			for (int i = 0; i < rtList.size(); i++) {
				JSONObject jo = new JSONObject();
				Router rt = rtList.get(i);
				jo.put("vmid", rt.getRouterUuid());
				jo.put("vmname", Utilities.encodeText(rt.getRouterName()));
				ja.put(jo);
			}
		}
		return ja;
	}
	
	public JSONArray getRoutersOfUser(int userId) {
		return this.getRouterDAO().getRoutersOfUser(userId);
	}
	
	public JSONObject addPortForwarding(int userId, String allocate,
			String protocol, String srcIP, String srcPort, String destIP,
			String destPort, String pfName, String routerUuid) {
		JSONObject jo = new JSONObject();
		Connection c = this.getConstant().getConnectionFromPool(allocate);
		boolean result = false;
		try {
			String url = "http://" + srcIP + ":9090";
			result = Host.addPortForwarding(c, url, protocol.toLowerCase(), destIP, destPort,
					srcIP, srcPort);
			if (result) {
				String uuidString = UUID.randomUUID().toString();
				ForwardPort pf = new ForwardPort(uuidString, pfName, protocol,
						Integer.parseInt(srcPort), destIP,
						Integer.parseInt(destPort), routerUuid);
				jo.put("uuid", uuidString);
				this.getForwardPortDAO().addPF(pf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jo.put("result", result);
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("端口转发添加成功"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("端口转发添加失败"));
		}
		return jo;
	}
	
	public JSONObject delPortForwarding(int userId, String allocate,
			String protocol, String srcIP, String srcPort, String destIP,
			String destPort, String uuid) {
		JSONObject jo = new JSONObject();
		Connection c = this.getConstant().getConnectionFromPool(allocate);
		boolean result = false;
		try {
			String url = "http://" + srcIP + ":9090";
			result = Host.delPortForwarding(c, url, protocol.toLowerCase(), destIP, destPort,
					srcIP, srcPort);
			if (result) {
				ForwardPort pf = new ForwardPort();
				pf.setPfUuid(uuid);
				this.getForwardPortDAO().deletePF(pf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jo.put("result", result);
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("端口转发删除成功"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("端口转发删除失败"));
		}
		return jo;
	}
	
	public JSONArray getpfList(String routerUuid) {
		JSONArray ja = new JSONArray();
		List<ForwardPort> pfList = this.getForwardPortDAO().getpfListByRouter(
				routerUuid);
		for (ForwardPort forwardPort : pfList) {
			JSONObject jo = new JSONObject();
			jo.put("pf_uuid", forwardPort.getPfUuid());
			jo.put("pf_name", Utilities.encodeText(forwardPort.getPfName()));
			jo.put("pf_protocal", forwardPort.getPfProtocal());
			jo.put("pf_sourceport", forwardPort.getPfSourcePort());
			jo.put("pf_internalIP", forwardPort.getPfInteranlIP());
			jo.put("pf_internalport", forwardPort.getPfInternalPort());
			ja.put(jo);
		}
		return ja;
	}
	
	public JSONObject checkPortForwarding(String routerUuid, String destIP, int destPort, int srcPort) {
		JSONObject jo = new JSONObject();
		List<Vnet> vxnetsList = this.getVnetDAO().getVnetsOfRouter(routerUuid);
		boolean destIPLegal = false;
		if (vxnetsList != null) {
			for (Vnet vnet : vxnetsList) {
				String net = "192.168." + vnet.getVnetNet() + ".";
				if (destIP.startsWith(net)) {
					destIPLegal = true;
					break;
				}
			}
		}
		jo.put("ipLegal", destIPLegal);
		List<ForwardPort> fpList = this.getForwardPortDAO().getpfListByRouter(routerUuid);
		boolean pfLegal = true;
		if (fpList != null) {
			for (ForwardPort fp : fpList) {
				String inIP = fp.getPfInteranlIP();
				int inPort = fp.getPfInternalPort();
				int outPort = fp.getPfSourcePort();
				if (outPort == srcPort || srcPort == 9090) {
					pfLegal = false;
					break;
				} else if (inIP.equals(destIP) && inPort == destPort) {
					pfLegal = false;
					break;
				}
			}
		}
		jo.put("pfLegal", pfLegal);
		return jo;
	}
	
	public JSONObject disableDHCP(String vnetUuid, int userId) {
		Vnet vnet = this.getVnetDAO().getVnet(vnetUuid);
		JSONObject jo = new JSONObject();
		boolean result = false;
		if (vnet != null) {
			if (vnet.getDhcpStatus() == 0) {
				result = true;
			} else {
				try {
					Connection c = this.getConstant().getConnection(userId);
					String routerUuid = vnet.getVnetRouter();
					Router router = this.getRouterDAO().getAliveRouter(
							routerUuid);
					String url = router.getRouterIP() + ":9090";
					String subnet = "192.168." + vnet.getVnetNet() + ".0";
					String netmask = "255.255.255.0";
					JSONObject delJo = new JSONObject();
					delJo.put("subnet", subnet);
					delJo.put("netmask", netmask);
					boolean delSubnetResult = Host.delSubnet(c, url,
							delJo.toString());
					if (delSubnetResult) {
						vnet.setDhcpStatus(0);
						this.getVnetDAO().updateVnet(vnet);
						unAssginIpAddress(c, vnetUuid);
						result = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		jo.put("result", result);
		return jo;
	}
	
	private void unAssginIpAddress(Connection c, String vnUuid) {
		List<OCVM> vmList = this.getVmDAO().getVMsOfVnet(vnUuid);
		if (vmList != null) {
			try {
				for (OCVM vm : vmList) {
					this.restartNetwork(c, vm.getVmUuid(), true);
					vm.setVmIP(null);
					this.getVmDAO().updateVM(vm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean restartNetwork(Connection c, String vmuuid, boolean sync) {
		try {
			VM temVM = VM.getByUuid(c, vmuuid);
			JSONObject temjo = new JSONObject();
			temjo.put("requestType", "Agent.RestartNetwork");
			temVM.sendRequestViaSerial(c, temjo.toString(), sync);
			return true;
		} catch (BadServerResponse e) {
			e.printStackTrace();
			return false;
		} catch (XenAPIException e) {
			e.printStackTrace();
			return false;
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public JSONObject enableDHCP(String vnetUuid, int userId) {
		Vnet vnet = this.getVnetDAO().getVnet(vnetUuid);
		JSONObject jo = new JSONObject();
		boolean result = false;
		if (vnet != null) {
			try {
				if (vnet.getDhcpStatus() == 1) {
					result = true;
				} else {
					Connection c = this.getConstant().getConnection(userId);
					String routerUuid = vnet.getVnetRouter();
					Router router = this.getRouterDAO().getAliveRouter(
							routerUuid);
					String url = router.getRouterIP() + ":9090";
					int net = vnet.getVnetNet();
					String subnet = "192.168." + net + ".0";
					String netmask = "255.255.255.0";
					String gateway = "192.168." + net + "."
							+ vnet.getVnetGate();
					String rangeStart = "192.168." + net + "." + "2";
					String rangeEnd = "192.168." + net + "." + "254";
					logger.info("Configure Subnet: URL [" + url + "] Netmask ["
							+ netmask + "] RangeStart [" + rangeStart
							+ "] RangeEnd [" + rangeEnd + "]");
					boolean addSubnetResult = addSubnet(c, url,
							subnet, netmask, gateway, rangeStart, rangeEnd);
					if (addSubnetResult) {
						vnet.setVnetStart(2);
						vnet.setVnetEnd(254);
						vnet.setDhcpStatus(1);
						this.getVnetDAO().updateVnet(vnet);
						assginIpAddress(c, url, subnet,
								vnetUuid);
						logger.info("Configure Subnet Result: "
								+ addSubnetResult);
						result = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jo.put("result", result);
		return jo;
	}
	
	private static boolean addSubnet(Connection c, String url, String subnet,
			String netmask, String gateway, String rangeStart, String rangeEnd) {
		boolean result = false;
		JSONObject params = new JSONObject();
		params.put("subnet", subnet);
		params.put("netmask", netmask);
		params.put("router", gateway);
		params.put("dns", "114.114.114.114");
		params.put("rangeStart", rangeStart);
		params.put("rangeEnd", rangeEnd);
		params.put("defaultLease", 21600);
		params.put("maxLease", 43200);
		try {
			result = Host.addSubnet(c, url, params.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void assginIpAddress(Connection c, String url, String subnet,
			String vnUuid) {
		List<OCVM> vmList = this.getVmDAO().getVMsOfVnet(vnUuid);
		if (vmList != null) {
			try {
				for (OCVM vm : vmList) {
					String mac = vm.getVmMac();
					String vnetIp = Host.assignIpAddress(c, url, mac, subnet);
					vm.setVmIP(vnetIp);
					this.getVmDAO().updateVM(vm);
					this.restartNetwork(c, vm.getVmUuid(), false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public JSONObject savePPTPUser(String name, String pwd, String routerUuid) {
		JSONObject jo = new JSONObject();
		PPTPUser pu = new PPTPUser();
		pu.setPptpName(name);
		pu.setPptpPwd(pwd);
		pu.setRouterUuid(routerUuid);
		boolean result = this.getPptpUserDAO().save(pu);
		jo.put("result", result);
		jo.put("pptpid", pu.getPptpId());
		return jo;
	}
	
	public JSONArray getPPTPList(String routerUuid) {
		JSONArray ja = new JSONArray();
		List<PPTPUser> list = this.getPptpUserDAO().getList(routerUuid);
		for (PPTPUser pu : list) {
			JSONObject jo = new JSONObject();
			jo.put("pptpid", pu.getPptpId());
			jo.put("name", pu.getPptpName());
			ja.put(jo);
		}
		return ja;
	}

	public JSONObject deletePPTP(int pptpid) {
		JSONObject jo = new JSONObject();
		PPTPUser pu = new PPTPUser();
		pu.setPptpId(pptpid);
		boolean result = this.getPptpUserDAO().delete(pu);
		jo.put("result", result);
		return jo;
	}
	
	public JSONObject updatePPTP(String pwd, int pptpid) {
		JSONObject jo = new JSONObject();
		PPTPUser pu = this.getPptpUserDAO().getPPTPUser(pptpid);
		pu.setPptpPwd(pwd);
		boolean result = this.getPptpUserDAO().update(pu);
		jo.put("result", result);
		return jo;
	}
	
	public boolean openPPTP(String routerUuid, User user) {
		Connection c = this.getConstant().getConnectionFromPool(user.getUserAllocate());
		boolean result = false;
		try {
			JSONObject jo = new JSONObject();
			String ip = this.getRouterDAO().getRouter(routerUuid).getRouterIP() + ":9090";
			jo.put("networkAddress", "172.16.1.0");
			jo.put("maxConnections", 253);
			JSONArray ja = new JSONArray();
			List<PPTPUser> list = this.getPptpUserDAO().getList(routerUuid);
			for (PPTPUser pu : list) {
				JSONObject tmjo = new JSONObject();
				tmjo.put("userName", pu.getPptpName());
				tmjo.put("password", pu.getPptpPwd());
				ja.put(tmjo);
			}
			jo.put("users", ja);
			result = Host.addPPTP(c, ip, jo.toString());
			if (result) {
				Router router = this.getRouterDAO().getRouter(routerUuid);
				router.setPptp(1);
				this.getRouterDAO().updateRouter(router);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result) {
			this.getMessagePush().pushMessage(user.getUserId(),
					Utilities.stickyToSuccess("PPTP服务开启成功"));
		} else {
			this.getMessagePush().pushMessage(user.getUserId(),
					Utilities.stickyToError("PPTP服务开启失败"));
		}
		return result;
	}
	
	public boolean closePPTP(String routerUuid, User user) {
		Connection c = this.getConstant().getConnectionFromPool(user.getUserAllocate());
		boolean result = false;
		try {
			String ip = this.getRouterDAO().getRouter(routerUuid).getRouterIP() + ":9090";
			result = Host.delPPTP(c, ip);
			if (result) {
				Router router = this.getRouterDAO().getRouter(routerUuid);
				router.setPptp(0);
				this.getRouterDAO().updateRouter(router);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result) {
			this.getMessagePush().pushMessage(user.getUserId(),
					Utilities.stickyToSuccess("PPTP服务关闭成功"));
		} else {
			this.getMessagePush().pushMessage(user.getUserId(),
					Utilities.stickyToError("PPTP服务关闭失败"));
		}
		return result;
	}
}
