package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.VoucherManager;

@RequestMapping("/VoucherAction")
@Controller
public class VoucherAction {

	private VoucherManager voucherManager;

	public VoucherManager getVoucherManager() {
		return voucherManager;
	}

	@Autowired
	public void setVoucherManager(VoucherManager voucherManager) {
		this.voucherManager = voucherManager;
	}

	@RequestMapping(value = "/Confirm", method = { RequestMethod.POST })
	@ResponseBody
	public String confirm(HttpServletRequest request, @RequestParam int userid) {
		JSONObject jo = this.getVoucherManager().confirmVoucher(userid);
		return jo.toString();
	}

	@RequestMapping(value = "/Deny", method = { RequestMethod.POST })
	@ResponseBody
	public String deny(HttpServletRequest request, @RequestParam int userid) {
		JSONObject jo = this.getVoucherManager().denyVoucher(userid);
		return jo.toString();
	}

	@RequestMapping(value = "/Apply", method = { RequestMethod.POST })
	@ResponseBody
	public String apply(HttpServletRequest request, @RequestParam int voucher) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getVoucherManager().applyVoucher(user.getUserId(),
				voucher);
		return jo.toString();
	}
}
