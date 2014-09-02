package com.oncecloud.dao;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OCVDI;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/08/21
 */
@Component
public class VDIDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	/**
	 * 获取指定模板空闲VDI
	 * 
	 * @param tplUuid
	 * @return
	 */
	public OCVDI getFreeVDI(String tplUuid) {
		OCVDI vdi = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCVDI where tplUuid= :tplUuid";
			Query query = session.createQuery(queryString);
			query.setString("tplUuid", tplUuid);
			vdi = (OCVDI) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vdi;
	}

	/**
	 * 获取指定模板空闲VDI总数
	 * 
	 * @param tplUuid
	 * @return
	 */
	public int countFreeVDI(String tplUuid) {
		int count = -1;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVDI where tplUuid= :tplUuid";
			Query query = session.createQuery(queryString);
			query.setString("tplUuid", tplUuid);
			count = ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * 删除VDI
	 * 
	 * @param vdi
	 * @return
	 */
	public boolean deleteVDI(OCVDI vdi) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.delete(vdi);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	/**
	 * 添加VDI
	 * 
	 * @param tplUuid
	 * @param vmUuid
	 * @return
	 */
	public OCVDI saveVDI(String tplUuid, String vmUuid) {
		OCVDI vdi = null;
		Session session = null;
		try {
			vdi = new OCVDI();
			vdi.setTplUuid(tplUuid);
			vdi.setVdiUuid(vmUuid);
			vdi.setCreateDate(new Date());
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(vdi);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vdi;
	}
}
