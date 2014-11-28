package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.AlarmLogManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/AlarmLogAction")
@Controller
public class AlarmLogAction {

	private AlarmLogManager alarmLogManager;

	public AlarmLogManager getAlarmLogManager() {
		return alarmLogManager;
	}
	
	@Autowired
	public void setAlarmLogManager(AlarmLogManager alarmLogManager) {
		this.alarmLogManager = alarmLogManager;
	}

	@RequestMapping(value = "/AlarmLogList", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String imageList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		int userLevel = user.getUserLevel();
		JSONArray ja = this.getAlarmLogManager().getAlarmLogList(userId, userLevel,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request,@RequestParam String alarmLogId) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getAlarmLogManager().deleteAlarmLog(user.getUserId(),alarmLogId);
		return jo.toString();
	}

	@RequestMapping(value = "/read", method = { RequestMethod.POST })
	@ResponseBody
	public String updateImage(HttpServletRequest request,@RequestParam String alarmLogId) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = new JSONObject();
		jo.put("result", this.getAlarmLogManager().updateAlarmLog(user.getUserId(),alarmLogId));
		return jo.toString();
	}
}
