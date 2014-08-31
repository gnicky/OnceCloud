package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.SnapshotManager;
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
}
