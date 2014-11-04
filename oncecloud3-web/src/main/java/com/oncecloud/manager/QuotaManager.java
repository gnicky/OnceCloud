package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface QuotaManager {
	/**
	 * 获取用户问题列表
	 * 
	 * @param userId
	 * @param userLevel
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getQuestionList(int userId, int userLevel,
			int page, int limit, String search);

	public abstract JSONArray closeQuestion(int userId, int qaId);

	public abstract JSONObject createQuestion(int userId, String qaTitle,
			String qaContent);

	public abstract JSONArray reply(int userId, int userLevel, int qaTid,
			String qaContent);

	public abstract JSONArray getQuestionDetail(int qaId);

	public abstract JSONArray getReplyList(int qaId);
}
