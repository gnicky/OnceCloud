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
import com.oncecloud.manager.DashboardManager;
import com.oncecloud.manager.DatacenterManager;
import com.oncecloud.manager.UserManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/DatacenterAction")
@Controller
public class DatacenterAction {
	private DatacenterManager datacenterManager;
	private DashboardManager dashboardManager;
	private UserManager userManager;
	
	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	private DatacenterManager getDatacenterManager() {
		return datacenterManager;
	}

	@Autowired
	private void setDatacenterManager(DatacenterManager datacenterManager) {
		this.datacenterManager = datacenterManager;
	}
	
	

	public DashboardManager getDashboardManager() {
		return dashboardManager;
	}
	
	@Autowired
	public void setDashboardManager(DashboardManager dashboardManager) {
		this.dashboardManager = dashboardManager;
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

	@RequestMapping(value = "/PoolList", method = { RequestMethod.GET })
	@ResponseBody
	public String poolList(HttpServletRequest request) {
		String dcid = (String) request.getSession().getAttribute("dcid");
		JSONArray ja = this.getDatacenterManager().getPoolList(dcid);
		return ja.toString();
	}

	@RequestMapping(value = "/RackList", method = { RequestMethod.GET })
	@ResponseBody
	public String rackList(HttpServletRequest request) {
		String dcid = (String) request.getSession().getAttribute("dcid");
		JSONArray ja = this.getDatacenterManager().getRackList(dcid);
		return ja.toString();
	}
	
	@RequestMapping(value = "/Switch", method = { RequestMethod.GET })
	@ResponseBody
	public String swicth(HttpServletRequest request,@RequestParam String uuid) {
		JSONArray ja = this.getDashboardManager().getSwitch(uuid);
		return ja.toString();
	}
	
	@RequestMapping(value = "/CompanyDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String companyDetail(HttpServletRequest request,@RequestParam int cid) {
		JSONArray ja = this.getUserManager().doGetcompanyDetail(cid);
		return ja.toString();
	}
}
