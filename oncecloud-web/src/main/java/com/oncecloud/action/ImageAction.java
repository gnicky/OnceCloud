package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.User;
import com.oncecloud.manager.ImageManager;

/**
 * @author hehai yly cyh
 * @version 2014/08/23
 */
@Component
public class ImageAction extends HttpServlet {
	private static final long serialVersionUID = 6797480496837378538L;
	private ImageManager imageManager;

	private ImageManager getImageManager() {
		return imageManager;
	}

	@Autowired
	private void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("getPageList")) {
				int userLevel = user.getUserLevel();
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				String type = request.getParameter("type");
				JSONArray ja = this.getImageManager().getImageList(userId,
						userLevel, page, limit, search, type);
				out.print(ja.toString());
			} else if (action.equals("create")) {
				int userLevel = user.getUserLevel();
				String imageUuid = request.getParameter("imageUUId");
				String imageName = request.getParameter("imageName");
				String imageServer = request.getParameter("imageServer");
				Integer imageOs = Integer.parseInt(request
						.getParameter("imageOs"));
				String imageDesc = request.getParameter("imageDesc");
				String imagePwd = request.getParameter("imagePwd");
				JSONArray ja = this.getImageManager().createImage(userId,
						userLevel, imageUuid, imageName, imageServer, imageOs,
						imageDesc, imagePwd);
				out.print(ja.toString());
			} else if (action.equals("delete")) {
				String imageId = request.getParameter("imageId");
				String imageName = request.getParameter("imageName");
				JSONObject jo = this.getImageManager().deleteImage(userId,
						imageId, imageName);
				out.print(jo.toString());
			} else if (action.equals("clone")) {
				int userLevel = user.getUserLevel();
				String vmUuid = request.getParameter("vmuuid");
				String imageName = request.getParameter("imagename");
				String imageDesc = request.getParameter("imagedesc");
				JSONObject jo = this.getImageManager().cloneImage(userId,
						userLevel, vmUuid, imageName, imageDesc);
				out.print(jo.toString());
			} else if (action.equals("detail")) {
				String imageuuid = request.getParameter("imageuuid");
				String imagetype = request.getParameter("imagetype");
				session.setAttribute("imageUuid", imageuuid);
				session.setAttribute("imageType", imagetype);
			} else if (action.equals("getoneimage")) {
				String imageUuid = request.getParameter("imageUuid");
				JSONObject jo = this.getImageManager()
						.getImageDetail(imageUuid);
				out.print(jo.toString());
			}
		}
	}
}
