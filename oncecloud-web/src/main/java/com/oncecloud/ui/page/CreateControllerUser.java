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

	@RequestMapping(value = "/snapshot/create", method = { RequestMethod.GET })
	public ModelAndView createSnapshot(HttpServletRequest request,
			@RequestParam String rsid, @RequestParam String rstype,
			@RequestParam String rsname) {
		Map<String, String> model = new HashMap<String, String>();
		model.put("resourceId", rsid);
		model.put("resourceType", rstype);
		model.put("resourceName", rsname);
		return new ModelAndView("user/create/createsnapshot", model);
	}

	@RequestMapping(value = "/image/clone", method = { RequestMethod.GET })
	public ModelAndView cloneImage(HttpServletRequest request,
			@RequestParam String rsid) {
		Map<String, String> model = new HashMap<String, String>();
		model.put("rsid", rsid);
		return new ModelAndView("user/create/createimage", model);
	}

	@RequestMapping(value = "/volume/create", method = { RequestMethod.GET })
	public ModelAndView createVolume(HttpServletRequest request) {
		return new ModelAndView("user/create/createvolume");
	}

	@RequestMapping(value = "/alarm/create", method = { RequestMethod.GET })
	public ModelAndView createAlarm(HttpServletRequest request) {
		return new ModelAndView("user/create/createalarm");
	}

	@RequestMapping(value = "/alarmrule/create", method = { RequestMethod.POST })
	public ModelAndView createAlarmRule(HttpServletRequest request) {
		String alarmType = request.getParameter("alarmType");
		request.setAttribute("alarmType", alarmType);
		return new ModelAndView("user/create/createalarmrule");
	}
	
	@RequestMapping(value = "/elasticip/create", method = { RequestMethod.POST })
	public ModelAndView createElasticIP(HttpServletRequest request) {
		return new ModelAndView("user/create/createelasticip");
	}
	
	@RequestMapping(value = "/router/create", method = { RequestMethod.GET })
	public ModelAndView createrouter(HttpServletRequest request) {
		return new ModelAndView("user/create/createrouter");
	}
		
	@RequestMapping(value = "/service/create", method = { RequestMethod.GET })
	public ModelAndView createService(HttpServletRequest request) {
		return new ModelAndView("user/create/createservice");
	}
	
	@RequestMapping(value = "/firewall/create", method = { RequestMethod.GET })
	public ModelAndView createFirewall(HttpServletRequest request) {
		return new ModelAndView("user/create/createfirewall");
	}
}
