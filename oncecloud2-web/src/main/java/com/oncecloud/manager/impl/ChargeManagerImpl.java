package com.oncecloud.manager.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.ChargeDAO;
import com.oncecloud.entity.ChargeRecord;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.ChargeManager;

@Component("ChargeManager")
public class ChargeManagerImpl implements ChargeManager {
	private ChargeDAO chargeDAO;

	private ChargeDAO getChargeDAO() {
		return chargeDAO;
	}

	@Autowired
	private void setChargeDAO(ChargeDAO chargeDAO) {
		this.chargeDAO = chargeDAO;
	}

	public JSONArray getChargeList(int userId, int page, int limit) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getChargeDAO().countChargeRecord(userId);
		ja.put(totalNum);
		List<ChargeRecord> crList = this.getChargeDAO().getOnePageChargeRecord(
				page, limit, userId);
		if (crList != null) {
			for (ChargeRecord cr : crList) {
				JSONObject tObj = new JSONObject();
				tObj.put("recordbill", cr.getRecordBill());
				tObj.put("recordtype", cr.getRecordType());
				tObj.put("recorddate", Utilities.formatTime(cr.getRecordDate()));
				ja.put(tObj);
			}
		}
		return ja;
	}
}
