package com.oncecloud.manager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.UserDAO;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class VoucherManager {
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

	public JSONObject confirmVoucher(int userId) {
		JSONObject jo = new JSONObject();
		if (this.getUserDAO().confirmVoucher(userId)) {
			jo.put("result", true);
		} else {
			jo.put("result", false);
		}
		return jo;
	}

	public JSONObject denyVoucher(int userId) {
		JSONObject jo = new JSONObject();
		if (this.getUserDAO().denyVoucher(userId)) {
			jo.put("result", true);
		} else {
			jo.put("result", false);
		}
		return jo;
	}
}
