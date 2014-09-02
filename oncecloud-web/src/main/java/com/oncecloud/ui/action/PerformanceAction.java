package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.manager.PerformanceManger;

@RequestMapping("/PerformanceAction")
@Controller
public class PerformanceAction {

	private PerformanceManger performanceManger;

	public PerformanceManger getPerformanceManger() {
		return performanceManger;
	}

	@Autowired
	public void setPerformanceManger(PerformanceManger performanceManger) {
		this.performanceManger = performanceManger;
	}

	@RequestMapping(value = "/CPU", method = { RequestMethod.GET })
	@ResponseBody
	public String cpu(HttpServletRequest request, @RequestParam String uuid,
			@RequestParam String type) {
		JSONObject jo = this.getPerformanceManger().getCpu(uuid, type);
		return jo.toString();
	}

	@RequestMapping(value = "/Memory", method = { RequestMethod.GET })
	@ResponseBody
	public String memory(HttpServletRequest request, @RequestParam String uuid,
			@RequestParam String type) {
		JSONArray ja = this.getPerformanceManger().getMemory(uuid, type);
		return ja.toString();
	}

	@RequestMapping(value = "/PIF", method = { RequestMethod.GET })
	@ResponseBody
	public String pif(HttpServletRequest request, @RequestParam String uuid,
			@RequestParam String type) {
		JSONObject jo = this.getPerformanceManger().getPif(uuid, type);
		return jo.toString();
	}
}
