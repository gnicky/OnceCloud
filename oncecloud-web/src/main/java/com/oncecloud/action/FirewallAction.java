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

import com.oncecloud.entity.User;
import com.oncecloud.manager.DatabaseManager;
import com.oncecloud.manager.FirewallManager;
import com.oncecloud.manager.LBManager;
import com.oncecloud.manager.RouterManager;
import com.oncecloud.manager.VMManager;

/**
 * @author hehai yly
 * @version 2014/08/25
 */
public class FirewallAction extends HttpServlet {
	private static final long serialVersionUID = -166260308972686968L;
	private FirewallManager firewallManager;
	private VMManager vmManager;
	private LBManager lbManager;
	private RouterManager routerManager;
	private DatabaseManager databaseManager;

	private FirewallManager getFirewallManager() {
		return firewallManager;
	}

	@Autowired
	private void setFirewallManager(FirewallManager firewallManager) {
		this.firewallManager = firewallManager;
	}

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	private LBManager getLbManager() {
		return lbManager;
	}

	@Autowired
	private void setLbManager(LBManager lbManager) {
		this.lbManager = lbManager;
	}

	private RouterManager getRouterManager() {
		return routerManager;
	}

	@Autowired
	private void setRouterManager(RouterManager routerManager) {
		this.routerManager = routerManager;
	}

	private DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	@Autowired
	private void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		User user = (User) session.getAttribute("user");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("getablevms")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getVmManager().getAbleVMs(userId, page,
						limit, search);
				out.print(ja.toString());
			} else if (action.equals("getablelbs")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getLbManager().getAbleLBs(userId, page,
						limit, search);
				out.print(ja.toString());
			} else if (action.equals("getablerts")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getRouterManager().getAbleRTs(userId, page,
						limit, search);
				out.print(ja.toString());
			} else if (action.equals("getabledbs")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getDatabaseManager().getAbleDBs(userId,
						page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("getabledfirewalls")) {
				JSONArray ja = this.getFirewallManager().getAvailableFirewalls(
						userId);
				out.print(ja.toString());
			}
		}
	}
}
