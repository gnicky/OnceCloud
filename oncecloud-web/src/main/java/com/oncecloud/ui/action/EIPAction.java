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
import com.oncecloud.manager.EIPManager;
import com.oncecloud.ui.model.CreateEIPModel;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/EIPAction")
@Controller
public class EIPAction {
	private EIPManager eipManager;

	public EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	public void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	@RequestMapping(value = "/EIPList", method = { RequestMethod.GET })
	@ResponseBody
	public String eipList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getEipManager().getEIPList(userId, list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/AvailableIPs", method = { RequestMethod.GET })
	@ResponseBody
	public String eipList(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getEipManager().eipGetAbleEips(userId);
		return ja.toString();
	}

	@RequestMapping(value = "/Bind", method = { RequestMethod.GET })
	@ResponseBody
	public String bind(HttpServletRequest request, @RequestParam String vmUuid,
			@RequestParam String eipIp, @RequestParam String bindType) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getEipManager().eipBind(userId, vmUuid, eipIp,
				bindType);
		return jo.toString();
	}

	@RequestMapping(value = "/UnBind", method = { RequestMethod.GET })
	@ResponseBody
	public String unbind(HttpServletRequest request,
			@RequestParam String eipIp, @RequestParam String vmUuid,
			@RequestParam String bindType) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getEipManager().eipUnbind(userId, vmUuid, eipIp,
				bindType);
		return jo.toString();
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.GET })
	@ResponseBody
	public String quota(HttpServletRequest request, @RequestParam int count,
			@RequestParam int size) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		String quota = this.getEipManager().getQuota(userId, count, size);
		return quota;
	}
	
	@RequestMapping(value = "/createEIP", method = { RequestMethod.POST })
	@ResponseBody
	public String createEIP(HttpServletRequest request, CreateEIPModel ceModel) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getEipManager().eipApply(ceModel.getEipName(), userId, ceModel.getEipSize(), ceModel.getEipUuid());
		return jo.toString();
	}
}
