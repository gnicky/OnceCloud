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
import com.oncecloud.manager.RouterManager;
import com.oncecloud.ui.model.AdminListModel;

@RequestMapping("/RouterAction")
@Controller
public class RouterAction {
	private RouterManager routerManager;

	public RouterManager getRouterManager() {
		return routerManager;
	}

	@Autowired
	public void setRouterManager(RouterManager routerManager) {
		this.routerManager = routerManager;
	}

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		JSONArray ja = this.getRouterManager().getAdminRouterList(
				alrModel.getPage(), alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
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

	@RequestMapping(value = "/UpdateStar", method = { RequestMethod.POST })
	@ResponseBody
	public void updateStar(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int num) {
		this.getRouterManager().updateImportance(uuid, num);
	}

}
