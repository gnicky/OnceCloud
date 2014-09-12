package com.oncecloud.ui.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.Assets;
import com.oncecloud.manager.AssetsManager;

@RequestMapping("/AssetsAction")
@Controller
public class AssetsAction {
	private AssetsManager assetsManager;

	public AssetsManager getAssetsManager() {
		return assetsManager;
	}
	
	@Autowired
	public void setAssetsManager(AssetsManager assetsManager) {
		this.assetsManager = assetsManager;
	}


	@RequestMapping(value = "/AssetsList")
	@ResponseBody
	public List<Assets> createFirewall(HttpServletRequest request) {
		return this.getAssetsManager().getAssets(0);
	}
}