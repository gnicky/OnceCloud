package com.oncecloud.common.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.common.helper.SessionHelper;
import com.oncecloud.dao.OCExceptionDAO;
import com.oncecloud.entity.OCHException;

@Component("OCExceptionDAO")
public class OCExceptionDAOImpl implements OCExceptionDAO{

	private SessionHelper sessionHelper;
	
	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public void save(OCHException exc) {
		Session session = null;
		try {
			if(this.getSessionHelper() != null) {
				session = this.getSessionHelper().getMainSession();
			}
			if (session == null) {
				SessionHelper sh = new SessionHelper();
				session = sh.getMainSession();
			}
			session.beginTransaction();
			session.save(exc);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}
	
	public void delete(Date startTime, Date endTime) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String str = "delete OCHException where excDate between ? and ?";
			Query query = session.createQuery(str);
			query.setDate(0, startTime);
			query.setDate(1, endTime);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}
	
	public List<OCHException> search(int userId, Date startTime, Date endTime) {
		List<OCHException> list = new ArrayList<OCHException>();
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String str = "from OCHException where excUid = ? and excDate between ? and ?";
			Query query = session.createQuery(str);
			query.setInteger(0, userId);
			query.setDate(1, startTime);
			query.setDate(2, endTime);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return list;
	}

}
