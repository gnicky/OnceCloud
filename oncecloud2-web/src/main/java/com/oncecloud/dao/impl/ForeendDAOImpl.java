package com.oncecloud.dao.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.ForeendDAO;
import com.oncecloud.entity.Backend;
import com.oncecloud.entity.Foreend;
import com.oncecloud.helper.SessionHelper;

@Component("ForeendDAO")
public class ForeendDAOImpl implements ForeendDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	/**
	 * @param foreUuid
	 * @param state
	 *            1可用,0 禁用
	 * @return true 成功,false 失败
	 * @author xpx 2014-7-11
	 */
	public boolean changeForeendStatus(String foreUuid, int state) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Foreend set foreStatus = :state where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setInteger("state", state);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	/**
	 * 检查端口的重复性
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @param forePort
	 *            新建端口
	 * @return true 端口可用,false 端口不可用
	 * @author xpx 2014-7-11
	 */
	public boolean checkRepeat(String lbUuid, Integer forePort) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int count = 1;
			Criteria criteria = session.createCriteria(Foreend.class)
					.add(Restrictions.eq("lbUuid", lbUuid))
					.add(Restrictions.eq("forePort", forePort))
					.setProjection(Projections.rowCount());
			count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			if (0 == count) {
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
	 * 创建前端监听
	 * 
	 * @param foreName
	 *            前端监听名称
	 * @param foreProtocol
	 *            监听协议"TCP"/"HTTP"
	 * @param forePort
	 *            监听端口
	 * @param forePolicy
	 *            均衡策略
	 * @param lbUuid
	 *            对应负载均衡对象id
	 * @return 更新后的Foreend前端对象
	 * @author xpx 2014-7-11
	 */
	public Foreend createForeend(String foreUuid, String foreName,
			String foreProtocol, Integer forePort, Integer forePolicy,
			String lbUuid) {
		Foreend foreend = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			foreend = new Foreend();
			foreend.setForeUuid(foreUuid);
			foreend.setForeName(foreName);
			foreend.setForeProtocol(foreProtocol);
			foreend.setForePort(forePort);
			foreend.setForePolicy(forePolicy);
			foreend.setLbUuid(lbUuid);
			foreend.setForeStatus(1);// 设置正常运行0/禁用,1/正常
			foreend.setCreateDate(new Date());
			session.save(foreend);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return foreend;
	}

	public boolean deleteForeend(String foreUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete from Foreend where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	private Foreend doGetForeend(Session session, String foreUuid) {
		Foreend foreend;
		Criteria criteria = session.createCriteria(Foreend.class).add(
				Restrictions.eq("foreUuid", foreUuid));
		foreend = (Foreend) criteria.uniqueResult();
		return foreend;
	}

	/**
	 * 根据负载均衡的id获取前端监听器的列表
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @return 监听器列表
	 * @author xpx 2014-7-11
	 */
	@SuppressWarnings("unchecked")
	public List<Foreend> getFEListByLB(String lbUuid) {
		JSONArray feArray = new JSONArray();
		Session session = null;
		List<Foreend> foreList = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Foreend where lbUuid ='" + lbUuid
					+ "' order by createDate desc";
			Query query = session.createQuery(queryString);
			foreList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return foreList;
	}

	/**
	 * 根据负载均衡的id 组成修改应用的json串
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @return 监听器列表
	 * @author xpx 2014-7-11
	 */
	@SuppressWarnings("unchecked")
	public List<Foreend> getSimpleFEListByLB(String lbUuid) {
		Session session = null;
		List<Foreend> foreList = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Foreend where lbUuid ='" + lbUuid
					+ "' and foreStatus >=1 order by createDate desc";
			Query query = session.createQuery(queryString);
			foreList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return foreList;
	}

	/**
	 * 更新前端监听
	 * 
	 * @param foreUuid
	 * @param foreName
	 * @param forePolicy
	 * @return
	 * @author xpx 2014-7-11
	 */
	public boolean updateForeend(String foreUuid, String foreName,
			Integer forePolicy) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Foreend foreend = this.doGetForeend(session, foreUuid);
			if (foreend != null) {
				foreend.setForeName(foreName);
				foreend.setForePolicy(forePolicy);
				session.update(foreend);
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
}
