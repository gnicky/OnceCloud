package com.oncecloud.manager;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
import com.oncecloud.message.MessagePush;

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
	private EIPDAO eipDAO;
	private VnetDAO vnetDAO;
	private VMDAO vmDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private FirewallDAO firewallDAO;
	private HostDAO hostDAO;
	private UserDAO userDAO;
	private ForwardPortDAO forwardPortDAO;
	private PPTPUserDAO pptpUserDAO;

	private MessagePush messagePush;
	private HashHelper hashHelper;
	private EIPManager eipManager;
	private VMManager vmManager;

	private Constant constant;

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

	private EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	private void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
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
			infoArray.put(Utilities.createLogInfo("原因",result.has("error")?result.getString("error"):"未知"));
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
			Image rtImage = this.getImageDAO().getRTImage(userId);
			OS = "linux";
			imagePwd = rtImage.getImagePwd();
			tplUuid = rtImage.getImageUuid();
			DHCP dhcp = this.getDhcpDAO().getFreeDHCP(uuid, 3);
			ip = dhcp.getDhcpIp();
			mac = dhcp.getDhcpMac();
			c = this.getConstant().getConnectionFromPool(poolUuid);
			allocateHost = this.getVmManager().getAllocateHost(poolUuid, 1024);
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
					Record rtrecord = this.getVmManager().createVMOnHost(c,
							uuid, tplUuid, "root", pwd, 1, 1024, mac, ip, OS,
							allocateHost, imagePwd, backendName, true);
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
		
		///cyh 绑定内部防火墙
		if (jo.getBoolean("isSuccess") == true) 
		{
			///1  创建内部防火墙，2 绑定到当前路由器
			String firewallinner = UUID.randomUUID().toString();
			if(this.getFirewallDAO().insertFirewallForinnerRoute(firewallinner, name, userId, new Date()))
			{
				this.getRouterDAO().updateInnerFirewall(uuid, firewallinner);
			}
		}
		return jo;
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
		try {
			Router currentRT = this.getRouterDAO().getRouter(uuid);
			if (currentRT != null) {
				boolean preStartRouter = this.getRouterDAO().updatePowerStatus(
						uuid, RouterManager.POWER_BOOT);
				if (preStartRouter == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					if (!powerState.equals("Running")) {
						hostUuid = this.getVmManager().getAllocateHost(
								poolUuid, 1024);
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
								poolUuid);
						logger.debug("Update Router Vlan: MAC ["
								+ vnet.getVifMac() + "] Vlan ["
								+ vnet.getVnetID() + "]");
					}
				}
			}
		}
		return result;
	}

	private void bindVlan(String routerUuid, String vifUuid, int vnetId,
			String poolUuid) {
		try {
			Connection c = this.getConstant().getConnectionFromPool(poolUuid);
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

	public JSONObject doUnlinkRouter(String vnetUuid, int userId) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			logger.info("Unlink to Router: Vnet [vn-"
					+ vnetUuid.substring(0, 8) + "]");
			Connection c = this.getConstant().getConnection(userId);
			Vnet vnet = this.getVnetDAO().getVnet(vnetUuid);
			String vifUuid = vnet.getVifUuid();
			String vifMac = vnet.getVifMac();
			String routerUuid = vnet.getVnetRouter();
			Router router = this.getRouterDAO().getAliveRouter(routerUuid);
			VM routerVm = VM.getByUuid(c, routerUuid);
			Set<VIF> vifs = routerVm.getVIFs(c);
			for (VIF vif : vifs) {
				VIF.Record record = vif.getRecord(c);
				System.out.println(record.MAC + "|" + record.device + "|" + record.uuid);
			}
			routerVm.destroyVIF(c, Types.toVIF(vifUuid));
			logger.info("Delete VIF: UUID [vif-" + vifUuid.substring(0, 8)
					+ "]");
			String url = router.getRouterIP() + ":9090";
			boolean delEthResult = Host.routeDelEth(c, url, vifMac);
			logger.info("Delete Ethernet: Mac [" + vifMac + "] Result ["
					+ delEthResult + "]");
			if (vnet.getDhcpStatus() == 1) {
				String subnet = "192.168." + vnet.getVnetNet() + ".0";
				String netmask = "255.255.255.0";
				JSONObject delJo = new JSONObject();
				delJo.put("subnet", subnet);
				delJo.put("netmask", netmask);
				Host.delSubnet(c, url, delJo.toString());
			}
			this.getVnetDAO().unlinkRouter(vnetUuid);
			this.getVmManager().unAssginIpAddress(c, vnetUuid);
			result.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject doLinkRouter(int userId, String vnetUuid,
			String routerUuid, int net, int gate, int start, int end,
			int dhcpState) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			logger.info("Link to Router: Router [rt-"
					+ routerUuid.substring(0, 8) + "] Vnet [vn-"
					+ vnetUuid.substring(0, 8) + "]");
			Connection c = this.getConstant().getConnection(userId);
			VM vm = VM.getByUuid(c, routerUuid);
			String mac = Utilities.randomMac();
			VIF vif = VIF.createBindToPhysicalNetwork(c, vm, "ovs0", mac);
			String vifUuid = vif.getUuid(c);
			Vnet vnet = this.getVnetDAO().getVnet(vnetUuid);
			vm.setTag(c, vif, String.valueOf(vnet.getVnetID()));
			String eth = vm.getVIFRecord(c, vif).device;
			logger.info("Create VIF: Mac [" + mac + "] Ethernet [" + eth + "]");
			if (!eth.equals("")) {
				String routerIp = this.getRouterDAO().getRouter(routerUuid)
						.getRouterIP();
				String url = routerIp + ":9090";
				String subnet = "192.168." + net + ".0";
				String netmask = "255.255.255.0";
				String gateway = "192.168." + net + "." + gate;
				logger.info("Configure Ethernet: URL [" + url + "] Gateway ["
						+ gateway + "] Netmask [" + netmask + "]");
				boolean addEthResult = Host.routeAddEth(c, url, mac, gateway,
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
						this.getVmManager().assginIpAddress(c, url, subnet,
								vnetUuid);
						logger.info("Configure Subnet Result: "
								+ addSubnetResult);
					}
					if (addSubnetResult) {
						this.getVnetDAO().linkToRouter(vnetUuid, routerUuid,
								net, gate, start, end, dhcpState, vifUuid, mac);
						result.put("result", true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
					boolean addSubnetResult = RouterManager.addSubnet(c, url,
							subnet, netmask, gateway, rangeStart, rangeEnd);
					if (addSubnetResult) {
						vnet.setVnetStart(2);
						vnet.setVnetEnd(254);
						vnet.setDhcpStatus(1);
						this.getVnetDAO().updateVnet(vnet);
						this.getVmManager().assginIpAddress(c, url, subnet,
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
						this.getVmManager().unAssginIpAddress(c, vnetUuid);
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

	public void routerAdminStartUp(String uuid, int userId) {
		Router router = this.getRouterDAO().getRouter(uuid);
		String poolUuid = this.getHostDAO().getHost(router.getHostUuid())
				.getPoolUuid();
		this.startRouter(uuid, userId, poolUuid);
	}

	public void routerAdminShutDown(String uuid, String force, int userId) {
		Router router = this.getRouterDAO().getRouter(uuid);
		String poolUuid = this.getHostDAO().getHost(router.getHostUuid())
				.getPoolUuid();
		this.shutdownRouter(uuid, force, userId, poolUuid);
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

	public void updateImportance(String uuid, int importance) {
		this.getRouterDAO().updateImportance(uuid, importance);
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
