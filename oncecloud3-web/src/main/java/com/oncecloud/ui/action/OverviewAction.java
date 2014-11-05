package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.manager.OverviewManager;

@RequestMapping("/OverviewAction")
@Controller
public class OverviewAction {

	private OverviewManager overviewManager;

	public OverviewManager getOverviewManager() {
		return overviewManager;
	}

	@Autowired
	public void setOverviewManager(OverviewManager overviewManager) {
		this.overviewManager = overviewManager;
	}

	@RequestMapping(value = "/Overview", method = { RequestMethod.GET })
	@ResponseBody
	public String overview(HttpServletRequest request) {
		JSONArray ja = this.getOverviewManager().getOverview();
		return ja.toString();
	}

}
