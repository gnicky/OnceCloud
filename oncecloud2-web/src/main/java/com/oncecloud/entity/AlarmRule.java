package com.oncecloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_rule_alarm")
public class AlarmRule {
	private String ruleAUuid;
	// 0主机cpu小于 1主机cpu大于 2主机内存小于 3主机内存大于 4主机磁盘小于 5主机磁盘大于 6主机内网进小于 7主机内网进大于
	// 8主机内网出小于 9主机内网出大于
	// 10公网ip外网进流量小于 11公网ip外网进流量大于 12公网ip外网出流量小于 13公网ip外网出流量大于
	// 20路由内网进流量小于 21路由内网进流量大于 22路由内网出流量小于 23路由内网出流量大于

	// 30负载HTTP请求数小于 31负载HTTP请求数大于 32负载HTTP并发数小于 33负载HTTP并发数大于 34负载HTTP响应时间小于
	// 35负载HTTP响应时间大于
	// 36负载HTTP 监听4** 小于 37负载HTTP 监听4** 大于 38负载HTTP 监听5** 小于 39负载HTTP 监听5** 大于
	// 40负载HTTP HTTP1** 小于 41负载HTTP HTTP1** 大于 42负载HTTP HTTP2** 小于 43负载HTTP
	// HTTP2** 大于 44负载HTTP HTTP3** 小于
	// 45负载HTTP HTTP3** 大于 46负载HTTP HTTP4** 小于 47负载HTTP HTTP4** 大于 48负载HTTP
	// HTTP5** 小于 49负载HTTP HTTP5** 大于

	// 50负载HTTP5请求数 小于 51负载HTTP5请求数 大于 52负载HTTP5并发数 小于 53负载HTTP5并发数 大于
	// 54负载HTTP5响应时间 小于 55负载HTTP5响应时间 大于
	// 56负载HTTP5 监听4** 小于 57负载HTTP5 监听4** 大于 58负载HTTP5 监听5** 小于 59负载HTTP5 监听5**
	// 大于
	// 60负载HTTP5 HTTP1** 小于 62负载HTTP5 HTTP2** 64负载HTTP5 HTTP3** 66负载HTTP5
	// HTTP4**
	// 61负载HTTP5 HTTP1** 大于 63负载HTTP5 HTTP2** 65负载HTTP5 HTTP3** 67负载HTTP5
	// HTTP4**
	// 68负载HTTP5 HTTP5**
	// 69负载HTTP5 HTTP5**

	// 70负载TCP并发数 72负载TCP连接数
	// 71负载TCP并发数 73负载TCP连接数

	// 80后端HTTP响应时间 82后端HTTP HTTP1** 84后端HTTP HTTP2** 86后端HTTP HTTP3** 88后端HTTP
	// HTTP4** 90后端HTTP HTTP5**
	// 81后端HTTP响应时间 83后端HTTP HTTP1** 85后端HTTP HTTP2** 87后端HTTP HTTP3** 89后端HTTP
	// HTTP4** 91后端HTTP HTTP5**

	// 100后端TCP连接数
	// 101后端TCP连接数
	private Integer ruleAType;
	private Integer ruleAThreshold;
	private Integer ruleAPeriod;
	private String ruleAAlarmUuid;

	public AlarmRule() {

	}

	public AlarmRule(String ruleAUuid, Integer ruleAType,
			Integer ruleAThreshold, String ruleAAlarmUuid, Integer ruleAPeriod) {
		this.ruleAAlarmUuid = ruleAAlarmUuid;
		this.ruleAThreshold = ruleAThreshold;
		this.ruleAType = ruleAType;
		this.ruleAUuid = ruleAUuid;
		this.ruleAPeriod = ruleAPeriod;
	}

	@Id
	@Column(name = "rule_alarm_uuid")
	public String getRuleAUuid() {
		return ruleAUuid;
	}

	public void setRuleAUuid(String ruleAUuid) {
		this.ruleAUuid = ruleAUuid;
	}

	@Column(name = "rule_alarm_type")
	public Integer getRuleAType() {
		return ruleAType;
	}

	public void setRuleAType(Integer ruleAType) {
		this.ruleAType = ruleAType;
	}

	@Column(name = "rule_alarm_threshold")
	public Integer getRuleAThreshold() {
		return ruleAThreshold;
	}

	public void setRuleAThreshold(Integer ruleAThreshold) {
		this.ruleAThreshold = ruleAThreshold;
	}

	@Column(name = "rule_alarm_alarmuuid")
	public String getRuleAAlarmUuid() {
		return ruleAAlarmUuid;
	}

	public void setRuleAAlarmUuid(String ruleAAlarmUuid) {
		this.ruleAAlarmUuid = ruleAAlarmUuid;
	}

	@Column(name = "rule_alarm_period")
	public Integer getRuleAPeriod() {
		return ruleAPeriod;
	}

	public void setRuleAPeriod(Integer ruleAPeriod) {
		this.ruleAPeriod = ruleAPeriod;
	}

	@Override
	public String toString() {
		return "AlarmRule [ruleAUuid=" + ruleAUuid + " ruleAType=" + ruleAType
				+ " ruleAThreshold=" + ruleAThreshold + " ruleAAlarmUuid="
				+ ruleAAlarmUuid + "ruleAPeriod=" + ruleAPeriod + "]";
	}
}
