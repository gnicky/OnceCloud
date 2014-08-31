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
import com.oncecloud.manager.UserManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/UserAction")
@Controller
public class UserAction {
	private UserManager userManager;

	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@RequestMapping(value = "/UserList", method = { RequestMethod.GET })
	@ResponseBody
	public String userList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getUserManager().getUserList(list.getPage(),
					list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}

	@RequestMapping(value = "/Balance", method = { RequestMethod.GET })
	@ResponseBody
	public String balance(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONObject jo = this.getUserManager().getBalance(userId);
			return jo.toString();
		} else {
			return "";
		}
	}
}