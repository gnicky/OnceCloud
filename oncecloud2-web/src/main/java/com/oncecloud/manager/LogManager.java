package com.oncecloud.manager;

import org.json.JSONArray;

public interface LogManager {

	public abstract JSONArray getLogList(int userId, int status, int start,
			int num);

}