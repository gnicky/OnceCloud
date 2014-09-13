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

	/**
	 * 获取端口号
	 * 
	 * @param portId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ServerPort> getServerPortOfSwPort(String portId) {
		List<ServerPort> vlanList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from ServerPort where serverportUuid = :portId";
			Query query = session.createQuery(queryString);
			query.setString("portId", portId);
			vlanList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vlanList;
	}
}
