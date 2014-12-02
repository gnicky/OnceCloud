package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface FirewallManager {

	public abstract JSONObject updateFirewall(int userId, String firewallId);

	//cyh 只用于更新路由器内部的防火墙  过滤器
	public abstract JSONObject updateFirewallForinnerFirewall(int userId,
			String firewallId);

//	public abstract boolean activeFirewall(Connection c, int userId, String ip,
//			String firewallId);
//
//	public abstract boolean deActiveFirewall(Connection c, int userId, String ip);

	public abstract JSONObject bindFirewall(int userId, String vmArray,
			String firewallId, String bindType);

	public abstract JSONObject createFirewall(int userId, String firewallName,
			String firewallUuid);

	public abstract JSONArray getFirewallList(int userId, int page, int limit,
			String search);

	public abstract JSONObject getBasicList(int userId, String firewallId);

	public abstract JSONObject createRule(String ruleId, String ruleName,
			int rulePriority, String ruleProtocol, String ruleIp,
			String ruleFirewall, int ruleSport, int ruleEport);

	public abstract JSONArray getRuleList(int page, int limit, String search,
			String firewallId);

	public abstract JSONObject deleteRule(String ruleId, String firewallId);

	public abstract JSONObject operateRule(String ruleId);

	public abstract JSONObject deleteFirewall(int userId, String firewallId);

	public abstract String getQuota(int userId, int count);

	public abstract JSONArray getAvailableFirewalls(int userId);

}
