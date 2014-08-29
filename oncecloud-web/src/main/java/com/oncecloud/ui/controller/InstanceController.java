package com.oncecloud.ui.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oncecloud.entity.User;
import com.oncecloud.main.Constant;

@Controller
public class InstanceController {

	@RequestMapping(value = "/instance", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView login(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("basePath", basePath);
		model.put("sideActive", 1);
		model.put("user", user);
		model.put("vncServer", Constant.noVNCServerPublic);
		if (user.getUserLevel() > 0) {
			model.put("title", "主机");
			return new ModelAndView("user/instance", model);
		} else {
			model.put("title", "虚拟机");
			return new ModelAndView("admin/instance", model);
		}
	}
}
