package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.ServerPort;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/03/28
 */
@Component
public class ServerPortDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<ServerPort> getServerPortOfSwPort(String serverPortid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "from ServerPort where serverportUuid ='"
				+ serverPortid + "'";
		Query query = session.createQuery(queryString);
		List<ServerPort> vlanList = query.list();
		session.close();
		return vlanList;
	}
}
