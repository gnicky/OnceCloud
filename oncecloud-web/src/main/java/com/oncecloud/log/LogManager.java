package com.oncecloud.log;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.LogDAO;
import com.oncecloud.entity.OCLog;
import com.oncecloud.main.Utilities;

/**
 * @author hehai
 * @version 2014/08/25
 */
@Component
public class LogManager {
	private LogDAO logDAO;

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public JSONArray getLogList(int userId, int status, int start, int num) {
		JSONArray logArray = new JSONArray();
		List<OCLog> logList = this.getLogDAO().getLogList(userId, status,
				start, num);
		if (logList != null) {
			for (com.oncecloud.entity.OCLog log : logList) {
				JSONObject logObj = new JSONObject();
				logObj.put("logId", log.getLogId());
				logObj.put("logUID", log.getLogUID());
				int logObject = log.getLogObject();
				logObj.put("logObject", logObject);
				logObj.put("logObjectStr", Utilities
						.encodeText(LogConstant.logObject.values()[logObject]
								.toString()));
				int logAction = log.getLogAction();
				logObj.put("logActionStr", Utilities
						.encodeText(LogConstant.logAction.values()[logAction]
								.toString()));
				logObj.put("logStatus", log.getLogStatus());
				logObj.put("logInfo", log.getLogInfo());
				logObj.put("logTime", Utilities.formatTime(log.getLogTime()));
				logObj.put("logElapse", log.getLogElapse());
				logArray.put(logObj);
			}
		}
		return logArray;
	}
}
