package com.oncecloud.ui.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.User;
import com.oncecloud.manager.UserManager;
import com.oncecloud.ui.model.LogOnRequestModel;

@Controller
public class AccountController {

	private UserManager userManager;
	private UserDAO userDAO;

	private UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	private void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView login(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, String> model = new HashMap<String, String>();
		model.put("basePath", basePath);
		return new ModelAndView("account/login", model);
	}

	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	@ResponseBody
	public ModelAndView login(HttpServletRequest request,
			LogOnRequestModel logOnRequestModel) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, String> model = new HashMap<String, String>();
		model.put("basePath", basePath);

		int result = -1;
		String captcha = (String) request.getSession().getAttribute("rand");
		// 会话存在过期现象
		if (logOnRequestModel.getVercode() == null || captcha == null) {
			return new ModelAndView("account/login", model);
		} else if (logOnRequestModel.getVercode().toLowerCase()
				.equals(captcha.toLowerCase())) {
			request.getSession().setAttribute("rand", null);
			if (logOnRequestModel.getUser() != null) {
				result = this.getUserManager().checkLogin(
						logOnRequestModel.getUser(),
						logOnRequestModel.getPassword());
			}
			if (result == 0) {
				User userlogin = this.getUserDAO().getUser(
						logOnRequestModel.getUser());
				int level = userlogin.getUserLevel();
				if (level != 0) {
					request.getSession().setAttribute("user", userlogin);
					return new ModelAndView(new RedirectView("/dashboard"));
				} else {
					return new ModelAndView("account/login", model);
				}
			} else {
				return new ModelAndView("account/login", model);
			}
		} else {
			return new ModelAndView("account/login", model);
		}
	}
	
	@RequestMapping(value = "/backdoor", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView backdoor(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, String> model = new HashMap<String, String>();
		model.put("basePath", basePath);
		return new ModelAndView("account/backdoor", model);
	}
	
	@RequestMapping(value = "/backdoor", method = { RequestMethod.POST })
	@ResponseBody
	public ModelAndView backdoor(HttpServletRequest request,
			LogOnRequestModel logOnRequestModel) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, String> model = new HashMap<String, String>();
		model.put("basePath", basePath);

		int result = -1;
		String captcha = (String) request.getSession().getAttribute("rand");
		// 会话存在过期现象
		if (logOnRequestModel.getVercode() == null || captcha == null) {
			return new ModelAndView("account/backdoor", model);
		} else if (logOnRequestModel.getVercode().toLowerCase()
				.equals(captcha.toLowerCase())) {
			request.getSession().setAttribute("rand", null);
			if (logOnRequestModel.getUser() != null) {
				result = this.getUserManager().checkLogin(
						logOnRequestModel.getUser(),
						logOnRequestModel.getPassword());
			}
			if (result == 0) {
				User userlogin = this.getUserDAO().getUser(
						logOnRequestModel.getUser());
				int level = userlogin.getUserLevel();
				if (level == 0) {
					request.getSession().setAttribute("user", userlogin);
					return new ModelAndView(new RedirectView("/dashboard"));
				} else {
					return new ModelAndView("account/backdoor", model);
				}
			} else {
				return new ModelAndView("account/backdoor", model);
			}
		} else {
			return new ModelAndView("account/backdoor", model);
		}
	}
}
