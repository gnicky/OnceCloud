package com.oncecloud.dao;

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
			Query query = session.createQuery("from OverView");
			overView = (OverView)query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return overView;
	}

	public boolean updateOverViewfield(Session session, String filedName,
			boolean isadd) {
		boolean result = false;
		if (session == null || !session.isOpen()) {
			return result;
		}
		try {
			String queryString = String.format(
					"update OverView set %s where viewId = 1",
					isadd ? (filedName + "=" + filedName + "+1") : (filedName
							+ "=" + filedName + "-1"));
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
