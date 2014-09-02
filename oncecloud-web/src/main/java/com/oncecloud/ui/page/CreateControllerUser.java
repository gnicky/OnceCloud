package com.oncecloud.ui.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CreateControllerUser {

	@RequestMapping(value = "/instance/create", method = { RequestMethod.GET })
	public ModelAndView createVM(HttpServletRequest request) {
		return new ModelAndView("user/create/createinstance");
	}
	
	@RequestMapping(value = "/instance/createsnapshot", method = { RequestMethod.GET })
	public ModelAndView createSnapshot(HttpServletRequest request,@RequestParam String rsid,@RequestParam String rstype,@RequestParam String rsname) {
		Map<String, String> model = new HashMap<String, String>();
		model.put("resourceId", rsid);
		model.put("resourceType", rstype);
		model.put("resourceName", rsname);
		return new ModelAndView("user/create/createsnapshot",model);
	}
}
