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
import com.oncecloud.manager.PoolManager;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class PoolAction extends HttpServlet {
	private static final long serialVersionUID = 4089790606490761161L;
	private PoolManager poolManager;

	private PoolManager getPoolManager() {
		return poolManager;
	}

	@Autowired
	private void setPoolManager(PoolManager poolManager) {
		this.poolManager = poolManager;
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
				String poolName = request.getParameter("poolname");
				String poolDesc = request.getParameter("pooldesc");
				String dcUuid = request.getParameter("dcuuid");
				String dcName = request.getParameter("dcname");
				JSONArray ja = this.getPoolManager().createPool(poolName,
						poolDesc, dcUuid, dcName, userId);
				out.print(ja.toString());
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limitnum"));
				String search = request.getParameter("search");
				JSONArray ja = this.getPoolManager().getPoolList(page, limit,
						search);
				out.print(ja.toString());
			} else if (action.equals("delete")) {
				String poolId = request.getParameter("poolid");
				String poolName = request.getParameter("poolname");
				JSONArray ja = this.getPoolManager().deletePool(poolId,
						poolName, userId);
				out.print(ja.toString());
			} else if (action.equals("bind")) {
				String poolId = request.getParameter("poolid");
				String dcid = request.getParameter("dcid");
				this.getPoolManager().bind(poolId, dcid, userId);
			} else if (action.equals("unbind")) {
				String poolId = request.getParameter("poolid");
				this.getPoolManager().unbind(poolId, userId);
			} else if (action.equals("update")) {
				String poolUuid = request.getParameter("pooluuid");
				String poolName = request.getParameter("poolname");
				String poolDesc = request.getParameter("pooldesc");
				String dcUuid = request.getParameter("dcuuid");
				this.getPoolManager().updatePool(poolUuid, poolName, poolDesc,
						dcUuid, userId);
			} else if (action.equals("getallpool")) {
				JSONArray ja = this.getPoolManager().getAllPool();
				out.print(ja.toString());
			} else if (action.equals("keepaccordance")) {
				String poolUuid = request.getParameter("pooluuid");
				this.getPoolManager().keepAccordance(userId, poolUuid);
			}
		}
	}
}
