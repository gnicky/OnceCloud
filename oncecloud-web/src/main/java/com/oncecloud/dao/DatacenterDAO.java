package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Datacenter;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/06/25
 */
@Component
public class DatacenterDAO {
	private SessionHelper sessionHelper;
	private OverViewDAO overViewDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	@SuppressWarnings("unchecked")
	public Datacenter getDatacenter(String dcUuid) {
		Datacenter dc = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Datacenter where dcUuid = :dcUuid";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			List<Datacenter> dcList = query.list();
			if (dcList.size() == 1) {
				dc = dcList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return dc;
	}

	public Datacenter createDatacenter(String dcName, String dcLocation,
			String dcDesc) {
		Datacenter dc = null;
		Session session = null;
		Transaction tx = null;
		try {
			dc = new Datacenter();
			dc.setDcUuid(UUID.randomUUID().toString());
			dc.setDcName(dcName);
			dc.setDcLocation(dcLocation);
			dc.setDcDesc(dcDesc);
			dc.setDcStatus(1);
			dc.setCreateDate(new Date());
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(dc);
			this.getOverViewDAO().updateOverViewfield(session, "viewDc", true);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return dc;
	}

	@SuppressWarnings("unchecked")
	public List<Datacenter> getOnePageDCList(int page, int limit, String search) {
		List<Datacenter> dcList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Datacenter where dcName like '%"
					+ search + "%' and dcStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			dcList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return dcList;
	}

	public int countAllDatacenter(String search) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Datacenter where dcName like '%"
					+ search + "%' and dcStatus = 1";
			Query query = session.createQuery(queryString);
			total = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return total;
	}

	public boolean deleteDatacenter(String dcUuid) {
		Datacenter delDC = this.getDatacenter(dcUuid);
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			if (delDC != null) {
				delDC.setDcStatus(0);
				session = this.getSessionHelper().openMainSession();
				tx = session.beginTransaction();
				session.update(delDC);
				String queryString = "update Rack set dcUuid = null where dcUuid = :dcUuid";
				Query query = session.createQuery(queryString);
				query.setString("dcUuid", dcUuid);
				query.executeUpdate();
				queryString = "update OCPool set dcUuid = null where dcUuid=:dcUuid";
				Query query2 = session.createQuery(queryString);
				query2.setString("dcUuid", dcUuid);
				query2.executeUpdate();
				this.getOverViewDAO().updateOverViewfield(session, "viewDc",
						false);
				tx.commit();
				result = true;
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Datacenter> getAllPageDCList() {
		List<Datacenter> dcList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Datacenter where dcStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			dcList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return dcList;
	}

	public boolean updateDatacenter(String dcUuid, String dcName,
			String dcLocation, String dcDesc) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Datacenter set dcName=:name,dcLocation=:location,dcDesc=:desc  where dcUuid = :dcUuid";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			query.setString("name", dcName);
			query.setString("location", dcLocation);
			query.setString("desc", dcDesc);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			result = false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}
}
