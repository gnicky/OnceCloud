package com.oncecloud.manager.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.UserDAO;
import com.oncecloud.manager.VoucherManager;

@Component("VoucherManager")
public class VoucherManagerImpl implements VoucherManager {
	private UserDAO userDAO;

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public JSONObject applyVoucher(int userId, int voucher) {
		JSONObject jo = new JSONObject();
		if (this.getUserDAO().applyVoucher(userId, voucher)) {
			jo.put("result", true);
		} else {
			jo.put("result", false);
		}
		return jo;
	}

}
