package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.VnetManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/VnetAction")
@Controller
public class VnetAction {
	private VnetManager vnetManager;

	public VnetManager getVnetManager() {
		return vnetManager;
	}

	@Autowired
	public void setVnetManager(VnetManager vnetManager) {
		this.vnetManager = vnetManager;
	}

	@RequestMapping(value = "/VnetList", method = { RequestMethod.GET })
	@ResponseBody
	public String vnetList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getVnetManager().getVnetList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/VnetDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String vnetDetail(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getVnetManager().getVnetDetail(uuid);
		return jo.toString();
	}
}
