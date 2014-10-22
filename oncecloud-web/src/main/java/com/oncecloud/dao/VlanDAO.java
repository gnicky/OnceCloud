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

	/**
	 * 获取交换机的VLAN列表
	 * 
	 * @param switchId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vlan> getVlanOfSwitch(String switchId) {
		List<Vlan> vlanList  = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Vlan where swUuid = :switchId";
			Query query = session.createQuery(queryString);
			query.setString("switchId", switchId);
			vlanList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vlanList;
	}
}
