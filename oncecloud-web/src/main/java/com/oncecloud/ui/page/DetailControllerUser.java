package com.oncecloud.ui.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oncecloud.main.Constant;

@Controller
public class DetailControllerUser {

	@RequestMapping(value = "/instance/detail")
	@ResponseBody
	public ModelAndView instanceDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "主机");
		model.put("sideActive", 1);
		model.put("vncServer", Constant.noVNCServerPublic);
		String uuid = request.getParameter("instanceUuid");
		if (uuid != null) {
			request.getSession().setAttribute("instanceUuid", uuid);
			request.getSession().setAttribute("showId",
					"i-" + uuid.substring(0, 8));
			return new ModelAndView("user/detail/instancedetail", model);
		} else {
			if (request.getSession().getAttribute("instanceUuid") != null) {
				return new ModelAndView("user/detail/instancedetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/volume/detail")
	@ResponseBody
	public ModelAndView volumeDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "硬盘");
		model.put("sideActive", 3);
		String uuid = request.getParameter("volumeUuid");
		if (uuid != null) {
			request.getSession().setAttribute("volumeUuid", uuid);
			request.getSession().setAttribute("showId",
					"vol-" + uuid.substring(0, 8));
			return new ModelAndView("user/detail/volumedetail", model);
		} else {
			if (request.getSession().getAttribute("volumeUuid") != null) {
				return new ModelAndView("user/detail/volumedetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/router/detail")
	@ResponseBody
	public ModelAndView routerDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "路由器");
		model.put("sideActive", 5);
		String uuid = request.getParameter("routerUuid");
		if (uuid != null) {
			request.getSession().setAttribute("routerUuid", uuid);
			request.getSession().setAttribute("showId",
					"rt-" + uuid.substring(0, 8));
			return new ModelAndView("user/detail/routerdetail", model);
		} else {
			if (request.getSession().getAttribute("routerUuid") != null) {
				return new ModelAndView("user/detail/routerdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/vnet/detail")
	@ResponseBody
	public ModelAndView vnetDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "私有网络");
		model.put("sideActive", 5);
		String uuid = request.getParameter("vnetUuid");
		if (uuid != null) {
			request.getSession().setAttribute("vnetUuid", uuid);
			request.getSession().setAttribute("showId",
					"vn-" + uuid.substring(0, 8));
			return new ModelAndView("user/detail/vnetdetail", model);
		} else {
			if (request.getSession().getAttribute("vnetUuid") != null) {
				return new ModelAndView("user/detail/vnetdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/elasticip/detail")
	@ResponseBody
	public ModelAndView elasticIPDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "公网IP");
		model.put("sideActive", 6);
		String eip = request.getParameter("eip");
		String eipUuid = request.getParameter("eipUuid");
		if (eipUuid != null && eip != null) {
			request.getSession().setAttribute("eip", eip);
			request.getSession().setAttribute("eipUuid", eipUuid);
			request.getSession().setAttribute("showId",
					"eip-" + eipUuid.substring(0, 8));
			return new ModelAndView("user/detail/elasticipdetail", model);
		} else {
			if (request.getSession().getAttribute("eip") != null
					&& request.getSession().getAttribute("eipUuid") != null) {
				return new ModelAndView("user/detail/elasticipdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/loadbalance/detail")
	@ResponseBody
	public ModelAndView loadBalanceDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "负载均衡");
		model.put("sideActive", 7);
		String uuid = request.getParameter("lbUuid");
		if (uuid != null) {
			request.getSession().setAttribute("lbUuid", uuid);
			request.getSession().setAttribute("showId",
					"lb-" + uuid.substring(0, 8));
			return new ModelAndView("user/detail/loadbalancedetail", model);
		} else {
			if (request.getSession().getAttribute("lbUuid") != null) {
				return new ModelAndView("user/detail/loadbalancedetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/firewall/detail")
	@ResponseBody
	public ModelAndView firewallDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "防火墙");
		model.put("sideActive", 8);
		String firewallId = request.getParameter("firewallId");
		if (firewallId != null) {
			request.getSession().setAttribute("firewallId", firewallId);
			request.getSession().setAttribute("showId",
					"fw-" + firewallId.substring(0, 8));
			return new ModelAndView("user/detail/firewalldetail", model);
		} else {
			if (request.getSession().getAttribute("firewallId") != null) {
				return new ModelAndView("user/detail/firewalldetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/snapshot/detail")
	@ResponseBody
	public ModelAndView snapshotDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "备份");
		model.put("sideActive", 4);
		String resourceUuid = request.getParameter("resourceUuid");
		String resourceType = request.getParameter("resourceType");
		String resourceName = request.getParameter("resourceName");
		if (resourceUuid != null) {
			request.getSession().setAttribute("resourceUuid", resourceUuid);
			request.getSession().setAttribute("resourceType", resourceType);
			request.getSession().setAttribute("resourceName", resourceName);
			request.getSession().setAttribute("showId", "bk-" + resourceUuid.substring(0, 8));
			return new ModelAndView("user/detail/snapshotdetail", model);
		} else {
			if (request.getSession().getAttribute("resourceUuid") != null) {
				return new ModelAndView("user/detail/snapshotdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}

	@RequestMapping(value = "/alarm/detail")
	@ResponseBody
	public ModelAndView alarmDetail(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "监控警告");
		model.put("sideActive", 13);
		String alarmUuid = request.getParameter("alarmUuid");
		if (alarmUuid != null) {
			request.getSession().setAttribute("alarmUuid", alarmUuid);
			request.getSession().setAttribute("showId",
					"al-" + alarmUuid.substring(0, 8));
			return new ModelAndView("user/detail/alarmdetail", model);
		} else {
			if (request.getSession().getAttribute("alarmUuid") != null) {
				return new ModelAndView("user/detail/alarmdetail", model);
			} else {
				return new ModelAndView(new RedirectView("/dashboard"));
			}
		}
	}
}
