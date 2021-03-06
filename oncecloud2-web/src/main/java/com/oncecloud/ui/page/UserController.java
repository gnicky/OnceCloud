package com.oncecloud.ui.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oncecloud.entity.User;

@Controller
public class UserController {

	@RequestMapping(value = "/volume", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView volume(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 3);
		model.put("title", "硬盘");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/volume", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/snapshot", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView snapshot(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 4);
		model.put("title", "备份");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/snapshot", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/router", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView router(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 5);
		model.put("title", "虚拟路由");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/router", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/vnet", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView vnet(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 5);
		model.put("title", "私有网络");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/vnet", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/elasticip", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView elasticIP(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 6);
		model.put("title", "公网IP");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/elasticip", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/loadbalance", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView loadBalance(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 7);
		model.put("title", "负载均衡");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/loadbalance", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/firewall", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView firewall(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 8);
		model.put("title", "防火墙");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/firewall", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/alarm", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView alarm(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 13);
		model.put("title", "监控警告");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/alarm", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/expense/summary", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView expenseSummary(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 10);
		model.put("title", "计费清单");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/expensesummary", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/expense/query", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView expenseQuery(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		model.put("sideActive", 10);
		model.put("title", "计费查询");
		if (user.getUserLevel() > 0) {
			return new ModelAndView("user/expensequery", model);
		} else {
			return new ModelAndView(new RedirectView("/dashboard"));
		}
	}

	@RequestMapping(value = "/account/recharge", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView recharge(HttpServletRequest request) {
		return new ModelAndView("account/recharge");
	}

	@RequestMapping(value = "/account/history", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView history(HttpServletRequest request) {
		return new ModelAndView("account/history");
	}

	@RequestMapping(value = "/account/voucher", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView voucher(HttpServletRequest request) {
		return new ModelAndView("account/voucher");
	}

	@RequestMapping(value = "/account/register", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView register(HttpServletRequest request) {
		return new ModelAndView("account/register");
	}
}
