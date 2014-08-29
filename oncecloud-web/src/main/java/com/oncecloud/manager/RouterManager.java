package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Types;
import com.once.xenapi.VIF;
import com.once.xenapi.VM;
import com.once.xenapi.VM.Record;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VDIDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.Image;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVDI;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;

/**
 * @author xpx hehai hty
 * @version 2014/08/23
 */
@Component
public class RouterManager {
	private final static Logger logger = Logger.getLogger(RouterManager.class);
	public static int[] capacity = { 250, 500, 1000 };
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;

	private ImageDAO imageDAO;
	private DHCPDAO dhcpDAO;
	private RouterDAO routerDAO;
	private VDIDAO vdiDAO;
	private EIPDAO eipDAO;
	private VnetDAO vnetDAO;
	private VMDAO vmDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private FirewallDAO firewallDAO;

	private EIPManager eipManager;

	private Constant constant;

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

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private VDIDAO getVdiDAO() {
		return vdiDAO;
	}

	@Autowired
	private void setVdiDAO(VDIDAO vdiDAO) {
		this.vdiDAO = vdiDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
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

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	private void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	public JSONObject createRouter(String uuid, int userId, String name,
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
			Image rtImage = this.getImageDAO().getRTImage(userId);
			OS = "linux";
			imagePwd = rtImage.getImagePwd();
			tplUuid = rtImage.getImageUuid();
			DHCP dhcp = this.getDhcpDAO().getFreeDHCP(uuid, 3);
			ip = dhcp.getDhcpIp();
			mac = dhcp.getDhcpMac();
			c = this.getConstant().getConnectionFromPool(poolUuid);
			allocateHost = VMManager.getAllocateHost(poolUuid, 1024);
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
				Date preEndDate = new Date();
				int elapse = Utilities.timeElapse(createDate, preEndDate);
				logger.info("Router [" + backendName + "] Pre Create Time ["
						+ elapse + "]");
				if (preCreate == true) {
					OCVDI freeVDI = this.getVdiDAO().getFreeVDI(tplUuid);
					Record rtrecord = null;
					// 如果不能获取该模板的空闲VDI，则直接创建该路由器，否则使用该VDI创建路由器
					if (freeVDI == null) {
						rtrecord = VMManager.createVMOnHost(c, uuid, tplUuid,
								"root", pwd, 1, 1024, mac, ip, OS,
								allocateHost, imagePwd, backendName);
						Date createEndDate = new Date();
						int elapse1 = Utilities.timeElapse(createDate,
								createEndDate);
						logger.info("Router [" + backendName
								+ "] Create Time [" + elapse1 + "]");
					} else {
						String vdiUuid = freeVDI.getVdiUuid();
						this.getVdiDAO().deleteVDI(freeVDI);
						rtrecord = VMManager.createVMFromVDI(c, uuid, vdiUuid,
								tplUuid, "root", pwd, 1, 1024, mac, ip, OS,
								allocateHost, imagePwd, backendName);
						Date createEndDate = new Date();
						int elapse1 = Utilities.timeElapse(createDate,
								createEndDate);
						logger.info("Router [" + backendName
								+ "] Create From VDI Time [" + elapse1 + "]");
					}
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
		return jo;
	}

	public boolean deleteRouter(int userId, String uuid, String poolUuid) {
		boolean result = false;
		Connection c = null;
		try {
			c = this.getConstant().getConnectionFromPool(poolUuid);
			boolean preDeleteRouter = this.getRouterDAO().setRouterPowerStatus(
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
					this.getEipManager().unbindElasticIp(userId, uuid,
							publicip, "rt");
				}
				this.getRouterDAO().removeRouter(userId, uuid);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean startRouter(String uuid, String poolUuid) {
		boolean result = false;
		String hostUuid = null;
		String powerState = null;
		try {
			Router currentRT = this.getRouterDAO().getRouter(uuid);
			if (currentRT != null) {
				boolean preStartRouter = this.getRouterDAO()
						.setRouterPowerStatus(uuid, RouterManager.POWER_BOOT);
				if (preStartRouter == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					if (!powerState.equals("Running")) {
						hostUuid = VMManager.getAllocateHost(poolUuid, 1024);
						Host allocateHost = Types.toHost(hostUuid);
						thisVM.startOn(c, allocateHost, false, true);
					} else {
						hostUuid = thisVM.getResidentOn(c).toWireString();
					}
					this.getRouterDAO().setRouterHostUuid(uuid, hostUuid);
					this.getRouterDAO().setRouterPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getRouterDAO().setRouterPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getRouterDAO().setRouterPowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getRouterDAO().setRouterPowerStatus(uuid,
						RouterManager.POWER_HALTED);
			}
		}
		return result;
	}

	public boolean shutdownRouter(String uuid, String force, String poolUuid) {
		boolean result = false;
		String powerState = null;
		String hostUuid = null;
		try {
			Router currentRT = this.getRouterDAO().getRouter(uuid);
			if (currentRT != null) {
				boolean preShutdownRouter = this.getRouterDAO()
						.setRouterPowerStatus(uuid,
								RouterManager.POWER_SHUTDOWN);
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
					this.getRouterDAO().setRouterHostUuid(uuid, hostUuid);
					this.getRouterDAO().setRouterPowerStatus(uuid,
							RouterManager.POWER_HALTED);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getRouterDAO().setRouterPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getRouterDAO().setRouterPowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getRouterDAO().setRouterPowerStatus(uuid,
						RouterManager.POWER_RUNNING);
			}
		}
		return result;
	}

	public JSONObject linkVnetToRouter(int userId, String vnUuid,
			String rtUuid, int net, int gate, int start, int end, int dhcpState) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			logger.info("Link to Router: Router [rt-" + rtUuid.substring(0, 8)
					+ "] Vnet [vn-" + vnUuid.substring(0, 8) + "]");
			Connection c = this.getConstant().getConnection(userId);
			VM vm = VM.getByUuid(c, rtUuid);
			String mac = Utilities.randomMac();
			VIF vif = VIF.createBindToPhysicalNetwork(c, vm, "ovs0", mac);
			Vnet vnet = this.getVnetDAO().getVnet(vnUuid);
			vm.setTag(c, vif, String.valueOf(vnet.getVnetID()));
			String eth = vm.getVIFRecord(c, vif).device;
			logger.info("Create VIF: Mac [" + mac + "] Ethernet [" + eth + "]");
			if (!eth.equals("")) {
				String routerIp = this.getRouterDAO().getRouter(rtUuid)
						.getRouterIP();
				String url = routerIp + ":9090";
				String subnet = "192.168." + net + ".0";
				String netmask = "255.255.255.0";
				String gateway = "192.168." + net + "." + gate;
				logger.info("Configure Ethernet: URL [" + url + "] Gateway ["
						+ gateway + "] Netmask [" + netmask + "]");
				boolean addEthResult = Host.routeAddEth(c, url, eth, gateway,
						netmask);
				logger.info("Configure Ethernet Result: " + addEthResult);
				if (addEthResult) {
					boolean addSubnetResult = true;
					if (dhcpState == 1) {
						String rangeStart = "192.168." + net + "." + start;
						String rangeEnd = "192.168." + net + "." + end;
						logger.info("Configure Subnet: URL [" + url
								+ "] Netmask [" + netmask + "] RangeStart ["
								+ rangeStart + "] RangeEnd [" + rangeEnd + "]");
						addSubnetResult = RouterManager.addSubnet(c, url,
								subnet, netmask, gateway, rangeStart, rangeEnd);
						logger.info("Configure Subnet Result: "
								+ addSubnetResult);
					}
					if (addSubnetResult) {
						this.getVnetDAO().linkToRouter(vnUuid, rtUuid, net,
								gate, start, end, dhcpState, mac);
						result.put("result", true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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

	/**
	 * @author hty
	 * @param rtuuid
	 * @return
	 * @throws JSONException
	 * @time 2014/08/14
	 */
	public JSONArray getVxnets(String rtuuid) {
		JSONArray ja = new JSONArray();
		List<Vnet> vxnetsList = this.getVnetDAO().getVxnets(rtuuid);
		if (vxnetsList != null) {
			for (Vnet vnet : vxnetsList) {
				JSONObject jo = new JSONObject();
				List<OCVM> ocvmList = this.getVmDAO().getVxnetsList(
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
				jo.put("vn_net", vnet.getVnetNet());
				jo.put("vn_gate", vnet.getVnetGate());
				jo.put("vn_dhcp_start", vnet.getVnetStart());
				jo.put("vn_dhcp_end", vnet.getVnetEnd());
				jo.put("vn_name", vnet.getVnetName());
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray routerGetList(int page, int limit, String search,
			int userId) {
		JSONArray ja = new JSONArray();
		int total = this.getRouterDAO().countAllRouterList(search, userId);
		List<Router> routerList = this.getRouterDAO().getOnePageRouterList(
				page, limit, search, userId);
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

	public JSONArray getAbleRTs(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getRouterDAO().countAllRouterList(search, userId);
		ja.put(totalNum);
		List<Router> rtList = this.getRouterDAO().getOnePageRouterList(page,
				limit, search, userId);
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

	public void routerCreate(String name, String uuid, String fwuuid,
			int capacity, int userId, String poolUuid) {
		Date startTime = new Date();
		JSONObject result = this.createRouter(uuid, userId, name, capacity,
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
			MessagePush.editRowStatus(userId, uuid, "running", "活跃");
			MessagePush.editRowIP(userId, uuid, "基础网络", result.getString("ip"));
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			infoArray.put(Utilities.createLogInfo("原因",
					result.getString("error")));
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.deleteRow(userId, uuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void routerStartUp(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.startRouter(uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + uuid.substring(0, 8)));
		if (result == true) {
			MessagePush.editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			MessagePush.editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void routerShutDown(String uuid, String force, int userId,
			String poolUuid) {
		Date startTime = new Date();
		boolean result = this.shutdownRouter(uuid, force, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + uuid.substring(0, 8)));
		if (result == true) {
			MessagePush.editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			MessagePush.editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void routerDestory(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.deleteRouter(userId, uuid, poolUuid);
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
			MessagePush.deleteRow(userId, uuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
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

	public JSONObject routerGetOneRouter(String routeruuid) {
		JSONObject jo = new JSONObject();
		Router router = this.getRouterDAO().getRouter(routeruuid);
		if (router != null) {
			jo.put("routerName", Utilities.encodeText(router.getRouterName()));
			jo.put("routerDesc", (null == router.getRouterDesc()) ? "&nbsp;"
					: Utilities.encodeText(router.getRouterDesc()));
			jo.put("routerIp", router.getRouterIP());
			String eip = this.getEipDAO().getEipIp(routeruuid);
			if (eip == null) {
				jo.put("eip", "");
			} else {
				jo.put("eip", eip);
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
		}
		return jo;
	}

	public JSONArray getAdminVMList(int page, int limit, String host,
			int importance, String type) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getRouterDAO()
				.countAllAdminVMList(host, importance);
		List<Router> rtList = this.getRouterDAO().getOnePageAdminVmList(page,
				limit, host, importance);
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
				ja.put(jo);
			}
		}
		return ja;
	}
}
