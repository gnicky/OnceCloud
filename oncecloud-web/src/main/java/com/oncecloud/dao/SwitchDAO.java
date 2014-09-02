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

	/**
	 * 获取交换机列表
	 * 
	 * @param switchId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Switch> getSwitch(String switchId) {
		List<Switch> switchList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Switch where swState = 1 and swUuid = :switchId "
					+ "order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("switchId", switchId);
			switchList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return switchList;
	}

	/**
	 * 获取机架交换机列表
	 * 
	 * @param rackUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Switch> getSwitchOfRack(String rackUuid) {
		List<Switch> switchList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Switch where swState = 1 and rackUuid = :rackUuid "
					+ "order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("rackUuid", rackUuid);
			switchList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return switchList;
	}
}
