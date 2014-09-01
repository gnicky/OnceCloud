package com.oncecloud.ui.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CreateControllerUser {

	@RequestMapping(value = "/instance/create", method = { RequestMethod.GET })
	public ModelAndView createVM(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		}
		
		return new ModelAndView("user/create/createinstance");
	}
}
