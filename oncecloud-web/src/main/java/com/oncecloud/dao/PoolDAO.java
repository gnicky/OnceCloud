package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OCPool;
import com.oncecloud.helper.SessionHelper;

@Component
public class PoolDAO {
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
	public OCPool getPool(String poolUuid) {
		Session session = this.getSessionHelper().getMainSession();
		OCPool pool = null;
		Query query = session.createQuery("from OCPool where poolUuid = '"
				+ poolUuid + "'");
		List<OCPool> poolList = query.list();
		if (poolList.size() == 1) {
			pool = poolList.get(0);
		}
		session.close();
		return pool;
	}

	public OCPool createPool(String poolName, String poolDesc, String dcuuid) {
		OCPool pool = new OCPool();
		pool.setPoolUuid(UUID.randomUUID().toString());
		pool.setPoolName(poolName);
		pool.setPoolDesc(poolDesc);
		pool.setDcUuid(dcuuid);
		pool.setPoolStatus(1);
		pool.setCreateDate(new Date());
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.save(pool);
			this.getOverViewDAO()
					.updateOverViewfield(session, "viewPool", true);
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
		return pool;
	}

	@SuppressWarnings("unchecked")
	public List<OCPool> getOnePagePoolList(int page, int limit, String search) {
		Session session = this.getSessionHelper().getMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "from OCPool where poolName like '%" + search
				+ "%' and poolStatus = 1 order by createDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<OCPool> poolList = query.list();
		session.close();
		return poolList;
	}

	@SuppressWarnings("unchecked")
	public List<OCPool> getPoolList() {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "from OCPool where poolStatus = 1 order by createDate desc";
		Query query = session.createQuery(queryString);
		List<OCPool> poolList = query.list();
		session.close();
		return poolList;
	}

	public int countAllPoolList(String search) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "select count(*) from OCPool where poolName like '%"
				+ search + "%' and poolStatus = 1";
		Query query = session.createQuery(queryString);
		return ((Number) query.iterate().next()).intValue();
	}

	public boolean deletePool(String poolId) {
		OCPool delPool = getPool(poolId);
		boolean result = false;
		if (delPool != null) {
			delPool.setPoolStatus(0);
			Session session = null;
			Transaction tx = null;
			try {
				session = this.getSessionHelper().getMainSession();
				tx = session.beginTransaction();
				session.update(delPool);
				this.getOverViewDAO().updateOverViewfield(session, "viewPool",
						false);
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

	// /绑定
	public boolean bindPool(String poolId, String dcUuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCPool set dcUuid =:dcUuid where poolUuid=:poolid";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			query.setString("poolid", poolId);
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

	// /卸载
	public boolean unbindPool(String poolId) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			OCPool pool = this.getPool(poolId);
			if (pool != null) {
				pool.setDcUuid(null);
				session = this.getSessionHelper().getMainSession();
				tx = session.beginTransaction();
				session.update(pool);
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
	public List<OCPool> getPoolListOfRack(String rackUuid) {
		List<OCPool> pooList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from OCPool where podUuid = :podUuid and poolStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("podUuid", rackUuid);
			pooList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pooList;
	}

	@SuppressWarnings("unchecked")
	public String getRandomPool() {
		String pool = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from OCPool where poolStatus = 1 order by rand()";
			Query query = session.createQuery(queryString);
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<OCPool> iplist = query.list();
			if (iplist.size() == 1) {
				pool = iplist.get(0).getPoolUuid();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pool;
	}

	@SuppressWarnings("unchecked")
	public List<OCPool> getPoolListOfDC(String dcid) {
		List<OCPool> pooList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from OCPool where dcUuid = :dcUuid and poolStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcid);
			pooList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pooList;
	}

	public boolean updatePool(String poolId, String poolName, String poolDesc,
			String dcuuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCPool set poolName =:poolName, poolDesc=:poolDesc,dcUuid=:dcuuid where poolUuid=:poolId";
			Query query = session.createQuery(queryString);
			query.setString("poolName", poolName);
			query.setString("poolDesc", poolDesc);
			query.setString("dcuuid", dcuuid);
			query.setString("poolId", poolId);
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
