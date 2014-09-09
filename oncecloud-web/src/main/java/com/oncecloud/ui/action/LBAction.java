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
import com.oncecloud.manager.LBManager;
import com.oncecloud.ui.model.AdminListModel;
import com.oncecloud.ui.model.CreateBackModel;
import com.oncecloud.ui.model.CreateForeModel;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/LBAction")
@Controller
public class LBAction {
	private LBManager lbManager;

	public LBManager getLbManager() {
		return lbManager;
	}

	@Autowired
	public void setLbManager(LBManager lbManager) {
		this.lbManager = lbManager;
	}

	@RequestMapping(value = "/LBList", method = { RequestMethod.GET })
	@ResponseBody
	public String lbList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getLbManager().getLBList(userId, list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/AdminStartUp", method = { RequestMethod.GET })
	public void lbAdminStartUp(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		this.getLbManager().lbAdminShutUp(uuid, userId);
	}

	@RequestMapping(value = "/AdminShutDown", method = { RequestMethod.GET })
	public void lbAdminShutDown(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			this.getLbManager().lbAdminShutDown(uuid, force, userId);
		}
	}

	@RequestMapping(value = "/UpdateStar", method = { RequestMethod.POST })
	public void lbupdateStar(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam int num) {
		this.getLbManager().updateImportance(uuid, num);
	}

	@RequestMapping(value = "/LBDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String volumeDetail(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getLbManager().getLBDetail(uuid);
		return jo.toString();
	}
	
	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		JSONArray ja = this.getLbManager().getAdminLBList(alrModel.getPage(),
				alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
		return ja.toString();
	}
	
	@RequestMapping(value = "/ShutDown", method = { RequestMethod.POST })
	@ResponseBody
	public void shutDown(HttpServletRequest request,@RequestParam String uuid,@RequestParam String force) {
		User user = (User) request.getSession().getAttribute("user");
     	this.getLbManager().shutdownLB(uuid, force, user.getUserId(),user.getUserAllocate());
	}
	
	
	@RequestMapping(value = "/Start", method = { RequestMethod.POST })
	@ResponseBody
	public void startUp(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().startLB(uuid, user.getUserId(),user.getUserAllocate());
	}
	
	@RequestMapping(value = "/Destroy", method = { RequestMethod.POST })
	@ResponseBody
	public void destroy(HttpServletRequest request,@RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().lbDelete(uuid,user.getUserId(), user.getUserAllocate());
	}
	
	@RequestMapping(value = "/Quota", method = { RequestMethod.POST })
	@ResponseBody
	public String quota(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getLbManager().lbQuota(user.getUserId());
		return ja.toString();
	}
	
	@RequestMapping(value = "/Create", method = { RequestMethod.POST })
	@ResponseBody
	public void create(HttpServletRequest request,@RequestParam String uuid,@RequestParam String name,@RequestParam int capacity) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().createLB(name, uuid, capacity, user.getUserId(),user.getUserAllocate());
	}
	
	@RequestMapping(value = "/ForeList", method = { RequestMethod.POST })
	@ResponseBody
	public String foreList(HttpServletRequest request,@RequestParam String lbuuid) {
		JSONArray feArray = this.getLbManager().getFEListByLB(lbuuid);
		return feArray.toString();
	}
	
	@RequestMapping(value = "/ApplyLB", method = { RequestMethod.POST })
	@ResponseBody
	public String applyLB(HttpServletRequest request,@RequestParam String lbuuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getLbManager().lbApplylb(lbuuid, user.getUserId());
		return jo.toString();
	}
	
	@RequestMapping(value = "/ForbidBack", method = { RequestMethod.POST })
	@ResponseBody
	public String forbidBack(HttpServletRequest request,@RequestParam int state,@RequestParam String backUuid,@RequestParam String lbUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getLbManager().lbForbidback(backUuid,state, lbUuid, user.getUserId());
		return jo.toString();
	}
	
	@RequestMapping(value = "/ForbidFore", method = { RequestMethod.POST })
	@ResponseBody
	public String forbidFore(HttpServletRequest request,@RequestParam int state,@RequestParam String foreUuid,@RequestParam String lbUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getLbManager().lbForbidfore(foreUuid,state, lbUuid, user.getUserId());
		return jo.toString();
	}
	
	@RequestMapping(value = "/DeleteFore", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteFore(HttpServletRequest request,@RequestParam String foreUuid,@RequestParam String lbUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getLbManager().lbDeletefore(foreUuid, lbUuid, user.getUserId());
		return jo.toString();
	}
	
	@RequestMapping(value = "/DeleteBack", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteBack(HttpServletRequest request,@RequestParam String foreUuid,@RequestParam String lbUuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getLbManager().lbDeleteBack(foreUuid, lbUuid, user.getUserId());
		return jo.toString();
	}
	
	@RequestMapping(value = "/CreateFore", method = { RequestMethod.POST })
	@ResponseBody
	public void createFore(HttpServletRequest request,CreateForeModel createForeModel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().lbCreateFore(createForeModel.getName(), createForeModel.getForeUuid(), createForeModel.getLbUuid(),
				createForeModel.getProtoCol(), createForeModel.getPort(), createForeModel.getPolicy(), user.getUserId());
	}
	
	@RequestMapping(value = "/CheckFore", method = { RequestMethod.POST })
	@ResponseBody
	public boolean checkFore(HttpServletRequest request,@RequestParam int port,@RequestParam String lbuuid) {
		return this.getLbManager().checkFore(lbuuid, port);
	}
	
	@RequestMapping(value = "/UpdateFore", method = { RequestMethod.POST })
	@ResponseBody
	public void checkFore(HttpServletRequest request,CreateForeModel createForeModel) {
		User user = (User) request.getSession().getAttribute("user");
	    this.getLbManager().lbUpdatefore(createForeModel.getName(), createForeModel.getForeUuid(), createForeModel.getPolicy(), createForeModel.getLbUuid(), user.getUserId());
	}
	
	@RequestMapping(value = "/CheckBack", method = { RequestMethod.POST })
	@ResponseBody
	public String checkBack(HttpServletRequest request,@RequestParam int port,@RequestParam String beuuid) {
		JSONObject jo = this.getLbManager().lbCheckBack(beuuid, port);
		return jo.toString();
	}
	
	@RequestMapping(value = "/CreateBack", method = { RequestMethod.POST })
	@ResponseBody
	public void createBack(HttpServletRequest request,CreateBackModel createBackModel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getLbManager().lbCreateBack(createBackModel.getName(), createBackModel.getLbUuid(), createBackModel.getBackUuid(), createBackModel.getVmUuid(), createBackModel.getVmIP(), createBackModel.getPort(), createBackModel.getWeight(), createBackModel.getFeUuid(), user.getUserId());
	}
	
	@RequestMapping(value = "/LBsOfUser", method = { RequestMethod.POST })
	@ResponseBody
	public String getLBsOfUser(HttpServletRequest request, ListModel lm) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getLbManager().getLBsOfUser(user.getUserId(),
				lm.getPage(), lm.getLimit(), lm.getSearch());
		return ja.toString();
	}
}
