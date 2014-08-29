package com.oncecloud.ui.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;
import com.oncecloud.manager.QuotaManager;

@Controller
public class DashboardController {
	private QuotaManager quotaManager;

	private QuotaManager getQuotaManager() {
		return quotaManager;
	}

	@Autowired
	private void setQuotaManager(QuotaManager quotaManager) {
		this.quotaManager = quotaManager;
	}

	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		Quota quotaUsed = this.getQuotaManager().getQuotaUsed(user.getUserId());
		model.put("basePath", basePath);
		model.put("title", "控制台");
		model.put("sideActive", 0);
		model.put("user", user);
		model.put("quotaUsed", quotaUsed);
		return new ModelAndView("user/dashboard", model);
	}

	@RequestMapping(value = "/user/modal/viewquota")
	public ModelAndView viewQuota(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		Quota quotaUsed = this.getQuotaManager().getQuotaUsed(user.getUserId());
		Quota quotaTotal = this.getQuotaManager().getQuotaTotal(
				user.getUserId());
		model.put("quotaUsed", quotaUsed);
		model.put("quotaTotal", quotaTotal);
		return new ModelAndView("user/modal/viewquota", model);
	}
}
