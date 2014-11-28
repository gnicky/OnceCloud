package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface FeeManager {
	/**
	 * 获取用户计费列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @param type
	 * @return
	 */
	public abstract JSONArray getFeeList(int userId, int page, int limit,
			String search, String type);

	/**
	 * 获得资源计费详细列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param type
	 * @param uuid
	 * @param userId
	 * @return
	 */
	public abstract JSONArray getDetailList(int page, int limit, String search,
			String type, String uuid, int userId);

	/**
	 * 用户计费清单
	 * 
	 * @param userId
	 * @return
	 */
	public abstract JSONObject getFeeSummary(int userId);

	/**
	 * 用户计费查询
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @param type
	 * @param month
	 * @return
	 */
	public abstract JSONArray getQueryList(int userId, int page, int limit,
			String search, String type, String month);

}
