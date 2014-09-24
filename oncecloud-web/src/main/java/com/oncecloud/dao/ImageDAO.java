package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Image;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/03/20
 */
@Component
public class ImageDAO {
	private OverViewDAO overViewDAO;
	private QuotaDAO quotaDAO;
	private SessionHelper sessionHelper;
	private UserDAO userDAO;

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public Image getImage(String imageUuid) {
		Image image = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Image where imageUuid = :imageUuid");
			query.setString("imageUuid", imageUuid);
			image = (Image) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return image;
	}

	public Image getDBImage(String type, int throughout) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Image getLBImage(int userId) {
		Image image = null;
		String poolUuid = this.getUserDAO().getUser(userId).getUserAllocate();
		if (poolUuid != null) {
			Session session = null;

			try {

				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				Query query = session
						.createQuery("from Image where imagePlatform = 2 and poolUuid = :poolUuid");
				query.setString("poolUuid", poolUuid);
				List<Image> imageList = query.list();
				image = imageList.get(0);
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return image;
	}

	public Image getRTImage(int userId) {
		Image image = null;
		String poolUuid = this.getUserDAO().getUser(userId).getUserAllocate();
		if (poolUuid != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				Query query = session
						.createQuery("from Image where imagePlatform = 3 and poolUuid = :poolUuid");
				query.setString("poolUuid", poolUuid);
				image = (Image) query.uniqueResult();
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return image;
	}

	@SuppressWarnings("unchecked")
	public List<Image> getSystemImage() {
		List<Image> imageList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Image where imageUID = 1 and preAllocate > 0");
			imageList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return imageList;
	}

	@SuppressWarnings("unchecked")
	public List<Image> getOnePageImageList(int userId, int userLevel, int page,
			int limit, String search, String type) {
		List<Image> imageList = null;
		Session session = null;
		try {
			String poolUuid = this.getUserDAO().getUser(userId)
					.getUserAllocate();
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			if (userLevel == 0) {
				if (type.equals("system")) {
					queryString = "from Image where imageUID = 1 and imageName like '%"
							+ search
							+ "%' and imageStatus = 1 order by createDate desc";
				} else {
					queryString = "from Image where imageUID != 1 and imageName like '%"
							+ search
							+ "%' and imageStatus = 1 order by createDate desc";
				}
			} else {
				if (type.equals("system")) {
					queryString = "from Image where imageUID = 1 and imageName like '%"
							+ search
							+ "%' and imageStatus = 1 and poolUuid = '"
							+ poolUuid
							+ "' and imagePlatform < 2 order by createDate desc";
				} else {
					queryString = "from Image where imageUID = " + userId
							+ " and imageName like '%" + search
							+ "%' and imageStatus = 1 order by createDate desc";
				}
			}
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			imageList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return imageList;
	}

	public int countByHost(String hostUuid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			StringBuilder builder = new StringBuilder(
					"select count(*) from Image where ");
			builder.append("imageStatus != 0 and hostUuid = :hostUuid");
			Query query = session.createQuery(builder.toString());
			query.setString("hostUuid", hostUuid);
			count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	public int countAllImageList(int userId, String search, int userLevel,
			String type) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			if (userLevel == 0) {
				if (type.equals("system")) {
					queryString = "select count(*) from Image where imageUID = 1 and imageName like '%"
							+ search + "%' and imageStatus = 1";
				} else {
					queryString = "select count(*) from Image where imageUID != 1 and imageName like '%"
							+ search + "%' and imageStatus = 1";
				}
			} else {
				if (type.equals("system")) {
					queryString = "select count(*) from Image where imageUID = 1 and imageName like '%"
							+ search
							+ "%' and imageStatus = 1 and imagePlatform < 2";
				} else {
					queryString = "select count(*) from Image where imageUID = "
							+ userId
							+ " and imageName like '%"
							+ search
							+ "%' and imageStatus = 1";
				}
			}
			Query query = session.createQuery(queryString);
			total = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return total;
	}

	public boolean deleteImage(String imageId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Image set imageStatus = 0 where imageUuid='"
					+ imageId + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewImage",
					false);
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public Image createImage(String imageUuid, String imageName, int imageUID,
			int imagePlatform, String imageServer, String imageDesc,
			String imagePwd) {
		Image image = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			image = new Image(imageUuid, imageName, imagePwd, imageUID, 20,
					imagePlatform, 1, imageServer, imageDesc, new Date(), null);
			image.setPreAllocate(0);
			session.saveOrUpdate(image);
			this.getQuotaDAO().updateQuotaFieldNoTransaction(imageUID,
					"quotaImage", 1, true);
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewImage",
					true);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return image;
	}

	/**
	 * @param imageuuid
	 * @param newName
	 * @param description
	 * @author xpx 2014-7-11
	 */
	public boolean updateName(String imageuuid, String newName,
			String description) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Image set imageName=:name, imageDesc=:desc where imageUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", imageuuid);
			query.setString("desc", description);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
	
	public boolean isShared(String poolUuid, String referenceUuid) {
		boolean result = false;
		Session session = null;
		Integer count = 0;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Image where poolUuid=:poolUuid and referenceUuid=:referenceUuid and imageStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("poolUuid", poolUuid);
			query.setString("referenceUuid", referenceUuid);
			count = ((Number)query.uniqueResult()).intValue();
			result = count > 0? false:true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
	
	public boolean shareImage(String imageUuid, String referenceUuid, String poolUuid) {
		boolean result = false;
		Session session = null;
		try {
			Image oldImage = this.getImage(referenceUuid);
			Image image = new Image(imageUuid, oldImage.getImageName(), oldImage.getImagePwd(), 
					oldImage.getImageUID(), oldImage.getImageDisk(), oldImage.getImagePlatform(), 
					oldImage.getImageStatus(), poolUuid, oldImage.getImageDesc(), oldImage.getCreateDate(),
					referenceUuid);
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(image);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
