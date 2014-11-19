package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.oncecloud.entity.Firewall;
import com.oncecloud.entity.Rule;

public interface FirewallDAO {
//	public abstract int countAllFirewallList(String search, int userId);
//
//	public abstract int countAllRuleList(String search, String firewallId);
//
//	public abstract void createDefaultFirewallNoTransaction(Integer userId)
//			throws Exception;
//
//	public abstract boolean deleteAllRuleOfFirewall(String firewallId);
//
//	public abstract boolean deleteFirewall(Integer userId, String firewallId);
//
//	public abstract boolean deleteRule(String ruleId);
//
//	// /获取可用防火墙
//	public abstract List<Firewall> getabledfirewalls(int uid);

	public abstract Firewall getDefaultFirewall(int userId);

//	public abstract Firewall getFirewall(String firewallId);
//
//	public abstract List<Firewall> getOnePageFirewallList(int page, int limit,
//			String search, int userId);
//
//	public abstract List<Rule> getOnePageRuleList(int page, int limit,
//			String search, String firewallId);
//
//	public abstract List<Object> getRSListOfFirewall(String firewallId);
//
//	public abstract List<Object> getRSListOfFirewallOnlyInnerRoute(
//			String firewallId);
//
//	public abstract Rule getRule(String ruleId);
//
//	public abstract List<Rule> getRuleList(String firewallId);
//
//	public abstract int getRuleSize(String firewallId);
//
//	public abstract JSONObject getSimpleFWList(int userId);
//
//	public abstract boolean insertFirewall(String firewallId,
//			String firewallName, int firewallUID, Date createDate);
//
//	// /cyh 插入用于路由器内部的防火墙 默认值状态 为2 ，在防火墙列表中，也不显示出来
//	public abstract boolean insertFirewallForinnerRoute(String firewallId,
//			String firewallName, int firewallUID, Date createDate);
//
//	public abstract boolean insertRule(String ruleId, String ruleName,
//			Integer rulePriority, String ruleProtocol, Integer ruleStartPort,
//			Integer ruleEndPort, Integer ruleState, String ruleIp,
//			String ruleFirewall);
//
//	public abstract boolean updateConfirm(String firewallId, int isConfirm);
//
//	public abstract boolean updateRuleState(String ruleId, int state);
}
