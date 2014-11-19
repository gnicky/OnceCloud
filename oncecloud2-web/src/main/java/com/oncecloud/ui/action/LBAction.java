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
import com.oncecloud.manager.LBManager;
import com.oncecloud.ui.model.AdminListModel;

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

	@RequestMapping(value = "/AdminList", method = { RequestMethod.GET })
	@ResponseBody
	public String adminList(HttpServletRequest request, AdminListModel alrModel) {
		JSONArray ja = this.getLbManager().getAdminLBList(alrModel.getPage(),
				alrModel.getLimit(), alrModel.getHost(),
				alrModel.getImportance(), alrModel.getType());
		return ja.toString();
	}
}
