package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.PoolDAO;
import com.oncecloud.entity.OCPool;
import com.oncecloud.helper.SessionHelper;

@Component("PoolDAO")
public class PoolDAOImpl implements PoolDAO{
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	/**
	 * 获取资源池
	 * 
	 * @param poolUuid
	 * @return
	 */
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

	public OCPool getPoolNoTransactional(String poolUuid) {
		OCPool pool = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String builder = "from OCPool where poolUuid = :poolUuid";
			Query query = session.createQuery(builder);
			query.setString("poolUuid", poolUuid);
			pool = (OCPool) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return pool;
	}

/*	*//**
	 * 获取随机资源池
	 * 
	 * @return
	 *//*
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
*/
	/**
	 * 获取一页资源池列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
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
/*
	/**
	 * 获取所有资源池列表
	 * 
	 * @return
	 *//*
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
*/
	/**
	 * 获取数据中心的资源池列表
	 * 
	 * @param dcUuid
	 * @return
	 */
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
/*
	public OCPool getPoolByMaster(String poolMaster) {
		OCPool pool = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCPool where poolMaster = :poolMaster and poolStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("poolMaster", poolMaster);
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
*/
	/**
	 * 获取资源池总数
	 * 
	 * @param search
	 * @return
	 */
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
			query.setString("search", "%" + search + "%");
			count = ((Number) query.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * 创建资源池
	 * 
	 * @param poolName
	 * @param poolDesc
	 * @param dcuuid
	 * @return
	 */
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

	/**
	 * 删除资源池
	 * 
	 * @param poolId
	 * @return
	 */
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
/*
	/**
	 * 添加资源池到数据中心
	 * 
	 * @param poolUuid
	 * @param dcUuid
	 * @return
	 *//*
	public boolean bindPool(String poolUuid, String dcUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCPool set dcUuid = :dcUuid where poolUuid = :poolUuid";
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

	*//**
	 * 从数据中心中删除资源池
	 * 
	 * @param poolUuid
	 * @param dcUuid
	 * @return
	 *//*
	public boolean unbindPool(String poolId) {
		boolean result = false;
		OCPool pool = this.getPool(poolId);
		if (pool != null) {
			Session session = null;
			try {
				pool.setDcUuid(null);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(pool);
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
*/
	/**
	 * 更新资源池
	 * 
	 * @param poolId
	 * @param poolName
	 * @param poolDesc
	 * @param dcuuid
	 * @return
	 */
	public boolean updatePool(String poolId, String poolName, String poolDesc,
			String dcUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCPool set poolName = :poolName, poolDesc = :poolDesc, "
					+ "dcUuid = :dcUuid where poolUuid = :poolId";
			Query query = session.createQuery(queryString);
			query.setString("poolName", poolName);
			query.setString("poolDesc", poolDesc);
			query.setString("dcUuid", dcUuid);
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
