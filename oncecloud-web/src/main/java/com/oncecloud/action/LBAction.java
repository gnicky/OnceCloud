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

import com.oncecloud.dao.ForeendDAO;
import com.oncecloud.entity.User;
import com.oncecloud.manager.LBManager;

/**
 * @author hehai
 * @version 2014/07/08
 */
@Component
public class LBAction extends HttpServlet {
	private static final long serialVersionUID = -7797574185575054134L;
	private LBManager lbManager;
	private ForeendDAO foreendDAO;

	private LBManager getLbManager() {
		return lbManager;
	}

	@Autowired
	private void setLbManager(LBManager lbManager) {
		this.lbManager = lbManager;
	}

	private ForeendDAO getForeendDAO() {
		return foreendDAO;
	}

	@Autowired
	private void setForeendDAO(ForeendDAO foreendDAO) {
		this.foreendDAO = foreendDAO;
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
				JSONArray ja = this.getLbManager().lbGetList(page, limit,
						search, userId);
				out.print(ja.toString());
			} else if (action.equals("getforelist")) {
				String lbuuid = request.getParameter("lbuuid");
				JSONArray feArray = this.getForeendDAO().getFEListByLB(lbuuid);
				out.print(feArray.toString());
			} else if (action.equals("create")) {
				String poolUuid = user.getUserAllocate();
				String name = request.getParameter("name");
				String uuid = request.getParameter("uuid");
				int capacity = Integer.parseInt(request
						.getParameter("capacity"));
				this.getLbManager().lbCreate(name, uuid, capacity, userId,
						poolUuid);
			} else if (action.equals("createfore")) {
				String name = request.getParameter("name");
				String foreuuid = request.getParameter("foreuuid");
				String lbuuid = request.getParameter("lbuuid");
				String protocol = request.getParameter("protocol");
				int port = Integer.parseInt(request.getParameter("port"));
				// 0＝轮询, 1＝最小连接
				int policy = Integer.parseInt(request.getParameter("policy"));
				this.getLbManager().lbCreateFore(name, foreuuid, lbuuid,
						protocol, port, policy, userId);
			} else if (action.equals("createback")) {
				String name = request.getParameter("name");
				String lbuuid = request.getParameter("lbuuid");
				String backuuid = request.getParameter("backuuid");
				String vmuuid = request.getParameter("vmuuid");
				String vmip = request.getParameter("vmip");
				int port = Integer.parseInt(request.getParameter("port"));
				int weight = Integer.parseInt(request.getParameter("weight"));
				String feuuid = request.getParameter("feuuid");
				this.getLbManager().lbCreateBack(name, lbuuid, backuuid,
						vmuuid, vmip, port, weight, feuuid, userId);
			} else if (action.equals("checkfore")) {
				String lbUuid = request.getParameter("lbuuid");
				int port = Integer.parseInt(request.getParameter("port"));
				boolean result = this.getForeendDAO().checkRepeat(lbUuid, port);
				out.print(result);
			} else if (action.equals("checkback")) {
				String beuuid = request.getParameter("beuuid");
				int port = Integer.parseInt(request.getParameter("port"));
				JSONObject jo = this.getLbManager().lbCheckBack(beuuid, port);
				out.print(jo);
			} else if (action.equals("deletefore")) {
				String foreuuid = request.getParameter("foreuuid");
				String lbuuid = request.getParameter("lbuuid");
				JSONObject jo = this.getLbManager().lbDeletefore(foreuuid,
						lbuuid, userId);
				out.print(jo.toString());
			} else if (action.equals("deleteback")) {
				String backuuid = request.getParameter("backuuid");
				String lbuuid = request.getParameter("lbuuid");
				JSONObject jo = this.getLbManager().lbDeleteBack(backuuid,
						lbuuid, userId);
				out.print(jo.toString());
			} else if (action.equals("forbidfore")) {
				String foreUuid = request.getParameter("foreuuid");
				int state = Integer.parseInt(request.getParameter("state"));
				String lbuuid = request.getParameter("lbuuid");
				JSONObject jo = this.getLbManager().lbForbidfore(foreUuid,
						state, lbuuid, userId);
				out.print(jo.toString());
			} else if (action.equals("forbidback")) {
				String backuuid = request.getParameter("backuuid");
				int state = Integer.parseInt(request.getParameter("state"));
				String lbuuid = request.getParameter("lbuuid");
				JSONObject jo = this.getLbManager().lbForbidback(backuuid,
						state, lbuuid, userId);
				out.print(jo.toString());
			} else if (action.equals("updatefore")) {
				String foreName = request.getParameter("name");
				String foreUuid = request.getParameter("foreuuid");
				// 0＝轮询, 1＝最小连接
				int forePolicy = Integer.parseInt(request
						.getParameter("policy"));
				String lbuuid = request.getParameter("lbuuid");
				this.getLbManager().lbUpdatefore(foreName, foreUuid,
						forePolicy, lbuuid, userId);
			} else if (action.equals("applylb")) {
				String lbuuid = request.getParameter("lbuuid");
				JSONObject jo = this.getLbManager().lbApplylb(lbuuid, userId);
				out.print(jo.toString());
			} else if (action.equals("startup")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getLbManager().lbShutup(uuid, userId, poolUuid);
				out.print("");
			} else if (action.equals("shutdown")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				String force = request.getParameter("force");
				this.getLbManager().lbShutdown(uuid, force, userId, poolUuid);
				out.print("");
			} else if (action.equals("destroy")) {
				String poolUuid = user.getUserAllocate();
				String uuid = request.getParameter("uuid");
				this.getLbManager().lbDelete(uuid, userId, poolUuid);
				out.print("");
			} else if (action.equals("quota")) {
				JSONArray ja = this.getLbManager().lbQuota(userId);
				out.print(ja.toString());
			} else if (action.equals("detail")) {
				String lbuuid = request.getParameter("lbUuid");
				session.setAttribute("lbUuid", lbuuid);
			} else if (action.equals("getonelb")) {
				String lbuuid = request.getParameter("lbUuid");
				JSONObject jo = this.getLbManager().getonelb(lbuuid);
				out.print(jo.toString());
			} else if (action.equals("getadminlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String host = request.getParameter("host");
				int importance = Integer.parseInt(request
						.getParameter("importance"));
				String type = request.getParameter("type");
				JSONArray ja = this.getLbManager().getAdminLBList(page, limit,
						host, importance, type);
				out.print(ja.toString());
			}
		}
	}
}
