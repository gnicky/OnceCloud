package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.Image;

public interface ImageDAO {

	public abstract Image getImage(String imageUuid);

//	public abstract Image getDBImage(String type, int throughout);
//
//	public abstract Image getLBImage(int userId);
//
//	public abstract Image getRTImage(int userId);
//
//	public abstract List<Image> getSystemImage();

	public abstract List<Image> getOnePageImageList(int userId, int userLevel,
			int page, int limit, String search, String type, String poolUuid);

//	public abstract int countByHost(String hostUuid);

	public abstract int countAllImageList(int userId, String search,
			int userLevel, String type);

//	public abstract boolean deleteImage(String imageId);
//
//	public abstract Image createImage(String imageUuid, String imageName,
//			int imageUID, int imagePlatform, String imageServer,
//			String imageDesc, String imagePwd);
//
//	/**
//	 * @param imageuuid
//	 * @param newName
//	 * @param description
//	 * @author xpx 2014-7-11
//	 */
//	public abstract boolean updateName(String imageuuid, String newName,
//			String description);
//
//	public abstract boolean isShared(String poolUuid, String referenceUuid);
//
//	public abstract boolean shareImage(String imageUuid, String referenceUuid,
//			String poolUuid);
//
//	public abstract boolean updateImage(Image image);
//
//	public abstract boolean save(Image image);
//
//	public abstract boolean checkImage(String uuid);

}
