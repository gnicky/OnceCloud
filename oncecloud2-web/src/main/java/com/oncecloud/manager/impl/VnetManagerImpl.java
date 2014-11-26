package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Types;
import com.once.xenapi.VIF;
import com.once.xenapi.VM;
import com.once.xenapi.Types.BadServerResponse;
import com.once.xenapi.Types.XenAPIException;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.RouterManager;
import com.oncecloud.manager.VnetManager;
import com.oncecloud.message.MessagePush;

@Component("VnetManager")
public class VnetManagerImpl implements VnetManager {
	private final static Logger logger = Logger.getLogger(VnetManager.class);
	
	private VnetDAO vnetDAO;
	
	private VnetDAO getVnetDAO() {
		return vnetDAO;
	}

	@Autowired
	private void setVnetDAO(VnetDAO vnetDAO) {
		this.vnetDAO = vnetDAO;
	}

	private VMDAO vmDAO;
	private DHCPDAO dhcpDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private RouterDAO routerDAO;
	private MessagePush messagePush;
	private Constant constant;

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private DHCPDAO getDhcpDAO() {
		return dhcpDAO;
	}

	@Autowired
	private void setDhcpDAO(DHCPDAO dhcpDAO) {
		this.dhcpDAO = dhcpDAO;
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

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	public enum DeleteVnetResult {
		Success, Using, Failed
	}

	public JSONObject checkNet(int userId, String routerid, Integer net) {
		JSONObject jo = new JSONObject();
		int count = this.getVnetDAO().countAbleNet(userId, routerid, net);
		if (count > 0) {
			jo.put("result", false);
		} else {
			jo.put("result", true);
		}
		return jo;
	}

	public JSONArray getVMs(String vnUuid) {
		JSONArray ja = new JSONArray();
		List<OCVM> ocvmList = this.getVmDAO().getVMsOfVnet(vnUuid);
		if (ocvmList != null) {
			for (OCVM ocvm : ocvmList) {
				JSONObject jovm = new JSONObject();
				jovm.put("hostid", ocvm.getVmUuid());
				jovm.put("hostname", Utilities.encodeText(ocvm.getVmName()));
				jovm.put("hoststatus", ocvm.getVmPower());
				jovm.put("hostip",
						null == ocvm.getVmIP() ? "null" : ocvm.getVmIP());
				ja.put(jovm);
			}
		}
		return ja;
	}

	private boolean bindVnetToVM(int userId, String vmuuid, String vnId,
			String poolUuid) {
		boolean result = false;
		Date startTime = new Date();
		Connection conn = null;
		JSONArray infoArray = new JSONArray();
		try {
			conn = this.getConstant().getConnectionFromPool(poolUuid);
			String vmUuid = vmuuid;
			OCVM vm = this.getVmDAO().getVM(vmUuid);
			if (vm != null) {
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.私有网络.toString(),
						"vn-" + vnId.substring(0, 8)));
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.主机.toString(),
						"i-" + vmUuid.substring(0, 8)));
				Vnet vnet = this.getVnetDAO().getVnet(vnId);
				setVlan(vmUuid, vnet.getVnetID(), conn);
				vm.setVmVlan(vnId);
				this.getVmDAO().updateVM(vm);
				if (vnet.getVnetRouter() != null) {
					String routerIp = this.getRouterDAO().getRouter(vnet.getVnetRouter())
							.getRouterIP();
					String url = routerIp + ":9090";
					if (vnet.getDhcpStatus() == 1) {
						String subnet = "192.168." + vnet.getVnetNet() + ".0";
						assginIpAddressToVM(conn, url, subnet, vm);
					}
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.私有网络.ordinal(),
					LogConstant.logAction.加入.ordinal(),
					LogConstant.logStatus.成功.ordinal(),
					infoArray.toString(), startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toAString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.私有网络.ordinal(),
					LogConstant.logAction.加入.ordinal(),
					LogConstant.logStatus.失败.ordinal(),
					infoArray.toString(), startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toAString()));
		}
		return result;
	}

	
	private boolean setVlan(String uuid, int vlanId, Connection c) {
		try {
			VM vm = VM.getByUuid(c, uuid);
			vm.setTag(c, vm.getVIFs(c).iterator().next(),
					String.valueOf(vlanId));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean assginIpAddressToVM(Connection c, String url, String subnet,
			OCVM vm) {
		boolean result = false;
		if (vm != null) {
			try {
				String mac = vm.getVmMac();
				String vnetIp = Host.assignIpAddress(c, url, mac, subnet);
				vm.setVmIP(vnetIp);
				this.getVmDAO().updateVM(vm);
				this.restartNetwork(c, vm.getVmUuid(), false);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
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
	
	/**
	 * 添加主机到私有网络
	 */
	private boolean bindVnetToVMs(int userId, String vmArray, String vnetId,
			String poolUuid, String content, String conid) {
		boolean result = false;
		Date startTime = new Date();
		Connection conn = null;
		try {
			conn = this.getConstant().getConnectionFromPool(poolUuid);
			JSONArray jArray = new JSONArray(vmArray);
			for (int i = 0; i < jArray.length(); i++) {
				String vmUuid = jArray.getString(i);
				OCVM vm = this.getVmDAO().getVM(vmUuid);
				if (vm.getVmVlan() != null || vm.getVmIP() != null) {
					result = false;
					this.getMessagePush().pushMessageClose(userId, content, conid);
					break;
				}
				if (vm != null) {
					JSONArray infoArray = new JSONArray();
					if (!vnetId.equals("-1")) {
						infoArray.put(Utilities.createLogInfo(
								LogConstant.logObject.私有网络.toString(), "vn-"
										+ vnetId.substring(0, 8)));
					}
					infoArray.put(Utilities.createLogInfo(
							LogConstant.logObject.主机.toString(),
							"i-" + vmUuid.substring(0, 8)));
					if (vnetId.equals("-1")) {
						this.setVlan(vmUuid, -1, conn);
						String ip = this
								.getDhcpDAO()
								.getDHCP(
										this.getVmDAO().getVM(vmUuid)
												.getVmMac()).getDhcpIp();
						result = this.restartNetwork(conn, vm.getVmUuid(), false);
						result = this.getVmDAO().returnToBasicNetwork(vmUuid,
								ip);
					} else {
						Vnet vnet = this.getVnetDAO().getVnet(vnetId);
						setVlan(vmUuid, vnet.getVnetID(), conn);
						vm.setVmVlan(vnetId);
						this.getVmDAO().updateVM(vm);
						if (vnet.getVnetRouter() != null) {
							String routerIp = this.getRouterDAO().getRouter(vnet.getVnetRouter())
									.getRouterIP();
							String url = routerIp + ":9090";
							if (vnet.getDhcpStatus() == 1) {
								String subnet = "192.168." + vnet.getVnetNet() + ".0";
								this.assginIpAddressToVM(conn, url, subnet, vm);
							}
						}
						result = true;
					}
					Date endTime = new Date();
					int elapse = Utilities.timeElapse(startTime, endTime);
					if (result) {
						OCLog log = this.getLogDAO().insertLog(userId,
								LogConstant.logObject.网络.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.成功.ordinal(),
								infoArray.toString(), startTime, elapse);
						this.getMessagePush().pushMessageClose(userId, content, conid);
						this.getMessagePush().pushMessage(userId,
								Utilities.stickyToSuccess(log.toAString()));
					} else {
						OCLog log = this.getLogDAO().insertLog(userId,
								LogConstant.logObject.网络.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.失败.ordinal(),
								infoArray.toString(), startTime, elapse);
						this.getMessagePush().pushMessageClose(userId, content, conid);
						this.getMessagePush().pushMessage(userId,
								Utilities.stickyToError(log.toAString()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.getMessagePush().pushMessageClose(userId, content, conid);
			result = false;
		}
		return result;
	}

	public String deleteVnet(int userId, String vnetUuid) {
		Date startTime = new Date();
		DeleteVnetResult result = DeleteVnetResult.Failed;
		int using = this.getVmDAO().countVMsOfVnet(vnetUuid);
		if (using == 0) {
			boolean ret = this.getVnetDAO().removeVnet(userId, vnetUuid);
			this.getQuotaDAO().updateQuota(userId, "quotaVlan", 1, false);
			result = ret ? DeleteVnetResult.Success : result;
		} else {
			result = DeleteVnetResult.Using;
		}

		if (result != DeleteVnetResult.Using) {
			Date endTime = new Date();
			int elapse = Utilities.timeElapse(startTime, endTime);
			JSONArray infoArray = new JSONArray();
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.私有网络.toString(),
					"vn-" + vnetUuid.substring(0, 8)));
			if (result == DeleteVnetResult.Success) {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.私有网络.ordinal(),
						LogConstant.logAction.删除.ordinal(),
						LogConstant.logStatus.成功.ordinal(),
						infoArray.toString(), startTime, elapse);
				this.getMessagePush().deleteRow(userId, vnetUuid);
				this.getMessagePush().pushMessage(userId,
						Utilities.stickyToSuccess(log.toString()));
			} else {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.私有网络.ordinal(),
						LogConstant.logAction.删除.ordinal(),
						LogConstant.logStatus.失败.ordinal(),
						infoArray.toString(), startTime, elapse);
				this.getMessagePush().pushMessage(userId,
						Utilities.stickyToError(log.toString()));
			}
		}
		return result.toString();
	}

	/**
	 * 获取用户私有网络列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getVnetList(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int total = this.getVnetDAO().countVnets(userId, search);
		List<Vnet> vnetList = this.getVnetDAO().getOnePageVnets(userId, page,
				limit, search);
		ja.put(total);
		if (vnetList != null) {
			for (Vnet vnet : vnetList) {
				JSONObject jo = new JSONObject();
				String vnetUuid = vnet.getVnetUuid();
				jo.put("uuid", vnetUuid);
				jo.put("name", Utilities.encodeText(vnet.getVnetName()));
				jo.put("routerid",
						null == vnet.getVnetRouter() ? "null" : vnet
								.getVnetRouter());
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed(vnet.getCreateDate()));
				timeUsed = timeUsed.replace("+", "%20");
				jo.put("createdate", timeUsed);
				ja.put(jo);
			}
		}
		return ja;
	}

	public void vnetCreate(String name, String uuid, String desc, int userId) {
		Date startTime = new Date();
		boolean result = this.getVnetDAO().createVnet(uuid, userId, name, desc);
		this.getQuotaDAO().updateQuota(userId, "quotaVlan", 1, true);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.私有网络.toString(),
				Utilities.encodeText(name)));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.私有网络.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.私有网络.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().deleteRow(userId, uuid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public JSONArray vnetQuota(int userId) {
		JSONArray ja = new JSONArray();
		int free = this.getQuotaDAO().getQuotaTotal(userId).getQuotaVlan()
				- this.getQuotaDAO().getQuotaUsed(userId).getQuotaVlan();
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

	public JSONObject getVnetDetail(String vnetUuid) {
		JSONObject jo = new JSONObject();
		Vnet vnet = this.getVnetDAO().getVnet(vnetUuid);
		if (vnet != null) {
			jo.put("vnetName", Utilities.encodeText(vnet.getVnetName()));
			jo.put("vnetDesc", (null == vnet.getVnetDesc()) ? "&nbsp;"
					: Utilities.encodeText(vnet.getVnetDesc()));
			jo.put("vnetUID", vnet.getVnetUID());
			jo.put("vnetNet",
					(null == vnet.getVnetNet()) ? "" : vnet.getVnetNet());
			jo.put("vnetGate",
					(null == vnet.getVnetGate()) ? "" : vnet.getVnetGate());
			jo.put("vnetStart",
					(null == vnet.getVnetStart()) ? "" : vnet.getVnetStart());
			jo.put("vnetEnd",
					(null == vnet.getVnetEnd()) ? "" : vnet.getVnetEnd());
			jo.put("dhcpStatus", vnet.getDhcpStatus());
			JSONArray ja = getVMs(vnetUuid);
			jo.put("vmList", ja);
			if (vnet.getVnetRouter() == null) {
				jo.put("vnetRouter", "&nbsp;");
			} else {
				jo.put("vnetRouter", vnet.getVnetRouter());
				jo.put("vnetRouterName",
						this.getRouterDAO().getRouter(vnet.getVnetRouter())
								.getRouterName());
			}
			jo.put("createDate", Utilities.formatTime(vnet.getCreateDate()));
			String timeUsed = Utilities.encodeText(Utilities.dateToUsed(vnet
					.getCreateDate()));
			jo.put("useDate", timeUsed);
		}
		return jo;
	}

	public JSONObject linkRouter(int userId, String vnetUuid, String routerId,
			Integer net, Integer gate, Integer start, Integer end,
			Integer dhcpState, String content, String conid) {
		Date startTime = new Date();
		JSONObject jo = doLinkRouter(userId, vnetUuid,
				routerId, net, gate, start, end, dhcpState);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.私有网络.toString(),
				"vn-" + vnetUuid.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.路由器.toString(),
				"rt-" + routerId.substring(0, 8)));
		if (jo.getBoolean("result")) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.连接.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessageClose(userId, content, conid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toAString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.连接.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessageClose(userId, content, conid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toAString()));
		}
		return jo;
	}
	
	private JSONObject doLinkRouter(int userId, String vnetUuid,
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
						addSubnetResult = addSubnet(c, url,
								subnet, netmask, gateway, rangeStart, rangeEnd);
						assginIpAddress(c, url, subnet, vnetUuid);
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
	

	public JSONObject unlinkRouter(String vnetUuid, int userId, String content, String conid) {
		Date startTime = new Date();
		JSONObject jo = doUnlinkRouter(vnetUuid, userId);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.私有网络.toString(),
				"vn-" + vnetUuid.substring(0, 8)));
		// push message
		if (jo.getBoolean("result")) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.离开.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessageClose(userId, content, conid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.路由器.ordinal(),
					LogConstant.logAction.离开.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessageClose(userId, content, conid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	private JSONObject doUnlinkRouter(String vnetUuid, int userId) {
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
			unAssginIpAddress(c, vnetUuid);
			result.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	
	public JSONObject vnetAddvm(int userId, String vmuuidStr, String vnId,
			String poolUuid, String content, String conid) {
		JSONObject jo = new JSONObject();
		if (this.bindVnetToVMs(userId, vmuuidStr, vnId, poolUuid, content, conid)) {
			jo.put("isSuccess", true);
		} else {
			jo.put("isSuccess", false);
		}
		return jo;
	}

	public JSONArray getVnetsOfUser(int userId) {
		JSONArray ja = new JSONArray();
		List<Vnet> vnetList = this.getVnetDAO().getVnetsOfUser(userId);
		if (vnetList != null) {
			for (Vnet vnet : vnetList) {
				JSONObject jo = new JSONObject();
				String vnetUuid = vnet.getVnetUuid();
				jo.put("vnetuuid", vnetUuid);
				jo.put("vnetname", Utilities.encodeText(vnet.getVnetName()));
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject bindVM(String vnId, String vmuuid, int userId,
			String poolUuid) {
		JSONObject jo = new JSONObject();
		if (this.bindVnetToVM(userId, vmuuid, vnId, poolUuid)) {
			jo.put("isSuccess", true);
		} else {
			jo.put("isSuccess", false);
		}
		return jo;
	}

	public boolean isRouterHasVnets(String routerUuid, int userId) {
		int count = this.getVnetDAO().countVnetsOfRouter(routerUuid, userId);
		return count > 0;
	}

	public JSONArray getAvailableVnet(Integer userId) {
		JSONArray ja = new JSONArray();
		
		List<Vnet> vnetList = this.getVnetDAO().getAvailableVnet(userId);
		if (vnetList != null) {
			for (Vnet vnet : vnetList) {
				JSONObject jo = new JSONObject();
				jo.put("uuid",  vnet.getVnetUuid());
				jo.put("name", Utilities.encodeText(vnet.getVnetName()));
				ja.put(jo);
			}
		}
		return ja;
	}
}
