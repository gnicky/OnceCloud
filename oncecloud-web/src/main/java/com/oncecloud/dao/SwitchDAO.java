package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Switch;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/03/28
 */
@Component
public class SwitchDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<Switch> getSwitchOfRack(String rackid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "from Switch where swState = 1 and rackUuid ='"
				+ rackid + "' order by createDate desc";
		Query query = session.createQuery(queryString);
		List<Switch> switchList = query.list();
		session.close();
		return switchList;
	}

	@SuppressWarnings("unchecked")
	public List<Switch> getSwitch(String switchid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "from Switch where swState = 1 and swUuid ='"
				+ switchid + "' order by createDate desc";
		Query query = session.createQuery(queryString);
		List<Switch> switchList = query.list();
		session.close();
		return switchList;
	}
}
