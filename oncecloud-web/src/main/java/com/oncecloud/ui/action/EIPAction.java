package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.EIPManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/EIPAction")
@Controller
public class EIPAction {
	private EIPManager eipManager;

	public EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	public void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	@RequestMapping(value = "/EIPList", method = { RequestMethod.GET })
	@ResponseBody
	public String eipList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getEipManager().getEIPList(userId, list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}
}
