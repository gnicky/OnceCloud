package com.oncecloud.manager.impl;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.User;
import com.oncecloud.helper.HashHelper;
import com.oncecloud.manager.UserManager;

@Component("UserManager")
public class UserManagerImpl implements UserManager{
	private static Logger logger = Logger.getLogger(UserManager.class);

	private UserDAO userDAO;
	
	private HashHelper hashHelper;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public HashHelper getHashHelper() {
		return hashHelper;
	}

	@Autowired
	public void setHashHelper(HashHelper hashHelper) {
		this.hashHelper = hashHelper;
	}

	public int checkLogin(String userName, String userPass) {
		try {
			int result = 1;
			User user = this.getUserDAO().getUser(userName);
			if (user == null) {
				// User does not exist
				result = 1;
			} else {
				if (user.getUserStatus() == 0) {
					result = 1;
				} else {
					String pass = user.getUserPass();
					if (pass.equals(this.getHashHelper().md5Hash(userPass))) {
						// Validated
						result = 0;
					} else {
						// Password is wrong
						result = 1;
					}
				}
			}
			logger.info("Check Login: User [" + userName + "] Result ["
					+ result + "]");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public User getUser(String user) {
		return this.getUserDAO().getUser(user);
	}
	
/*	private User userRegister(String userName, String userPassword,
			String userEmail, String userTelephone, String userCompany,
			String uLevel, String poolUuid) {
		try {
			if (userName == null || userPassword == null) {
				logger.error("User Register: User [" + userName
						+ "] Failed: Null Exception");
				return null;
			}
			int userLevel = Integer.valueOf(uLevel);
			Date date = new Date();
			User user = this.getUserDAO().getUser(userName);
			if (user != null) {
				logger.error("User Register: User [" + userName
						+ "] Failed: Exist Yet");
				return null;
			}
			if (poolUuid.equals("0")){
				poolUuid = this.getPoolDAO().getRandomPool();
			}
			int userId = this.getUserDAO().insertUser(userName, userPassword, userEmail,
					userTelephone, userCompany, userLevel, date, poolUuid);
			this.getQuotaDAO().initQuota(userId);
			this.getFirewallDAO().createDefaultFirewall(userId);
			User check = this.getUserDAO().getUser(userName);
			if (check == null) {
				logger.error("User Register: User [" + userName
						+ "] Failed: Check Failed");
				return null;
			}
			logger.info("User Register: User [" + userName + "] Password ["
					+ userPassword + "] Mail [" + userEmail + "] Telephone ["
					+ userTelephone + "] Company [" + userCompany
					+ "] UserLevel [" + userLevel + "] Successful");
			return check;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public JSONArray doQueryUser(String userName) {
		JSONArray ja = new JSONArray();
		User query = this.getUserDAO().getUser(userName);
		JSONObject tObj = new JSONObject();
		if (query != null) {
			tObj.put("exist", true);
		} else {
			tObj.put("exist", false);
		}
		ja.put(tObj);
		return ja;
	}
	
	/**
	 * 获取用户余额
	 * 
	 * @param userId
	 * @return
	 */
	public JSONObject getBalance(int userId) {
		JSONObject jo = new JSONObject();
		User user = userDAO.getUser(userId);
		jo.put("balance", user.getUserBalance());
		return jo;
	}
}
