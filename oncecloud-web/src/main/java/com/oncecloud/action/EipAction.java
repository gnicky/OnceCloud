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

import com.oncecloud.dao.EIPDAO;
import com.oncecloud.entity.User;
import com.oncecloud.manager.EIPManager;

/**
 * @author hty yly
 * @version 2014/08/23
 */
public class EipAction extends HttpServlet {
	private static final long serialVersionUID = -5456022026355746707L;
	private EIPDAO eipDAO;
	private EIPManager eipManager;

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	private void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("bind")) {
				String vmUuid = request.getParameter("vmuuid");
				String eipIp = request.getParameter("eipIp");
				String bindtype = request.getParameter("bindtype");
				JSONObject jo = this.getEipManager().eipBind(userId, vmUuid,
						eipIp, bindtype);
				out.print(jo.toString());
			} else if (action.equals("unbind")) {
				String eipIp = request.getParameter("eipIp");
				String vmUuid = request.getParameter("vmuuid");
				String bindtype = request.getParameter("bindtype");
				JSONObject jo = this.getEipManager().eipUnbind(userId, vmUuid,
						eipIp, bindtype);
				out.print(jo.toString());
			} else if (action.equals("getablevms")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String searchStr = request.getParameter("search");
				String bindtype = request.getParameter("bindtype");
				JSONArray ja = this.getEipManager().eipGetAbleVMs(page, limit,
						searchStr, bindtype, userId);
				out.print(ja.toString());
			} else if (action.equals("detail")) {
				String eip = request.getParameter("eip");
				String eipid = this.getEipDAO().getEipId(eip);
				session.setAttribute("eipId", eipid);
				session.setAttribute("eip", eip);
			} else if (action.equals("getoneeip")) {
				String eipIp = request.getParameter("eip");
				JSONObject jo = this.getEipManager().eipGetOneEip(eipIp);
				out.print(jo.toString());
			} else if (action.equals("getableeips")) {
				JSONArray ja = this.getEipManager().eipGetAbleEips(userId);
				out.print(ja.toString());
			}
		}
	}
}
