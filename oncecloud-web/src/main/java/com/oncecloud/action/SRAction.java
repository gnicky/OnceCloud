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
import com.oncecloud.manager.SRManager;

/**
 * @author hehai
 * @version 2014/08/25
 */
public class SRAction extends HttpServlet {
	private static final long serialVersionUID = 3496683502624450442L;
	private SRManager srManager;

	private SRManager getSrManager() {
		return srManager;
	}

	@Autowired
	private void setSrManager(SRManager srManager) {
		this.srManager = srManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("load2server")) {
				String srUuid = request.getParameter("sruuid");
				String hostUuid = request.getParameter("hostuuid");
				JSONArray ja = this.getSrManager().load2Server(userId, srUuid,
						hostUuid);
				out.print(ja.toString());
			}
		}
	}
}
