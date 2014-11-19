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

	@RequestMapping(value = "/AdminDeleteVM", method = { RequestMethod.GET })
	@ResponseBody
	public void vmAdminDelete(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getVmManager().adminDeleteVM(userId, uuid);
	}

	@RequestMapping(value = "/UpdateStar", method = { RequestMethod.POST })
	@ResponseBody
	public void updateStar(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int num) {
		this.getVmManager().updateImportance(uuid, num);
	}

	@RequestMapping(value = "/ISOList", method = { RequestMethod.POST })
	@ResponseBody
	public String isoList(HttpServletRequest request,
			@RequestParam String poolUuid) {
		JSONArray ja = this.getVmManager().getISOList(poolUuid);
		return ja.toString();
	}

	@RequestMapping(value = "/ISOCreate", method = { RequestMethod.POST })
	@ResponseBody
	public void createWithISO(HttpServletRequest request,
			@RequestParam String pooluuid, @RequestParam int cpu,
			@RequestParam int memory, @RequestParam String vmName,
			@RequestParam String isouuid, @RequestParam String vmUuid,
			@RequestParam String diskuuid, @RequestParam int volum) {
		User user = (User) request.getSession().getAttribute("user");
		this.getVmManager().createVMByISO(vmUuid, isouuid, diskuuid, vmName,
				cpu, memory, volum, pooluuid, user.getUserId());
	}

	@RequestMapping(value = "/SaveToDataBase", method = { RequestMethod.POST })
	@ResponseBody
	public void saveToDataBase(HttpServletRequest request,
			@RequestParam String vmUuid, @RequestParam String vmPWD,
			@RequestParam int vmPlatform, @RequestParam String vmName,
			@RequestParam String vmIP, @RequestParam int vmUID) {
		this.getVmManager().saveToDataBase(vmUuid, vmPWD, vmUID, vmPlatform,
				vmName, vmIP);
	}

	@RequestMapping(value = "/MacList", method = { RequestMethod.POST })
	@ResponseBody
	public String getMacList(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type) {
		JSONArray ja = this.getVmManager().getMacs(uuid, type);
		return ja.toString();
	}

	@RequestMapping(value = "/NetList", method = { RequestMethod.POST })
	@ResponseBody
	public String getNetList(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type) {
		JSONArray ja = this.getVmManager().getNets(uuid, type);
		return ja.toString();
	}

	@RequestMapping(value = "/AddMac", method = { RequestMethod.POST })
	@ResponseBody
	public String addMac(HttpServletRequest request, @RequestParam String uuid,
			@RequestParam String type, @RequestParam String physical,
			@RequestParam String vnetid) {
		JSONObject jo = new JSONObject();
		jo.put("result",
				this.getVmManager().addMac(uuid, type, physical, vnetid));
		return jo.toString();
	}

	@RequestMapping(value = "/DeleteMac", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteMac(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type,
			@RequestParam String vifUuid) {
		JSONObject jo = new JSONObject();
		jo.put("result", this.getVmManager().deleteMac(uuid, type, vifUuid));
		return jo.toString();
	}

	@RequestMapping(value = "/ModifyVnet", method = { RequestMethod.POST })
	@ResponseBody
	public String modifyVnet(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type,
			@RequestParam String vifUuid, @RequestParam String vnetid) {
		JSONObject jo = new JSONObject();
		jo.put("result",
				this.getVmManager().modifyVnet(uuid, type, vnetid, vifUuid));
		return jo.toString();
	}

	@RequestMapping(value = "/ModifyPhysical", method = { RequestMethod.POST })
	@ResponseBody
	public String modifyPhysical(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type,
			@RequestParam String vifUuid, @RequestParam String physical) {
		JSONObject jo = new JSONObject();
		jo.put("result",
				this.getVmManager().modifyPhysical(uuid, type, physical,
						vifUuid));
		return jo.toString();
	}

	@RequestMapping(value = "/TemplateToVM", method = { RequestMethod.POST })
	@ResponseBody
	public String templateToVM(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = new JSONObject();
		jo.put("result", this.getVmManager().templateToVM(uuid));
		return jo.toString();
	}

	@RequestMapping(value = "/VMToTemplate", method = { RequestMethod.POST })
	@ResponseBody
	public String vmToTemplate(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = new JSONObject();
		jo.put("result", this.getVmManager().vmToTemplate(uuid));
		return jo.toString();
	}

	@RequestMapping(value = "/Migration", method = { RequestMethod.POST })
	@ResponseBody
	public String vmToTemplate(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String tarHost,
			@RequestParam String content, @RequestParam String conid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = new JSONObject();
		jo.put("result",
				this.getVmManager().migrate(user.getUserId(), uuid, tarHost,
						content, conid));
		return jo.toString();
	}

}
