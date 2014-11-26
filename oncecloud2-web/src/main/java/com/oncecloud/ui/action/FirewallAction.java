package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.FirewallManager;

@RequestMapping("/FirewallAction")
@Controller
public class FirewallAction {
	private FirewallManager firewallManager;

	public FirewallManager getFirewallManager() {
		return firewallManager;
	}

	@Autowired
	public void setFirewallManager(FirewallManager firewallManager) {
		this.firewallManager = firewallManager;
	}

	/*@RequestMapping(value = "/CreateFirewall", method = { RequestMethod.POST })
	@ResponseBody
	public String createFirewall(HttpServletRequest request,
			CreateFirewallModel cfm) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getFirewallManager().createFirewall(userId,
				cfm.getFirewallName(), cfm.getFirewallUuid());
		return jo.toString();
	}

	@RequestMapping(value = "/DeleteFirewall", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteFirewall(HttpServletRequest request,
			@RequestParam String firewallId) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getFirewallManager().deleteFirewall(userId,
				firewallId);
		return jo.toString();
	}

	@RequestMapping(value = "/FirewallList", method = { RequestMethod.GET })
	@ResponseBody
	public String firewallList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getFirewallManager().getFirewallList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/RuleList", method = { RequestMethod.GET })
	@ResponseBody
	public String ruleList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getFirewallManager().getRuleList(list.getPage(),
				list.getLimit(), list.getSearch(), list.getUuid());
		return ja.toString();
	}

	@RequestMapping(value = "/BasicList", method = { RequestMethod.GET })
	@ResponseBody
	public String basicList(HttpServletRequest request,
			@RequestParam String firewallId) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getFirewallManager().getBasicList(userId,
				firewallId);
		return jo.toString();
	}
*/
	@RequestMapping(value = "/AvailableFirewalls", method = { RequestMethod.POST })
	@ResponseBody
	public String availablefirewalls(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getFirewallManager().getAvailableFirewalls(userId);
		return ja.toString();
	}
/*
	@RequestMapping(value = "/Bind", method = { RequestMethod.POST })
	@ResponseBody
	public String bindFirewall(HttpServletRequest request,
			@RequestParam String firewallId, @RequestParam String vmUuidStr,
			@RequestParam String bindType) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONObject jo = this.getFirewallManager().bindFirewall(userId,
				vmUuidStr, firewallId, bindType);
		return jo.toString();
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.GET })
	@ResponseBody
	public String quota(HttpServletRequest request, @RequestParam int count) {
		User user = (User) request.getSession().getAttribute("user");
		String quota = this.getFirewallManager().getQuota(user.getUserId(),
				count);
		return quota;
	}

	@RequestMapping(value = "/OperateRule", method = { RequestMethod.GET })
	@ResponseBody
	public String operateRule(HttpServletRequest request,
			@RequestParam String ruleId) {
		JSONObject jo = this.getFirewallManager().operateRule(ruleId);
		return jo.toString();
	}

	@RequestMapping(value = "/CreateRule", method = { RequestMethod.POST })
	@ResponseBody
	public String createRule(HttpServletRequest request, CreateRuleModel crm) {
		JSONObject jo = this.getFirewallManager().createRule(crm.getRuleId(),
				crm.getRuleName(), crm.getRulePriority(),
				crm.getRuleProtocol(), crm.getRuleIp(), crm.getFirewallId(),
				crm.getRuleSport(), crm.getRuleEport());
		return jo.toString();
	}
	
	@RequestMapping(value = "/DeleteRule", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteRule(HttpServletRequest request, @RequestParam String ruleId, @RequestParam String firewallId) {
		JSONObject jo = this.getFirewallManager().deleteRule(ruleId, firewallId);
		return jo.toString();
	}

	@RequestMapping(value = "/UpdateFirewall", method = { RequestMethod.GET })
	@ResponseBody
	public String updateFirewall(HttpServletRequest request,
			@RequestParam String firewallId) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getFirewallManager().updateFirewall(
				user.getUserId(), firewallId);
		return jo.toString();
	}
	
	@RequestMapping(value = "/UpdateFirewallForRouteInner", method = { RequestMethod.GET })
	@ResponseBody
	public String updateFirewallForRouteInner(HttpServletRequest request,
			@RequestParam String firewallId) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getFirewallManager().updateFirewallForinnerFirewall(
				user.getUserId(), firewallId);
		return jo.toString();
	}*/
}