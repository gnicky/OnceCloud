package com.oncecloud.ui.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;
import com.oncecloud.manager.QuotaManager;

@Controller
public class ModalControllerUser {
	private QuotaManager quotaManager;

	private QuotaManager getQuotaManager() {
		return quotaManager;
	}

	@Autowired
	private void setQuotaManager(QuotaManager quotaManager) {
		this.quotaManager = quotaManager;
	}

	@RequestMapping(value = "/user/modal/viewquota", method = { RequestMethod.GET })
	public ModelAndView viewQuota(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		Quota quotaUsed = this.getQuotaManager().getQuotaUsed(user.getUserId());
		Quota quotaTotal = this.getQuotaManager().getQuotaTotal(
				user.getUserId());
		model.put("quotaUsed", quotaUsed);
		model.put("quotaTotal", quotaTotal);
		return new ModelAndView("user/modal/viewquota", model);
	}

	@RequestMapping(value = "/user/modal/bindalarm")
	public ModelAndView bindAlarm(HttpServletRequest request) {
		String uuid = request.getParameter("uuid");
		request.setAttribute("showid", "al-" + uuid.substring(0, 8));
		return new ModelAndView("user/modal/bindalarm");
	}

	@RequestMapping(value = "/user/modal/modifyperiod")
	public ModelAndView modifyperiod(HttpServletRequest request) {
		String alarmUuid = request.getParameter("alarmUuid");
		request.setAttribute("alarmUuid", alarmUuid);
		return new ModelAndView("user/modal/modifyperiod");
	}

	@RequestMapping(value = "/user/modal/modifyalert")
	public ModelAndView modifyalert(HttpServletRequest request) {
		String alarmUuid = request.getParameter("alarmUuid");
		request.setAttribute("alarmUuid", alarmUuid);
		return new ModelAndView("user/modal/modifyalert");
	}

	@RequestMapping(value = "/elasticip/bind", method = { RequestMethod.POST })
	public ModelAndView bindElasticIP(HttpServletRequest request,
			@RequestParam String type) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("type", type);
		return new ModelAndView("user/modal/bindelasticip", model);
	}

	@RequestMapping(value = "user/modal/modifyalarmrule")
	public ModelAndView modifyAlarmRule(HttpServletRequest request,
			@RequestParam String alarmType, @RequestParam String ruleId,
			@RequestParam String ruleName, @RequestParam String ruleprio,
			@RequestParam String ruleprot, @RequestParam String rulesport,
			@RequestParam String unit) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("alarmType", alarmType);
		model.put("ruleId", ruleId);
		model.put("ruleName", ruleName);
		model.put("ruleprio", ruleprio);
		model.put("ruleprot", ruleprot);
		model.put("rulesport", rulesport);
		model.put("unit", unit);
		return new ModelAndView("user/modal/modifyalarmrule", model);
	}
	
	@RequestMapping(value = "/volume/bind", method = { RequestMethod.GET })
	public ModelAndView bindVolume(HttpServletRequest request) {
		return new ModelAndView("user/modal/bindvolume");
	}
	
	@RequestMapping(value = "/expense/view", method = { RequestMethod.POST })
	public ModelAndView viewExpense(HttpServletRequest request, @RequestParam String type, @RequestParam String resourceUuid) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("type", type);
		model.put("resourceUuid", resourceUuid);
		String showId = "";
		if (type.equals("instance")) {
			showId = "i-" + resourceUuid.substring(0, 8);
		} else if (type.equals("eip")) {
			showId = "eip-" + resourceUuid.substring(0, 8);
		} else if (type.equals("volume")) {
			showId = "vol-" + resourceUuid.substring(0, 8);
		} else if (type.equals("snapshot")) {
			showId = "ss-" + resourceUuid.substring(0, 8);
		} else if (type.equals("image")) {
			showId = "image-" + resourceUuid.substring(0, 8);
		}
		model.put("showId", showId);
		return new ModelAndView("user/modal/viewexpense", model);
	}
	
}
