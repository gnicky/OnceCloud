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
import com.oncecloud.main.Constant;
import com.oncecloud.manager.QuotaManager;
import com.oncecloud.ui.model.CommonModifyModel;

@Controller
public class CommonController {
	private QuotaManager quotaManager;

	private QuotaManager getQuotaManager() {
		return quotaManager;
	}

	@Autowired
	private void setQuotaManager(QuotaManager quotaManager) {
		this.quotaManager = quotaManager;
	}
	
	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ModelAndView home(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			return new ModelAndView(new RedirectView("/login"));
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}
	
	@RequestMapping(value = "/dashboard", method = { RequestMethod.GET })
	public ModelAndView dashboard(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("title", "控制台");
		model.put("sideActive", 0);
		if (user.getUserLevel() > 0) {
			Quota quotaUsed = this.getQuotaManager().getQuotaUsed(user.getUserId());
			model.put("quotaUsed", quotaUsed);
			return new ModelAndView("user/dashboard", model);
		} else {
			return new ModelAndView("admin/dashboard", model);
		}
	}
	
	@RequestMapping(value = "/instance", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView instance(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 1);
		model.put("vncServer", Constant.noVNCServerPublic);
		if (user.getUserLevel() > 0) {
			model.put("title", "主机");
			return new ModelAndView("user/instance", model);
		} else {
			model.put("title", "虚拟机管理");
			return new ModelAndView("admin/instance", model);
		}
	}

	@RequestMapping(value = "/image", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView image(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sideActive", 2);
		model.put("title", "映像");
		return new ModelAndView("common/image", model);
	}

	@RequestMapping(value = "/log", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView log(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sideActive", 9);
		model.put("title", "操作日志");
		return new ModelAndView("common/log", model);
	}

	@RequestMapping(value = "/service", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView service(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sideActive", 11);
		model.put("title", "表单");
		return new ModelAndView("common/service", model);
	}
	
	@RequestMapping(value = "/image/detail")
	@ResponseBody
	public ModelAndView imageDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "映像");
		model.put("sideActive", 2);
		String imageUuid = request.getParameter("imageUuid");
		String imageType = request.getParameter("imageType");
		if (imageUuid != null) {
			request.getSession().setAttribute("imageUuid", imageUuid);
			request.getSession().setAttribute("imageType", imageType);
			return new ModelAndView("common/detail/imagedetail", model);
		} else {
			if (request.getSession().getAttribute("imageUuid") != null) {
				return new ModelAndView("common/detail/imagedetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/service/detail")
	@ResponseBody
	public ModelAndView questionDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "表单");
		model.put("sideActive", 11);
		String qaId = request.getParameter("qaId");
		if (qaId != null) {
			request.getSession().setAttribute("qaId", qaId);
			return new ModelAndView("common/detail/servicedetail", model);
		} else {
			if (request.getSession().getAttribute("qaId") != null) {
				return new ModelAndView("common/detail/servicedetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}
	
	@RequestMapping(value = "/common/modify")
	@ResponseBody
	public ModelAndView commonmodify(HttpServletRequest request,CommonModifyModel commonModifyModel) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("modifyType", commonModifyModel.getModifyType());
		model.put("modifyUuid", commonModifyModel.getModifyUuid());
		model.put("modifyName", commonModifyModel.getModifyName());
		String desc = commonModifyModel.getModifyDesc();
		model.put("modifyDesc", desc.equals("&nbsp;") ? "" : desc);
		return new ModelAndView("common/modify", model);
	}
}
