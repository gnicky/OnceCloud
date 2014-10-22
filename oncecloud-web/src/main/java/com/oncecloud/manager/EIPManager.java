package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.oncecloud.dao.DatabaseDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FeeDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.Database;
import com.oncecloud.entity.EIP;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Quota;
import com.oncecloud.entity.Router;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author hty
 * @version 2014/08/23
 */
@Component
public class EIPManager {
	private final static Logger logger = Logger.getLogger(EIPManager.class);

	private EIPDAO eipDAO;
	private FeeDAO feeDAO;
	private LogDAO logDAO;
	private VMDAO vmDAO;
	private LBDAO lbDAO;
	private DatabaseDAO databaseDAO;
	private RouterDAO routerDAO;
	private QuotaDAO quotaDAO;
	private FirewallManager firewallManager;
	private Constant constant;
	private MessagePush messagePush;

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	private void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	private DatabaseDAO getDatabaseDAO() {
		return databaseDAO;
	}

	@Autowired
	private void setDatabaseDAO(DatabaseDAO databaseDAO) {
		this.databaseDAO = databaseDAO;
	}

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	private FirewallManager getFirewallManager() {
		return firewallManager;
	}

	@Autowired
	private void setFirewallManager(FirewallManager firewallManager) {
		this.firewallManager = firewallManager;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public JSONObject eipApply(String eipName, int uid, int eipBandwidth,
			String eipUuid) {
		boolean result = false;
		JSONObject jo = new JSONObject();
		String eipIp = null;
		Date startTime = new Date();
		try {
			EIP eip = this.getEipDAO().applyEip(eipName, uid, eipBandwidth,
					startTime, eipUuid);
			if (eip != null) {
				this.getFeeDAO().insertFeeEip(uid, startTime,
						Utilities.AddMinuteForDate(startTime, 60),
						eipBandwidth * Constant.EIP_PRICE, 1, eipUuid, eipName);
				jo.put("result", true);
				jo.put("eipIp", eip.getEipIp());
				jo.put("eipId", "eip-" + eipUuid.substring(0, 8));
				jo.put("createDime",
						Utilities.encodeText(Utilities.dateToUsed(startTime)));
				eipIp = eip.getEipIp();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jo.put("result", false);
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.公网IP.toString(), eipIp));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.申请.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.申请.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	/**
	 * 获取公网IP列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getEIPList(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		try {
			int totalNum = this.getEipDAO().countAllEipList(userId, search);
			List<EIP> eipList = this.getEipDAO().getOnePageEipList(userId,
					page, limit, search);
			ja.put(totalNum);
			if (eipList != null) {
				for (int i = 0; i < eipList.size(); i++) {
					JSONObject jo = new JSONObject();
					EIP eip = eipList.get(i);
					jo.put("eipIp", eip.getEipIp());
					jo.put("eipName", Utilities.encodeText(eip.getEipName()));
					String vmuuid = eip.getEipDependency();
					if (vmuuid != null) {
						jo.put("depenType", eip.getDepenType());
						jo.put("eipDepen", vmuuid);
						switch (eip.getDepenType()) {
						case 0:
							jo.put("depenName",
									Utilities.encodeText(this.getVmDAO()
											.getVM(vmuuid).getVmName()));
							break;
						case 1:
							jo.put("depenName", Utilities.encodeText(this
									.getLbDAO().getLBName(vmuuid)));
							break;
						case 2:
							jo.put("depenName", Utilities.encodeText(this
									.getDatabaseDAO().getDBName(vmuuid)));
							break;
						case 3:
							jo.put("depenName", Utilities.encodeText(this
									.getRouterDAO().getRouterName(vmuuid)));
							break;
						default:
							break;
						}
						jo.put("isused", true);
					} else {
						jo.put("depenType", "");
						jo.put("eipDepen", "");
						jo.put("depenName", "");
						jo.put("isused", false);
					}
					jo.put("eipId", eip.getEipUuid());
					jo.put("eipBandwidth", eip.getEipBandwidth());
					String timeUsed = Utilities.encodeText(Utilities
							.dateToUsed(eip.getCreateDate()));
					jo.put("createDate", timeUsed);
					ja.put(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}

	public void eipDelete(String eipIp, int uid) {
		boolean result = false;
		Date startTime = new Date();
		EIP eip = new EIP();
		try {
			eip = this.getEipDAO().getEip(eipIp);
			Date endDate = new Date();
			String eipUuid = eip.getEipUuid();
			this.getEipDAO().abandonEip(eipIp, uid);
			this.getFeeDAO().abandonEip(endDate, eipUuid);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.公网IP.toString(), eip.getEipIp()));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToError(log.toString()));
		}
	}

	public JSONObject eipBind(int uid, String vmUuid, String eipIp,
			String bindtype) {
		Date startTime = new Date();
		JSONObject jo = bindElasticIp(uid, vmUuid, eipIp, bindtype);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.公网IP.toString(), eipIp));
		if (bindtype.equals("vm")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.主机.toString(),
					"i-" + vmUuid.substring(0, 8)));
		} else if (bindtype.equals("lb")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.负载均衡.toString(),
					"lb-" + vmUuid.substring(0, 8)));
		} else if (bindtype.equals("rt")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.路由器.toString(),
					"rt-" + vmUuid.substring(0, 8)));
		} else if (bindtype.equals("db")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.数据库.toString(),
					"db-" + vmUuid.substring(0, 8)));
		}
		if (jo.getBoolean("result")) {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.绑定.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.绑定.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject eipUnbind(int uid, String vmUuid, String eipIp,
			String bindtype) {
		Date startTime = new Date();
		JSONObject jo = unbindElasticIp(uid, vmUuid, eipIp, bindtype);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.公网IP.toString(), eipIp));
		if (bindtype.equals("vm")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.主机.toString(),
					"i-" + vmUuid.substring(0, 8)));
		} else if (bindtype.equals("lb")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.负载均衡.toString(),
					"lb-" + vmUuid.substring(0, 8)));
		} else if (bindtype.equals("db")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.数据库.toString(),
					"db-" + vmUuid.substring(0, 8)));
		} else if (bindtype.equals("rt")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.路由器.toString(),
					"rt-" + vmUuid.substring(0, 8)));
		}
		if (jo.getBoolean("result") == true) {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.解绑.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(uid,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.解绑.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(uid,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject bindElasticIp(int userId, String uuid, String eipIp,
			String type) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			String ip = null;
			String firewallId = null;
			String name = null;
			int bindtype = 0;
			String eif = this.getEipDAO().getEip(eipIp).getEipInterface();
			if (type.equals("vm")) {
				OCVM vm = this.getVmDAO().getVM(uuid);
				ip = vm.getVmIP();
				firewallId = vm.getVmFirewall();
				name = vm.getVmName();
				bindtype = 0;
			} else if (type.equals("lb")) {
				LB lb = this.getLbDAO().getLB(uuid);
				ip = lb.getLbIP();
				firewallId = lb.getFirewallUuid();
				name = lb.getLbName();
				bindtype = 1;
			} else if (type.equals("rt")) {
				Router rt = this.getRouterDAO().getRouter(uuid);
				ip = rt.getRouterIP();
				firewallId = rt.getFirewallUuid();
				name = rt.getRouterName();
				bindtype = 3;
			}
			Connection c = this.getConstant().getConnection(userId);
			logger.info("Bind EIP: UUID [" + uuid + "] IP [" + ip + "] + EIP ["
					+ eipIp + "] Ethernet [" + eif + "]");
			boolean activeResult = this.getFirewallManager().activeFirewall(c,
					userId, ip, firewallId);
			logger.info("Active Firewall: " + activeResult);
			if (activeResult) {
				boolean bindResult = Host.bindOuterIp(c, ip, eipIp, eif);
				logger.info("Bind Result: " + bindResult);
				if (bindResult) {
					EIP eip = this.getEipDAO().getEip(eipIp);
					this.changeBandwidth(userId, eip.getEipBandwidth(), ip);
					this.getEipDAO().bindEip(eipIp, uuid, bindtype);
					result.put("result", true);
					result.put("rsUuid", uuid);
					result.put("rsName", Utilities.encodeText(name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean changeBandwidth(int userId, int size, String ip) {
		boolean result = false;
		try {
			Connection c = this.getConstant().getConnection(userId);
			if (ip != null) {
				result = Host.addIOLimit(c, ip, String.valueOf(size));
				logger.info("Change Bandwidth: IP [" + ip + "] Bandwidth ["
						+ size + " Mbps]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject unbindElasticIp(int userId, String uuid, String eipIp,
			String type) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			String ip = null;
			String eif = this.getEipDAO().getEip(eipIp).getEipInterface();
			if (type.equals("vm")) {
				OCVM vm = this.getVmDAO().getVM(uuid);
				ip = vm.getVmIP();
			} else if (type.equals("lb")) {
				LB lb = this.getLbDAO().getLB(uuid);
				ip = lb.getLbIP();
			} else if (type.equals("db")) {
				Database db = this.getDatabaseDAO().getDatabase(uuid);
				ip = db.getDatabaseIp();
			} else if (type.equals("rt")) {
				Router rt = this.getRouterDAO().getRouter(uuid);
				ip = rt.getRouterIP();
			}
			Connection c = this.getConstant().getConnection(userId);
			boolean deActiveResult = this.getFirewallManager()
					.deActiveFirewall(c, userId, ip);
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

	public JSONArray getAvailableVMs(int page, int limit, String searchStr,
			String bindtype, int uid) {
		JSONArray ja = new JSONArray();
		if (bindtype.equals("vm")) {
			List<OCVM> VMList = this.getVmDAO().getOnePageVMsWithoutEIP(page,
					limit, searchStr, uid);
			int totalNum = this.getVmDAO().countVMsWithoutEIP(searchStr, uid);
			ja.put(totalNum);
			if (VMList != null) {
				for (int i = 0; i < VMList.size(); i++) {
					JSONObject jo = new JSONObject();
					OCVM ocvm = VMList.get(i);
					jo.put("vmid", ocvm.getVmUuid());
					jo.put("vmname", Utilities.encodeText(ocvm.getVmName()));
					ja.put(jo);
				}
			}
		} else if (bindtype.equals("lb")) {
			List<LB> LBList = this.getLbDAO().getOnePageLBsWithoutEip(page,
					limit, searchStr, uid);
			int totalNum = this.getLbDAO().countLBsWithoutEIP(searchStr, uid);
			ja.put(totalNum);
			if (LBList != null) {
				for (int i = 0; i < LBList.size(); i++) {
					JSONObject jo = new JSONObject();
					LB lb = LBList.get(i);
					jo.put("vmid", lb.getLbUuid());
					jo.put("vmname", Utilities.encodeText(lb.getLbName()));
					ja.put(jo);
				}
			}
		} else if (bindtype.equals("rt")) {
			List<Router> routerList = this.getRouterDAO()
					.getOnePageRoutersWithoutEip(page, limit, searchStr, uid);
			int totalNum = this.getRouterDAO().countRoutersWithoutEIP(
					searchStr, uid);
			ja.put(totalNum);
			if (routerList != null) {
				for (int i = 0; i < routerList.size(); i++) {
					JSONObject jo = new JSONObject();
					Router rt = routerList.get(i);
					jo.put("vmid", rt.getRouterUuid());
					jo.put("vmname", Utilities.encodeText(rt.getRouterName()));
					ja.put(jo);
				}
			}
		} else if (bindtype.equals("db")) {
			List<Database> routerList = this.getDatabaseDAO()
					.getOnePageDatabasesWithoutEip(page, limit, searchStr, uid);
			int totalNum = this.getDatabaseDAO().countDatabasesWithoutEIP(
					searchStr, uid);
			ja.put(totalNum);
			if (routerList != null) {
				for (int i = 0; i < routerList.size(); i++) {
					JSONObject jo = new JSONObject();
					Database db = routerList.get(i);
					jo.put("vmid", db.getDatabaseUuid());
					jo.put("vmname", Utilities.encodeText(db.getDatabaseName()));
					ja.put(jo);
				}
			}
		}
		return ja;
	}

	public JSONObject eipBandwidth(String eip, int size, int userId) {
		Date startTime = new Date();
		EIP eipObj = this.getEipDAO().getEip(eip);
		JSONObject jo = new JSONObject();
		if (eipObj != null) {
			boolean limitResult = true;
			if (eipObj.getEipDependency() != null) {
				String uuid = eipObj.getEipDependency();
				int type = eipObj.getDepenType();
				logger.info("Change Bandwidth: ResourceUuid [" + uuid + "] ResourceType [" + type + "]");
				String ip = null;
				if (type == 0) {
					ip = this.getVmDAO().getAliveVM(uuid).getVmIP();
				} else if (type == 1) {
					ip = this.getLbDAO().getAliveLB(uuid).getLbIP();
				} else if (type == 3) {
					ip = this.getRouterDAO().getAliveRouter(uuid).getRouterIP();
				}
				limitResult = this.changeBandwidth(userId, size, ip);
			}
			if (limitResult == true) {
				boolean cr = this.getEipDAO().changeBandwidth(userId, eipObj,
						size);
				if (cr) {
					Date endDate = new Date();
					String eipUuid = eipObj.getEipUuid();
					String eipName = eipObj.getEipName();
					this.getFeeDAO().abandonEip(endDate, eipUuid);
					this.getFeeDAO().insertFeeEip(userId, endDate,
							Utilities.AddMinuteForDate(endDate, 60),
							size * Constant.EIP_PRICE, 1, eipUuid, eipName);
					jo.put("result", true);
				} else {
					jo.put("result", false);
				}
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.公网IP.toString(), eip));
		if (jo.getBoolean("result") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.公网带宽.ordinal(),
					LogConstant.logAction.调整.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.公网带宽.ordinal(),
					LogConstant.logAction.调整.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject getBasicList(String eipIp) {
		EIP eip = this.getEipDAO().getEip(eipIp);
		JSONObject jo = new JSONObject();
		jo.put("eipName", Utilities.encodeText(eip.getEipName()));
		jo.put("eipUuid", eip.getEipUuid());
		jo.put("eipIp", eip.getEipIp());
		jo.put("eipUID", eip.getEipUID());
		jo.put("eipDependency", (null == eip.getEipDependency()) ? "&nbsp;"
				: eip.getEipDependency());
		jo.put("dependType",
				(null == eip.getDepenType()) ? "&nbsp;" : eip.getDepenType());
		jo.put("eipDescription", (null == eip.getEipDescription()) ? "&nbsp;"
				: Utilities.encodeText(eip.getEipDescription()));
		jo.put("eipBandwidth", eip.getEipBandwidth());
		jo.put("createDate", Utilities.formatTime(eip.getCreateDate()));
		String timeUsed = Utilities.encodeText(Utilities.dateToUsed(eip
				.getCreateDate()));
		jo.put("eipType", eip.getEipType());
		jo.put("useDate", timeUsed);
		return jo;
	}

	public JSONArray getAvailableEIPs(int uid) {
		List<EIP> eiplist = this.getEipDAO().getableeips(uid);
		JSONArray ja = new JSONArray();
		for (EIP eip : eiplist) {
			JSONObject jo = new JSONObject();
			String eipname = eip.getEipName();
			jo.put("eipName",
					Utilities.encodeText(eipname == null ? "" : eipname));
			jo.put("eipUuid", eip.getEipUuid());
			jo.put("eipIp", eip.getEipIp());
			ja.put(jo);
		}
		return ja;
	}

	public String getQuota(int userId, int count, int size) {
		String result = "ok";
		Quota qt = this.getQuotaDAO().getQuotaTotal(userId);
		Quota qu = this.getQuotaDAO().getQuotaUsed(userId);
		int freeC = qt.getQuotaIP() - qu.getQuotaIP();
		int freeS = qt.getQuotaBandwidth() - qu.getQuotaBandwidth();
		if (freeC < count || freeS < size) {
			result = freeC + ":" + freeS;
		}
		return result;
	}
}
