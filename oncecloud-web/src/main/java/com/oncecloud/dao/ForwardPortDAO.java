package com.oncecloud.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.ForwardPort;
import com.oncecloud.helper.SessionHelper;

@Component
public class ForwardPortDAO {

	private SessionHelper sessionHelper;
	
	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}
	
	public boolean addPF (ForwardPort pf) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(pf);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
	
	public boolean deletePF(ForwardPort pf) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.delete(pf);
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<ForwardPort> getpfListByRouter(String routerUuid) {
		Session session = null;
		List<ForwardPort> pfList = new ArrayList<ForwardPort>();
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from ForwardPort where routerUuid=:routerUuid";
			Query query = session.createQuery(queryString);
			query.setString("routerUuid", routerUuid);
			pfList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return pfList;
	}
}
