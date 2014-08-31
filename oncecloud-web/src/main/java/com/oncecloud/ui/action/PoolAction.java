package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.poolManager.getPoolList(list.getPage(), list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
	
}
