package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.manager.PerformanceManger;

/**
 * @author yly hehai
 * @version 2014/08/23
 */
public class PerformanceAction extends HttpServlet {
	private static final long serialVersionUID = -3406679652823547514L;
	private PerformanceManger performanceManger;

	private PerformanceManger getPerformanceManger() {
		return performanceManger;
	}

	@Autowired
	private void setPerformanceManger(PerformanceManger performanceManger) {
		this.performanceManger = performanceManger;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		String uuid = request.getParameter("uuid");
		String type = request.getParameter("type");
		if (action != null && uuid != null && type != null)
			if (action.equals("getcpu")) {
				JSONObject jo = this.getPerformanceManger().getCpu(uuid, type);
				out.print(jo.toString());
			} else if (action.equals("getmemory")) {
				JSONArray ja = this.getPerformanceManger()
						.getMemory(uuid, type);
				out.print(ja.toString());
			} else if (action.equals("getvbd")) {
				JSONObject jo = this.getPerformanceManger().getVbd(uuid, type);
				out.print(jo.toString());
			} else if (action.equals("getvif")) {
				JSONObject jo = this.getPerformanceManger().getVif(uuid, type);
				out.print(jo.toString());
			} else if (action.equals("getpif")) {
				JSONObject jo = this.getPerformanceManger().getPif(uuid, type);
				out.print(jo.toString());
			}
	}
}
