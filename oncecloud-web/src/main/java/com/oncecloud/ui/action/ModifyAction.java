package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.ModifyManager;
import com.oncecloud.ui.model.CommonModifyModel;
import com.oncecloud.ui.model.ListModel;


@RequestMapping("/ModifyAction")
@Controller
public class ModifyAction {
	private ModifyManager modifyManager;
	

	public ModifyManager getModifyManager() {
		return modifyManager;
	}

	@Autowired
	public void setModifyManager(ModifyManager modifyManager) {
		this.modifyManager = modifyManager;
	}

	@RequestMapping(value = "/ModifyBasicInfo", method = { RequestMethod.POST })
	@ResponseBody
	public String modifyBasicInfo(HttpServletRequest request, CommonModifyModel commonModifyModel) {
		String modifyType = commonModifyModel.getModifyType();
		String modifyUuid = commonModifyModel.getModifyUuid();
		String modifyName = commonModifyModel.getModifyName();
		String modifyDesc = commonModifyModel.getModifyDesc();
		modifyDesc = modifyDesc.trim();
		if (modifyDesc.equals("")) {
			modifyDesc = null;
		} else if (modifyDesc.length() > 80) {
			modifyDesc = modifyDesc.substring(0, 79);
		}
		
		JSONObject jo = this.getModifyManager().modifyBasicInfo(modifyUuid, modifyName, modifyDesc, modifyType);
		return jo.toString();
	}

}
