package com.oncecloud.ui.action;

import java.io.UnsupportedEncodingException;

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

/*	@RequestMapping(value = "/QueryUser", method = { RequestMethod.GET })
	@ResponseBody
	public String queryUser(HttpServletRequest request, @RequestParam String userName) {
		JSONArray ja = this.getUserManager().doQueryUser(userName);
		return ja.toString();
	}*/
	
	@RequestMapping(value = "/Balance", method = { RequestMethod.GET })
	@ResponseBody
	public String balance(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getUserManager().getBalance(userId);
		return jo.toString();
	}
	
/*	@RequestMapping(value = "/Register", method = { RequestMethod.GET })
	@ResponseBody
	public String register(HttpServletRequest request, @RequestParam String username
			, @RequestParam String userpwd, @RequestParam String useremail
			, @RequestParam String usertel) {
		JSONArray ja = new JSONArray();
		try {
			ja = this.getUserManager().doRegister(username, userpwd, useremail, usertel);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ja.toString();
	}*/

}