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
import com.oncecloud.manager.VnetManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/VnetAction")
@Controller
public class VnetAction {
	private VnetManager vnetManager;

	public VnetManager getVnetManager() {
		return vnetManager;
	}

	@Autowired
	public void setVnetManager(VnetManager vnetManager) {
		this.vnetManager = vnetManager;
	}

	@RequestMapping(value = "/VnetList", method = { RequestMethod.GET })
	@ResponseBody
	public String getVnetList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getVnetManager().getVnetList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/VnetDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String vnetDetail(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getVnetManager().getVnetDetail(uuid);
		return jo.toString();
	}

	@RequestMapping(value = "/ListOfUser", method = { RequestMethod.POST })
	@ResponseBody
	public String getVnetsOfUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getVnetManager().getVnetsOfUser(userId);
		return ja.toString();
	}

	@RequestMapping(value = "/VMs", method = { RequestMethod.GET })
	@ResponseBody
	public String vms(HttpServletRequest request, @RequestParam String vnetUuid) {
		JSONArray ja = this.getVnetManager().getVMs(vnetUuid);
		return ja.toString();
	}

	/**
	 * 私有网络绑定主机
	 * 
	 * @param request
	 * @param vnId
	 * @param vmUuid
	 */
	@RequestMapping(value = "/BindVM", method = { RequestMethod.POST })
	@ResponseBody
	public void bindVM(HttpServletRequest request, @RequestParam String vnId,
			@RequestParam String vmUuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getVnetManager().bindVM(vnId, vmUuid, userId,
				user.getUserAllocate());
	}

	@RequestMapping(value = "/UnlinkRouter", method = { RequestMethod.POST })
	@ResponseBody
	public String unlinkRouter(HttpServletRequest request,
			@RequestParam String vnetId, @RequestParam String content, @RequestParam String conid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getVnetManager().unlinkRouter(vnetId,
				user.getUserId(), content, conid);
		return jo.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		String result = this.getVnetManager()
				.deleteVnet(user.getUserId(), uuid);
		return result;
	}

	/**
	 * 主机加入私有网络
	 * 
	 * @param request
	 * @param vnId
	 * @param vmuuidStr
	 * @param content
	 * @param conid
	 * @return
	 */
	@RequestMapping(value = "/AddVM", method = { RequestMethod.GET })
	@ResponseBody
	public String addVM(HttpServletRequest request, @RequestParam String vnId,
			@RequestParam String vmuuidStr, @RequestParam String content, @RequestParam String conid) {
		User user = (User) request.getSession().getAttribute("user");
		String poolUuid = user.getUserAllocate();
		JSONObject jo = this.getVnetManager().vnetAddvm(user.getUserId(),
				vmuuidStr, vnId, poolUuid, content, conid);
		return jo.toString();
	}

	@RequestMapping(value = "/CheckNet", method = { RequestMethod.POST })
	@ResponseBody
	public String checkNet(HttpServletRequest request,
			@RequestParam String routerid, @RequestParam int net) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getVnetManager().checkNet(user.getUserId(),
				routerid, net);
		return jo.toString();
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.GET })
	@ResponseBody
	public String quota(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getVnetManager().vnetQuota(user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/LinkRouter", method = { RequestMethod.POST })
	@ResponseBody
	public String linkRouter(HttpServletRequest request,
			@RequestParam String vnetuuid, @RequestParam String routerid,
			@RequestParam int net, @RequestParam int gate,
			@RequestParam int start, @RequestParam int end,
			@RequestParam int dhcpState, @RequestParam String content
			, @RequestParam String conid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getVnetManager().linkRouter(user.getUserId(),
				vnetuuid, routerid, net, gate, start, end, dhcpState, content, conid);
		return jo.toString();
	}

	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public void create(HttpServletRequest request, @RequestParam String name,
			@RequestParam String uuid, @RequestParam String desc) {
		User user = (User) request.getSession().getAttribute("user");
		this.getVnetManager().vnetCreate(name, uuid, desc, user.getUserId());
	}

	@RequestMapping(value = "/AvailableVnet", method = { RequestMethod.POST })
	@ResponseBody
	public String getAvailableVnet(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getVnetManager().getAvailableVnet(user.getUserId());
		return ja.toString();
	}
}
