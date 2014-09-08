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
import com.oncecloud.manager.RouterManager;
import com.oncecloud.ui.model.AdminListModel;
import com.oncecloud.ui.model.CreateRouterModel;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/RouterAction")
@Controller
public class RouterAction {
	private RouterManager routerManager;

	public RouterManager getRouterManager() {
		return routerManager;
	}

	@Autowired
	public void setRouterManger(RouterManager rtManager) {
		this.routerManager = rtManager;
	}

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		JSONArray ja = this.getRouterManager().getAdminRouterList(
				alrModel.getPage(), alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
		return ja.toString();
	}

	@RequestMapping(value = "/RouterList", method = { RequestMethod.GET })
	@ResponseBody
	public String routerList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getRouterManager().getRouterList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/AdminStartUp", method = { RequestMethod.GET })
	@ResponseBody
	public void routerAdminStartUp(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getRouterManager().routerAdminStartUp(uuid, userId);
	}

	@RequestMapping(value = "/AdminShutDown", method = { RequestMethod.GET })
	@ResponseBody
	public void routerAdminShutDown(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getRouterManager().routerAdminShutDown(uuid, force, userId);
	}

	@RequestMapping(value = "/RouterDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String routerDetail(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getRouterManager().getRouterDetail(uuid);
		return jo.toString();
	}
	
	@RequestMapping(value = "/StartUp", method = { RequestMethod.POST })
	@ResponseBody
	public void startUp(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getRouterManager().routerStartUp(uuid, user.getUserId(), user.getUserAllocate());
	}
	
	@RequestMapping(value = "/Destroy", method = { RequestMethod.POST })
	@ResponseBody
	public void destroy(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getRouterManager().routerDestory(uuid, user.getUserId(), user.getUserAllocate());
	}
	
	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public void create(HttpServletRequest request,CreateRouterModel createRouterModel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getRouterManager().createRouter(createRouterModel.getUuid(), user.getUserId(), createRouterModel.getName(), createRouterModel.getCapacity(), createRouterModel.getFwUuid(),  user.getUserAllocate());
	}

	@RequestMapping(value = "/UpdateStar", method = { RequestMethod.POST })
	@ResponseBody
	public void updateStar(HttpServletRequest request,@RequestParam String uuid, @RequestParam int num) {
		this.getRouterManager().updateImportance(uuid, num);
	}
	
	@RequestMapping(value = "/ShutDown", method = { RequestMethod.POST })
	@ResponseBody
	public void shutDown(HttpServletRequest request,@RequestParam String uuid,@RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
     	this.getRouterManager().routerShutDown(uuid, force,user.getUserId(), user.getUserAllocate());
	}
	
	@RequestMapping(value = "/Quota", method = { RequestMethod.POST })
	@ResponseBody
	public String quota(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getRouterManager().routerQuota(user.getUserId());
		return ja.toString();
	}
	
	@RequestMapping(value = "/Vxnets", method = { RequestMethod.POST })
	@ResponseBody
	public String getVxnets(HttpServletRequest request,@RequestParam String routerUuid) {
		JSONArray jo = this.getRouterManager().getVxnets(routerUuid);
		return jo.toString();
	}
	
	
}
