package com.oncecloud.ui.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DetailControllerAdmin {
	
	@RequestMapping(value = "/datacenter/detail")
	@ResponseBody
	public ModelAndView datacenterDetail(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/backdoor"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "数据中心管理");
		model.put("sideActive", 12);
		String dcid = request.getParameter("dcid");
		if (dcid != null) {
			request.getSession().setAttribute("dcid", dcid);
			return new ModelAndView("admin/detail/datacenterdetail", model);
		} else {
			if (request.getSession().getAttribute("dcid") != null) {
				return new ModelAndView("admin/detail/datacenterdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/host/detail")
	@ResponseBody
	public ModelAndView hostDetail(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/backdoor"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "服务器");
		model.put("sideActive", 15);
		String hostid = request.getParameter("hostid");
		if (hostid != null) {
			request.getSession().setAttribute("hostid", hostid);
			request.getSession().setAttribute("showId", "host-" + hostid.substring(0, 8));
			return new ModelAndView("admin/detail/hostdetail", model);
		} else {
			if (request.getSession().getAttribute("hostid") != null) {
				return new ModelAndView("admin/detail/hostdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/rack/detail")
	@ResponseBody
	public ModelAndView rackDetail(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/backdoor"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "机架");
		model.put("sideActive", 13);
		String rackid = request.getParameter("rackid");
		if (rackid != null) {
			request.getSession().setAttribute("rackid", rackid);
			return new ModelAndView("admin/detail/rackdetail", model);
		} else {
			if (request.getSession().getAttribute("rackid") != null) {
				return new ModelAndView("admin/detail/rackdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/user/detail")
	@ResponseBody
	public ModelAndView userDetail(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/backdoor"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "用户");
		model.put("sideActive", 3);
		String userid = request.getParameter("userid");
		String username = request.getParameter("userid");
		if (userid != null) {
			request.getSession().setAttribute("userid", userid);
			request.getSession().setAttribute("username", username);
			return new ModelAndView("admin/detail/userdetail", model);
		} else {
			if (request.getSession().getAttribute("userid") != null) {
				return new ModelAndView("admin/detail/userdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}
	
}
