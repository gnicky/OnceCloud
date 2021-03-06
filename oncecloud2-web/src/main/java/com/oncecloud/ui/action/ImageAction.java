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
import com.oncecloud.manager.ImageManager;
import com.oncecloud.ui.model.ImageCloneModel;
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

	@RequestMapping(value = "/ImageList", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String imageList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		int userLevel = user.getUserLevel();
		JSONArray ja = this.getImageManager().getImageList(userId, userLevel,
				list.getPage(), list.getLimit(), list.getSearch(),
				list.getType());
		return ja.toString();
	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request,
			@RequestParam String imageId, @RequestParam String imageName) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getImageManager().deleteImage(user.getUserId(),
				imageId, imageName);
		return jo.toString();
	}

	@RequestMapping(value = "/Clone", method = { RequestMethod.POST })
	@ResponseBody
	public void clone(HttpServletRequest request,
			ImageCloneModel imagecloneModel) {
		User user = (User) request.getSession().getAttribute("user");
		this.getImageManager().cloneImage(user.getUserId(),
				user.getUserLevel(), imagecloneModel.getVmUuid(),
				imagecloneModel.getImageName(), imagecloneModel.getImageDesc());
	}

	@RequestMapping(value = "/BasicList", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String basicList(HttpServletRequest request,
			@RequestParam String uuid) {
		JSONObject jo = this.getImageManager().getBasciList(uuid);
		return jo.toString();
	}

}
