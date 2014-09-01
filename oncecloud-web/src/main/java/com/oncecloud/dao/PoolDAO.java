package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
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

	public OCPool getPool(String poolUuid) {
		OCPool pool = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String builder = "from OCPool where poolUuid = :poolUuid";
			Query query = session.createQuery(builder);
			query.setString("poolUuid", poolUuid);
			pool = (OCPool) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(pool);
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewPool",
					true);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			pool = null;
		}
		return pool;
	}

	@SuppressWarnings("unchecked")
	public List<OCPool> getOnePagePoolList(int page, int limit, String search) {
		List<OCPool> poolList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from OCPool where poolName like :search and poolStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			poolList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return poolList;
	}

	public int countAllPoolList(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			StringBuilder builder = new StringBuilder(
					"select count(*) from OCPool where ");
			builder.append("poolName like :search and poolStatus = 1");
			Query query = session.createQuery(builder.toString());
			count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<OCPool> getPoolList() {
		List<OCPool> poolList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCPool where poolStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			poolList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return poolList;
	}

	public boolean deletePool(String poolId) {
		OCPool delPool = getPool(poolId);
		boolean result = false;
		if (delPool != null) {
			delPool.setPoolStatus(0);
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(delPool);
				this.getOverViewDAO().updateOverViewfieldNoTransaction(
						"viewPool", false);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	public boolean bindPool(String poolUuid, String dcUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCPool set dcUuid = :dcUuid where poolUuid= :poolUuid";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			query.setString("poolUuid", poolUuid);
			query.executeUpdate();
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

	public boolean unbindPool(String poolId) {
		OCPool pool = this.getPool(poolId);
		boolean result = false;
		Session session = null;
		try {
			if (pool != null) {
				pool.setDcUuid(null);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(pool);
				session.getTransaction().commit();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
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
			session.beginTransaction();
			String queryString = "from OCPool where podUuid = :podUuid and poolStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("podUuid", rackUuid);
			pooList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return pooList;
	}

	public String getRandomPool() {
		String pool = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCPool where poolStatus = 1 order by rand()";
			Query query = session.createQuery(queryString);
			query.setFirstResult(0);
			query.setMaxResults(1);
			pool = ((OCPool) query.list().get(0)).getPoolUuid();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return pool;
	}

	@SuppressWarnings("unchecked")
	public List<OCPool> getPoolListOfDC(String dcUuid) {
		List<OCPool> pooList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCPool where dcUuid = :dcUuid and poolStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			pooList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return pooList;
	}

	public boolean updatePool(String poolId, String poolName, String poolDesc,
			String dcuuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCPool set poolName =:poolName, poolDesc=:poolDesc,dcUuid=:dcuuid where poolUuid=:poolId";
			Query query = session.createQuery(queryString);
			query.setString("poolName", poolName);
			query.setString("poolDesc", poolDesc);
			query.setString("dcuuid", dcuuid);
			query.setString("poolId", poolId);
			query.executeUpdate();
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
}
