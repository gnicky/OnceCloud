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

@Controller
public class StorageController {
	@RequestMapping(value = "/storage", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView storage(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		request.getSession().setAttribute("sessionBasePath", basePath);
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
//		model.put("basePath", basePath);
		model.put("sideActive", 16);
//		model.put("user", user);
		model.put("title", "存储管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/storage", model);
		} else {
			return new ModelAndView(new RedirectView("/login"));
		}
	}
}
