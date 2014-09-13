package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Backend;
import com.oncecloud.helper.SessionHelper;

@Component
public class BackendDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public boolean changeBackendStatus(String backUuid, int state) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			if (backend != null) {
				backend.setBackStatus(state);
				session.update(backend);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean checkRepeat(String beuuid, int port) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("vmUuid", beuuid))
					.add(Restrictions.eq("vmPort", port))
					.setProjection(Projections.rowCount());
			int total = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			if (0 == total) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public Backend createBackend(String backUuid, String backName,
			String vmUuid, String vmIp, Integer vmPort, Integer backWeight,
			String foreUuid) {
		Backend backend = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			backend = new Backend();
			backend.setBackUuid(backUuid);
			backend.setBackName(backName);
			backend.setVmUuid(vmUuid);
			backend.setVmIp(vmIp);
			backend.setVmPort(vmPort);
			backend.setBackWeight(backWeight);
			backend.setForeUuid(foreUuid);
			backend.setBackStatus(1);// 设置正常运行0/禁用,1/正常
			backend.setCreateDate(new Date());
			session.save(backend);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backend;
	}

	public boolean deleteBackend(String backUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			if (backend != null) {
				session.delete(backend);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean deleteBackendByFrontend(Session session, String foreUuid) {
		boolean result = false;
		try {
			String queryString = "delete from Backend where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> doGetBackendListByFrontend(Session session,
			String foreUuid, int forbid) {
		List<Backend> backendList = null;
		try {
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("foreUuid", foreUuid))
					.add(Restrictions.ge("backStatus", forbid))
					.addOrder(Order.desc("createDate"));
			backendList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backendList;
	}
}
