package com.oncecloud.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.entity.OverView;
import com.oncecloud.helper.SessionHelper;

@Component("OverViewDAO")
public class OverViewDAOImpl implements OverViewDAO {
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

	public boolean updateOverViewfield(String filedName, boolean isadd) {
		Session session = null;
		try {
			session = getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = String.format(
					"update OverView set %s where viewId = 1",
					isadd ? (filedName + "=" + filedName + "+1") : (filedName
							+ "=" + filedName + "-1"));
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}
}
