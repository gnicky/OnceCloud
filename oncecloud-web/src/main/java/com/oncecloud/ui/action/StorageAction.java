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

	@RequestMapping(value = "/Delete", method = { RequestMethod.GET })
	@ResponseBody
	public String delete(HttpServletRequest request, @RequestParam String srid,
			@RequestParam String srname) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getSrManager().deleteStorage(user.getUserId(),
				srid, srname);
		return ja.toString();
	}
}