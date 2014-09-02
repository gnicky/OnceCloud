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
import com.oncecloud.manager.UserManager;

/**
 * @author hehai wuheng tangzhen
 * @version 2014/08/25
 */
public class UserAction extends HttpServlet {
	private static final long serialVersionUID = 3496683502624450442L;
	private UserManager userManager;

	private UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	private void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		User user = (User) session.getAttribute("user");
		// 登录登出
		if (action.equals("adminlogin")) {
			this.getUserManager().doAdminLogin(request, response, basePath,
					session);
			return;
		} else if (action.equals("logout")) {
			this.getUserManager().doLogout(response, basePath, session, user);
			return;
		}

		// 注册
		if (user == null && action != null) {
			if (action.equals("register")) {
				String userName = request.getParameter("username");
				String userPassword = request.getParameter("userpwd");
				String userEmail = request.getParameter("useremail");
				String userTelephone = request.getParameter("usertel");
				JSONArray ja = this.getUserManager().doRegister(userName,
						userPassword, userEmail, userTelephone);
				out.print(ja.toString());
			} else if (action.equals("queryuser")) {
				String userName = request.getParameter("username");
				JSONArray ja = this.getUserManager().doQueryUser(userName);
				out.print(ja.toString());
			} else if (action.equals("queryvercode")) {
				JSONArray ja = new JSONArray();
				String ver2 = (String) session.getAttribute("rand");
				JSONObject jo = new JSONObject();
				jo.put("vercode", ver2);
				ja.put(jo);
				out.print(ja.toString());
			}
		}

		// 其他业务流程
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("getcompanylist")) {
				JSONArray ja = this.getUserManager().doGetCompanyList();
				out.print(ja.toString());
			}
		}
	}
}