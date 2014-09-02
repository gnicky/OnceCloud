package com.oncecloud.ui.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin/modal")
@Controller
public class ModalControllerAdmin {

	public ModelAndView addtopool(HttpServletRequest request, @RequestParam String uuidjsonstr) {
		request.setAttribute("uuidjsonstr", uuidjsonstr);
		return new ModelAndView("admin/modal/addtopool");
	}
	
}
