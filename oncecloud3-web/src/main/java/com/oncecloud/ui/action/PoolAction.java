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
import com.oncecloud.manager.PoolManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/PoolAction")
@Controller
public class PoolAction {

	private PoolManager poolManager;

	public PoolManager getPoolManager() {
		return poolManager;
	}

	@Autowired
	public void setPoolManager(PoolManager poolManager) {
		this.poolManager = poolManager;
	}

	@RequestMapping(value = "/PoolList", method = { RequestMethod.GET })
	@ResponseBody
	public String poolList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getPoolManager().getPoolList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request,
			@RequestParam String poolid, @RequestParam String poolname) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getPoolManager().deletePool(poolid, poolname,
				user.getUserId());
		return ja.toString();
	}
/*
	@RequestMapping(value = "/UnBind", method = { RequestMethod.GET })
	@ResponseBody
	public String unBind(HttpServletRequest request, @RequestParam String poolid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getPoolManager().unbind(poolid, user.getUserId());
		return ja.toString();
	}
*/
	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public String create(HttpServletRequest request, @RequestParam String poolname, @RequestParam String pooldesc
			,@RequestParam String dcuuid, @RequestParam String dcname) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getPoolManager().createPool(poolname, pooldesc, dcuuid, dcname, user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/Update", method = { RequestMethod.POST })
	@ResponseBody
	public void update(HttpServletRequest request, @RequestParam String pooluuid, @RequestParam String poolname
			,@RequestParam String pooldesc, @RequestParam String dcuuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getPoolManager().updatePool(pooluuid, poolname, pooldesc, dcuuid, user.getUserId());
	}
/*	
	@RequestMapping(value = "/AllPool", method = {RequestMethod.POST })
	@ResponseBody
	public String allPool(HttpServletRequest request, ImageCloneModel imagecloneModel) {
		JSONArray ja = this.getPoolManager().getAllPool();
		return ja.toString();
	}
*/	
	@RequestMapping(value = "/KeepAccordance", method = {RequestMethod.POST })
	@ResponseBody
	public void keepAccordance(HttpServletRequest request, @RequestParam String poolUuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getPoolManager().keepAccordance(user.getUserId(), poolUuid);
	}
}
