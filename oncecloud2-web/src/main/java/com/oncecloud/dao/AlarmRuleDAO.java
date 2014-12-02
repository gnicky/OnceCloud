package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.AlarmRule;

public interface AlarmRuleDAO {
	public abstract boolean addRule(String ruleAUuid, Integer ruleAType,
			Integer ruleAThreshold, String ruleAAlarmUuid, Integer ruleAPeriod);

	public abstract int countAlarmList(String ruleAAlarmUuid);

	public abstract List<AlarmRule> getOnePageList(int page, int limit,
			String ruleAAlarmUuid);

	public abstract boolean removeAlarmRule(AlarmRule alarmrule);

	public abstract boolean removeRules(String alarmUuid);

	public abstract boolean updateRule(String ruleAUuid, int ruleAPeriod,
			int ruleAThreshold, int ruleAType);
}
