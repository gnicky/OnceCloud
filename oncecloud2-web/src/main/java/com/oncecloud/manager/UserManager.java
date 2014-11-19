package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.oncecloud.entity.User;

public interface UserManager {

	public abstract int checkLogin(String userName, String userPass);

	public abstract User getUser(String user);

	/**
	 * 获取用户余额
	 * 
	 * @param userId
	 * @return
	 */
	public abstract JSONObject getBalance(int userId);

//	public abstract User userRegister(String userName, String userPassword,
//			String userEmail, String userTelephone, String userCompany,
//			String uLevel, String poolUuid);
//
//	public abstract JSONArray doRegister(String userName, String userPassword,
//			String userEmail, String userTelephone)
//			throws UnsupportedEncodingException;

	public abstract JSONArray doQueryUser(String userName);

}