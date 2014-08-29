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
@Component
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
			if (action.equals("create")) {
				String userName = request.getParameter("username");
				String userPassword = request.getParameter("userpwd");
				String userEmail = request.getParameter("useremail");
				String userTelephone = request.getParameter("usertel");
				String userCompany = request.getParameter("usercom");
				String userLevel = request.getParameter("userlevel");
				JSONArray ja = this.getUserManager().doCreateUser(userName,
						userPassword, userEmail, userTelephone, userCompany,
						userLevel, userId);
				out.print(ja.toString());
			} else if (action.equals("queryuser")) {
				String userName = request.getParameter("username");
				JSONArray ja = this.getUserManager().doQueryUser(userName);
				out.print(ja.toString());
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limitnum"));
				String search = request.getParameter("search");
				JSONArray ja = this.getUserManager().getUserList(page, limit,
						search);
				out.print(ja.toString());
			} else if (action.equals("delete")) {
				int changeId = Integer.parseInt(request.getParameter("userid"));
				String userName = request.getParameter("username");
				JSONObject jo = this.getUserManager().doDeleteUser(userId,
						changeId, userName);
				out.print(jo.toString());
			} else if (action.equals("update")) {
				int changeId = Integer.parseInt(request.getParameter("userid"));
				String userName = request.getParameter("username");
				String userEmail = request.getParameter("useremail");
				String userTel = request.getParameter("usertel");
				String userCom = request.getParameter("usercom");
				String userLevel = request.getParameter("userlevel");
				this.getUserManager().doUpdateUser(userId, changeId, userName,
						userEmail, userTel, userCom, userLevel);
			} else if (action.equals("detail")) {
				int uid = Integer.parseInt(request.getParameter("userid"));
				String userName = request.getParameter("username");
				session.setAttribute("userid", uid);
				session.setAttribute("username", userName);
			} else if (action.equals("getoneuser")) {
				int uid = Integer.parseInt(request.getParameter("userid"));
				JSONObject jo = this.getUserManager().doGetOneUser(uid);
				out.print(jo.toString());
			} else if (action.equals("getuserquota")) {
				int uid = Integer.parseInt(request.getParameter("userid"));
				JSONObject jo = this.getUserManager().doGetUserQuota(uid);
				out.print(jo.toString());
			} else if (action.equals("quotaupdate")) {
				int quotaid = Integer.parseInt(request.getParameter("quotaid"));
				int changerId = Integer
						.parseInt(request.getParameter("userid"));
				int eip = Integer.parseInt(request.getParameter("eip"));
				int vm = Integer.parseInt(request.getParameter("vm"));
				int bk = Integer.parseInt(request.getParameter("bk"));
				int img = Integer.parseInt(request.getParameter("img"));
				int vol = Integer.parseInt(request.getParameter("vol"));
				int ssh = Integer.parseInt(request.getParameter("ssh"));
				int fw = Integer.parseInt(request.getParameter("fw"));
				int rt = Integer.parseInt(request.getParameter("rt"));
				int vlan = Integer.parseInt(request.getParameter("vlan"));
				int lb = Integer.parseInt(request.getParameter("lb"));
				int disk = Integer.parseInt(request.getParameter("disk"));
				int bw = Integer.parseInt(request.getParameter("bw"));
				int mem = Integer.parseInt(request.getParameter("mem"));
				int cpu = Integer.parseInt(request.getParameter("cpu"));
				this.getUserManager().doQuotaUpdate(quotaid, changerId, eip,
						vm, bk, img, vol, ssh, fw, rt, vlan, lb, disk, bw, mem,
						cpu, userId);
			} else if (action.equals("getbalance")) {
				double userBalance = user.getUserBalance();
				JSONObject jo = new JSONObject();
				jo.put("balance", userBalance);
				out.print(jo.toString());
			} else if (action.equals("getcompanylist")) {
				JSONArray ja = this.getUserManager().doGetCompanyList();
				out.print(ja.toString());
			}
		}
	}
}