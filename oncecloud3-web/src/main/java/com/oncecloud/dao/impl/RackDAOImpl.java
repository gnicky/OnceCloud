package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.RackDAO;
import com.oncecloud.entity.Rack;
import com.oncecloud.helper.SessionHelper;

@Component("RackDAO")
public class RackDAOImpl implements RackDAO {
	
	private SessionHelper sessionHelper;
	
	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}
	
	/**
	 * 获取机架
	 * 
	 * @param rackUuid
	 * @return
	 */
	public Rack getRack(String rackUuid) {
		Rack rack = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Rack where rackUuid = :rackUuid");
			query.setString("rackUuid", rackUuid);
			rack = (Rack) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rack;
	}

	/**
	 * 获取一页机架列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Rack> getOnePageRackList(int page, int limit, String search) {
		List<Rack> rackList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from Rack where rackName like :search and rackStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			rackList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rackList;
	}

	/**
	 * 获取全部机架列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Rack> getRackList() {
		List<Rack> rackList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Rack where rackStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			rackList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rackList;
	}

	/**
	 * 获取数据中心的机架列表
	 * 
	 * @param dcUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Rack> getRackListOfDC(String dcUuid) {
		List<Rack> rackList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Rack where dcUuid = :dcUuid and rackStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("dcUuid", dcUuid);
			rackList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rackList;
	}

	/**
	 * 获取机架总数
	 * 
	 * @param search
	 * @return
	 */
	public int countAllRackList(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Rack where rackName like :search and rackStatus = 1";
			Query query = session.createQuery(queryString);
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
	 * 创建机架
	 * 
	 * @param rackName
	 * @param dcId
	 * @param rackDesc
	 * @return
	 */
	public Rack createRack(String rackName, String dcId, String rackDesc) {
		Rack rack = new Rack();
		rack.setRackUuid(UUID.randomUUID().toString());
		rack.setRackName(rackName);
		rack.setDcUuid(dcId);
		rack.setRackDesc(rackDesc);
		rack.setRackStatus(1);
		rack.setCreateDate(new Date());
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(rack);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rack;
	}

	/**
	 * 删除机架
	 * 
	 * @param rackId
	 * @return
	 */
	public boolean deleteRack(String rackId) {
		boolean result = false;
		Rack delRack = this.getRack(rackId);
		if (delRack != null) {
			delRack.setRackStatus(0);
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(delRack);
				String queryString = "update OCHost set rackUuid = null where rackUuid = :rackId";
				Query query = session.createQuery(queryString);
				query.setString("rackId", rackId);
				query.executeUpdate();
				queryString = "update Storage set rackuuid = null where rackuuid = :rackId";
				Query query2 = session.createQuery(queryString);
				query2.setString("rackId", rackId);
				query2.executeUpdate();
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
	*//**
	 * 添加机架到数据中心
	 * 
	 * @param rackId
	 * @param dcId
	 * @return
	 *//*
	public boolean bindDatacenter(String rackId, String dcId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Rack set dcUuid = :dcId where rackUuid = :rackId";
			Query query = session.createQuery(queryString);
			query.setString("dcId", dcId);
			query.setString("rackId", rackId);
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
	 * 从数据中心中删除机架
	 * 
	 * @param rackId
	 * @return
	 *//*
	public boolean unbindDatacenter(String rackId) {
		boolean result = false;
		Rack rack = this.getRack(rackId);
		if (rack != null) {
			Session session = null;
			try {
				rack.setDcUuid(null);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(rack);
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
	 * 更新机架
	 * 
	 * @param rackId
	 * @param rackName
	 * @param rackDesc
	 * @param dcid
	 * @return
	 */
	public boolean updateRack(String rackUuid, String rackName,
			String rackDesc, String dcUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Rack set rackName= :rackName, "
					+ "rackDesc = :rackDesc, dcUuid = :dcUuid where rackUuid = :rackUuid";
			Query query = session.createQuery(queryString);
			query.setString("rackName", rackName);
			query.setString("rackDesc", rackDesc);
			query.setString("dcUuid", dcUuid);
			query.setString("rackUuid", rackUuid);
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
