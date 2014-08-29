package com.oncecloud.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.DatacenterManager;

@RequestMapping("/DatacenterAction")
@Controller
public class DatacenterController {
	private DatacenterManager datacenterManager;

	private DatacenterManager getDatacenterManager() {
		return datacenterManager;
	}

	@Autowired
	private void setDatacenterManager(DatacenterManager datacenterManager) {
		this.datacenterManager = datacenterManager;
	}

	@RequestMapping(value = "/GetList", method = { RequestMethod.GET })
	@ResponseBody
	public String getList(HttpServletRequest request) {
		return "";
	}

	@RequestMapping(value = "/Overview", method = { RequestMethod.GET })
	@ResponseBody
	public String overview(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getDatacenterManager().getOverview();
			return ja.toString();
		} else {
			return "";
		}
	}
}
