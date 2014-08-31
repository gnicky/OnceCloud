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
public class DetailControllerUser {
	
	@RequestMapping(value = "/instance/detail", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView volume(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("title", "主机");
		model.put("uuid", request.getParameter("uuid"));
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/detail/instancedetail", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
}
