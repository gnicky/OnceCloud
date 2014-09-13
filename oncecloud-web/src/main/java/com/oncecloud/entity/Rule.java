package com.oncecloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_rule")
public class Rule {
	private String ruleId;
	private String ruleName;
	private Integer rulePriority;
	private String ruleProtocol;
	private Integer ruleStartPort;
	private Integer ruleEndPort;
	private Integer ruleState;
	private String ruleIp;
	private String ruleFirewall;

	public Rule() {
	}

	public Rule(String ruleId, String ruleName, Integer rulePriority,
			String ruleProtocol, Integer ruleStartPort, Integer ruleEndPort,
			Integer ruleState, String ruleIp, String ruleFirewall) {
		this.ruleId = ruleId;
		this.ruleName = ruleName;
		this.rulePriority = rulePriority;
		this.ruleProtocol = ruleProtocol;
		this.ruleStartPort = ruleStartPort;
		this.ruleEndPort = ruleEndPort;
		this.ruleState = ruleState;
		this.ruleIp = ruleIp;
		this.ruleFirewall = ruleFirewall;
	}

	@Id
	@Column(name = "rule_id")
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "rule_name")
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@Column(name = "rule_priority")
	public Integer getRulePriority() {
		return rulePriority;
	}

	public void setRulePriority(Integer rulePriority) {
		this.rulePriority = rulePriority;
	}

	@Column(name = "rule_protocol")
	public String getRuleProtocol() {
		return ruleProtocol;
	}

	public void setRuleProtocol(String ruleProtocol) {
		this.ruleProtocol = ruleProtocol;
	}

	@Column(name = "rule_sport")
	public Integer getRuleStartPort() {
		return ruleStartPort;
	}

	public void setRuleStartPort(Integer ruleStartPort) {
		this.ruleStartPort = ruleStartPort;
	}

	@Column(name = "rule_eport")
	public Integer getRuleEndPort() {
		return ruleEndPort;
	}

	public void setRuleEndPort(Integer ruleEndPort) {
		this.ruleEndPort = ruleEndPort;
	}

	@Column(name = "rule_state")
	public Integer getRuleState() {
		return ruleState;
	}

	public void setRuleState(Integer ruleState) {
		this.ruleState = ruleState;
	}

	@Column(name = "rule_firewall")
	public String getRuleFirewall() {
		return ruleFirewall;
	}

	public void setRuleFirewall(String ruleFirewall) {
		this.ruleFirewall = ruleFirewall;
	}

	@Column(name = "rule_ip")
	public String getRuleIp() {
		return ruleIp;
	}

	public void setRuleIp(String ruleIp) {
		this.ruleIp = ruleIp;
	}

}
