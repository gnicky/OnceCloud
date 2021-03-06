package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author xpx hty
 * @version 2014/08/25
 */
@Component
public class VnetManager {
	private VMDAO vmDAO;
	private DHCPDAO dhcpDAO;
	private VnetDAO vnetDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private RouterDAO routerDAO;
	private VMManager vmManager;
	private RouterManager routerManager;
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

	private VnetDAO getVnetDAO() {
		return vnetDAO;
	}

	@Autowired
	private void setVnetDAO(VnetDAO vnetDAO) {
		this.vnetDAO = vnetDAO;
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

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	private RouterManager getRouterManager() {
		return routerManager;
	}

	@Autowired
	private void setRouterManager(RouterManager routerManager) {
		this.routerManager = routerManager;
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

	public boolean bindVnetToVM(int userId, String vmuuid, String vnId,
			String poolUuid) {
		boolean result = false;
		Date startTime = new Date();
		try {
			String vmUuid = vmuuid;
			OCVM vm = this.getVmDAO().getVM(vmUuid);
			if (vm != null) {
				JSONArray infoArray = new JSONArray();
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.私有网络.toString(),
						"vn-" + vnId.substring(0, 8)));
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.主机.toString(),
						"i-" + vmUuid.substring(0, 8)));
				Vnet vnet = this.getVnetDAO().getVnet(vnId);
				this.getVmManager().setVlan(vmUuid,
						vnet.getVnetID(), poolUuid);
				vm.setVmVlan(vnId);
				this.getVmDAO().updateVM(vm);
				if (vnet.getVnetRouter() != null) {
					String routerIp = this.getRouterDAO().getRouter(vnet.getVnetRouter())
							.getRouterIP();
					String url = routerIp + ":9090";
					if (vnet.getDhcpStatus() == 1) {
						String subnet = "192.168." + vnet.getVnetNet() + ".0";
						Connection c = this.getConstant().getConnectionFromPool(poolUuid);
						this.getVmManager().assginIpAddressToVM(c, url, subnet, vm);
					}
				}
				result = true;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 添加主机到私有网络
	 */
	public boolean bindVnetToVMs(int userId, String vmArray, String vnetId,
			String poolUuid, String content, String conid) {
		boolean result = false;
		Date startTime = new Date();
		try {
			JSONArray jArray = new JSONArray(vmArray);
			for (int i = 0; i < jArray.length(); i++) {
				String vmUuid = jArray.getString(i);
				OCVM vm = this.getVmDAO().getVM(vmUuid);
				if (vm.getVmVlan() != null || vm.getVmIP() != null) {
					result = false;
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
						this.getVmManager().setVlan(vmUuid, -1, poolUuid);
						String ip = this
								.getDhcpDAO()
								.getDHCP(
										this.getVmDAO().getVM(vmUuid)
												.getVmMac()).getDhcpIp();
						result = this.getVmDAO().returnToBasicNetwork(vmUuid,
								ip);
					} else {
						Vnet vnet = this.getVnetDAO().getVnet(vnetId);
						this.getVmManager().setVlan(vmUuid,
								vnet.getVnetID(),
								poolUuid);
						vm.setVmVlan(vnetId);
						this.getVmDAO().updateVM(vm);
						if (vnet.getVnetRouter() != null) {
							String routerIp = this.getRouterDAO().getRouter(vnet.getVnetRouter())
									.getRouterIP();
							String url = routerIp + ":9090";
							if (vnet.getDhcpStatus() == 1) {
								String subnet = "192.168." + vnet.getVnetNet() + ".0";
								Connection c = this.getConstant().getConnectionFromPool(poolUuid);
								this.getVmManager().assginIpAddressToVM(c, url, subnet, vm);
							}
						}
						result = true;
					}
					Date endTime = new Date();
					int elapse = Utilities.timeElapse(startTime, endTime);
					if (result) {
						OCLog log = this.getLogDAO().insertLog(userId,
								LogConstant.logObject.私有网络.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.成功.ordinal(),
								infoArray.toString(), startTime, elapse);
						this.getMessagePush().pushMessageClose(userId, content, conid);
						this.getMessagePush().pushMessage(userId,
								Utilities.stickyToSuccess(log.toAString()));
					} else {
						OCLog log = this.getLogDAO().insertLog(userId,
								LogConstant.logObject.私有网络.ordinal(),
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
		JSONObject jo = this.getRouterManager().doLinkRouter(userId, vnetUuid,
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

	public JSONObject unlinkRouter(String vnetUuid, int userId, String content, String conid) {
		Date startTime = new Date();
		JSONObject jo = this.getRouterManager().doUnlinkRouter(vnetUuid, userId);
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
