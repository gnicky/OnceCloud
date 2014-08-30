package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.User;
import com.oncecloud.manager.SnapshotManager;

/**
 * @author yly hehai
 * @version 2014/08/23
 */
public class SnapshotAction extends HttpServlet {
	private static final long serialVersionUID = 954252847305367390L;

	private SnapshotManager snapshotManager;

	private SnapshotManager getSnapshotManager() {
		return snapshotManager;
	}

	@Autowired
	private void setSnapshotManager(SnapshotManager snapshotManager) {
		this.snapshotManager = snapshotManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getSnapshotManager().getSnapshotList(
						userId, page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("create")) {
				String snapshotId = request.getParameter("snapshotId");
				String snapshotName = request.getParameter("snapshotName");
				String resourceUuid = request.getParameter("resourceUuid");
				String resourceType = request.getParameter("resourceType");
				JSONObject jo = this.getSnapshotManager().createSnapshot(
						userId, snapshotId, snapshotName, resourceUuid,
						resourceType);
				out.print(jo.toString());
			} else if (action.equals("delete")) {
				String resourceUuid = request.getParameter("rsuuid");
				String resourceType = request.getParameter("rstype");
				JSONObject jo = this.getSnapshotManager().deleteSnapshot(
						userId, resourceUuid, resourceType);
				out.print(jo.toString());
			} else if (action.equals("detail")) {
				session.setAttribute("resourceUuid",
						request.getParameter("resourceUuid"));
				session.setAttribute("resourceType",
						request.getParameter("resourceType"));
				session.setAttribute("resourceName",
						request.getParameter("resourceName"));
			} else if (action.equals("getdetaillist")) {
				String resourceUuid = request.getParameter("resourceUuid");
				String resourceType = request.getParameter("resourceType");
				JSONArray ja = this.getSnapshotManager().getDetailList(
						resourceUuid, resourceType);
				out.print(ja.toString());
			} else if (action.equals("rollback")) {
				String snapshotId = request.getParameter("id");
				String resourceUuid = request.getParameter("rsuuid");
				String resourceType = request.getParameter("rstype");
				JSONObject jo = this.getSnapshotManager().rollbackSnapshot(
						userId, snapshotId, resourceUuid, resourceType);
				out.print(jo.toString());
			} else if (action.equals("deleteone")) {
				String snapshotId = request.getParameter("id");
				String resourceUuid = request.getParameter("rsuuid");
				String resourceType = request.getParameter("rstype");
				JSONObject jo = this.getSnapshotManager().deleteOneSnapshot(
						userId, snapshotId, resourceUuid, resourceType);
				out.print(jo.toString());
			} else if (action.equals("getoneresource")) {
				String resourceUuid = request.getParameter("resourceUuid");
				String resourceType = request.getParameter("resourceType");
				JSONObject jo = this.getSnapshotManager().getOneResource(
						userId, resourceUuid, resourceType);
				out.print(jo.toString());
			} else if (action.equals("quota")) {
				int count = Integer.parseInt(request.getParameter("count"));
				String result = this.getSnapshotManager().getQuota(userId,
						count);
				out.print(result);
			}
		}
	}
}
