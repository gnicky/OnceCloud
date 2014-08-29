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
import com.oncecloud.manager.VMManager;

/**
 * @author hehai yly
 * @version 2014/06/28
 */
@Component
public class VMAction extends HttpServlet {
	private static final long serialVersionUID = -5507074693173158640L;
	private VMManager vmManager;

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
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
				String poolUuid = user.getUserAllocate();
				String vmUuid = request.getParameter("vmuuid");
				String tplUuid = request.getParameter("tpluuid");
				int cpu = Integer.parseInt(request.getParameter("cpuCore"));
				double memory = Double.parseDouble(request
						.getParameter("memoryCapacity"));
				String vmName = request.getParameter("vmName");
				String loginPwd = request.getParameter("loginPwd");
				this.getVmManager().doCreateVM(vmUuid, tplUuid, userId, vmName,
						cpu, memory, loginPwd, poolUuid);
			} else if (action.equals("startup")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getVmManager().doStartVM(userId, uuid, poolUuid);
			} else if (action.equals("shutdown")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				String force = request.getParameter("force");
				this.getVmManager().doShutdownVM(userId, uuid, force, poolUuid);
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getVmManager().getVMList(userId, page,
						limit, search);
				out.print(ja.toString());
			} else if (action.equals("simplelist")) {
				JSONArray ja = this.getVmManager().getSimpleVMList(userId);
				out.print(ja.toString());
			} else if (action.equals("restart")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getVmManager().doRestartVM(userId, uuid, poolUuid);
			} else if (action.equals("destroy")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getVmManager().doDelete(userId, uuid, poolUuid);
			} else if (action.equals("quota")) {
				int userLevel = user.getUserLevel();
				int count = Integer.parseInt(request.getParameter("count"));
				String quota = this.getVmManager().getQuota(userId, userLevel,
						count);
				out.print(quota);
			} else if (action.equals("detail")) {
				String vmUuid = request.getParameter("instanceuuid");
				session.setAttribute("instanceUuid", vmUuid);
			} else if (action.equals("getoneinstance")) {
				String vmUuid = request.getParameter("instanceUuid");
				JSONObject jo = this.getVmManager().getVMDetail(vmUuid);
				out.print(jo.toString());
			} else if (action.equals("getadminlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String host = request.getParameter("host");
				int importance = Integer.parseInt(request
						.getParameter("importance"));
				String type = request.getParameter("type");
				JSONArray ja = this.getVmManager().getAdminVMList(page, limit,
						host, importance, type);
				out.print(ja.toString());
			}
		}
	}
}
