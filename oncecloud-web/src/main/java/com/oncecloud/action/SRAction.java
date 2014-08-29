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
@Component
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
			if (action.equals("add")) {
				String srname = request.getParameter("srname");
				String srAddress = request.getParameter("srAddress");
				String srDesc = request.getParameter("srDesc");
				String srType = request.getParameter("srType");
				String srDir = request.getParameter("srdir");
				String rackId = request.getParameter("rackid");
				String rackName = request.getParameter("rackname");
				JSONArray ja = this.getSrManager().addStorage(userId, srname,
						srAddress, srDesc, srType, srDir, rackId, rackName);
				out.print(ja.toString());
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limitnum"));
				String search = request.getParameter("search");
				JSONArray ja = this.getSrManager().getStorageList(page, limit,
						search);
				out.print(ja.toString());
			} else if (action.equals("delete")) {
				String srId = request.getParameter("srid");
				String srName = request.getParameter("srname");
				JSONArray ja = this.getSrManager().deleteStorage(userId, srId,
						srName);
				out.print(ja.toString());
			} else if (action.equals("load2server")) {
				String srUuid = request.getParameter("sruuid");
				String hostUuid = request.getParameter("hostuuid");
				JSONArray ja = this.getSrManager().load2Server(userId, srUuid,
						hostUuid);
				out.print(ja.toString());
			} else if (action.equals("queryaddress")) {
				String address = request.getParameter("address");
				JSONArray ja = this.getSrManager().getStorageByAddress(address);
				out.print(ja.toString());
			} else if (action.equals("update")) {
				String srId = request.getParameter("srid");
				String srName = request.getParameter("srname");
				String srDesc = request.getParameter("srDesc");
				String rackId = request.getParameter("rackid");
				this.getSrManager().updateStorage(userId, srId, srName, srDesc,
						rackId);
			}
		}
	}
}
