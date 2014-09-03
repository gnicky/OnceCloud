package com.oncecloud.ui.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
	
	
}
