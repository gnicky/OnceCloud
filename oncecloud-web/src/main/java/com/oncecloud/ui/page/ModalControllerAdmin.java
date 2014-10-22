package com.oncecloud.ui.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin/modal")
@Controller
public class ModalControllerAdmin {

	@RequestMapping(value = "/addtopool")
	public ModelAndView addtopool(HttpServletRequest request,
			@RequestParam String uuidjsonstr) {
		request.setAttribute("uuidjsonstr", uuidjsonstr);
		return new ModelAndView("admin/modal/addtopool");
	}

	@RequestMapping(value = "/addtohost")
	public ModelAndView addtohost(HttpServletRequest request) {
		return new ModelAndView("admin/modal/addtohost");
	}

	@RequestMapping(value = "/savetodb")
	public ModelAndView savetodb(HttpServletRequest request) {
		return new ModelAndView("admin/modal/savetodb");
	}

	@RequestMapping(value = "/storageofhost")
	public ModelAndView storageofhost(HttpServletRequest request,
			@RequestParam String hostuuid, @RequestParam String hostname) {
		request.setAttribute("hostuuid", hostuuid);
		request.setAttribute("hostname", hostname);
		return new ModelAndView("admin/modal/storageofhost");
	}

	@RequestMapping(value = "/modifynetwork")
	public ModelAndView modifynetwork(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type,
			@RequestParam String isrun) {
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", type);
		request.setAttribute("isrun", isrun);
		return new ModelAndView("admin/modal/modifynetwork");
	}

}
