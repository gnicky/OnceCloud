package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
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
		Backend backend = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.getTransaction().commit();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			backend = (Backend) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backend;
	}

	public Backend createBackend(String backUuid, String backName,
			String vmUuid, String vmIp, Integer vmPort, Integer backWeight,
			String foreUuid) {
		Backend backend = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			backend = new Backend();
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
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backend;
	}

	public boolean updateBackend(String backUuid, String backName,
			Integer vmPort, Integer backWeight) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			if (backend != null) {
				backend.setBackName(backName);
				backend.setVmPort(vmPort);
				backend.setBackWeight(backWeight);
				session.update(backend);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getOnePageBEList(int page, int limit, String search) {
		List<Backend> backendList = null;
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
			backendList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backendList;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getBEListByFE(String foreUuid) {
		List<Backend> backendList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("foreUuid", foreUuid))
					.addOrder(Order.desc("createDate"));
			backendList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backendList;
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
	public List<Backend> doGetBackendListByFrontend(Session session,
			String foreUuid, int forbid) {
		List<Backend> backendList = null;
		try {
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("foreUuid", foreUuid))
					.add(Restrictions.ge("backStatus", forbid))
					.addOrder(Order.desc("createDate"));
			backendList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backendList;
	}

	public int countAllBackends(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Backend.class)
					.add(Restrictions.like("backName", search,
							MatchMode.ANYWHERE))
					.setProjection(Projections.rowCount());
			count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	public boolean deleteBackend(String backUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			if (backend != null) {
				session.delete(backend);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean deleteBackendByFrontend(Session session, String foreUuid) {
		boolean result = false;
		try {
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("foreUuid", foreUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			if (backend != null) {
				session.delete(backend);
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

	/**
	 * @param backUuid
	 * @param state
	 *            1可用,0 禁用
	 * @return true 成功,false 失败
	 * @author xpx 2014-7-11
	 */
	public boolean changeBackendStatus(String backUuid, int state) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).add(
					Restrictions.eq("backUuid", backUuid));
			Backend backend = (Backend) criteria.uniqueResult();
			if (backend != null) {
				backend.setBackStatus(state);
				session.update(backend);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getAllPageBEList() {
		List<Backend> backendList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class).addOrder(
					Order.desc("createDate"));
			backendList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return backendList;
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
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Backend.class)
					.add(Restrictions.eq("vmUuid", beuuid))
					.add(Restrictions.eq("vmPort", port))
					.setProjection(Projections.rowCount());
			int total = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			if (0 == total) {
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

}
