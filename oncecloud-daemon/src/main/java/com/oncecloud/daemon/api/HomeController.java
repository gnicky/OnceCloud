package com.oncecloud.daemon.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@RequestMapping("/")
	@ResponseBody
	public ModelAndView index() {
		return new ModelAndView("WEB-INF/views/index.jsp");
	}
}
