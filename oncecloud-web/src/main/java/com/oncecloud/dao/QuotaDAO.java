package com.oncecloud.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Quota;
import com.oncecloud.helper.SessionHelper;

@Component
public class QuotaDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public Quota getQuotaUsed(int userId) {
		Quota quota = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Quota where quotaUID = :userId and quotaType = 1");
			query.setInteger("userId", userId);
			quota = (Quota) query.uniqueResult();
			session.getTransaction().commit();
			return quota;
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return quota;
	}

	public Quota getQuotaTotal(int userId) {
		Quota quota = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Quota where quotaUID = :userId and quotaType = 0");
			query.setInteger("userId", userId);
			quota = (Quota) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return quota;
	}

	public Quota getQuotaTotalNoTransaction(int userId) {
		Session session = this.getSessionHelper().getMainSession();
		Query query = session
				.createQuery("from Quota where quotaUID = :userId and quotaType = 0");
		query.setInteger("userId", userId);
		return (Quota) query.uniqueResult();
	}

	public void insertQuotaNoTransaction(int quotaUID, int quotaIP,
			int quotaVM, int quotaSnapshot, int quotaImage, int quotaDiskN,
			int quotaDiskS, int quotaSsh, int quotaFirewall, int quotaRoute,
			int quotaVlan, int quotaLoadBalance, int quotaBandwidth,
			int quotaMemory, int quotaCpu, int quotaType) throws Exception {
		Session session = sessionHelper.getMainSession();
		Quota quota = new Quota(quotaUID, quotaIP, quotaVM, quotaSnapshot,
				quotaImage, quotaDiskN, quotaDiskS, quotaSsh, quotaFirewall,
				quotaRoute, quotaVlan, quotaLoadBalance, quotaBandwidth,
				quotaMemory, quotaCpu, quotaType);
		session.save(quota);
	}

	public synchronized void updateQuotaFieldNoTransaction(int userId,
			String filedName, int size, boolean isadd) {
		Session session = sessionHelper.getMainSession();
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
	}

	public void initQuotaNoTransaction(Integer userId) throws Exception {
		Session session = sessionHelper.getMainSession();
		if (userId == 1) {
			insertQuotaNoTransaction(1, 2, 5, 5, 5, 10, 100, 10, 10, 2, 5, 3,
					10, 20, 10, 0);
		} else {
			Quota def = getQuotaTotalNoTransaction(userId);
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.update(newQuota);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

}
