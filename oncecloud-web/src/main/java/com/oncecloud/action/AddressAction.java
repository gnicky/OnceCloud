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
import com.oncecloud.manager.AddressManager;

/**
 * @author hehai
 * @version 2014/08/25
 */
public class AddressAction extends HttpServlet {
	private static final long serialVersionUID = 3846316514285369076L;
	private AddressManager addressManager;

	private AddressManager getAddressManager() {
		return addressManager;
	}

	@Autowired
	private void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("adddhcp")) {
				String prefix = request.getParameter("prefix");
				int start = Integer.parseInt(request.getParameter("start"));
				int end = Integer.parseInt(request.getParameter("end"));
				JSONArray ja = this.getAddressManager().addDHCPPool(userId,
						prefix, start, end);
				out.print(ja.toString());
			} else if (action.equals("addpublicip")) {
				String prefix = request.getParameter("prefix");
				int start = Integer.parseInt(request.getParameter("start"));
				int end = Integer.parseInt(request.getParameter("end"));
				int eiptype = Integer.parseInt(request.getParameter("eiptype"));
				String eipInterface = request.getParameter("eipif");
				JSONArray ja = this.getAddressManager().addPublicIP(userId,
						prefix, start, end, eiptype, eipInterface);
				out.print(ja.toString());
			} else if (action.equals("deleteDHCP")) {
				String ip = request.getParameter("ip");
				String mac = request.getParameter("mac");
				JSONArray ja = this.getAddressManager().deleteDHCP(userId, ip,
						mac);
				out.print(ja.toString());
			} else if (action.equals("deleteEIP")) {
				String ip = request.getParameter("ip");
				String uuid = request.getParameter("uuid");
				JSONArray ja = this.getAddressManager().deletePublicIP(userId,
						ip, uuid);
				out.print(ja.toString());
			}
		}
	}
}
