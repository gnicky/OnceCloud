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
		int userId = user.getUserId();
		JSONArray ja = this.getSnapshotManager().getSnapshotList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.GET })
	@ResponseBody
	public String quota(HttpServletRequest request, @RequestParam int count) {
		User user = (User) request.getSession().getAttribute("user");
		String result = this.getSnapshotManager().getQuota(user.getUserId(),
				count);
		return result;
	}

	@RequestMapping(value = "/CreateSnapshot", method = { RequestMethod.POST })
	@ResponseBody
	public String createSnapshot(HttpServletRequest request,
			CreateSnapshotModel createsnapshotModel) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jsonobect = this.getSnapshotManager().createSnapshot(
				user.getUserId(), createsnapshotModel.getSnapshotId(),
				createsnapshotModel.getSnapshotName(),
				createsnapshotModel.getResourceUuid(),
				createsnapshotModel.getResourceType());
		return jsonobect.toString();
	}

	@RequestMapping(value = "/DetailList", method = { RequestMethod.GET })
	@ResponseBody
	public String detailList(HttpServletRequest request,
			CreateSnapshotModel createsnapshotModel) {
		JSONArray ja = this.getSnapshotManager().getDetailList(
				createsnapshotModel.getResourceUuid(),
				createsnapshotModel.getResourceType());
		return ja.toString();
	}

	@RequestMapping(value = "/BasicList", method = { RequestMethod.GET })
	@ResponseBody
	public String basicList(HttpServletRequest request,
			CreateSnapshotModel createsnapshotModel) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jsonobect = this.getSnapshotManager().getBasicList(
				user.getUserId(), createsnapshotModel.getResourceUuid(),
				createsnapshotModel.getResourceType());
		return jsonobect.toString();
	}

	@RequestMapping(value = "/DeleteSnapshotSeries", method = { RequestMethod.GET })
	@ResponseBody
	public String deleteSnapshotSeries(HttpServletRequest request,
			@RequestParam String resourceUuid, @RequestParam String resourceType) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jsonobect = this.getSnapshotManager().deleteSnapshotSeries(
				userId, resourceUuid, resourceType);
		return jsonobect.toString();
	}

	@RequestMapping(value = "/DeleteSnapshot", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteSnapshot(HttpServletRequest request, @RequestParam String snapshotId,
			@RequestParam String resourceUuid, @RequestParam String resourceType) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getSnapshotManager().deleteSnapshot(userId, snapshotId, resourceUuid, resourceType);
		return jo.toString();
	}
	
	@RequestMapping(value = "/Rollback", method = { RequestMethod.POST })
	@ResponseBody
	public String rollbackSnapshot(HttpServletRequest request, @RequestParam String snapshotId,
			@RequestParam String resourceUuid, @RequestParam String resourceType) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getSnapshotManager().rollbackSnapshot(userId, snapshotId, resourceUuid, resourceType);
		return jo.toString();
	}
}