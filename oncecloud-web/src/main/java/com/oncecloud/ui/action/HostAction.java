package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.HostManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/HostAction")
@Controller
public class HostAction {
	private HostManager hostManager;

	public HostManager getHostManager() {
		return hostManager;
	}

	@Autowired
	public void setHostManager(HostManager hostManager) {
		this.hostManager = hostManager;
	}

	@RequestMapping(value = "/ALLList", method = { RequestMethod.GET })
	@ResponseBody
	public String allList(HttpServletRequest request) {
		JSONArray ja = this.getHostManager().getAllList();
		return ja.toString();
	}

	@RequestMapping(value = "/LoadList", method = { RequestMethod.GET })
	@ResponseBody
	public String loadList(HttpServletRequest request, ListModel list,
			@RequestParam String sruuid) {
		JSONArray ja = this.getHostManager().getHostLoadList(list.getPage(),
				list.getLimit(), list.getSearch(), sruuid);
		return ja.toString();
	}

	@RequestMapping(value = "/HostList", method = { RequestMethod.GET })
	@ResponseBody
	public String hostList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getHostManager().getHostList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request,
			@RequestParam("hostid") String hostId,
			@RequestParam("hostname") String hostName) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getHostManager().deleteAction(hostId, hostName,
				user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/Issamesr", method = { RequestMethod.GET })
	@ResponseBody
	public String isSameSR(HttpServletRequest request,
			@RequestParam("uuidjsonstr") String uuidJsonStr) {
		JSONArray ja = this.getHostManager().isSameSr(uuidJsonStr);
		return ja.toString();
	}

	@RequestMapping(value = "/RemoveFromPool", method = { RequestMethod.POST })
	@ResponseBody
	public String removeFromPool(HttpServletRequest request,
			@RequestParam String hostuuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getHostManager().r4Pool(hostuuid, user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/OneHost", method = { RequestMethod.GET })
	@ResponseBody
	public String oneHost(HttpServletRequest request,
			@RequestParam String hostUuid) {
		JSONArray ja = this.getHostManager().getOneHost(hostUuid);
		return ja.toString();
	}

	@RequestMapping(value = "/QueryAddress", method = { RequestMethod.GET })
	@ResponseBody
	public String queryAddress(HttpServletRequest request,
			@RequestParam String address) {
		JSONArray ja = this.getHostManager().queryAddress(address);
		return ja.toString();
	}

	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public String create(HttpServletRequest request,@RequestParam String hostname,
			@RequestParam String hostpwd,@RequestParam String hostdesc,
			@RequestParam String hostip,@RequestParam String rackUuid,
			@RequestParam String rackName) {
		User user = (User)request.getSession().getAttribute("user");
		JSONArray ja = this.getHostManager().createHost(hostname, hostpwd, hostdesc, hostip, rackUuid, rackName, user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/AddToPool", method = { RequestMethod.POST })
	@ResponseBody
	public String addToPool(HttpServletRequest request,@RequestParam String uuidjsonstr,
			@RequestParam String hasmaster,@RequestParam String pooluuid) {
		User user = (User)request.getSession().getAttribute("user");
		JSONArray ja = this.getHostManager().add2Pool(uuidjsonstr, hasmaster, pooluuid, user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/UnbindSR", method = { RequestMethod.GET })
	@ResponseBody
	public void unbindSR(HttpServletRequest request,@RequestParam String hostuuid,
			@RequestParam String sruuid) {
		User user = (User)request.getSession().getAttribute("user");
		this.getHostManager().unbindSr(hostuuid, sruuid, user.getUserId());
	}

	@RequestMapping(value = "/TablePool", method = { RequestMethod.POST })
	@ResponseBody
	public String tablePool(HttpServletRequest request,@RequestParam String uuidjsonstr) {
		JSONArray ja = this.getHostManager().getTablePool(uuidjsonstr);
		return ja.toString();
	}

	@RequestMapping(value = "/Update", method = { RequestMethod.POST })
	@ResponseBody
	public void update(HttpServletRequest request,@RequestParam String hostid,
			@RequestParam String hostname,@RequestParam String hostdesc,
			@RequestParam String rackUuid) {
		this.getHostManager().updateHost(hostid, hostname, hostdesc, rackUuid);
	}

	@RequestMapping(value = "/SRofhost", method = { RequestMethod.GET })
	@ResponseBody
	public String srOfHost(HttpServletRequest request,@RequestParam String hostUuid) {
		JSONArray ja = this.getHostManager().getSrOfHost(hostUuid);
		return ja.toString();
	}
}
