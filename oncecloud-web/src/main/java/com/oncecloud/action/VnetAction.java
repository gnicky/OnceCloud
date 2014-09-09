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

import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.User;
import com.oncecloud.manager.VnetManager;

/**
 * @author hehai hty
 * @version 2014/07/08
 */
public class VnetAction extends HttpServlet {
	private static final long serialVersionUID = -7797574185575054134L;

	private RouterDAO routerDAO;
	private VMDAO vmDAO;
	private VnetManager vnetManager;

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private VnetManager getVnetManager() {
		return vnetManager;
	}

	@Autowired
	private void setVnetManager(VnetManager vnetManager) {
		this.vnetManager = vnetManager;
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
				String name = request.getParameter("name");
				String uuid = request.getParameter("uuid");
				String desc = request.getParameter("desc");
				this.getVnetManager().vnetCreate(name, uuid, desc, userId);
			} else if (action.equals("getablerts")) {
				JSONArray jo = this.getRouterDAO().getRoutersOfUser(userId);
				out.print(jo.toString());
			} else if (action.equals("quota")) {
				JSONArray ja = this.getVnetManager().vnetQuota(userId);
				out.print(ja.toString());
			} else if (action.equals("checknet")) {
				String routerid = request.getParameter("routerid");
				Integer net = Integer.parseInt(request.getParameter("net"));
				JSONObject jo = this.getVnetManager().vnetChecknet(routerid,
						net, userId);
				out.print(jo.toString());
			} else if (action.equals("linkrouter")) {
				String vnetuuid = request.getParameter("vnetid");
				String routerid = request.getParameter("routerid");
				Integer net = Integer.parseInt(request.getParameter("net"));
				Integer gate = Integer.parseInt(request.getParameter("gate"));
				Integer start = Integer.parseInt(request.getParameter("start"));
				Integer end = Integer.parseInt(request.getParameter("end"));
				Integer dhcpState = Integer.parseInt(request
						.getParameter("dhcpState"));
				JSONObject jo = this.getVnetManager().vnetLinkrouter(userId,
						vnetuuid, routerid, net, gate, start, end, dhcpState);
				out.print(jo);
			} else if (action.equals("getVms")) {
				String vnUuid = request.getParameter("vnUuid");
				JSONArray ja = this.getVnetManager().getVMs(vnUuid);
				out.print(ja.toString());
			}
		}
	}
}
