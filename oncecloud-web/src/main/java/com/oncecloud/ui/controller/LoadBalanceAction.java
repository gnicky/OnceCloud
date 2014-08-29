package com.oncecloud.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.LBManager;
import com.oncecloud.ui.model.AdminListRequestModel;

@RequestMapping("/LBAction")
@Controller
public class LoadBalanceAction {
	private LBManager lbManager;

	public LBManager getLbManager() {
		return lbManager;
	}

	@Autowired
	public void setLbManager(LBManager lbManager) {
		this.lbManager = lbManager;
	}

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request,
			AdminListRequestModel alrModel) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getLbManager().getAdminLBList(
					alrModel.getPage(), alrModel.getLimit(),
					alrModel.getHost(), alrModel.getImportance(),
					alrModel.getType());
			return ja.toString();
		} else {
			return "";
		}
	}
}
