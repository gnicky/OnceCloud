package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

	@SuppressWarnings("unchecked")
	public Backend getBackend(String backUuid) {
		Backend back = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Backend where backUuid = :backUuid";
			Query query = session.createQuery(queryString);
			query.setString("backUuid", backUuid);
			List<Backend> backList = query.list();
			if (backList.size() == 1) {
				back = backList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return back;
	}

	public Backend createBackend(String backUuid, String backName,
			String vmUuid, String vmIp, Integer vmPort, Integer backWeight,
			String foreUuid) {
		Backend back = null;
		Session session = null;
		Transaction tx = null;
		try {
			back = new Backend();
			back.setBackUuid(backUuid);
			back.setBackName(backName);
			back.setVmUuid(vmUuid);
			back.setVmIp(vmIp);
			back.setVmPort(vmPort);
			back.setBackWeight(backWeight);
			back.setForeUuid(foreUuid);
			back.setBackStatus(1);// 设置正常运行0/禁用,1/正常
			back.setCreateDate(new Date());
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(back);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			back = null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return back;
	}

	public boolean updateBackend(String backUuid, String backName,
			Integer vmPort, Integer backWeight) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Backend set backName=:name, vmPort=:port,backWeight=:weight where backUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", backName);
			query.setInteger("port", vmPort);
			query.setInteger("weight", backWeight);
			query.setString("uuid", backUuid);

			query.executeUpdate();
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
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getOnePageBEList(int page, int limit, String search) {
		List<Backend> backList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Backend where backName like '%" + search
					+ "%' order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			backList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return backList;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getBEListByFE(String foreUuid) {
		List<Backend> backList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Backend where foreUuid ='" + foreUuid
					+ "' order by createDate desc";
			Query query = session.createQuery(queryString);
			backList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return backList;
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
			String queryString = "from Backend where foreUuid ='" + foreUuid
					+ "' and backStatus >=" + forbid
					+ " order by createDate desc";
			Query query = session.createQuery(queryString);
			backList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return backList;
	}

	public int countAllBackend(String search) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Backend where backName like '%"
					+ search + "%'";
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

	public boolean deleteBackend(String backUuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete from Backend where backUuid = :backUuid";
			Query query = session.createQuery(queryString);
			query.setString("backUuid", backUuid);
			query.executeUpdate();
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
		return result;
	}

	public void deleteBackendByFE(Session session, String foreUuid) {
		try {
			String queryString = "delete from Backend where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
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
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Backend set backStatus = :state where backUuid = :backUuid";
			Query query = session.createQuery(queryString);
			query.setInteger("state", state);
			query.setString("backUuid", backUuid);
			query.executeUpdate();
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
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Backend> getAllPageBEList() {
		List<Backend> backList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Backend order by createDate desc";
			Query query = session.createQuery(queryString);
			backList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return backList;
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
		int total = 1;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Backend where vmUuid=:vmUuid and vmPort=:port";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", beuuid);
			query.setInteger("port", port);
			total = ((Number) query.iterate().next()).intValue();
			if (0 == total) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

}
