package com.oncecloud.ui.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.Assets;
import com.oncecloud.entity.Quota;
import com.oncecloud.manager.AssetsManager;
import com.oncecloud.ui.model.ListModel;
import com.oncecloud.ui.model.UserMoneyModel;
import com.oncecloud.ui.model.UserQuotaModel;

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
	public List<Assets> assetsList(HttpServletRequest request) {
		return this.getAssetsManager().getAssets(0);
	}
	
	@RequestMapping(value = "/QuotaList")
	@ResponseBody
	public List<UserQuotaModel> quotaList(HttpServletRequest request,ListModel listModel) {
		return this.getAssetsManager().getQuotaList(0, listModel.getPage(), listModel.getLimit(),listModel.getSearch());
	}
	
	/*@RequestMapping(value = "/AssetsMonthList")
	@ResponseBody
	public List<UserMoneyModel> assetsMonthList(HttpServletRequest request,ListModel listModel) {
		return this.getAssetsManager().getAssetsMonthList(0, listModel.getPage(), listModel.getLimit(),listModel.getSearch());
	}*/
}