package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Backend;
import com.oncecloud.helper.SessionHelper;

@Component
public class BackendDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public Backend getBackend(String backUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.getTransaction().commit();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			session.getTransaction().commit();
			return backend;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public Backend createBackend(String backUuid, String backName,
			String vmUuid, String vmIp, Integer vmPort, Integer backWeight,
			String foreUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Backend backend = new Backend();
			backend.setBackUuid(backUuid);
			backend.setBackName(backName);
			backend.setVmUuid(vmUuid);
			backend.setVmIp(vmIp);
			backend.setVmPort(vmPort);
			backend.setBackWeight(backWeight);
			backend.setForeUuid(foreUuid);
			backend.setBackStatus(1);// 设置正常运行0/禁用,1/正常
			backend.setCreateDate(new Date());
			session.save(backend);
			session.getTransaction().commit();
			return backend;

		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public boolean updateBackend(String backUuid, String backName,
			Integer vmPort, Integer backWeight) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Backend set backName=:name, vmPort=:port,backWeight=:weight where backUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", backName);
			query.setInteger("port", vmPort);
			query.setInteger("weight", backWeight);
			query.setString("uuid", backUuid);
			query.executeUpdate();
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getOnePageBEList(int page, int limit, String search) {
		List<Backend> backList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(Backend.class)
					.add(Restrictions.like("backName", search,
							MatchMode.ANYWHERE))
					.addOrder(Order.desc("createDate"))
					.setFirstResult(startPos).setMaxResults(limit);
			backList = criteria.list();
			session.getTransaction().commit();
			return backList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getBEListByFE(String foreUuid) {
		List<Backend> backList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("foreUuid", foreUuid))
					.addOrder(Order.desc("createDate"));
			backList = criteria.list();
			session.getTransaction().commit();
			return backList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * @param session
	 * @param foreUuid
	 * @param forbid
	 *            获取类型,0表示所有监听器,1表示只获取未禁用的
	 * @return
	 * @author xpx 2014-7-18
	 */
	@SuppressWarnings("unchecked")
	public List<Backend> getBEListByFE(Session session, String foreUuid,
			int forbid) {
		List<Backend> backList = null;
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("foreUuid", foreUuid))
					.add(Restrictions.ge("backStatus", forbid))
					.addOrder(Order.desc("createDate"));
			backList = criteria.list();
			session.getTransaction().commit();
			return backList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllBackend(String search) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Backend.class)
					.add(Restrictions.like("backName", search,
							MatchMode.ANYWHERE))
					.setProjection(Projections.rowCount());
			int count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public boolean deleteBackend(String backUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete from Backend where backUuid = :backUuid";
			Query query = session.createQuery(queryString);
			query.setString("backUuid", backUuid);
			query.executeUpdate();
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public void deleteBackendByFE(Session session, String foreUuid) {
		try {
			session.beginTransaction();
			String queryString = "delete from Backend where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * @param backUuid
	 * @param state
	 *            1可用,0 禁用
	 * @return true 成功,false 失败
	 * @author xpx 2014-7-11
	 */
	public boolean changeBackendStatus(String backUuid, int state) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Backend set backStatus = :state where backUuid = :backUuid";
			Query query = session.createQuery(queryString);
			query.setInteger("state", state);
			query.setString("backUuid", backUuid);
			query.executeUpdate();
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getAllPageBEList() {
		List<Backend> backList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).addOrder(
					Order.desc("createDate"));
			backList = criteria.list();
			session.getTransaction().commit();
			return backList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	/**
	 * 检测创建的后端端口是否重复
	 * 
	 * @param foreuuid
	 * @param port
	 * @return
	 * @author xpx 2014-7-16
	 */
	public boolean checkRepeat(String beuuid, int port) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Backend where vmUuid=:vmUuid and vmPort=:port";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", beuuid);
			query.setInteger("port", port);
			int total = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			if (0 == total) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

}
