package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;

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
	private VnetManager vnetManager;
	private RouterManager routerManager;

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

	private VnetManager getVnetManager() {
		return vnetManager;
	}

	@Autowired
	private void setVnetManager(VnetManager vnetManager) {
		this.vnetManager = vnetManager;
	}

	private RouterManager getRouterManager() {
		return routerManager;
	}

	@Autowired
	private void setRouterManager(RouterManager routerManager) {
		this.routerManager = routerManager;
	}

	/**
	 * 添加主机到私有网络
	 */
	public boolean addVmToVnet(int userId, String vmArray, String vnetId,
			String hostUuid) {
		boolean result = false;
		Date startTime = new Date();
		try {
			JSONArray jArray = new JSONArray(vmArray);
			for (int i = 0; i < jArray.length(); i++) {
				String vmUuid = jArray.getString(i);
				OCVM vm = this.getVmDAO().getVM(vmUuid);
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
						this.getVmManager().setVlan(vmUuid, -1, hostUuid);
						String ip = this
								.getDhcpDAO()
								.getDHCP(
										this.getVmDAO().getVM(vmUuid)
												.getVmMac()).getDhcpIp();
						result = this.getVmDAO().returnToBasicNetwork(vmUuid,
								ip);
					} else {
						this.getVmManager().setVlan(vmUuid,
								this.getVnetDAO().getVnet(vnetId).getVnetID(),
								hostUuid);
						result = this.getVmDAO().updateVMVlan(vmUuid, vnetId);
					}
					Date endTime = new Date();
					int elapse = Utilities.timeElapse(startTime, endTime);
					if (result) {
						OCLog log = this.getLogDAO().insertLog(userId,
								LogConstant.logObject.私有网络.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.成功.ordinal(),
								infoArray.toString(), startTime, elapse);
						MessagePush.pushMessage(userId,
								Utilities.stickyToSuccess(log.toAString()));
					} else {
						OCLog log = this.getLogDAO().insertLog(userId,
								LogConstant.logObject.私有网络.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.失败.ordinal(),
								infoArray.toString(), startTime, elapse);
						MessagePush.pushMessage(userId,
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

	public boolean checkNet(int userId, String routerid, Integer net) {
		return 0 == this.getVnetDAO().countAbleNet(userId, routerid, net);
	}

	public JSONArray getVMs(String vnUuid) {
		JSONArray ja = new JSONArray();
		List<OCVM> ocvmList = this.getVmDAO().getVxnetsList(vnUuid);
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

	public boolean addOneVmToVnet(int userId, String vmuuid, String vnId,
			String hostUuid) {
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
				this.getVmManager().setVlan(vmUuid,
						this.getVnetDAO().getVnet(vnId).getVnetID(), hostUuid);
				result = this.getVmDAO().updateVMVlan(vmUuid, vnId);
				Date endTime = new Date();
				int elapse = Utilities.timeElapse(startTime, endTime);
				if (result) {
					OCLog log = this.getLogDAO().insertLog(userId,
							LogConstant.logObject.私有网络.ordinal(),
							LogConstant.logAction.加入.ordinal(),
							LogConstant.logStatus.成功.ordinal(),
							infoArray.toString(), startTime, elapse);
					MessagePush.pushMessage(userId,
							Utilities.stickyToSuccess(log.toAString()));
				} else {
					OCLog log = this.getLogDAO().insertLog(userId,
							LogConstant.logObject.私有网络.ordinal(),
							LogConstant.logAction.加入.ordinal(),
							LogConstant.logStatus.失败.ordinal(),
							infoArray.toString(), startTime, elapse);
					MessagePush.pushMessage(userId,
							Utilities.stickyToError(log.toAString()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public enum DeleteVnetResult {
		Success, Using, Failed
	}

	public DeleteVnetResult deleteVnet(int userId, String vnetUuid) {
		Date startTime = new Date();
		DeleteVnetResult result = DeleteVnetResult.Failed;
		int using = this.getVmDAO().countVMOfVnet(vnetUuid);
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
				MessagePush.deleteRow(userId, vnetUuid);
				MessagePush.pushMessage(userId,
						Utilities.stickyToSuccess(log.toString()));
			} else {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.私有网络.ordinal(),
						LogConstant.logAction.删除.ordinal(),
						LogConstant.logStatus.失败.ordinal(),
						infoArray.toString(), startTime, elapse);
				MessagePush.pushMessage(userId,
						Utilities.stickyToError(log.toString()));
			}
		}
		return result;
	}

	public JSONArray vnetGetList(int page, int limit, String search, int userId) {
		JSONArray ja = new JSONArray();
		int total = this.getVnetDAO().countAllVnetList(search, userId);
		List<Vnet> vnetList = this.getVnetDAO().getOnePageVnetList(page, limit,
				search, userId);
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
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.私有网络.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.deleteRow(userId, uuid);
			MessagePush.pushMessage(userId,
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

	public JSONObject vnetGetOneVnet(String vnetuuid) {
		Vnet vnet = this.getVnetDAO().getVnet(vnetuuid);
		JSONObject jo = new JSONObject();
		jo.put("vnetName", Utilities.encodeText(vnet.getVnetName()));
		jo.put("vnetDesc", (null == vnet.getVnetDesc()) ? "&nbsp;" : Utilities
				.encodeText(vnet.getVnetDesc()).replace("+", "%20"));
		jo.put("vnetUID", vnet.getVnetUID());
		jo.put("vnetNet", (null == vnet.getVnetNet()) ? "" : vnet.getVnetNet());
		jo.put("vnetGate",
				(null == vnet.getVnetGate()) ? "" : vnet.getVnetGate());
		jo.put("vnetStart",
				(null == vnet.getVnetStart()) ? "" : vnet.getVnetStart());
		jo.put("vnetEnd", (null == vnet.getVnetEnd()) ? "" : vnet.getVnetEnd());
		jo.put("dhcpStatus", vnet.getDhcpStatus());
		JSONArray ja = getVMs(vnetuuid);
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
		return jo;
	}

	public JSONObject vnetChecknet(String routerid, Integer net, int userId) {
		JSONObject jo = new JSONObject();
		if (this.getVnetManager().checkNet(userId, routerid, net)) {
			jo.put("isSuccess", true);
		} else {
			jo.put("isSuccess", false);
		}
		return jo;
	}

	public JSONObject vnetLinkrouter(int userId, String vnetuuid,
			String routerid, Integer net, Integer gate, Integer start,
			Integer end, Integer dhcpState) {
		JSONObject jo = this.getRouterManager().linkVnetToRouter(userId,
				vnetuuid, routerid, net, gate, start, end, dhcpState);
		// push message
		if (jo.getBoolean("result")) {
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess("连接路由器成功"));
		} else {
			MessagePush.pushMessage(userId, Utilities.stickyToError("连接路由器失败"));
		}
		return jo;
	}

	public void vnetUnlink(String vnetuuid, int userId) {
		boolean result = this.getVnetDAO().unlink(vnetuuid);
		// push message
		if (result) {
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess("离开路由器成功"));
		} else {
			MessagePush.pushMessage(userId, Utilities.stickyToError("离开路由器失败"));
		}
	}

	public JSONObject vnetAddvm(String vnId, String vmuuidStr, int userId,
			String hostUuid) {
		JSONObject jo = new JSONObject();
		if (this.getVnetManager()
				.addVmToVnet(userId, vmuuidStr, vnId, hostUuid)) {
			jo.put("isSuccess", true);
		} else {
			jo.put("isSuccess", false);
		}
		return jo;
	}

	public JSONArray vnetGetAllList(int userId) {
		JSONArray ja = new JSONArray();
		List<Vnet> vnetList = this.getVnetDAO().getallVnetList(userId);
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

	public JSONObject vnetAddOnevm(String vnId, String vmuuid, int userId,
			String hostUuid) {
		JSONObject jo = new JSONObject();
		if (this.getVnetManager()
				.addOneVmToVnet(userId, vmuuid, vnId, hostUuid)) {
			jo.put("isSuccess", true);
		} else {
			jo.put("isSuccess", false);
		}
		return jo;
	}
}