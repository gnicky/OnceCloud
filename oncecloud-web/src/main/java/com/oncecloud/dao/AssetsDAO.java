package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Alarm;
import com.oncecloud.entity.Assets;
import com.oncecloud.entity.Firewall;
import com.oncecloud.entity.Volume;
import com.oncecloud.entity.VolumeStatus;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/09/12
 */
@Component
public class AssetsDAO {
	private SessionHelper sessionHelper;
	
	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<Assets> getAssets(int cid) {
		List<Assets> assetsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Assets  order by id desc";
			Query query = session.createQuery(queryString);
			assetsList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return assetsList;
	}
}
