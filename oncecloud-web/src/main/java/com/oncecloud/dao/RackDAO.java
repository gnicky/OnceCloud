package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Rack;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/04/04
 */
@Component
public class RackDAO {
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
	public Rack getRack(String rackUuid) {
		Session session = this.getSessionHelper().getMainSession();
		Rack rack = null;
		Query query = session.createQuery("from Rack where rackUuid = '"
				+ rackUuid + "'");
		List<Rack> rackList = query.list();
		if (rackList.size() == 1) {
			rack = rackList.get(0);
		}
		session.close();
		return rack;
	}

	public Rack createRack(String rackName, String dcId, String rackDesc) {
		Rack rack = new Rack();
		rack.setRackUuid(UUID.randomUUID().toString());
		rack.setRackName(rackName);
		rack.setDcUuid(dcId);
		rack.setRackDesc(rackDesc);
		rack.setRackStatus(1);
		rack.setCreateDate(new Date());
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.save(rack);
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewRack",
					true);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return rack;
	}

	@SuppressWarnings("unchecked")
	public List<Rack> getOnePageRackList(int page, int limit, String search) {
		Session session = this.getSessionHelper().getMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "from Rack where rackName like '%" + search
				+ "%' and rackStatus = 1 order by createDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<Rack> rackList = query.list();
		session.close();
		return rackList;
	}

	public int countAllRackList(String search) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "select count(*) from Rack where rackName like '%"
				+ search + "%' and rackStatus = 1";
		Query query = session.createQuery(queryString);
		return ((Number) query.iterate().next()).intValue();
	}

	public boolean deleteRack(String rackId) {
		Rack delRack = this.getRack(rackId);
		boolean result = false;
		if (delRack != null) {
			delRack.setRackStatus(0);
			Session session = null;
			Transaction tx = null;
			try {
				session = this.getSessionHelper().getMainSession();
				tx = session.beginTransaction();
				session.update(delRack);
				String queryString = "update OCHost set rackUuid= null where rackUuid = :rackId";
				Query query = session.createQuery(queryString);
				query.setString("rackId", rackId);
				query.executeUpdate();
				queryString = "update Storage set rackuuid= null where rackuuid = :rackId";
				Query query2 = session.createQuery(queryString);
				query2.setString("rackId", rackId);
				query2.executeUpdate();
				this.getOverViewDAO().updateOverViewfieldNoTransaction(
						"viewRack", false);
				tx.commit();
				result = true;
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
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Rack> getAllPageRackList() {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "from Rack where rackStatus = 1 order by createDate desc";
		Query query = session.createQuery(queryString);
		List<Rack> rackList = query.list();
		session.close();
		return rackList;
	}

	public boolean bindDatacenter(String rackId, String dcId) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update Rack set dcUuid =:dcId where rackUuid=:rackId";
			Query query = session.createQuery(queryString);
			query.setString("dcId", dcId);
			query.setString("rackId", rackId);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public boolean unbindDatacenter(String rackId) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			Rack rack = this.getRack(rackId);
			if (rack != null) {
				rack.setDcUuid(null);
				session = this.getSessionHelper().getMainSession();
				tx = session.beginTransaction();
				session.update(rack);
				tx.commit();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Rack> getRackListOfDC(String dcUuid) {
		List<Rack> rackList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from Rack where dcUuid = :dcUuid and rackStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			rackList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return rackList;
	}

	public boolean updateRack(String rackId, String rackName, String rackDesc,
			String dcid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update Rack set rackName=:name,rackDesc=:desc,dcUuid =:dcId where rackUuid=:rackId";
			Query query = session.createQuery(queryString);
			query.setString("name", rackName);
			query.setString("desc", rackDesc);
			query.setString("dcId", dcid);
			query.setString("rackId", rackId);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			result = false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}
}
