package com.oncecloud.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.EIP;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.User;
import com.oncecloud.entity.Volume;
import com.oncecloud.helper.HashHelper;
import com.oncecloud.manager.UserManager;

@Component("UserManager")
public class UserManagerImpl implements UserManager{
	private static Logger logger = Logger.getLogger(UserManager.class);

	private UserDAO userDAO;
	private LogDAO logDAO;
	
	private HashHelper hashHelper;
	
	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private HashHelper getHashHelper() {
		return hashHelper;
	}

	@Autowired
	private void setHashHelper(HashHelper hashHelper) {
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

}
