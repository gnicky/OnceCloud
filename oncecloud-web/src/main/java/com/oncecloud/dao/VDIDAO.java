package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OCVDI;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/08/21
 */
@Component
public class VDIDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public int countFreeVDI(String tplUuid) {
		int count = -1;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select count(*) from OCVDI where tplUuid=:tplUuid";
			Query query = session.createQuery(queryString);
			query.setString("tplUuid", tplUuid);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public OCVDI getFreeVDI(String tplUuid) {
		OCVDI vdi = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from OCVDI where tplUuid=:tplUuid";
			Query query = session.createQuery(queryString);
			query.setString("tplUuid", tplUuid);
			List<OCVDI> vdiList = query.list();
			vdi = vdiList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vdi;
	}

	public boolean deleteVDI(OCVDI vdi) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.delete(vdi);
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public OCVDI saveVDI(String tplUuid, String vmUuid) {
		OCVDI vdi = null;
		Session session = null;
		Transaction tx = null;
		try {
			vdi = new OCVDI();
			vdi.setTplUuid(tplUuid);
			vdi.setVdiUuid(vmUuid);
			vdi.setCreateDate(new Date());
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.save(vdi);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vdi;
	}
}
