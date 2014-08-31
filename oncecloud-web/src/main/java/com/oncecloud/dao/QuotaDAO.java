package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;
import com.oncecloud.helper.SessionHelper;

@Component
public class QuotaDAO {
	private SessionHelper sessionHelper;
	private UserDAO userDAO;

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

	@SuppressWarnings("unchecked")
	public Quota getQuotaUsed(int quotaUID) {
		Quota quota = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session.createQuery("from Quota where quotaUID = "
					+ quotaUID + " and quotaType = 1");
			List<Quota> quotaList = query.list();
			if (quotaList.size() == 1) {
				quota = quotaList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return quota;
	}

	@SuppressWarnings("unchecked")
	public Quota getQuotaTotal(int quotaUID) {
		Quota quota = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session.createQuery("from Quota where quotaUID = "
					+ quotaUID + " and quotaType = 0");
			List<Quota> quotaList = query.list();
			if (quotaList.size() == 1) {
				quota = quotaList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return quota;
	}

	public void insertQuota(Session session, int quotaUID, int quotaIP,
			int quotaVM, int quotaSnapshot, int quotaImage, int quotaDiskN,
			int quotaDiskS, int quotaSsh, int quotaFirewall, int quotaRoute,
			int quotaVlan, int quotaLoadBalance, int quotaBandwidth,
			int quotaMemory, int quotaCpu, int quotaType) throws Exception {
		if (session == null || !session.isOpen()) {
			throw new Exception("Session not open");
		}
		Quota quota = new Quota(quotaUID, quotaIP, quotaVM, quotaSnapshot,
				quotaImage, quotaDiskN, quotaDiskS, quotaSsh, quotaFirewall,
				quotaRoute, quotaVlan, quotaLoadBalance, quotaBandwidth,
				quotaMemory, quotaCpu, quotaType);
		session.save(quota);
		session.close();
	}

	public synchronized boolean updateQuotaField(Session session, int userId,
			String filedName, int size, boolean isadd) {
		boolean result = false;
		if (session == null || !session.isOpen()) {
			return result;
		}
		try {
			User user = this.getUserDAO().getUser(userId);
			if (user.getUserLevel() == 0) {
				return true;
			}
			String setString = filedName + "=" + filedName + "+" + size;
			if (isadd == false) {
				setString = filedName + "=" + filedName + "-" + size;
			}
			String queryString = String
					.format("update Quota set %s where quotaUID = :userId and quotaType = 1",
							setString);
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.executeUpdate();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public synchronized void changeQuotaBandwidth(int quotaUID, int size) {
		Quota quotaUsed = getQuotaUsed(quotaUID);
		quotaUsed.setQuotaBandwidth(quotaUsed.getQuotaBandwidth() + size);
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		session.update(quotaUsed);
		tx.commit();
		session.close();
	}

	public void initQuota(Session session, Integer userId) throws Exception {
		if (session == null || !session.isOpen()) {
			throw new Exception("Session not open");
		}
		if (userId == 1) {
			insertQuota(session, 1, 2, 5, 5, 5, 10, 100, 10, 10, 2, 5, 3, 10,
					20, 10, 0);
		} else {
			Quota def = getQuotaTotal(1);
			def.setQuotaUID(userId);
			def.setQuotaID(null);
			Quota current = new Quota(userId, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
					0, 0, 0, 1);
			session.save(def);
			session.save(current);
		}
	}

	public boolean updateQuota(Quota newQuota) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(newQuota);
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

}
