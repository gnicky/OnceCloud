package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.User;
import com.oncecloud.manager.DatacenterManager;

/**
 * @author hehai
 * @version 2014/08/23
 */
public class DatacenterAction extends HttpServlet {
	private static final long serialVersionUID = 841283868952006704L;
	private DatacenterManager datacenterManager;

	private DatacenterManager getDatacenterManager() {
		return datacenterManager;
	}

	private void setDatacenterManager(DatacenterManager datacenterManager) {
		this.datacenterManager = datacenterManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userid = user.getUserId();
			if (action.equals("create")) {
				String dcName = request.getParameter("dcname");
				String dcLocation = request.getParameter("dclocation");
				String dcDesc = request.getParameter("dcdesc");
				JSONArray ja = this.getDatacenterManager().createDatacenter(
						dcName, dcLocation, dcDesc, userid);
				out.print(ja.toString());
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limitnum"));
				String search = request.getParameter("search");
				JSONArray ja = this.getDatacenterManager().getDatacenterList(
						page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("getalllist")) {
				JSONArray qaArray = this.getDatacenterManager()
						.getDatacenterAllList();
				out.print(qaArray.toString());
			} else if (action.equals("getPoolList")) {
				String dcid = (String) session.getAttribute("dcid");
				JSONArray qaArray = this.getDatacenterManager().getPoolList(
						dcid);
				out.print(qaArray.toString());
			} else if (action.equals("getRackList")) {
				String dcid = (String) session.getAttribute("dcid");
				JSONArray qaArray = this.getDatacenterManager().getRackList(
						dcid);
				out.print(qaArray.toString());
			} else if (action.equals("update")) {
				String dcUuid = request.getParameter("dcuuid");
				String dcName = request.getParameter("dcname");
				String dcLocation = request.getParameter("dclocation");
				String dcDesc = request.getParameter("dcdesc");
				this.getDatacenterManager().update(dcUuid, dcName, dcLocation,
						dcDesc, userid);
			}
		}
	}
}
