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

import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.entity.User;
import com.oncecloud.manager.RouterManager;

/**
 * @author hehai hty
 * @version 2014/07/08
 */
@Component
public class RouterAction extends HttpServlet {
	private static final long serialVersionUID = -7797574185575054134L;

	private FirewallDAO firewallDAO;
	private RouterManager routerManager;

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private RouterManager getRouterManager() {
		return routerManager;
	}

	@Autowired
	private void setRouterManager(RouterManager routerManager) {
		this.routerManager = routerManager;
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
				JSONArray ja = this.getRouterManager().routerGetList(page,
						limit, search, userId);
				out.print(ja.toString());
			} else if (action.equals("create")) {
				String poolUuid = user.getUserAllocate();
				String name = request.getParameter("name");
				String uuid = request.getParameter("uuid");
				String fwuuid = request.getParameter("fwuuid");
				int capacity = Integer.parseInt(request
						.getParameter("capacity"));
				this.getRouterManager().routerCreate(name, uuid, fwuuid,
						capacity, userId, poolUuid);
			} else if (action.equals("getablefws")) {
				JSONObject jo = this.getFirewallDAO().getSimpleFWList(userId);
				out.print(jo.toString());
			} else if (action.equals("startup")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getRouterManager().routerStartUp(uuid, userId, poolUuid);
			} else if (action.equals("shutdown")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				String force = request.getParameter("force");
				this.getRouterManager().routerShutDown(uuid, force, userId,
						poolUuid);
			} else if (action.equals("destroy")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getRouterManager().routerDestory(uuid, userId, poolUuid);
			} else if (action.equals("quota")) {
				JSONArray ja = this.getRouterManager().routerQuota(userId);
				out.print(ja.toString());
			} else if (action.equals("detail")) {
				String routeruuid = request.getParameter("routerUuid");
				session.setAttribute("routerUuid", routeruuid);
			} else if (action.equals("getonerouter")) {
				String routeruuid = request.getParameter("routerUuid");
				JSONObject jo = this.getRouterManager().routerGetOneRouter(
						routeruuid);
				out.print(jo.toString());
			} else if (action.equals("getvxnets")) {
				String routeruuid = request.getParameter("routerUuid");
				JSONArray jo = this.getRouterManager().getVxnets(routeruuid);
				out.print(jo.toString());
			}
		}
	}
}
