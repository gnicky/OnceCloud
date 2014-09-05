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
import com.oncecloud.manager.LBManager;
import com.oncecloud.ui.model.AdminListModel;

@RequestMapping("/LBAction")
@Controller
public class LoadBalanceAction {
	private LBManager lbManager;

	public LBManager getLbManager() {
		return lbManager;
	}

	@Autowired
	public void setLbManager(LBManager lbManager) {
		this.lbManager = lbManager;
	}

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		JSONArray ja = this.getLbManager().getAdminLBList(alrModel.getPage(),
				alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
		return ja.toString();
	}
	
	@RequestMapping(value = "/ShutDown", method = { RequestMethod.POST })
	@ResponseBody
	public void shutDown(HttpServletRequest request,@RequestParam String uuid,@RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
     	this.getLbManager().shutdownLB(uuid, force, user.getUserAllocate());
	}
	
	
	@RequestMapping(value = "/Start", method = { RequestMethod.POST })
	@ResponseBody
	public void startUp(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().startLB(uuid, user.getUserAllocate());
	}
	
	@RequestMapping(value = "/Destroy", method = { RequestMethod.POST })
	@ResponseBody
	public void destroy(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().deleteLB(user.getUserId(), uuid);
	}
	
	@RequestMapping(value = "/Quota", method = { RequestMethod.POST })
	@ResponseBody
	public String quota(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getLbManager().lbQuota(user.getUserId());
		return ja.toString();
	}
	
	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public void create(HttpServletRequest request,@RequestParam String uuid,@RequestParam String name,@RequestParam int capacity) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().lbCreate(name, uuid, capacity, user.getUserId(),user.getUserAllocate());
	}
}
