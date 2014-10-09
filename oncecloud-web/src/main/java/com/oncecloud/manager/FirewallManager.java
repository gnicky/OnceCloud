package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.Firewall;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.Rule;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author hehai
 * @version 2014/06/27
 */
@Component
public class FirewallManager {
	private FirewallDAO firewallDAO;
	private EIPDAO eipDAO;
	private LogDAO logDAO;
	private VMDAO vmDAO;
	private LBDAO lbDAO;
	private RouterDAO routerDAO;
	private QuotaDAO quotaDAO;
	private Constant constant;
	private MessagePush messagePush;

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
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

	public JSONObject updateFirewall(int userId, String firewallId) {
		JSONObject obj = new JSONObject();
		boolean result = false;
		Date startTime = new Date();
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.防火墙.toString(),
				"fw-" + firewallId.substring(0, 8)));
		List<Object> rsList = this.getFirewallDAO().getRSListOfFirewall(
				firewallId);
		if (rsList != null && rsList.size() != 0) {
			JSONObject total = new JSONObject();
			JSONArray ipArray = new JSONArray();
			String addr = "";
			for (int i = 0; i < rsList.size(); i++) {
				Object[] values = (Object[]) rsList.get(i);
				String rsUuid = values[0].toString();
				String rsIP = values[1].toString();
				if (i != rsList.size() - 1) {
					addr += rsIP + "， ";
				} else {
					addr += rsIP;
				}
				if (this.getEipDAO().getEipIp(rsUuid) != null) {
					ipArray.put(rsIP);
				}
			}
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.地址.toString(), addr));
			if (ipArray.length() > 0) {
				JSONArray ruleArray = new JSONArray();
				List<Rule> ruleList = this.getFirewallDAO().getRuleList(
						firewallId);
				if (ruleList != null) {
					for (int j = 0; j < ruleList.size(); j++) {
						Rule rule = ruleList.get(j);
						JSONObject ruleObject = new JSONObject();
						String protocol = rule.getRuleProtocol().toLowerCase();
						ruleObject.put("protocol", protocol);
						if (!protocol.equals("icmp")) {
							ruleObject
									.put("startPort", rule.getRuleStartPort());
							ruleObject.put("endPort", rule.getRuleEndPort());
						}
						ruleObject.put("IP", rule.getRuleIp());
						ruleArray.put(ruleObject);
					}
				}
				total.put("IP", ipArray);
				total.put("rules", ruleArray);
				Connection c = this.getConstant().getConnection(userId);
				boolean applyResult = false;
				try {
					applyResult = Host.firewallApplyRule(c, total.toString(), null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (applyResult == true) {
					this.getFirewallDAO().updateConfirm(firewallId, 1);
					result = true;
				}
			} else {
				this.getFirewallDAO().updateConfirm(firewallId, 1);
				result = true;
			}
		} else {
			this.getFirewallDAO().updateConfirm(firewallId, 1);
			result = true;
		}
		obj.put("result", result);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.防火墙.ordinal(),
					LogConstant.logAction.更新.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.防火墙.ordinal(),
					LogConstant.logAction.更新.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return obj;
	}

	public boolean activeFirewall(Connection c, int userId, String ip,
			String firewallId) {
		boolean result = false;
		try {
			JSONObject total = new JSONObject();
			JSONArray ipArray = new JSONArray();
			ipArray.put(ip);
			JSONArray ruleArray = new JSONArray();
			List<Rule> ruleList = this.getFirewallDAO().getRuleList(firewallId);
			if (ruleList != null) {
				for (int j = 0; j < ruleList.size(); j++) {
					Rule rule = ruleList.get(j);
					JSONObject ruleObject = new JSONObject();
					String protocol = rule.getRuleProtocol().toLowerCase();
					ruleObject.put("protocol", protocol);
					if (!protocol.equals("icmp")) {
						ruleObject.put("startPort", rule.getRuleStartPort());
						ruleObject.put("endPort", rule.getRuleEndPort());
					}
					ruleObject.put("IP", rule.getRuleIp());
					ruleArray.put(ruleObject);
				}
			}
			total.put("IP", ipArray);
			total.put("rules", ruleArray);
			result = Host.firewallApplyRule(c, total.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean deActiveFirewall(Connection c, int userId, String ip) {
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

	public JSONObject bindFirewall(int userId, String vmArray,
			String firewallId, String bindType) {
		JSONObject jo = new JSONObject();
		boolean result = false;
		Date startTime = new Date();
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.防火墙.toString(),
				"fw-" + firewallId.substring(0, 8)));
		JSONObject total = new JSONObject();
		JSONArray ipArray = new JSONArray();
		JSONArray jArray = new JSONArray(vmArray);
		String addr = "";
		if (bindType.equals("vm")) {
			for (int i = 0; i < jArray.length(); i++) {
				String vmUuid = jArray.getString(i);
				OCVM vm = this.getVmDAO().getVM(vmUuid);
				if (vm != null) {
					if (i != jArray.length() - 1) {
						addr += vm.getVmIP() + "， ";
					} else {
						addr += vm.getVmIP();
					}
					this.getVmDAO().updateFirewall(vmUuid, firewallId);
					if (this.getEipDAO().getEipIp(vmUuid) != null) {
						ipArray.put(vm.getVmIP());
					}
				}
			}
		} else if (bindType.equals("lb")) {
			for (int i = 0; i < jArray.length(); i++) {
				String lbUuid = jArray.getString(i);
				LB lb = this.getLbDAO().getLB(lbUuid);
				if (lb != null) {
					if (i != jArray.length() - 1) {
						addr += lb.getLbIP() + "， ";
					} else {
						addr += lb.getLbIP();
					}
					this.getLbDAO().updateFirewall(lbUuid, firewallId);
					if (this.getEipDAO().getEipIp(lbUuid) != null) {
						ipArray.put(lb.getLbIP());
					}
				}
			}
		} else if (bindType.equals("rt")) {
			for (int i = 0; i < jArray.length(); i++) {
				String rtUuid = jArray.getString(i);
				Router rt = this.getRouterDAO().getRouter(rtUuid);
				if (rt != null) {
					if (i != jArray.length() - 1) {
						addr += rt.getRouterIP() + "， ";
					} else {
						addr += rt.getRouterIP();
					}
					this.getRouterDAO().updateFirewall(rtUuid, firewallId);
					if (this.getEipDAO().getEipIp(rtUuid) != null) {
						ipArray.put(rt.getRouterIP());
					}
				}
			}
		}
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.地址.toString(), addr));
		if (ipArray.length() > 0) {
			JSONArray ruleArray = new JSONArray();
			List<Rule> ruleList = this.getFirewallDAO().getRuleList(firewallId);
			if (ruleList != null) {
				for (int j = 0; j < ruleList.size(); j++) {
					Rule rule = ruleList.get(j);
					JSONObject ruleObject = new JSONObject();
					String protocol = rule.getRuleProtocol().toLowerCase();
					ruleObject.put("protocol", protocol);
					if (!protocol.equals("icmp")) {
						ruleObject.put("startPort", rule.getRuleStartPort());
						ruleObject.put("endPort", rule.getRuleEndPort());
					}
					ruleObject.put("IP", rule.getRuleIp());
					ruleArray.put(ruleObject);
				}
			}
			total.put("IP", ipArray);
			total.put("rules", ruleArray);
			Connection c = this.getConstant().getConnection(userId);
			try {
				result = Host.firewallApplyRule(c, total.toString(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result == false) {
				this.getFirewallDAO().updateConfirm(firewallId, 0);
			}
		} else {
			result = true;
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		jo.put("isSuccess", result);
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.防火墙.ordinal(),
					LogConstant.logAction.绑定.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.防火墙.ordinal(),
					LogConstant.logAction.绑定.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject createFirewall(int userId, String firewallName,
			String firewallUuid) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		this.getFirewallDAO().insertFirewall(firewallUuid, firewallName,
				userId, startTime);
		jo.put("isSuccess", true);
		jo.put("createDate", Utilities.formatTime(startTime));
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.防火墙.toString(),
				"fw-" + firewallUuid.substring(0, 8)));
		OCLog log = this.getLogDAO().insertLog(userId,
				LogConstant.logObject.防火墙.ordinal(),
				LogConstant.logAction.创建.ordinal(),
				LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
				startTime, elapse);
		this.getMessagePush().pushMessage(userId,
				Utilities.stickyToSuccess(log.toString()));
		return jo;
	}

	public JSONArray getFirewallList(int userId, int page, int limit,
			String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getFirewallDAO().countAllFirewallList(search,
				userId);
		ja.put(totalNum);
		List<Firewall> firewallList = this.getFirewallDAO()
				.getOnePageFirewallList(page, limit, search, userId);
		if (firewallList != null) {
			for (int i = 0; i < firewallList.size(); i++) {
				JSONObject jo = new JSONObject();
				Firewall firewall = firewallList.get(i);
				String firewallId = firewall.getFirewallId();
				int ruleSize = this.getFirewallDAO().getRuleSize(firewallId);
				jo.put("firewallId", firewall.getFirewallId());
				jo.put("firewallName",
						Utilities.encodeText(firewall.getFirewallName()));
				jo.put("ruleSize", ruleSize);
				jo.put("createDate",
						Utilities.formatTime(firewall.getCreateDate()));
				jo.put("def", firewall.getIsDefault());
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject getBasicList(int userId, String firewallId) {
		JSONObject jo = new JSONObject();
		Firewall fw = this.getFirewallDAO().getFirewall(firewallId);
		if (fw != null) {
			jo.put("firewallName", Utilities.encodeText(fw.getFirewallName()));
			int ruleSize = this.getFirewallDAO().getRuleSize(firewallId);
			jo.put("ruleSize", ruleSize);
			jo.put("createDate", Utilities.formatTime(fw.getCreateDate()));
		}
		return jo;
	}

	public JSONObject createRule(String ruleId, String ruleName,
			int rulePriority, String ruleProtocol, String ruleIp,
			String ruleFirewall, int ruleSport, int ruleEport) {
		JSONObject jo = new JSONObject();
		this.getFirewallDAO().insertRule(ruleId, ruleName, rulePriority,
				ruleProtocol, ruleSport, ruleEport, 1, ruleIp, ruleFirewall);
		this.getFirewallDAO().updateConfirm(ruleFirewall, 0);
		jo.put("isSuccess", true);
		return jo;
	}

	public JSONArray getRuleList(int page, int limit, String search,
			String firewallId) {
		JSONArray ja = new JSONArray();
		JSONObject obj = new JSONObject();
		int totalNum = this.getFirewallDAO().countAllRuleList(search,
				firewallId);
		obj.put("total", totalNum);
		List<Rule> ruleList = this.getFirewallDAO().getOnePageRuleList(page,
				limit, search, firewallId);
		int isConfirm = this.getFirewallDAO().getFirewall(firewallId)
				.getIsConfirm();
		obj.put("confirm", isConfirm);
		ja.put(obj);
		if (ruleList != null) {
			for (int i = 0; i < ruleList.size(); i++) {
				JSONObject jo = new JSONObject();
				Rule rule = ruleList.get(i);
				jo.put("ruleId", rule.getRuleId());
				jo.put("ruleName", Utilities.encodeText(rule.getRuleName()));
				jo.put("rulePriority", rule.getRulePriority());
				jo.put("ruleProtocol", rule.getRuleProtocol());
				jo.put("ruleSport",
						rule.getRuleStartPort() == null ? "" : rule
								.getRuleStartPort());
				jo.put("ruleEport",
						rule.getRuleEndPort() == null ? "" : rule
								.getRuleEndPort());
				jo.put("ruleState", rule.getRuleState());
				jo.put("ruleIp", rule.getRuleIp());
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject deleteRule(String ruleId, String firewallId) {
		JSONObject jo = new JSONObject();
		this.getFirewallDAO().deleteRule(ruleId);
		this.getFirewallDAO().updateConfirm(firewallId, 0);
		jo.put("result", true);
		return jo;
	}

	public JSONObject operateRule(String ruleId) {
		JSONObject jo = new JSONObject();
		Rule rule = this.getFirewallDAO().getRule(ruleId);
		int ruleState = rule.getRuleState();
		if (ruleState == 1) {
			this.getFirewallDAO().updateRuleState(ruleId, 0);
			jo.put("isSuccess", true);
		} else {
			this.getFirewallDAO().updateRuleState(ruleId, 1);
			jo.put("isSuccess", true);
		}
		this.getFirewallDAO().updateConfirm(rule.getRuleFirewall(), 0);
		return jo;
	}

	public JSONObject deleteFirewall(int userId, String firewallId) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		List<Object> rsList = this.getFirewallDAO().getRSListOfFirewall(
				firewallId);
		if (rsList != null && rsList.size() > 0) {
			jo.put("result", false);
		} else {
			this.getFirewallDAO().deleteAllRuleOfFirewall(firewallId);
			this.getFirewallDAO().deleteFirewall(userId, firewallId);
			jo.put("result", true);
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.防火墙.toString(),
				"fw-" + firewallId.substring(0, 8)));
		if (jo.getBoolean("result") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.防火墙.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.防火墙.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public String getQuota(int userId, int count) {
		String result = "ok";
		int free = this.getQuotaDAO().getQuotaTotal(userId).getQuotaFirewall()
				- this.getQuotaDAO().getQuotaUsed(userId).getQuotaFirewall();
		if (free < count) {
			result = String.valueOf(free);
		}
		return result;
	}

	public JSONArray getAvailableFirewalls(int userId) {
		JSONArray ja = new JSONArray();
		List<Firewall> firewallList = this.getFirewallDAO().getabledfirewalls(
				userId);
		if (firewallList != null) {
			for (int i = 0; i < firewallList.size(); i++) {
				JSONObject jo = new JSONObject();
				Firewall firewall = firewallList.get(i);
				jo.put("firewallId", firewall.getFirewallId());
				jo.put("firewallName",
						Utilities.encodeText(firewall.getFirewallName()));
				ja.put(jo);
			}
		}
		return ja;
	}
}
