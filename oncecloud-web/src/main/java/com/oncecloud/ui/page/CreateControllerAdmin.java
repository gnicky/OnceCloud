package com.oncecloud.ui.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CreateControllerAdmin {

	@RequestMapping(value = "/user/create", method = { RequestMethod.GET })
	public ModelAndView createUser(HttpServletRequest request) {
		return new ModelAndView("admin/create/createuser");
	}

	@RequestMapping(value = "/storage/create", method = { RequestMethod.GET })
	public ModelAndView createStorage(HttpServletRequest request) {
		return new ModelAndView("admin/create/createstorage");
	}

	@RequestMapping(value = "/rack/create", method = { RequestMethod.GET })
	public ModelAndView createRack(HttpServletRequest request) {
		return new ModelAndView("admin/create/createrack");
	}

	@RequestMapping(value = "/pool/create", method = { RequestMethod.GET })
	public ModelAndView createPool(HttpServletRequest request) {
		return new ModelAndView("admin/create/createpool");
	}

	@RequestMapping(value = "/host/create", method = { RequestMethod.GET })
	public ModelAndView createHost(HttpServletRequest request) {
		return new ModelAndView("admin/create/createhost");
	}

	@RequestMapping(value = "/datacenter/create", method = { RequestMethod.GET })
	public ModelAndView createDatecenter(HttpServletRequest request) {
		return new ModelAndView("admin/create/createdatacenter");
	}

	@RequestMapping(value = "/address/create", method = { RequestMethod.GET })
	public ModelAndView createAddress(HttpServletRequest request) {
		return new ModelAndView("admin/create/createaddress");
	}

	@RequestMapping(value = "/image/create", method = { RequestMethod.GET })
	public ModelAndView createImage(HttpServletRequest request) {
		return new ModelAndView("admin/create/createimage");
	}

	@RequestMapping(value = "/instanceiso/create", method = { RequestMethod.GET })
	public ModelAndView createInstanceWithISO(HttpServletRequest request) {
		return new ModelAndView("admin/create/createinstancewithiso");
	}
	
}
