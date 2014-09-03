package com.oncecloud.newapi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.main.Utilities;
import com.oncecloud.newentity.User;
import com.oncecloud.newservice.UserService;
import com.oncecloud.ui.model.ListModel;

@Controller
@RequestMapping("/New/UserAction")
public class NewUserController {
	private UserService userService;

	private UserService getUserService() {
		return userService;
	}

	@Autowired
	private void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/UserList", method = { RequestMethod.GET })
	@ResponseBody
	public List<Object> userList(HttpServletRequest request, ListModel list) {
		List<Object> result = new ArrayList<Object>();
		List<User> userList = this.getUserService().getUserList(list.getPage(),
				list.getLimit(), list.getSearch());
		if (userList == null) {
			result.add(Integer.valueOf(0));
			return result;
		}
		result.add(Integer.valueOf(userList.size()));
		for (User user : userList) {
			result.add(this.parseUserModel(user));
		}
		return result;
	}

	@RequestMapping(value = "/Balance", method = { RequestMethod.GET })
	@ResponseBody
	public BalanceModel balance(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		double balance = this.getUserService().getBalance(user.getId());
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setBalance(balance);
		return balanceModel;
	}

	// @RequestMapping(value = "/OneUser", method = { RequestMethod.GET })
	// @ResponseBody
	// public String oneUser(HttpServletRequest request,
	// @RequestParam("userid") int userid) {
	// JSONObject jo = this.getUserService().doGetOneUser(userid);
	// return jo.toString();
	// }

	//
	// @RequestMapping(value = "/Delete", method = { RequestMethod.GET })
	// @ResponseBody
	// public String delete(HttpServletRequest request,
	// @RequestParam("userid") int changeId, @RequestParam String username) {
	// User user = (User) request.getSession().getAttribute("user");
	// int userId = user.getUserId();
	// JSONObject jo = this.getUserService().doDeleteUser(userId, changeId,
	// username);
	// return jo.toString();
	// }
	//
	// @RequestMapping(value = "/UserQuota", method = { RequestMethod.GET })
	// @ResponseBody
	// public String userQuota(HttpServletRequest request,
	// @RequestParam("userid") int userid) {
	// JSONObject jo = this.getUserService().doGetUserQuota(userid);
	// return jo.toString();
	// }
	//
	// @RequestMapping(value = "/QuotaUpdate", method = { RequestMethod.POST })
	// @ResponseBody
	// public void quotaUpdate(HttpServletRequest request, QuotaModel list) {
	// User user = (User) request.getSession().getAttribute("user");
	// this.getUserService().doQuotaUpdate(list.getQuotaid(),
	// list.getChangerId(), list.getEip(), list.getVm(), list.getBk(),
	// list.getImg(), list.getVol(), list.getSsh(), list.getFw(),
	// list.getRt(), list.getVlan(), list.getLb(), list.getDisk(),
	// list.getBw(), list.getMem(), list.getCpu(), user.getUserId());
	// }
	//
	// @RequestMapping(value = "/Create", method = { RequestMethod.POST })
	// @ResponseBody
	// public String create(HttpServletRequest request,
	// @RequestParam String userName, @RequestParam String userPassword,
	// @RequestParam String userEmail, @RequestParam String userTelephone,
	// @RequestParam String userCompany, @RequestParam String userLevel) {
	// User user = (User) request.getSession().getAttribute("user");
	// JSONArray ja = this.getUserService().doCreateUser(userName,
	// userPassword, userEmail, userTelephone, userCompany, userLevel,
	// user.getUserId());
	// return ja.toString();
	// }
	//
	// @RequestMapping(value = "/Update", method = { RequestMethod.POST })
	// @ResponseBody
	// public void update(HttpServletRequest request,
	// @RequestParam String userName, @RequestParam int changeId,
	// @RequestParam String userEmail, @RequestParam String userTelephone,
	// @RequestParam String userCompany, @RequestParam String userLevel) {
	// User user = (User) request.getSession().getAttribute("user");
	// this.getUserService().doUpdateUser(user.getUserId(), changeId,
	// userName, userEmail, userTelephone, userCompany, userLevel);
	// }
	//
	// @RequestMapping(value = "/QueryUser", method = { RequestMethod.GET })
	// @ResponseBody
	// public String queryUser(HttpServletRequest request,
	// @RequestParam String userName) {
	// JSONArray ja = this.getUserService().doQueryUser(userName);
	// return ja.toString();
	// }

	private UserModel parseUserModel(User user) {
		UserModel userModel = new UserModel();
		userModel.setUsername(Utilities.encodeText(user.getUserName()));
		userModel.setUserid(user.getId());
		userModel.setUsercom(user.getCompany());
		userModel.setUserdate(Utilities.formatTime(user.getCreateDate()));
		userModel.setUserlevel(user.getLevel().ordinal());
		userModel.setUsermail(user.getEmail());
		userModel.setUserphone(user.getTelephone());
		if (user.getVoucher() == null) {
			userModel.setUservoucher(0);
		} else {
			userModel.setUservoucher(user.getVoucher());
		}
		return userModel;
	}
}
