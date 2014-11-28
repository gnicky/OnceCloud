package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.LBDAO;
import com.oncecloud.entity.LB;
import com.oncecloud.helper.SessionHelper;

@Component("LBDAO")
public class LBDAOImpl implements LBDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	/*public int countAllAdminVMList(String host, int importance) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(LB.class);
			criteria = criteria.add(Restrictions.ne("lbStatus", 0));
			if (!host.equals("all")) {
				criteria = criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria = criteria.add(Restrictions.eq("lbImportance",
						importance));
			}
			criteria = criteria.setProjection(Projections.rowCount());
			count = Integer.parseInt(criteria.uniqueResult().toString());
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}
*/
	public int countAllLBList(String search, int lbUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(LB.class)
					.add(Restrictions.eq("lbUID", lbUID))
					.add(Restrictions
							.like("lbName", search, MatchMode.ANYWHERE))
					.add(Restrictions.gt("lbStatus", 0))
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
/*
	public int countAllLBListAlarm(String search, int lbUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from LB where lbUID=:lbUID and lbName like '%"
					+ search + "%' and lbStatus > 0 and alarmUuid is null";
			Query query = session.createQuery(queryString);
			query.setInteger("lbUID", lbUID);
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
*/
	public int countLBsWithoutEIP(String search, int userId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from LB where lbUID = :userId "
					+ "and lbName like :search and lbStatus <> 0 and lbUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID = :uid "
					+ "and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setInteger("uid", userId);
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

	public LB getAliveLB(String lbUuid) {
		LB lb = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from LB where lbUuid = :lbUuid and lbStatus != 0";
			Query query = session.createQuery(queryString);
			query.setString("lbUuid", lbUuid);
			lb = (LB) query.list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return lb;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @param uid
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	public List<LB> getAllListAlarm(int lbUID, String alarmUuid) {
		List<LB> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from LB where lbUID =:lbUID and lbStatus > 0 and alarmUuid =:alarmUuid order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("lbUID", lbUID);
			query.setString("alarmUuid", alarmUuid);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return list;
	}
*/
	public LB getLB(String lbUuid) {
		LB lb = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from LB where lbUuid = :lbUuid";
			Query query = session.createQuery(queryString);
			query.setString("lbUuid", lbUuid);
			lb = (LB) query.list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return lb;
	}

	@SuppressWarnings("unchecked")
	public String getLBName(String lbuuid) {
		Session session = null;
		String result = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select lbName from LB where lbUuid='"
					+ lbuuid + "'";
			Query query = session.createQuery(queryString);
			List<String> list = query.list();
			result = list.get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
/*
	@SuppressWarnings("unchecked")
	public List<LB> getOnePageAdminVmList(int page, int limit, String host,
			int importance) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session.createCriteria(LB.class);
			criteria.add(Restrictions.ne("lbStatus", 0));
			criteria.setFirstResult(startPos);
			criteria.setMaxResults(limit);
			if (!host.equals("all")) {
				criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria.add(Restrictions.eq("lbImportance", importance));
			}
			lbList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return lbList;
	}
*/
	@SuppressWarnings("unchecked")
	public List<LB> getOnePageLBList(int userId, int page, int limit,
			String search) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from LB where lbUID = :userId and lbName like :search and lbStatus > 0 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			lbList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return lbList;
	}

	/**
	 * @author hty
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	public List<LB> getOnePageLBListAlarm(int page, int limit, String search,
			int lbUID) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from LB where lbUID =:lbUID and lbName like '%"
					+ search
					+ "%' and lbStatus > 0 and alarmUuid is null order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("lbUID", lbUID);
			lbList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return lbList;
	}
*/
	@SuppressWarnings("unchecked")
	public List<LB> getOnePageLBsWithoutEip(int page, int limit, String search,
			int userId) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from LB where lbUID = :userId "
					+ "and lbName like :search and lbStatus <> 0 and lbUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID = :uid "
					+ "and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setInteger("uid", userId);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			lbList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return lbList;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	public boolean isLNotExistAlarm(String alarmUuid) {
		boolean result = true;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from LB where alarmUuid = :alarmUuid and lbStatus >0";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			List<LB> lbList = query.list();
			if (lbList.size() > 0) {
				result = false;
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
*/
	public boolean preCreateLB(String uuid, String pwd, int userId,
			String name, String mac, int capacity, int power, int status,
			Date createDate) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().getMainSession();
			LB lb = new LB(uuid, pwd, userId, name, mac, capacity, power,
					status, createDate);
			tx = session.beginTransaction();
			session.save(lb);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public void removeLB(int userId, String uuid) {
		Session session = null;
		Transaction tx = null;
		try {
			LB toDelete = this.getLB(uuid);
			toDelete.setLbStatus(0);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(toDelete);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public boolean setLBHostUuid(String uuid, String hostUuid) {
		boolean result = false;
		LB lb = this.getLB(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			lb.setHostUuid(hostUuid);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean setLBPowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		LB lb = this.getLB(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			lb.setLbPower(powerStatus);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean updateLBImportance(String uuid, int lbImportance) {
		boolean result = false;
		LB lb = this.getLB(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			lb.setLbImportance(lbImportance);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean setLBStatus(String lbUuid, int state) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set lbStatus=" + state
					+ " where lbUuid ='" + lbUuid + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}


	/**
	 * @author hty
	 * @param lbUuid
	 * @param alarmUuid
	 *//*
	public void updateAlarm(String lbUuid, String alarmUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set alarmUuid=:alarmUuid where lbUuid =:lbUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			query.setString("lbUuid", lbUuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}
*/
	public void updateFirewall(String lbUuid, String firewallId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set firewallUuid=:fid where lbUuid ='"
					+ lbUuid + "'";
			Query query = session.createQuery(queryString);
			query.setString("fid", firewallId);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void updateLB(int userId, String uuid, String pwd, int power,
			String hostUuid, String ip, String firewallId) {
		Session session = null;
		Transaction tx = null;
		try {
			LB lb = this.getLB(uuid);
			lb.setLbPWD(pwd);
			lb.setLbPower(power);
			lb.setHostUuid(hostUuid);
			lb.setFirewallUuid(firewallId);
			lb.setLbIP(ip);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}
/*
	public void updateName(String lbuuid, String newName, String description) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set lbName=:name, lbDesc=:desc where lbUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", lbuuid);
			query.setString("desc", description);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	*//**
	 * 更新负载均衡电源状态和所在服务器
	 * 
	 * @param session
	 * @param uuid
	 * @param power
	 * @param hostUuid
	 *//*
	public void updatePowerAndHostNoTransaction(String uuid, int power,
			String hostUuid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "update LB set lbPower = :power, hostUuid = :hostUuid where lbUuid = :uuid";
		Query query = session.createQuery(queryString);
		query.setInteger("power", power);
		query.setString("hostUuid", hostUuid);
		query.setString("uuid", uuid);
		query.executeUpdate();
	}*/

}
