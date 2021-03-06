package com.oncecloud.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.entity.Quota;
import com.oncecloud.helper.SessionHelper;

@Component("QuotaDAO")
public class QuotaDAOImpl implements QuotaDAO{
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	/**
	 * 获取已用的配额
	 * 
	 * @param userId
	 * @return
	 */
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

	/**
	 * 获取全部的配额
	 * 
	 * @param userId
	 * @return
	 */
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

	/**
	 * 更新配额
	 * 
	 * @param userId
	 * @param filedName
	 * @param size
	 * @param isadd
	 */
	public synchronized void updateQuota(int userId,
			String filedName, int size, boolean isadd) {
		Session session = sessionHelper.getMainSession();
		session.beginTransaction();
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
		session.getTransaction().commit();
	}

	/**
	 * 初始化配额
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public void initQuota(Integer userId) {
		try {
			Session session = sessionHelper.getMainSession();
			session.beginTransaction();
			if (userId == 1) {
				Quota quota = new Quota(1, 2, 5, 5, 5, 10, 100, 10, 10, 2, 5, 3,
						10, 20, 10, 0);
				session.save(quota);
			} else {
				Quota def = new Quota(1, 2, 5, 5, 5, 10, 100, 10, 10, 2, 5, 3,
						10, 20, 10, 0);
				def.setQuotaUID(userId);
				Quota current = new Quota(userId, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
						0, 0, 0, 1);
				session.save(def);
				session.save(current);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新配额
	 * 
	 * @param newQuota
	 * @return
	 */
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
