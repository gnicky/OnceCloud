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
import com.oncecloud.manager.SnapshotManager;
import com.oncecloud.ui.model.CreateSnapshotModel;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/SnapshotAction")
@Controller
public class SnapshotAction {
	private SnapshotManager snapshotManager;

	public SnapshotManager getSnapshotManager() {
		return snapshotManager;
	}

	@Autowired
	public void setSnapshotManager(SnapshotManager snapshotManager) {
		this.snapshotManager = snapshotManager;
	}
	
	@RequestMapping(value = "/SnapshotList", method = { RequestMethod.GET })
	@ResponseBody
	public String snapshotList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONArray ja = this.getSnapshotManager().getSnapshotList(userId, list.getPage(),
					list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
	
	@RequestMapping(value = "/quota", method = { RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String quota(HttpServletRequest request, @RequestParam int count) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			String result = this.getSnapshotManager().getQuota(user.getUserId(),
					count);
			return result;
		} else {
			return "";
		}
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String create(HttpServletRequest request,CreateSnapshotModel createsnapshotModel) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
		   JSONObject jsonobect = this.getSnapshotManager().createSnapshot(user.getUserId(), createsnapshotModel.getSnapshotId(), createsnapshotModel.getSnapshotName(), createsnapshotModel.getResourceUuid(), createsnapshotModel.getResourceType());
		   return  jsonobect.toString();
		}
		return "";
	}
	
	@RequestMapping(value = "/detaillist", method = { RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String detaillist(HttpServletRequest request,CreateSnapshotModel createsnapshotModel) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getSnapshotManager().getDetailList(createsnapshotModel.getResourceUuid(), createsnapshotModel.getResourceType());
		   return  ja.toString();
		}
		return "";
	}
	
	@RequestMapping(value = "/getresource", method = { RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String getresource(HttpServletRequest request,CreateSnapshotModel createsnapshotModel) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONObject jsonobect = this.getSnapshotManager().getOneResource(user.getUserId(),createsnapshotModel.getResourceUuid(), createsnapshotModel.getResourceType());
		   return  jsonobect.toString();
		}
		return "";
	}
}
