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
			if (action.equals("create")) {
				String firewallName = request.getParameter("firewallName");
				String firewallUuid = request.getParameter("firewallUuid");
				JSONObject jo = this.getFirewallManager().createFirewall(
						userId, firewallName, firewallUuid);
				out.print(jo.toString());
			} else if (action.equals("detail")) {
				String firewallId = request.getParameter("firewallId");
				session.setAttribute("firewallId", firewallId);
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getFirewallManager().getFirewallList(
						userId, page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("getfirewall")) {
				String firewallId = request.getParameter("firewallId");
				JSONObject jo = this.getFirewallManager().getFirewallDetail(
						userId, firewallId);
				out.print(jo.toString());
			} else if (action.equals("createrule")) {
				String ruleId = request.getParameter("ruleId");
				String ruleName = request.getParameter("ruleName");
				int rulePriority = Integer.parseInt(request
						.getParameter("rulePriority"));
				String ruleProtocol = request.getParameter("ruleProtocol");
				String ruleIp = request.getParameter("ruleIp");
				String ruleFirewall = request.getParameter("firewallId");
				int ruleSport = 0;
				int ruleEport = 0;
				if (ruleProtocol.equals("TCP") || ruleProtocol.equals("UDP")) {
					ruleSport = Integer.parseInt(request
							.getParameter("ruleSport"));
					ruleEport = Integer.parseInt(request
							.getParameter("ruleEport"));
				}
				JSONObject jo = this.getFirewallManager().createRule(out,
						ruleId, ruleName, rulePriority, ruleProtocol, ruleIp,
						ruleFirewall, ruleSport, ruleEport);
				out.print(jo);
			} else if (action.equals("getrulelist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				String firewallId = request.getParameter("firewallId");
				JSONArray ja = this.getFirewallManager().getRuleList(page,
						limit, search, firewallId);
				out.print(ja.toString());
			} else if (action.equals("deleterule")) {
				String ruleId = request.getParameter("ruleId");
				String firewallId = request.getParameter("firewallId");
				JSONObject jo = this.getFirewallManager().deleteRule(ruleId,
						firewallId);
				out.print(jo.toString());
			} else if (action.equals("banrule")) {
				String ruleId = request.getParameter("ruleId");
				JSONObject jo = this.getFirewallManager().banRule(ruleId);
				out.print(jo.toString());
			} else if (action.equals("bindfirewall")) {
				String firewallId = request.getParameter("firewallId");
				String vmuuidStr = request.getParameter("vmuuidStr");
				String bindtype = request.getParameter("bindtype");
				JSONObject jo = this.getFirewallManager().bindFirewall(userId,
						firewallId, vmuuidStr, bindtype);
				out.print(jo.toString());
			} else if (action.equals("deletefirewall")) {
				String firewallId = request.getParameter("firewallId");
				JSONObject jo = this.getFirewallManager().deleteFirewall(
						userId, firewallId);
				out.print(jo.toString());
			} else if (action.equals("quota")) {
				int count = Integer.parseInt(request.getParameter("count"));
				String quota = this.getFirewallManager()
						.getQuota(userId, count);
				out.print(quota);
			} else if (action.equals("applyfirewall")) {
				String firewallId = request.getParameter("firewallId");
				JSONObject jo = this.getFirewallManager().updateFirewall(
						userId, firewallId);
				out.print(jo.toString());
			} else if (action.equals("getablevms")) {
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
				JSONArray ja = this.getFirewallManager().getAbledFirewallList(
						userId);
				out.print(ja.toString());
			}
		}
	}
}
