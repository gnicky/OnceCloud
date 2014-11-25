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
import com.oncecloud.ui.model.CreateVolumeModel;
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
		int userId = user.getUserId();
		JSONArray ja = this.getVolumeManager().getVolumeList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/CreateVolume", method = { RequestMethod.POST })
	@ResponseBody
	public void createVolume(HttpServletRequest request,
			CreateVolumeModel cvModel) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getVolumeManager().createVolume(userId, cvModel.getVolumeUuid(),
				cvModel.getVolumeName(), cvModel.getVolumeSize());
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String quota(HttpServletRequest request, @RequestParam int count,
			@RequestParam int size) {
		User user = (User) request.getSession().getAttribute("user");
		String quota = this.getVolumeManager().getQuota(user.getUserId(),
				count, size);
		return quota;
	}

	@RequestMapping(value = "/VolumeDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String volumeDetail(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getVolumeManager().getVolumeDetail(uuid);
		return jo.toString();
	}

	@RequestMapping(value = "/VolumesOfVM", method = { RequestMethod.GET })
	@ResponseBody
	public String volumeOfVm(HttpServletRequest request,
			@RequestParam String vmUuid) {
		JSONArray ja = this.getVolumeManager().getVolumesOfVM(vmUuid);
		return ja.toString();
	}

	/*@RequestMapping(value = "/AvailableVolumes", method = { RequestMethod.POST })
	@ResponseBody
	public String getAvailableVolumes(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getVolumeManager().getAvailableVolumes(
				user.getUserId());
		return ja.toString();
	}
	
	@RequestMapping(value = "/Bind", method = { RequestMethod.POST })
	@ResponseBody
	public void bind(HttpServletRequest request,
			@RequestParam String volumeUuid, @RequestParam String vmUuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getVolumeManager().bindVolume(userId, volumeUuid, vmUuid);
	}

	@RequestMapping(value = "/Unbind", method = { RequestMethod.POST })
	@ResponseBody
	public void unbind(HttpServletRequest request,
			@RequestParam String volumeUuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getVolumeManager().unbindVolume(user.getUserId(), volumeUuid);
	}
	
	@RequestMapping(value = "/DeleteVolume", method = { RequestMethod.POST })
	@ResponseBody
	public void deleteVolume(HttpServletRequest request,
			@RequestParam String volumeUuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getVolumeManager().deleteVolume(user.getUserId(), volumeUuid);
	}*/
}
