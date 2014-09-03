package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.FeeManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/FeeAction")
@Controller
public class FeeAction {
	private FeeManager feeManager;

	public FeeManager getFeeManager() {
		return feeManager;
	}

	@Autowired
	public void setFeeManager(FeeManager feeManager) {
		this.feeManager = feeManager;
	}

	@RequestMapping(value = "/FeeList", method = { RequestMethod.GET })
	@ResponseBody
	public String feeList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getFeeManager().getFeeList(userId, list.getPage(),
				list.getLimit(), list.getSearch(), list.getType());
		return ja.toString();
	}

	@RequestMapping(value = "/QueryList", method = { RequestMethod.GET })
	@ResponseBody
	public String queryList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getFeeManager().getQueryList(userId,
				list.getPage(), list.getLimit(), list.getSearch(),
				list.getType(), list.getUuid());
		return ja.toString();
	}

	@RequestMapping(value = "/FeeSummary", method = { RequestMethod.GET })
	@ResponseBody
	public String feeSummary(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getFeeManager().getFeeSummary(userId);
		return jo.toString();
	}

	@RequestMapping(value = "/DetailList", method = { RequestMethod.GET })
	@ResponseBody
	public String detailList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getFeeManager().getDetailList(list.getPage(),
				list.getLimit(), list.getSearch(), list.getType(),
				list.getUuid(), userId);
		return ja.toString();
	}
}