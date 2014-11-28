package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.LogDAO;

public interface AlarmLogManager {
	public abstract LogDAO getLogDAO();

	public abstract void setLogDAO(LogDAO logDAO);

	public abstract HostDAO getHostDAO();

	public abstract void setHostDAO(HostDAO hostDAO);

	public abstract void checkCPU();

	public abstract void checkIO();

	// /内存 只有物理机内存 Kb
	public abstract void checkMem();

	public abstract JSONArray getAlarmLogList(int userId, int userLevel,
			int page, int limit, String search);

	public abstract JSONObject deleteAlarmLog(Integer userId, String alarmLogId);

	public abstract JSONObject updateAlarmLog(Integer userId, String alarmLogId);

}
