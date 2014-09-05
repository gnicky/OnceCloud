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
import com.oncecloud.ui.model.CreateVMModel;
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
		JSONArray ja = this.getVmManager().getAdminVMList(alrModel.getPage(),
				alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
		return ja.toString();
	}

	@RequestMapping(value = "/VMList", method = { RequestMethod.GET })
	@ResponseBody
	public String vmList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getVmManager().getVMList(userId, list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/AdminStartUp", method = { RequestMethod.GET })
	@ResponseBody
	public void vmAdminStartUp(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getVmManager().doAdminStartVm(userId, uuid);
	}

	@RequestMapping(value = "/AdminShutDown", method = { RequestMethod.GET })
	@ResponseBody
	public void vmAdminShutDown(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getVmManager().doAdminShutDown(userId, uuid, force);
	}

	@RequestMapping(value = "/VMDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String vmDetail(HttpServletRequest request, @RequestParam String uuid) {
		JSONObject jo = this.getVmManager().getVMDetail(uuid);
		return jo.toString();
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String quota(HttpServletRequest request, @RequestParam int count) {
		User user = (User) request.getSession().getAttribute("user");
		String quota = this.getVmManager().getQuota(user.getUserId(),
				user.getUserLevel(), count);
		return quota;
	}

	@RequestMapping(value = "/StartVM", method = { RequestMethod.GET })
	@ResponseBody
	public void startVM(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		String poolUuid = user.getUserAllocate();
		this.getVmManager().doStartVM(user.getUserId(), uuid, poolUuid);
	}

	@RequestMapping(value = "/UpdateStar", method = { RequestMethod.POST })
	@ResponseBody
	public void updateStar(HttpServletRequest request, @RequestParam String uuid, @RequestParam int num) {
		this.getVmManager().updateImportance(uuid, num);
	}

	@RequestMapping(value = "/RestartVM", method = { RequestMethod.GET })
	@ResponseBody
	public void restartVM(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		String poolUuid = user.getUserAllocate();
		this.getVmManager().doRestartVM(user.getUserId(), uuid, poolUuid);
	}

	@RequestMapping(value = "/DeleteVM", method = { RequestMethod.GET })
	@ResponseBody
	public void deleteVM(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		String poolUuid = user.getUserAllocate();
		this.getVmManager().doDelete(user.getUserId(), uuid, poolUuid);
	}

	@RequestMapping(value = "/ShutdownVM", method = { RequestMethod.GET })
	@ResponseBody
	public void shutdownVM(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		String poolUuid = user.getUserAllocate();
		this.getVmManager().doShutdownVM(user.getUserId(), uuid, force,
				poolUuid);
	}

	@RequestMapping(value = "/CreateVM", method = { RequestMethod.POST })
	@ResponseBody
	public void createVM(HttpServletRequest request, CreateVMModel createvmModel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getVmManager().doCreateVM(createvmModel.getVmUuid(),
				createvmModel.getImageUuid(), user.getUserId(),
				createvmModel.getVmName(), createvmModel.getCpu(),
				createvmModel.getMemory(), createvmModel.getPassword(),
				user.getUserAllocate());
	}
	
	@RequestMapping(value = "/SimpleList", method = { RequestMethod.POST })
	@ResponseBody
	public String simpleList(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getVmManager().getSimpleVMList(user.getUserId());
		return ja.toString();
	}
}
