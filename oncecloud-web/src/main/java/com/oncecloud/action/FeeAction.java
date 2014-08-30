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
import com.oncecloud.manager.FeeManager;

/**
 * @author yly hty
 * @version 2014/08/25
 */
public class FeeAction extends HttpServlet {
	private static final long serialVersionUID = -5547355801781417104L;
	private FeeManager feeManager;

	private FeeManager getFeeManager() {
		return feeManager;
	}

	@Autowired
	private void setFeeManager(FeeManager feeManager) {
		this.feeManager = feeManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (action.equals("getlist")) {
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = Integer.parseInt(request.getParameter("limitnum"));
			String searchStr = request.getParameter("search");
			String type = request.getParameter("type");
			JSONArray ja = this.getFeeManager().feeGetList(page, limit,
					searchStr, type, user);
			out.print(ja.toString());
		} else if (action.equals("getdetaillist")) {
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = Integer.parseInt(request.getParameter("limitnum"));
			String searchStr = request.getParameter("search");
			String type = request.getParameter("type");
			String uuid = request.getParameter("uuid");
			JSONArray ja = this.getFeeManager().feeGetDetailList(page, limit,
					searchStr, type, uuid, user);
			out.print(ja.toString());
		} else if (action.equals("initfee")) {
			int userid = user.getUserId();
			JSONObject jo = this.getFeeManager().feeInitfee(userid);
			out.print(jo.toString());
		} else if (action.equals("querylist")) {
			int userid = user.getUserId();
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = Integer.parseInt(request.getParameter("limitnum"));
			String searchStr = request.getParameter("search");
			String type = request.getParameter("type");
			String monthStr = request.getParameter("month");
			JSONArray ja = this.getFeeManager().feeQuerylist(userid, page,
					limit, searchStr, type, monthStr);
			out.print(ja.toString());
		}
	}
}
