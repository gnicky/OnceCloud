package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.LBManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/LBAction")
@Controller
public class LBAction {
	private LBManager lbManager;

	public LBManager getLbManager() {
		return lbManager;
	}

	@Autowired
	public void setLbManager(LBManager lbManager) {
		this.lbManager = lbManager;
	}

	@RequestMapping(value = "/LBList", method = { RequestMethod.GET })
	@ResponseBody
	public String lbList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONArray ja = this.getLbManager().getLBList(userId,
					list.getPage(), list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
}