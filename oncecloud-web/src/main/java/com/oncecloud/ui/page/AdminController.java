package com.oncecloud.ui.page;

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
public class AdminController {
	
	@RequestMapping(value = "/user", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView user(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 3);
		model.put("title", "用户管理");
		if (user.getUserLevel() == 0) {
			request.getSession().setAttribute("userid", request.getParameter("userid"));
			return new ModelAndView("admin/user", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/storage", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView storage(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 16);
		model.put("title", "存储管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/storage", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/host", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView host(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 15);
		model.put("title", "服务器管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/host", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/pool", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView pool(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 14);
		model.put("title", "资源池管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/pool", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/address", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView address(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 4);
		model.put("title", "地址池管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/address", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/assets", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView assets(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sideActive", 5);
		model.put("title", "硬件网络资源台账");
		User user = (User) request.getSession().getAttribute("user");
		if (user.getUserLevel() == 0) {
		    return new ModelAndView("admin/assets",model);
		}
		else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/datacenter", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView datacenter(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 12);
		model.put("title", "数据中心管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/datacenter", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/rack", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView rack(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 13);
		model.put("title", "机架管理");
		if (user.getUserLevel() == 0) {
			return new ModelAndView("admin/rack", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/CompanyMap", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView companyMap(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sideActive", 0);
		model.put("title", "用户分布展示");
		User user = (User) request.getSession().getAttribute("user");
		if (user.getUserLevel() == 0) {
		    return new ModelAndView("admin/companymap",model);
		}
		else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
}
