package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.OCLog;

public interface LogDAO {

	public List<OCLog> getLogList(int logUID, int logStatus, int start, int num);

	public OCLog insertLog(int logUID, int logObject, int logAction,
			int logStatus, String logInfo, Date logTime, int logElapse);
}
