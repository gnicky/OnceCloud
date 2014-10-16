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

	@RequestMapping(value = "/UserList", method = { RequestMethod.GET })
	@ResponseBody
	public String userList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getUserManager().getUserList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}
	

	@RequestMapping(value = "/Companylist", method = { RequestMethod.GET })
	@ResponseBody
	public String companylist(HttpServletRequest request) {
		JSONArray ja = this.getUserManager().doGetCompanyList();
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

	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public String create(HttpServletRequest request,
			@RequestParam String userName, @RequestParam String userPassword,
			@RequestParam String userEmail, @RequestParam String userTelephone,
			@RequestParam String userCompany, @RequestParam String userLevel,
			@RequestParam String poolUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getUserManager().doCreateUser(userName,
				userPassword, userEmail, userTelephone, userCompany, userLevel,
				user.getUserId(), poolUuid);
		return ja.toString();
	}

	@RequestMapping(value = "/Update", method = { RequestMethod.POST })
	@ResponseBody
	public void update(HttpServletRequest request,
			@RequestParam String userName, @RequestParam int changeId,
			@RequestParam String userEmail, @RequestParam String userTelephone,
			@RequestParam String userCompany, @RequestParam String userLevel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getUserManager().doUpdateUser(user.getUserId(),
				changeId, userName, userEmail, userTelephone, userCompany,
				userLevel);
	}

	@RequestMapping(value = "/QueryUser", method = { RequestMethod.GET })
	@ResponseBody
	public String queryUser(HttpServletRequest request, @RequestParam String userName) {
		JSONArray ja = this.getUserManager().doQueryUser(userName);
		return ja.toString();
	}

	@RequestMapping(value = "/Register", method = { RequestMethod.GET })
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
	}

	@RequestMapping(value = "/CompanyDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String companyDetail(HttpServletRequest request) {
		int companyuid = Integer.parseInt(request.getParameter("companyuid"));
		JSONArray ja = this.getUserManager().doGetcompanyDetail(companyuid);
		return ja.toString();
	}

}