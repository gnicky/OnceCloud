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
public class UserController {
	
	@RequestMapping(value = "/volume", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView volume(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 3);
		model.put("title", "硬盘");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/volume", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/snapshot", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView snapshot(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 4);
		model.put("title", "备份");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/snapshot", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/router", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView router(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 5);
		model.put("title", "虚拟路由");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/router", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/vnet", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView vnet(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 5);
		model.put("title", "私有网络");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/vnet", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/elasticip", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView elasticIP(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 6);
		model.put("title", "公网IP");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/elasticip", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/loadbalance", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView loadBalance(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 7);
		model.put("title", "负载均衡");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/loadbalance", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/firewall", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView firewall(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 8);
		model.put("title", "防火墙");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/firewall", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/alarm", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView alarm(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 13);
		model.put("title", "监控警告");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/alarm", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
}
