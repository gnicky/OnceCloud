package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
