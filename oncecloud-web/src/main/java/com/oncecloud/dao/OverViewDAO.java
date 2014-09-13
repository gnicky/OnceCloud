package com.oncecloud.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OverView;
import com.oncecloud.helper.SessionHelper;

@Component
public class OverViewDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public OverView getOverViewTotal() {
		OverView overView = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(OverView.class);
			overView = (OverView) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return overView;
	}

	public boolean updateOverViewfieldNoTransaction(String filedName,
			boolean isadd) {
		boolean result = false;
		Session session = null;
		try {
			session = getSessionHelper().getMainSession();
			String queryString = String.format(
					"update OverView set %s where viewId = 1",
					isadd ? (filedName + "=" + filedName + "+1") : (filedName
							+ "=" + filedName + "-1"));
			Query query = session.createQuery(queryString);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
}
