package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.ChargeDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.manager.VoucherManager;

@Component("VoucherManager")
public class VoucherManagerImpl implements VoucherManager {
	private UserDAO userDAO;
	private ChargeDAO chargeDAO;

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

//	public JSONObject applyVoucher(int userId, int voucher) {
//		JSONObject jo = new JSONObject();
//		if (this.getUserDAO().applyVoucher(userId, voucher)) {
//			jo.put("result", true);
//		} else {
//			jo.put("result", false);
//		}
//		return jo;
//	}

	public ChargeDAO getChargeDAO() {
		return chargeDAO;
	}

	@Autowired
	public void setChargeDAO(ChargeDAO chargeDAO) {
		this.chargeDAO = chargeDAO;
	}

	public JSONObject confirmVoucher(int userId) {
		JSONObject jo = new JSONObject();
		int voucher = this.getUserDAO().getUser(userId).getUserVoucher();
		if (this.getUserDAO().confirmVoucher(userId)) {
			Date date = new Date();
			String uuid = UUID.randomUUID().toString();
			this.getChargeDAO().createChargeRecord(uuid, (double) voucher,
					1, date, userId, 1);
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
