package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.ChargeManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/ChargeAction")
@Controller
public class ChargeAction {
	private ChargeManager chargeManager;

	public ChargeManager getChargeManager() {
		return chargeManager;
	}

	@Autowired
	public void setChargeManager(ChargeManager chargeManager) {
		this.chargeManager = chargeManager;
	}

	@RequestMapping(value = "/ChargeList", method = { RequestMethod.GET })
	@ResponseBody
	public String alarmList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getChargeManager().getChargeList(userId,
				list.getPage(), list.getLimit());
		return ja.toString();
	}
}
