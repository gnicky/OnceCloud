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
import com.oncecloud.manager.RackManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/RackAction")
@Controller
public class RackAction {
	private RackManager rackManager;

	public RackManager getRackManager() {
		return rackManager;
	}

	@Autowired
	public void setRackManager(RackManager rackManager) {
		this.rackManager = rackManager;
	}

	@RequestMapping(value = "/RackList", method = { RequestMethod.GET })
	@ResponseBody
	public String rackList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getRackManager().getRackList(list.getPage(), list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public void delete(HttpServletRequest request, @RequestParam String rackid, @RequestParam String rackname) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.getRackManager().deleteRack(rackid, rackname, user.getUserId());
		}
	}
}
