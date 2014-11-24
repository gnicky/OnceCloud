package com.oncecloud.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.PowerDAO;
import com.oncecloud.entity.Power;
import com.oncecloud.helper.SessionHelper;

@Component("PowerDAO")
public class PowerDAOImpl implements PowerDAO {

    private SessionHelper sessionHelper;
	
	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}
	
	
	public boolean addPower(Power power) {
		// TODO Auto-generated method stub
		boolean result = false;
		if (power != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.save(power);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	public boolean editPower(Power power) {
		// TODO Auto-generated method stub
		boolean result = false;
		if (power != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.saveOrUpdate(power);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	public boolean removePower(Power power) {
		// TODO Auto-generated method stub
		boolean result = false;
		if (power != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.delete(power);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	public Power getPowerByHostID(String hostUuid) {
		// TODO Auto-generated method stub
		Power power = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session.createQuery("from Power where hostUuid = '" + hostUuid + "'");
			power =(Power) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return power;
	}

}
