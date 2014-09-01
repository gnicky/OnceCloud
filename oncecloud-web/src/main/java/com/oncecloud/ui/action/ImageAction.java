package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.ImageManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/ImageAction")
@Controller
public class ImageAction {

	private ImageManager imageManager;

	public ImageManager getImageManager() {
		return imageManager;
	}

	@Autowired
	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	@RequestMapping(value = "/ImageList", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String imageList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			int userId = user.getUserId();
			int userLevel = user.getUserLevel();
			JSONArray ja = imageManager.getImageList(userId, userLevel, list.getPage(),
					list.getLimit(), list.getSearch(), list.getType());
			return ja.toString();
		} else {
			return "";
		}
	}

}
