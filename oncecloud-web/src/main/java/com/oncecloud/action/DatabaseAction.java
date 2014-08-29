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
import com.oncecloud.manager.DatabaseManager;

/**
 * @author hehai
 * @version 2014/08/25
 */
@Component
public class DatabaseAction extends HttpServlet {
	private static final long serialVersionUID = 7473731904034230348L;
	private DatabaseManager databaseManager;

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
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getDatabaseManager().getDatabaseList(
						userId, page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("create")) {
				String hostUuid = user.getUserAllocate();
				String dbUuid = request.getParameter("dbuuid");
				String dbName = request.getParameter("dbname");
				String dbUser = request.getParameter("dbuser");
				String dbPwd = request.getParameter("dbpwd");
				int dbPort = Integer.parseInt(request.getParameter("dbport"));
				String dbType = request.getParameter("dbtype");
				int dbthroughout = Integer.parseInt(request
						.getParameter("dbthroughout"));
				this.getDatabaseManager().doCreateDB(userId, dbUuid, dbName,
						dbUser, dbPwd, dbPort, dbType, dbthroughout, hostUuid);
			} else if (action.equals("startup")) {
				String dbUuid = request.getParameter("uuid");
				this.getDatabaseManager().doStartupDB(userId, dbUuid);
			} else if (action.equals("destory")) {
				String dbUuid = request.getParameter("uuid");
				this.getDatabaseManager().doDeleteDB(userId, dbUuid);
			} else if (action.equals("shutdown")) {
				String dbUuid = request.getParameter("uuid");
				String forse = request.getParameter("forse");
				this.getDatabaseManager().doShutdownDB(userId, dbUuid, forse);
			}
		}
	}
}
