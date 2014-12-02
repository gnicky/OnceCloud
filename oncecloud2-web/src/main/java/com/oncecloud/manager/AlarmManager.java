package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface AlarmManager {
	public abstract boolean saveAlarm(String alarmUuid, String alarmName,
			int alarmType, int alarmIsalarm, int alarmTouch, int alarmPeriod,
			String alarmRules, String alarmThreshold, int userId);

	public abstract boolean deleteAlarm(String uuid);

	public abstract void removeRS(String rsUuid, int type);

	public abstract JSONArray getResourceList(String alarmUuid, int page,
			int limit, String search, int uid);

	public abstract boolean addResourceToAlarm(int userId, String rsuuidStr,
			String alarmUuid);

	public abstract JSONObject getBasicInfo(String alarmUuid, int uid);

	public abstract JSONArray getRuleList(String alarmUuid, int page, int limit);

	/**
	 * 获取监控警告列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getAlarmList(int userId, int page, int limit,
			String search);

	public abstract void modifyRule(int ruletype, int rulethreshold,
			int ruleperiod, String ruleId);

	public abstract void addRule(String alarmUuid, int ruletype,
			int rulethreshold, int ruleperiod, String ruleId);

	public abstract JSONObject modifyPeriod(String alarmUuid, int period);

	public abstract void deleteRule(String ruleId);

	public abstract void modifyTouch(String alarmUuid, int alarmTouch,
			int alarmIsalarm);

}
