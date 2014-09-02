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
import com.oncecloud.manager.UserManager;
import com.oncecloud.ui.model.ListModel;
import com.oncecloud.ui.model.QuotaModel;

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
		JSONArray ja = this.getUserManager().getUserList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Balance", method = { RequestMethod.GET })
	@ResponseBody
	public String balance(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getUserManager().getBalance(userId);
		return jo.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.GET })
	@ResponseBody
	public String delete(HttpServletRequest request,
			@RequestParam("userid") int changeId, @RequestParam String username) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getUserManager().doDeleteUser(userId, changeId,
				username);
		return jo.toString();
	}

	@RequestMapping(value = "/OneUser", method = { RequestMethod.GET })
	@ResponseBody
	public String oneUser(HttpServletRequest request,
			@RequestParam("userid") int userid) {
		JSONObject jo = this.getUserManager().doGetOneUser(userid);
		return jo.toString();
	}

	@RequestMapping(value = "/UserQuota", method = { RequestMethod.GET })
	@ResponseBody
	public String userQuota(HttpServletRequest request,
			@RequestParam("userid") int userid) {
		JSONObject jo = this.getUserManager().doGetUserQuota(userid);
		return jo.toString();
	}

	@RequestMapping(value = "/QuotaUpdate", method = { RequestMethod.POST })
	@ResponseBody
	public void quotaUpdate(HttpServletRequest request, QuotaModel list) {
		User user = (User) request.getSession().getAttribute("user");
		this.getUserManager().doQuotaUpdate(list.getQuotaid(),
				list.getChangerId(), list.getEip(), list.getVm(), list.getBk(),
				list.getImg(), list.getVol(), list.getSsh(), list.getFw(),
				list.getRt(), list.getVlan(), list.getLb(), list.getDisk(),
				list.getBw(), list.getMem(), list.getCpu(), user.getUserId());
	}
}