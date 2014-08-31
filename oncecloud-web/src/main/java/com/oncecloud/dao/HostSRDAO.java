package com.oncecloud.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.HostSR;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/04/04
 */
@Component
public class HostSRDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public HostSR createHostSR(String hostUuid, String srUuid) {
		HostSR hsr = new HostSR();
		hsr.setHostUuid(hostUuid);
		hsr.setSrUuid(srUuid);
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.save(hsr);
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
		return hsr;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getSRList(String hostUuid) {
		Session session = this.getSessionHelper().getMainSession();
		Set<String> srList = new HashSet<String>();
		Query query = session.createQuery("from HostSR where hostUuid = '"
				+ hostUuid + "'");
		List<HostSR> hsrList = query.list();
		for (HostSR hsr : hsrList) {
			srList.add(hsr.getSrUuid());
		}
		session.close();
		return srList;
	}
}
