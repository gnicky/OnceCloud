package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Position;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/03/28
 */
@Component
public class PositionDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public Position getPosition(String keyid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "from Position where objectUuid ='" + keyid + "'";
		Query query = session.createQuery(queryString);
		List<Position> positionList = query.list();
		session.close();
		if (positionList != null && positionList.size() == 1) {
			return positionList.get(0);
		}
		return null;
	}

	public Position savePosition(Position position) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(position);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return position;
	}
}
