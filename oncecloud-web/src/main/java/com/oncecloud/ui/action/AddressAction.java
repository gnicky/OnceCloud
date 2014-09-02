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
import com.oncecloud.manager.AddressManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/AddressAction")
@Controller
public class AddressAction {
	private AddressManager addressManager;

	public AddressManager getAddressManager() {
		return addressManager;
	}

	@Autowired
	public void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}

	@RequestMapping(value = "/DHCPList", method = { RequestMethod.GET })
	@ResponseBody
	public String adressDHCPList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getAddressManager().getDHCPList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/EIPList", method = { RequestMethod.GET })
	@ResponseBody
	public String addressEIPList(HttpServletRequest request, ListModel list) {
		JSONArray ja = this.getAddressManager().getPublicIPList(list.getPage(),
				list.getLimit(), list.getSearch());
		return ja.toString();
	}

	@RequestMapping(value = "/DeleteEIP", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteEIP(HttpServletRequest request,
			@RequestParam String ip, @RequestParam String uuid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getAddressManager().deletePublicIP(
				user.getUserId(), ip, uuid);
		return ja.toString();
	}

	@RequestMapping(value = "/DeleteDHCP", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteDHCP(HttpServletRequest request,
			@RequestParam String ip, @RequestParam String mac) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getAddressManager().deleteDHCP(user.getUserId(),
				ip, mac);
		return ja.toString();
	}
}
