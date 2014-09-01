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
import com.oncecloud.manager.VolumeManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/VolumeAction")
@Controller
public class VolumeAction {
	private VolumeManager volumeManager;

	public VolumeManager getVolumeManager() {
		return volumeManager;
	}

	@Autowired
	public void setVolumeManager(VolumeManager volManager) {
		this.volumeManager = volManager;
	}
	
	@RequestMapping(value = "/VolumeList", method = { RequestMethod.GET })
	@ResponseBody
	public String volumeList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONArray ja = this.getVolumeManager().getVolumeList(userId, list.getPage(),
					list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
	
	@RequestMapping(value = "/VolumeDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String volumeDetail(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONObject jo = this.getVolumeManager().getVolumeDetail(uuid);
			return jo.toString();
		} else {
			return "";
		}
	}
}
