package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.User;
import com.oncecloud.manager.VoucherManager;

/**
 * @author hehai
 * @version 2014/08/24
 */
@Component
public class VoucherAction extends HttpServlet {
	private static final long serialVersionUID = -1944929235768258466L;
	private VoucherManager voucherManager;

	private VoucherManager getVoucherManager() {
		return voucherManager;
	}

	@Autowired
	private void setVoucherManager(VoucherManager voucherManager) {
		this.voucherManager = voucherManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null) {
			int userId = user.getUserId();
			if (action.equals("apply")) {
				int voucher = Integer.parseInt(request.getParameter("voucher"));
				JSONObject jo = this.getVoucherManager().applyVoucher(userId,
						voucher);
				out.print(jo.toString());
			} else if (action.equals("confirm")) {
				int uid = Integer.parseInt(request.getParameter("userid"));
				JSONObject jo = this.getVoucherManager().confirmVoucher(uid);
				out.print(jo.toString());
			} else if (action.equals("deny")) {
				int uid = Integer.parseInt(request.getParameter("userid"));
				JSONObject jo = this.getVoucherManager().denyVoucher(uid);
				out.print(jo.toString());
			}
		}
	}
}
