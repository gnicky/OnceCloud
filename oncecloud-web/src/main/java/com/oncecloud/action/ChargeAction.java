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
import com.oncecloud.manager.ChargeManager;

/**
 * @author hehai
 * @version 2014/08/25
 */
@Component
public class ChargeAction extends HttpServlet {
	private static final long serialVersionUID = 922878943565145613L;
	private ChargeManager chargeManager;

	private ChargeManager getChargeManager() {
		return chargeManager;
	}

	@Autowired
	private void setChargeManager(ChargeManager chargeManager) {
		this.chargeManager = chargeManager;
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
				JSONArray ja = this.getChargeManager().getChargeList(userId,
						page, limit);
				out.print(ja.toString());
			}
		}
	}
}
