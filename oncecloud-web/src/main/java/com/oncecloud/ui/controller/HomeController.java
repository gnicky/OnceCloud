package com.oncecloud.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(method = { RequestMethod.GET }, value = "/")
	@ResponseBody
	public ModelAndView logOn() {
		return new ModelAndView("index");
	}

}
