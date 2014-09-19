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
import com.oncecloud.manager.SRManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/StorageAction")
@Controller
public class StorageAction {
	private SRManager srManager;

	public SRManager getSrManager() {
		return srManager;
	}

	@Autowired
	public void setSrManager(SRManager srManager) {
		this.srManager = srManager;
	}

	@RequestMapping(value = "/StorageList", method = { RequestMethod.GET })
	@ResponseBody
	public String storageList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getSrManager().getStorageList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request, @RequestParam String srid,
			@RequestParam String srname) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getSrManager().deleteStorage(user.getUserId(),
				srid, srname);
		return ja.toString();
	}

	@RequestMapping(value = "/Add", method = { RequestMethod.POST })
	@ResponseBody
	public String add(HttpServletRequest request, @RequestParam String srname,
			@RequestParam String srAddress, @RequestParam String srDesc,
			@RequestParam String srType, @RequestParam String srDir,
			@RequestParam String rackId, @RequestParam String rackName) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getSrManager().addStorage(user.getUserId(), srname,
				srAddress, srDesc, srType, srDir, rackId, rackName);
		return ja.toString();
	}

	@RequestMapping(value = "/Update", method = { RequestMethod.POST })
	@ResponseBody
	public void update(HttpServletRequest request, @RequestParam String srid,
			@RequestParam String srName, @RequestParam String srDesc,
			@RequestParam String rackid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getSrManager().updateStorage(user.getUserId(), srid, srName, srDesc, rackid);
	}

	@RequestMapping(value = "/QueryAddress", method = { RequestMethod.GET })
	@ResponseBody
	public String queryAddress(HttpServletRequest request, @RequestParam String address) {
		JSONArray ja = this.getSrManager().getStorageByAddress(address);
		return ja.toString();
	}

	@RequestMapping(value = "/LoadToServer", method = { RequestMethod.GET })
	@ResponseBody
	public String loadToServer(HttpServletRequest request, @RequestParam String sruuid, @RequestParam String hostuuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getSrManager().load2Server(user.getUserId(), sruuid, hostuuid);
		return ja.toString();
	}

	@RequestMapping(value = "/RealSRList", method = { RequestMethod.POST })
	@ResponseBody
	public String getRealSRList(HttpServletRequest request, @RequestParam String poolUuid) {
		JSONArray ja = this.getSrManager().getRealSRList(poolUuid);
		return ja.toString();
	}
}