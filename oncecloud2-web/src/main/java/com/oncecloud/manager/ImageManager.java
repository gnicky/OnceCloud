package com.oncecloud.manager;

import org.json.JSONArray;


public interface ImageManager {
//	public abstract PoolDAO getPoolDAO();
//
//	public abstract void setPoolDAO(PoolDAO poolDAO);
//
//	public abstract HostSRDAO getHostSRDAO();
//
//	public abstract void setHostSRDAO(HostSRDAO hostSRDAO);
//
//	public abstract boolean imageExist(String imageUuid, String poolUuid);

	public abstract JSONArray getImageList(int userId, int userLevel, int page,
			int limit, String search, String type);

//	public abstract JSONObject cloneImage(int userId, int userLevel,
//			String vmUuid, String imageName, String imageDesc);
//
//	public abstract JSONObject makeImage(String uuid, String newName, int uid,
//			String desc);
//
//	public abstract JSONArray createImage(int userId, int userLevel,
//			String imageUuid, String imageName, String imageServer,
//			int imageOs, String imageDesc, String imagePwd);
//
//	public abstract JSONObject deleteImage(int userId, String imageId,
//			String imageName);
//
//	public abstract JSONObject getBasciList(String imageUuid);
//
//	public abstract JSONArray getShareImageList(String poolUuid,
//			String[] imageUuids);
//
//	public abstract JSONArray shareImages(String sorpoolUuid,
//			String despoolUuid, String[] imageUuids);
//
//	public abstract boolean updateImage(String uuid, String pwd, String desc,
//			int disk, int platform);
}
