package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.AlarmManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/AlarmAction")
@Controller
public class AlarmAction {
	private AlarmManager alarmManager;

	public AlarmManager getAlarmManager() {
		return alarmManager;
	}

	@Autowired
	public void setAlarmManager(AlarmManager alarmManager) {
		this.alarmManager = alarmManager;
	}
	
	@RequestMapping(value = "/AlarmList", method = { RequestMethod.GET })
	@ResponseBody
	public String alarmList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONArray ja = this.getAlarmManager().getAlarmList(userId,
					list.getPage(), list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
}
