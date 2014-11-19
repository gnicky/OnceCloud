package com.oncecloud.manager;

import org.json.JSONArray;

public interface OverviewManager {
	/**
	 * 获取管理员全局视图
	 * 
	 * @return
	 */
	public abstract JSONArray getOverview();
}
