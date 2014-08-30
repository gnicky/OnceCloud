package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Vlan;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/03/28
 */
@Component
public class VlanDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<Vlan> getVlanOfSwitch(String switchId) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "from Vlan where swUuid ='" + switchId + "'";
		Query query = session.createQuery(queryString);
		List<Vlan> vlanList = query.list();
		session.close();
		return vlanList;
	}
}
