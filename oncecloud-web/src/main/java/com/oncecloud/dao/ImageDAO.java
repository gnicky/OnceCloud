package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
	private SessionHelper sessionHelper;
	private UserDAO userDAO;
	private OverViewDAO overViewDAO;

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

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	@SuppressWarnings("unchecked")
	public Image getImage(String imageUuid) {
		Image image = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session.createQuery("from Image where imageUuid = '"
					+ imageUuid + "'");
			List<Image> imageList = query.list();
			if (imageList.size() == 1) {
				image = imageList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return image;
	}

	@SuppressWarnings("unchecked")
	public List<Image> getOnePageImageList(int userId, int userLevel, int page,
			int limit, String search, String type) {
		List<Image> imageList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			int startPos = (page - 1) * limit;
			String poolUuid = this.getUserDAO().getUser(userId)
					.getUserAllocate();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return imageList;
	}

	public int countAllImageList(int userId, String search, int userLevel,
			String type) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return total;
	}

	public Image createImage(String imageUId, String imageName, int imageUID,
			int imagePlatform, String imageServer, String imageDesc,
			String imagePwd) {
		Image image = null;
		Session session = null;
		Transaction tx = null;
		try {
			image = new Image(imageUId, imageName, imagePwd, imageUID, 20,
					imagePlatform, 1, imageServer, imageDesc, new Date());
			image.setPreAllocate(0);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(image);
			this.getOverViewDAO().updateOverViewfield(session, "viewImage",
					true);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			image = null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return image;
	}

	public boolean deleteImage(String imageId) {
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update Image set imageStatus = 0 where imageUuid='"
					+ imageId + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			this.getOverViewDAO().updateOverViewfield(session, "viewImage",
					false);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public int countByHost(String hostUuid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "";
		queryString = "select count(*) from Image where imageStatus!=0 and hostUuid='"
				+ hostUuid + "'";
		Query query = session.createQuery(queryString);
		int count = ((Number) query.iterate().next()).intValue();
		session.close();
		return count;
	}

	/**
	 * @param imageuuid
	 * @param newName
	 * @param description
	 * @author xpx 2014-7-11
	 */
	public void updateName(String imageuuid, String newName, String description) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update Image set imageName=:name, imageDesc=:desc where imageUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", imageuuid);
			query.setString("desc", description);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Image getLBImage(int userId) {
		Image image = null;
		Session session = null;
		try {
			String poolUuid = this.getUserDAO().getUser(userId)
					.getUserAllocate();
			session = this.getSessionHelper().getMainSession();
			Query query = session
					.createQuery("from Image where imagePlatform = 2 and poolUuid = :poolUuid");
			query.setString("poolUuid", poolUuid);
			List<Image> imageList = query.list();
			image = imageList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return image;
	}

	public Image getDBImage(String type, int throughout) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Image getRTImage(int userId) {
		Image image = null;
		Session session = null;
		try {
			String poolUuid = this.getUserDAO().getUser(userId)
					.getUserAllocate();
			session = this.getSessionHelper().getMainSession();
			// 目前使用LB镜像,创建完成后改为=3
			Query query = session
					.createQuery("from Image where imagePlatform = 3 and poolUuid = :poolUuid");
			query.setString("poolUuid", poolUuid);
			List<Image> imageList = query.list();
			image = imageList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
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
			Query query = session
					.createQuery("from Image where imageUID = 1 and preAllocate > 0");
			imageList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return imageList;
	}
}
