package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.SwPort;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/03/28
 */
@Component
public class SwPortDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<SwPort> getSwPortOfVlan(String vlanid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "from SwPort where vlanUuid ='" + vlanid + "'";
		Query query = session.createQuery(queryString);
		List<SwPort> vlanList = query.list();
		session.close();
		return vlanList;
	}
}
