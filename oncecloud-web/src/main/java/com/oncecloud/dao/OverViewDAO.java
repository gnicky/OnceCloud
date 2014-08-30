package com.oncecloud.dao;

import java.util.List;

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

	@SuppressWarnings("unchecked")
	public OverView getOverViewTotal() {
		Session session = null;
		OverView overView = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session.createQuery("from OverView ");
			query.setCacheable(false);
			List<OverView> quotaList = query.list();
			if (quotaList.size() >= 1) {
				overView = quotaList.get(0);
			}
		} catch (Exception e) {
			System.out.println("出错了：");
			e.printStackTrace();
		} finally {
			if (session != null) {
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
