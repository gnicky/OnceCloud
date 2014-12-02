package com.oncecloud.manager;

import org.json.JSONArray;

public interface ChargeManager {
	
	public abstract JSONArray getChargeList(int userId, int page, int limit);
	
}
