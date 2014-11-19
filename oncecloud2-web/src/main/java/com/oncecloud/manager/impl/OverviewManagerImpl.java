package com.oncecloud.manager.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.entity.OverView;
import com.oncecloud.manager.OverviewManager;

@Component("OverviewManager")
public class OverviewManagerImpl implements OverviewManager {

	private OverViewDAO overViewDAO;

	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	public JSONArray getOverview() {
		JSONArray qaArray = new JSONArray();
		OverView ov = this.getOverViewDAO().getOverViewTotal();
		if (ov != null) {
			JSONObject tObj = new JSONObject();
			tObj.put("viewId", ov.getViewId());
			tObj.put("viewDc", ov.getViewDc());
			tObj.put("viewDhcp", ov.getViewDhcp());
			tObj.put("viewFirewall", ov.getViewFirewall());
			tObj.put("viewImage", ov.getViewImage());
			tObj.put("viewOutip", ov.getViewOutip());
			tObj.put("viewPool", ov.getViewPool());
			tObj.put("viewRack", ov.getViewRack());
			tObj.put("viewServer", ov.getViewServer());
			tObj.put("viewSr", ov.getViewSr());
			tObj.put("viewVm", ov.getViewVm());
			qaArray.put(tObj);
		}
		return qaArray;
	}

}
