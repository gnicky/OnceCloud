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
import com.oncecloud.manager.VolumeManager;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class VolumeAction extends HttpServlet {
	private static final long serialVersionUID = 1657977316431427617L;
	private VolumeManager volumeManager;

	private VolumeManager getVolumeManager() {
		return volumeManager;
	}

	@Autowired
	private void setVolumeManager(VolumeManager volumeManager) {
		this.volumeManager = volumeManager;
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
				JSONArray ja = this.getVolumeManager().getVolumeList(userId,
						page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("create")) {
				String volUuid = request.getParameter("volumeuuid");
				String volName = request.getParameter("volumeName");
				int volSize = Integer.parseInt(request
						.getParameter("volumeSize"));
				this.getVolumeManager().createVolume(userId, volUuid, volName,
						volSize);
			} else if (action.equals("delete")) {
				String volUuid = request.getParameter("volumeuuid");
				this.getVolumeManager().deleteVolume(userId, volUuid);
			} else if (action.equals("bind")) {
				String volUuid = request.getParameter("volumeuuid");
				String vmUuid = request.getParameter("vmuuid");
				this.getVolumeManager().bindVolume(userId, volUuid, vmUuid);
			} else if (action.equals("unbind")) {
				String volUuid = request.getParameter("volumeuuid");
				this.getVolumeManager().unbindVolume(userId, volUuid);
			} else if (action.equals("quota")) {
				int count = Integer.parseInt(request.getParameter("count"));
				int size = Integer.parseInt(request.getParameter("size"));
				String quota = this.getVolumeManager().getQuota(userId, count,
						size);
				out.print(quota);
			} else if (action.equals("detail")) {
				String volumeuuid = request.getParameter("volumeuuid");
				session.setAttribute("volumeUuid", volumeuuid);
			} else if (action.equals("getvolumedetail")) {
				String volUuid = request.getParameter("volumeuuid");
				JSONObject jo = this.getVolumeManager()
						.getVolumeDetail(volUuid);
				out.print(jo.toString());
			} else if (action.equals("getabledvolumes")) {
				JSONArray ja = this.getVolumeManager().getAbledVolumeList(
						userId);
				out.print(ja.toString());
			} else if (action.equals("getvolumesbyvm")) {
				String vmUuid = request.getParameter("vmuuid");
				JSONArray ja = this.getVolumeManager()
						.getVolumeListByVM(vmUuid);
				out.print(ja.toString());
			}
		}
	}
}
