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
import com.oncecloud.manager.RouterManager;
import com.oncecloud.manager.VnetManager;
import com.oncecloud.ui.model.AdminListModel;
import com.oncecloud.ui.model.CreateRouterModel;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/RouterAction")
@Controller
public class RouterAction {
	private RouterManager routerManager;
	private VnetManager vnetManager;

	public RouterManager getRouterManager() {
		return routerManager;
	}

	public VnetManager getVnetManager() {
		return vnetManager;
	}

	@Autowired
	public void setVnetManager(VnetManager vnetManager) {
		this.vnetManager = vnetManager;
	}

	@Autowired
	public void setRouterManager(RouterManager routerManager) {
		this.routerManager = routerManager;
	}

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		JSONArray ja = this.getRouterManager().getAdminRouterList(
				alrModel.getPage(), alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
		return ja.toString();
	}

	@RequestMapping(value = "/RouterList", method = { RequestMethod.GET })
	@ResponseBody
	public String routerList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getRouterManager().getRouterList(userId,
				list.getPage(), list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/AdminStartUp", method = { RequestMethod.GET })
	@ResponseBody
	public void routerAdminStartUp(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getRouterManager().routerAdminStartUp(uuid, userId);
	}

	@RequestMapping(value = "/AdminShutDown", method = { RequestMethod.GET })
	@ResponseBody
	public void routerAdminShutDown(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getRouterManager().routerAdminShutDown(uuid, force, userId);
	}

	@RequestMapping(value = "/RouterDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String routerDetail(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getRouterManager().getRouterDetail(uuid);
		return jo.toString();
	}

	@RequestMapping(value = "/StartUp", method = { RequestMethod.POST })
	@ResponseBody
	public void startUp(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getRouterManager().startRouter(uuid, user.getUserId(),
				user.getUserAllocate());
	}

	@RequestMapping(value = "/Destroy", method = { RequestMethod.POST })
	@ResponseBody
	public String destroy(HttpServletRequest request, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		if (!this.getVnetManager().isRouterHasVnets(uuid, user.getUserId())) {
			this.getRouterManager().deleteRouter(uuid, user.getUserId(),
					user.getUserAllocate());
			return "ok";
		} else {
			return "no";
		}
	}

	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public void create(HttpServletRequest request,
			CreateRouterModel createRouterModel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getRouterManager().createRouter(createRouterModel.getUuid(),
				user.getUserId(), createRouterModel.getName(),
				createRouterModel.getCapacity(), createRouterModel.getFwUuid(),
				user.getUserAllocate());
	}

	@RequestMapping(value = "/UpdateStar", method = { RequestMethod.POST })
	@ResponseBody
	public void updateStar(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int num) {
		this.getRouterManager().updateImportance(uuid, num);
	}

	@RequestMapping(value = "/ShutDown", method = { RequestMethod.POST })
	@ResponseBody
	public void shutDown(HttpServletRequest request, @RequestParam String uuid,
			@RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		this.getRouterManager().shutdownRouter(uuid, force, user.getUserId(),
				user.getUserAllocate());
	}

	@RequestMapping(value = "/Quota", method = { RequestMethod.POST })
	@ResponseBody
	public String quota(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getRouterManager().routerQuota(user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/Vxnets", method = { RequestMethod.GET })
	@ResponseBody
	public String getVxnets(HttpServletRequest request,
			@RequestParam String routerUuid) {
		JSONArray jo = this.getRouterManager().getVxnets(routerUuid);
		return jo.toString();
	}

	@RequestMapping(value = "/RoutersOfUser", method = { RequestMethod.POST })
	@ResponseBody
	public String getRoutersOfUser(HttpServletRequest request, ListModel lm) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getRouterManager().getRoutersOfUser(
				user.getUserId(), lm.getPage(), lm.getLimit(), lm.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/TableRTs", method = { RequestMethod.GET })
	@ResponseBody
	public String tableRTs(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getRouterManager().getRoutersOfUser(
				user.getUserId());
		return ja.toString();
	}

	@RequestMapping(value = "/AddPortForwarding", method = { RequestMethod.POST })
	@ResponseBody
	public String addPortForwarding(HttpServletRequest request,
			@RequestParam String protocol, @RequestParam String srcIP,
			@RequestParam String srcPort, @RequestParam String destIP,
			@RequestParam String destPort, @RequestParam String pfName, @RequestParam String routerUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getRouterManager().addPortForwarding(
				user.getUserId(), user.getUserAllocate(), protocol, srcIP, srcPort, destIP, destPort, pfName, routerUuid);
		return jo.toString();
	}

	@RequestMapping(value = "/DelPortForwarding", method = { RequestMethod.POST })
	@ResponseBody
	public String delPortForwarding(HttpServletRequest request,
			@RequestParam String protocol, @RequestParam String srcIP,
			@RequestParam String srcPort, @RequestParam String destIP,
			@RequestParam String destPort, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getRouterManager().delPortForwarding(
				user.getUserId(), user.getUserAllocate(), protocol, srcIP, srcPort, destIP, destPort, uuid);
		return jo.toString();
	}

	@RequestMapping(value = "/ForwardPortList", method = { RequestMethod.POST })
	@ResponseBody
	public String forwardPortList(HttpServletRequest request,
			@RequestParam String routerUuid) {
		JSONArray ja = this.getRouterManager().getpfList(routerUuid);
		return ja.toString();
	}

	@RequestMapping(value = "/CheckPortForwarding", method = { RequestMethod.POST })
	@ResponseBody
	public String checkPortForwarding(HttpServletRequest request,
			@RequestParam String routerUuid, @RequestParam String internalIP, @RequestParam int destPort, @RequestParam int srcPort) {
		JSONObject jo = this.getRouterManager().checkPortForwarding(routerUuid, internalIP, destPort, srcPort);
		return jo.toString();
	}

	@RequestMapping(value = "/DisableDHCP", method = { RequestMethod.POST })
	@ResponseBody
	public String disableDHCP(HttpServletRequest request,
			@RequestParam String vxuuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getRouterManager().disableDHCP(vxuuid, user.getUserId());
		return jo.toString();
	}

	@RequestMapping(value = "/EnableDHCP", method = { RequestMethod.POST })
	@ResponseBody
	public String enableDHCP(HttpServletRequest request,
			@RequestParam String vxuuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getRouterManager().enableDHCP(vxuuid, user.getUserId());
		return jo.toString();
	}
}
