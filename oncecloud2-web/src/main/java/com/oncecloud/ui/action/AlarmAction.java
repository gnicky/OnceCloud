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
		int userId = user.getUserId();
		JSONArray ja = this.getAlarmManager().getAlarmList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/RuleList", method = { RequestMethod.GET })
	@ResponseBody
	public String ruleList(HttpServletRequest request,
			@RequestParam String alarmUuid, @RequestParam int page,
			@RequestParam int limit) {
		JSONArray ja = this.getAlarmManager().getRuleList(alarmUuid, page,
				limit);
		return ja.toString();
	}

	@RequestMapping(value = "/Resource", method = { RequestMethod.GET })
	@ResponseBody
	public String resource(HttpServletRequest request, ListModel list,
			@RequestParam String alarmUuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getAlarmManager().getResourceList(alarmUuid,
				list.getPage(), list.getLimit(), list.getSearch(), userId);
		return ja.toString();
	}

	@RequestMapping(value = "/Create", method = { RequestMethod.GET })
	@ResponseBody
	public void create(HttpServletRequest request,
			@RequestParam String alarmUuid, @RequestParam String alarmName,
			@RequestParam int alarmType, @RequestParam int alarmIsalarm,
			@RequestParam int alarmTouch, @RequestParam int alarmPeriod,
			@RequestParam String alarmRules, @RequestParam String alarmThreshold) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getAlarmManager().saveAlarm(alarmUuid, alarmName,
				alarmType, alarmIsalarm, alarmTouch, alarmPeriod, alarmRules,
				alarmThreshold, userId);
	}

	@RequestMapping(value = "/AddResource", method = { RequestMethod.GET })
	@ResponseBody
	public String addResource(HttpServletRequest request,
			@RequestParam String alarmUuid, @RequestParam String rsuuidStr) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		boolean result = this.getAlarmManager().addResourceToAlarm(userId,
				rsuuidStr, alarmUuid);
		JSONObject jo = new JSONObject();
		jo.put("obj", result);
		return jo.toString();
	}

	@RequestMapping(value = "/ModifyRule", method = { RequestMethod.POST })
	@ResponseBody
	public void modifyRule(HttpServletRequest request,
			@RequestParam int ruletype, @RequestParam int rulethreshold,
			@RequestParam int ruleperiod, @RequestParam String ruleId) {
		this.getAlarmManager().modifyRule(ruletype, rulethreshold, ruleperiod,
				ruleId);
	}

	@RequestMapping(value = "/CreateRule", method = { RequestMethod.POST })
	@ResponseBody
	public void createRule(HttpServletRequest request,
			@RequestParam String alarmUuid, @RequestParam int ruletype,
			@RequestParam int rulethreshold, @RequestParam int ruleperiod,
			@RequestParam String ruleId) {
		this.getAlarmManager().addRule(alarmUuid, ruletype, rulethreshold,
				ruleperiod, ruleId);
	}

	@RequestMapping(value = "/ModifyPeriod", method = { RequestMethod.POST })
	@ResponseBody
	public String modifyPeriod(HttpServletRequest request,
			@RequestParam String alarmUuid, @RequestParam int period) {
		JSONObject jo = this.getAlarmManager().modifyPeriod(alarmUuid, period);
		return jo.toString();
	}

	@RequestMapping(value = "/ModifyTouch", method = { RequestMethod.POST })
	@ResponseBody
	public void modifyTouch(HttpServletRequest request,
			@RequestParam int alarmTouch, @RequestParam String alarmUuid,
			@RequestParam int alarmIsalarm) {
		this.getAlarmManager().modifyTouch(alarmUuid, alarmTouch, alarmIsalarm);
	}

	@RequestMapping(value = "/Destory", method = { RequestMethod.GET })
	@ResponseBody
	public String destory(HttpServletRequest request,
			@RequestParam String uuid) {
		boolean result = this.getAlarmManager().deleteAlarm(uuid);
		JSONObject jo = new JSONObject();
		jo.put("obj", result);
		return jo.toString();
	}

	@RequestMapping(value = "/Remove", method = { RequestMethod.GET })
	@ResponseBody
	public String remove(HttpServletRequest request, @RequestParam String rsUuid,
			@RequestParam int rsType) {
		this.getAlarmManager().removeRS(rsUuid, rsType);
		return "";
	}

	@RequestMapping(value = "/DeleteRule", method = { RequestMethod.GET })
	@ResponseBody
	public void deleteRule(HttpServletRequest request,
			@RequestParam String ruleId) {
		this.getAlarmManager().deleteRule(ruleId);
	}

	@RequestMapping(value = "/GetAlarm", method = { RequestMethod.GET })
	@ResponseBody
	public String getAlarm(HttpServletRequest request,
			@RequestParam String alarmUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getAlarmManager().getBasicInfo(alarmUuid,
				user.getUserId());
		return jo.toString();
	}

}
