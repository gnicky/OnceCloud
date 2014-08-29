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
import com.oncecloud.manager.HostManager;

/**
 * @author hehai
 * @version 2014/06/17
 */
@Component
public class HostAction extends HttpServlet {
	private static final long serialVersionUID = -165030289898263867L;
	private HostManager hostManager;

	private HostManager getHostManager() {
		return hostManager;
	}

	@Autowired
	private void setHostManager(HostManager hostManager) {
		this.hostManager = hostManager;
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
				String hostName = request.getParameter("hostname");
				String hostPwd = request.getParameter("hostpwd");
				String hostDesc = request.getParameter("hostdesc");
				String hostIp = request.getParameter("hostip");
				String rackUuid = request.getParameter("rackUuid");
				String rackName = request.getParameter("rackName");
				JSONArray ja = this.getHostManager().createHost(hostName,
						hostPwd, hostDesc, hostIp, rackUuid, rackName, userId);
				out.print(ja.toString());
			} else if (action.equals("getlist")) {
				String pageStr = request.getParameter("page");
				String limitStr = request.getParameter("limitnum");
				String searchStr = request.getParameter("search");
				JSONArray ja = this.getHostManager().getHostList(pageStr,
						limitStr, searchStr);
				out.print(ja.toString());
			} else if (action.equals("getloadlist")) {
				String pageStr = request.getParameter("page");
				String limitStr = request.getParameter("limitnum");
				String searchStr = request.getParameter("search");
				String srUuid = request.getParameter("sruuid");
				JSONArray ja = this.getHostManager().getHostLoadList(pageStr,
						limitStr, searchStr, srUuid);
				out.print(ja.toString());
			} else if (action.equals("bind")) {
				String srUuid = request.getParameter("sruuid");
				String hostUuid = request.getParameter("hostuuid");
				this.getHostManager().bindHost(srUuid, hostUuid, userId);
			} else if (action.equals("getsrofhost")) {
				String hostUuid = request.getParameter("hostuuid");
				this.getHostManager().getSrOfHost(hostUuid);
			} else if (action.equals("unbindsr")) {
				String hostUuid = request.getParameter("hostuuid");
				String srUuid = request.getParameter("sruuid");
				this.getHostManager().unbindSr(hostUuid, srUuid, userId);
			} else if (action.equals("issamesr")) {
				String uuidJsonStr = request.getParameter("uuidjsonstr");
				JSONArray ja = this.getHostManager().isSameSr(uuidJsonStr);
				out.print(ja.toString());
			} else if (action.equals("getablepool")) {
				String uuidJsonStr = request.getParameter("uuidjsonstr");
				JSONArray ja = this.getHostManager().getTablePool(uuidJsonStr);
				out.print(ja.toString());
			} else if (action.equals("delete")) {
				String hostId = request.getParameter("hostid");
				String hostName = request.getParameter("hostname");
				JSONArray ja = this.getHostManager().deleteAction(hostId,
						hostName, userId);
				out.print(ja.toString());
			} else if (action.equals("queryaddress")) {
				String address = request.getParameter("address");
				JSONArray ja = this.getHostManager().queryAddress(address);
				out.print(ja.toString());
			} else if (action.equals("a2pool")) {
				String uuidJsonStr = request.getParameter("uuidjsonstr");
				String hasMaster = request.getParameter("hasmaster");
				String poolUuid = request.getParameter("pooluuid");
				JSONArray ja = this.getHostManager().add2Pool(uuidJsonStr,
						hasMaster, poolUuid, userId);
				out.print(ja.toString());
			} else if (action.equals("r4pool")) {
				String hostUuid = request.getParameter("hostuuid");
				JSONArray ja = this.getHostManager().r4Pool(hostUuid, userId);
				out.print(ja.toString());
			} else if (action.equals("getHostForImage")) {
				out.print(this.getHostManager().getHostForImage().toString());
			} else if (action.equals("detail")) {
				String hostid = request.getParameter("hostid");
				session.setAttribute("hostUuid", hostid);
			} else if (action.equals("getonehost")) {
				String hostid = request.getParameter("hostUuid");
				JSONArray ja = this.getHostManager().getOneHost(hostid);
				out.print(ja.toString());
			} else if (action.equals("update")) {
				String hostId = request.getParameter("hostid");
				String hostName = request.getParameter("hostname");
				String hostDesc = request.getParameter("hostdesc");
				String rackUuid = request.getParameter("rackUuid");
				HostManager
						.update(hostId, hostName, hostDesc, rackUuid, userId);
			}
		}
	}
}
