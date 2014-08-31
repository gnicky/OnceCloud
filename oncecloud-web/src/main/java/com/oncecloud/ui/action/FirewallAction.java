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
import com.oncecloud.ui.model.ListModel;

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
	
	@RequestMapping(value = "/FirewallList", method = { RequestMethod.GET })
	@ResponseBody
	public String firewallList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			JSONArray ja = this.getFirewallManager().getFirewallList(userId,
					list.getPage(), list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
}
