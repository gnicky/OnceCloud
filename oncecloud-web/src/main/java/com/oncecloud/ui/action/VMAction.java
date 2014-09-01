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

import com.oncecloud.entity.User;
import com.oncecloud.manager.VMManager;
import com.oncecloud.ui.model.AdminListModel;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/VMAction")
@Controller
public class VMAction {
	private VMManager vmManager;

	public VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	public void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getVmManager().getAdminVMList(
					alrModel.getPage(), alrModel.getLimit(),
					alrModel.getHost(), alrModel.getImportance(),
					alrModel.getType());
			return ja.toString();
		} else {
			return "";
		}
	}

	@RequestMapping(value = "/VMList", method = { RequestMethod.GET })
	@ResponseBody
	public String vmList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONArray ja = this.getVmManager().getVMList(userId,
					list.getPage(), list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
	
	@RequestMapping(value = "/AdminStartUp", method = { RequestMethod.GET })
	@ResponseBody
	public void vmAdminStartUp(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			this.getVmManager().doAdminStartVm(userId, uuid);
		}
	}

	@RequestMapping(value = "/AdminShutDown", method = { RequestMethod.GET })
	@ResponseBody
	public void vmAdminShutDown(HttpServletRequest request, @RequestParam String uuid, @RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			this.getVmManager().doAdminShutDown(userId, uuid, force);
		}
	}

	@RequestMapping(value = "/VMDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String vmDetail(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONObject jo = this.getVmManager().getVMDetail(uuid);
			return jo.toString();
		} else {
			return "";
		}
	}
	
	@RequestMapping(value = "/StartVM", method = { RequestMethod.GET })
	@ResponseBody
	public void startVM(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			String poolUuid = user.getUserAllocate();
			this.getVmManager().startVM(uuid, poolUuid);
		}
	}
}
