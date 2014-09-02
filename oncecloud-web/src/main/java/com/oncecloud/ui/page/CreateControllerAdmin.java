package com.oncecloud.ui.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CreateControllerAdmin {

	@RequestMapping(value = "/user/create", method = { RequestMethod.GET })
	public ModelAndView createVM(HttpServletRequest request) {
		return new ModelAndView("user/create/createinstance");
	}
	
}
