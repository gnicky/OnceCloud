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
import com.oncecloud.manager.DashboardManager;

/**
 * @author hehai
 * @version 2014/05/09
 */
@Component
public class DashboardAction extends HttpServlet {
	private static final long serialVersionUID = 2234918116676127248L;
	private DashboardManager dashboardManager;

	private DashboardManager getDashboardManager() {
		return dashboardManager;
	}

	@Autowired
	private void setDashboardManager(DashboardManager dashboardManager) {
		this.dashboardManager = dashboardManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			if (action.equals("getSwitchlist")) {
				String rackId = (String) session.getAttribute("rackid");
				JSONArray ja = this.getDashboardManager().getSwitchList(rackId);
				out.print(ja.toString());
			} else if (action.equals("switchdetail")) {
				String switchid = request.getParameter("switchid");
				session.setAttribute("switchid", switchid);
			} else if (action.equals("getSwitch")) {
				String getSwitch = session.getAttribute("switchid").toString();
				JSONArray ja = this.getDashboardManager().getSwitch(getSwitch);
				out.print(ja.toString());
			} else if (action.equals("getTuoputu")) {
				String rackId = session.getAttribute("rackid").toString();
				JSONArray ja = this.getDashboardManager().getTuoputu(rackId);
				out.print(ja.toString());
			}
		}
	}
}
