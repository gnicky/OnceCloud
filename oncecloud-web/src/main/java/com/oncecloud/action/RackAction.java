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
import com.oncecloud.manager.RackManager;

/**
 * @author hehai yly
 * @version 2014/08/23
 */
public class RackAction extends HttpServlet {
	private static final long serialVersionUID = 8115097537107892427L;
	private RackManager rackManager;

	private RackManager getRackManager() {
		return rackManager;
	}

	@Autowired
	private void setRackManager(RackManager rackManager) {
		this.rackManager = rackManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("create")) {
				String rackName = request.getParameter("rackname");
				String rackDesc = request.getParameter("rackdesc");
				String dcId = request.getParameter("dcid");
				JSONArray ja = this.getRackManager().createRack(rackName, dcId,
						rackDesc, userId);
				out.print(ja.toString());
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limitnum"));
				String search = request.getParameter("search");
				JSONArray ja = this.getRackManager().getRackList(page, limit,
						search);
				out.print(ja.toString());
			} else if (action.equals("getalllist")) {
				JSONArray qaArray = this.getRackManager().getRackAllList();
				out.print(qaArray.toString());
			} else if (action.equals("delete")) {
				String rackId = request.getParameter("rackid");
				String rackName = request.getParameter("rackname");
				this.getRackManager().deleteRack(rackId, rackName, userId);
			} else if (action.equals("bind")) {
				String rackId = request.getParameter("rackid");
				String dcId = request.getParameter("dcid");
				JSONArray ja = this.getRackManager().bind(rackId, dcId, userId);
				out.print(ja.toString());
			} else if (action.equals("unbind")) {
				String rackId = request.getParameter("rackid");
				JSONArray ja = this.getRackManager().unbind(rackId, userId);
				out.print(ja.toString());
			} else if (action.equals("update")) {
				String rackId = request.getParameter("rackid");
				String rackName = request.getParameter("rackname");
				String rackDesc = request.getParameter("rackdesc");
				String dcId = request.getParameter("dcid");
				this.getRackManager().update(rackId, rackName, rackDesc, dcId,
						userId);
			} else if (action.equals("detail")) {
				String rackid = request.getParameter("rackid");
				session.setAttribute("rackid", rackid);
			}
		}
	}
}
