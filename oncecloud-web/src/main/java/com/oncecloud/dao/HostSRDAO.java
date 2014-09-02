package com.oncecloud.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.HostSR;
import com.oncecloud.helper.SessionHelper;

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
		HostSR hostSR = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			hostSR = new HostSR();
			hostSR.setHostUuid(hostUuid);
			hostSR.setSrUuid(srUuid);
			session.save(hostSR);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return hostSR;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getSRList(String hostUuid) {
		Set<String> srList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			srList = new HashSet<String>();
			Query query = session.createQuery("from HostSR where hostUuid = '"
					+ hostUuid + "'");
			List<HostSR> hsrList = query.list();
			for (HostSR hsr : hsrList) {
				srList.add(hsr.getSrUuid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return srList;
	}

}
