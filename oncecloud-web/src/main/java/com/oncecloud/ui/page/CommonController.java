package com.oncecloud.ui.page;

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

import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;
import com.oncecloud.main.Constant;
import com.oncecloud.manager.QuotaManager;

@Controller
public class CommonController {
	private QuotaManager quotaManager;

	private QuotaManager getQuotaManager() {
		return quotaManager;
	}

	@Autowired
	private void setQuotaManager(QuotaManager quotaManager) {
		this.quotaManager = quotaManager;
	}
	
	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ModelAndView home(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/dashboard", method = { RequestMethod.GET })
	public ModelAndView dashboard(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}

		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("title", "控制台");
		model.put("sideActive", 0);
		if (user.getUserLevel() > 0) {
			Quota quotaUsed = this.getQuotaManager().getQuotaUsed(user.getUserId());
			model.put("quotaUsed", quotaUsed);
			return new ModelAndView("user/dashboard", model);
		} else {
			return new ModelAndView("admin/dashboard", model);
		}
	}
	
	@RequestMapping(value = "/instance", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView login(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 1);
		model.put("vncServer", Constant.noVNCServerPublic);
		if (user.getUserLevel() > 0) {
			model.put("title", "主机");
			return new ModelAndView("user/instance", model);
		} else {
			model.put("title", "虚拟机管理");
			return new ModelAndView("admin/instance", model);
		}
	}
}
