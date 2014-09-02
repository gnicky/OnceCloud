package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.DatacenterManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/DatacenterAction")
@Controller
public class DatacenterAction {
	private DatacenterManager datacenterManager;

	private DatacenterManager getDatacenterManager() {
		return datacenterManager;
	}

	@Autowired
	private void setDatacenterManager(DatacenterManager datacenterManager) {
		this.datacenterManager = datacenterManager;
	}

	@RequestMapping(value = "/Overview", method = { RequestMethod.GET })
	@ResponseBody
	public String overview(HttpServletRequest request) {
		JSONArray ja = this.getDatacenterManager().getOverview();
		return ja.toString();
	}

	@RequestMapping(value = "/DCList", method = { RequestMethod.GET })
	@ResponseBody
	public String dcList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getDatacenterManager().getDatacenterList(
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request,
			@RequestParam("dcid") String dcId,
			@RequestParam("dcname") String dcName) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getDatacenterManager().deleteDatacenter(dcId,
				dcName, user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/AllList", method = { RequestMethod.GET })
	@ResponseBody
	public String allList(HttpServletRequest request) {
		JSONArray ja = this.getDatacenterManager().getDatacenterAllList();
		return ja.toString();
	}
	
	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public String create(HttpServletRequest request,
			@RequestParam String dcname, @RequestParam String dclocation,
			@RequestParam String dcdesc) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getDatacenterManager().createDatacenter(dcname, dclocation, dcdesc, user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/Update", method = { RequestMethod.POST })
	@ResponseBody
	public void update(HttpServletRequest request,
			@RequestParam String dcuuid, @RequestParam String dcname,
			@RequestParam String dclocation, @RequestParam String dcdesc) {
		User user = (User) request.getSession().getAttribute("user");
		this.getDatacenterManager().update(dcuuid, dcname, dclocation, dcdesc, user.getUserId());
	}
}
