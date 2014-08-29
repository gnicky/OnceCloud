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

import com.oncecloud.dao.AlarmDAO;
import com.oncecloud.dao.AlarmRuleDAO;
import com.oncecloud.entity.Alarm;
import com.oncecloud.entity.AlarmRule;
import com.oncecloud.entity.User;
import com.oncecloud.manager.AlarmManager;

/**
 * @author hty
 * @version 2014/08/25
 */
@Component
public class AlarmAction extends HttpServlet {
	private static final long serialVersionUID = -2385069247881004402L;

	private AlarmDAO alarmDAO;
	private AlarmRuleDAO alarmRuleDAO;
	private AlarmManager alarmManager;

	private AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	@Autowired
	private void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	private AlarmRuleDAO getAlarmRuleDAO() {
		return alarmRuleDAO;
	}

	@Autowired
	private void setAlarmRuleDAO(AlarmRuleDAO alarmRuleDAO) {
		this.alarmRuleDAO = alarmRuleDAO;
	}

	private AlarmManager getAlarmManager() {
		return alarmManager;
	}

	@Autowired
	private void setAlarmManager(AlarmManager alarmManager) {
		this.alarmManager = alarmManager;
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
				String alarmUuid = request.getParameter("alarmUuid");
				String alarmName = request.getParameter("alarmName");
				int alarmType = Integer.parseInt(request
						.getParameter("alarmType"));
				int alarmIsalarm = Integer.parseInt(request
						.getParameter("alarmIsalarm"));
				int alarmTouch = Integer.parseInt(request
						.getParameter("alarmTouch"));
				int alarmPeriod = Integer.parseInt(request
						.getParameter("alarmPeriod"));
				String alarmRules = request.getParameter("alarmRules");
				String alarmThreshold = request.getParameter("alarmThreshold");
				boolean result = this.getAlarmManager().saveAlarm(alarmUuid,
						alarmName, alarmType, alarmIsalarm, alarmTouch,
						alarmPeriod, alarmRules, alarmThreshold, userId);
				out.print(result);
			} else if (action.equals("destroy")) {
				String uuid = request.getParameter("uuid");
				boolean result = this.getAlarmManager().deleteAlarm(uuid);
				out.print(result);
			} else if (action.equals("detail")) {
				String alarmUuid = request.getParameter("alarmUuid");
				session.setAttribute("alarmUuid", alarmUuid);
			} else if (action.equals("getlist")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getAlarmManager().alarmGetList(page, limit,
						search, userId);
				out.print(ja.toString());
			} else if (action.equals("getResource")) {
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				String alarmUuid = request.getParameter("alarmUuid");
				int type = this.getAlarmDAO().getAlarm(alarmUuid)
						.getAlarmType();
				JSONArray ja = this.getAlarmManager().getResourceList(page,
						limit, search, type, userId);
				out.print(ja.toString());
			} else if (action.equals("addresource")) {
				String alarmUuid = request.getParameter("alarmUuid");
				String rsuuidStr = request.getParameter("rsuuidStr");
				this.getAlarmManager().addResourceToAlarm(userId, rsuuidStr,
						alarmUuid);
				out.print(true);
			} else if (action.equals("getalarm")) {
				String alarmUuid = request.getParameter("alarmUuid");
				JSONObject jo = this.getAlarmManager().getBasicInfo(alarmUuid,
						userId);
				out.print(jo);
			} else if (action.equals("removeRs")) {
				String rsUuid = request.getParameter("rsUuid");
				String rsType = request.getParameter("rsType");
				int type = Integer.parseInt(rsType);
				this.getAlarmManager().removeRS(rsUuid, type);
				out.print(true);
			} else if (action.equals("mperiod")) {
				String alarmUuid = request.getParameter("alarmUuid");
				int period = Integer.parseInt(request.getParameter("period"));
				this.getAlarmDAO().updatePeriod(alarmUuid, period);
				JSONObject jo = new JSONObject();
				jo.put("isSuccess", true);
				out.print(jo.toString());
			} else if (action.equals("createrule")) {
				String alarmUuid = request.getParameter("alarmUuid");
				int ruletype = Integer.parseInt(request
						.getParameter("ruletype"));
				int rulethreshold = Integer.parseInt(request
						.getParameter("rulethreshold"));
				int ruleperiod = Integer.parseInt(request
						.getParameter("ruleperiod"));
				String ruleId = request.getParameter("ruleId");
				this.getAlarmRuleDAO().addRule(ruleId, ruletype, rulethreshold,
						alarmUuid, ruleperiod);
				out.print(true);
			} else if (action.equals("getrulelist")) {
				String alarmUuid = request.getParameter("alarmUuid");
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				JSONArray ja = this.getAlarmManager().getRuleList(alarmUuid,
						page, limit);
				out.print(ja.toString());
			} else if (action.equals("deleterule")) {
				String ruleId = request.getParameter("ruleId");
				AlarmRule ar = new AlarmRule();
				ar.setRuleAUuid(ruleId);
				this.getAlarmRuleDAO().removeAlarmRule(ar);
				out.print(true);
			} else if (action.equals("modifyrule")) {
				int ruletype = Integer.parseInt(request
						.getParameter("ruletype"));
				int rulethreshold = Integer.parseInt(request
						.getParameter("rulethreshold"));
				int ruleperiod = Integer.parseInt(request
						.getParameter("ruleperiod"));
				String ruleId = request.getParameter("ruleId");
				this.getAlarmRuleDAO().updateRule(ruleId, ruleperiod,
						rulethreshold, ruletype);
				out.print(true);
			} else if (action.equals("mtouch")) {
				String alarmUuid = request.getParameter("alarmUuid");
				int alarmTouch = Integer.parseInt(request
						.getParameter("alarmTouch"));
				int alarmIsalarm = Integer.parseInt(request
						.getParameter("alarmIsalarm"));
				Alarm alarm = this.getAlarmDAO().getAlarm(alarmUuid);
				alarm.setAlarmIsalarm(alarmIsalarm);
				alarm.setAlarmTouch(alarmTouch);
				this.getAlarmDAO().updateAlarm(alarm);
				out.print(true);
			}
		}
	}

}
