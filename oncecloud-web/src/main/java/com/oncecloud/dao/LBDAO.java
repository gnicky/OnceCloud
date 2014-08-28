package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.LB;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai hty
 * @version 2014/08/23
 */
@Component
public class LBDAO {
	private SessionHelper sessionHelper;
	private QuotaDAO quotaDAO;
	private FirewallDAO firewallDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	public LB getLB(String lbUuid) {
		LB lb = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from LB where lbUuid = :lbUuid";
			Query query = session.createQuery(queryString);
			query.setString("lbUuid", lbUuid);
			lb = (LB) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return lb;
	}

	public LB getAliveLB(String lbUuid) {
		LB lb = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from LB where lbUuid = :lbUuid and lbStatus = 0";
			Query query = session.createQuery(queryString);
			query.setString("lbUuid", lbUuid);
			lb = (LB) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return lb;
	}

	@SuppressWarnings("unchecked")
	public List<LB> getOnePageLBList(int page, int limit, String search, int uid) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from LB where lbUID = " + uid
					+ " and lbName like '%" + search
					+ "%' and lbStatus > 0 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			lbList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
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
	 */
	@SuppressWarnings("unchecked")
	public List<LB> getOnePageLBListAlarm(int page, int limit, String search,
			int lbUID) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from LB where lbUID =:lbUID and lbName like '%"
					+ search
					+ "%' and lbStatus > 0 and alarmUuid is null order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("lbUID", lbUID);
			lbList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return lbList;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LB> getAllListAlarm(int lbUID, String alarmUuid) {
		List<LB> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from LB where lbUID =:lbUID and lbStatus > 0 and alarmUuid =:alarmUuid order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("lbUID", lbUID);
			query.setString("alarmUuid", alarmUuid);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public int countAllLBList(String search, int lbUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from LB where lbUID=:lbUID  and lbName like '%"
					+ search + "%' and lbStatus > 0";
			Query query = session.createQuery(queryString);
			query.setInteger("lbUID", lbUID);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	/**
	 * @author hty
	 * @param search
	 * @param uid
	 * @return
	 */
	public int countAllLBListAlarm(String search, int lbUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from LB where lbUID=:lbUID and lbName like '%"
					+ search + "%' and lbStatus > 0 and alarmUuid is null";
			Query query = session.createQuery(queryString);
			query.setInteger("lbUID", lbUID);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	public int countAllAdminVMList(String host, int importance) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Criteria criteria = session.createCriteria(LB.class);
			criteria.add(Restrictions.ne("lbStatus", 0));
			if (!host.equals("all")) {
				criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria.add(Restrictions.eq("lbImportance", importance));
			}
			criteria.setProjection(Projections.rowCount());
			count = Integer.parseInt(criteria.uniqueResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<LB> getOnePageAdminVmList(int page, int limit, String host,
			int importance) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return lbList;
	}

	public boolean preCreateLB(String uuid, String pwd, int userId,
			String name, String mac, int capacity, int power, int status,
			Date createDate) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().openMainSession();
			LB lb = new LB(uuid, pwd, userId, name, mac, capacity, power,
					status, createDate);
			tx = session.beginTransaction();
			session.save(lb);
			this.getQuotaDAO().updateQuotaField(session, userId,
					"quotaLoadBalance", 1, true);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	public void updateLB(int userId, String uuid, String pwd, int power,
			String hostUuid, String ip) {
		Session session = null;
		Transaction tx = null;
		try {
			LB lb = this.getLB(uuid);
			String firewallId = this.getFirewallDAO()
					.getDefaultFirewall(userId).getFirewallId();
			lb.setLbPWD(pwd);
			lb.setLbPower(power);
			lb.setHostUuid(hostUuid);
			lb.setFirewallUuid(firewallId);
			lb.setLbIP(ip);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void removeLB(int userId, String uuid) {
		Session session = null;
		Transaction tx = null;
		try {
			LB toDelete = this.getLB(uuid);
			toDelete.setLbStatus(0);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(toDelete);
			this.getQuotaDAO().updateQuotaField(session, userId,
					"quotaLoadBalance", 1, false);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean setLBPowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		LB lb = this.getLB(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			lb.setLbPower(powerStatus);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return result;
	}

	public boolean setLBStatus(String lbUuid, int state) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set lbStatus=" + state
					+ " where lbUuid ='" + lbUuid + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	public void updateFirewall(String lbUuid, String firewallId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set firewallUuid=:fid where lbUuid ='"
					+ lbUuid + "'";
			Query query = session.createQuery(queryString);
			query.setString("fid", firewallId);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

	public void updateName(String lbuuid, String newName, String description) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
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
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<LB> getOnePageLBsWithoutEip(int page, int limit, String search,
			int uid) {
		List<LB> lbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from LB where lbUID = "
					+ uid
					+ " and lbName like '%"
					+ search
					+ "%' and lbStatus <>0 and lbUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID="
					+ uid + " and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			lbList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return lbList;
	}

	public int countLBsWithoutEIP(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from LB where lbUID= "
					+ uid
					+ " and lbName like '%"
					+ search
					+ "%' and lbStatus <> 0 and lbUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID="
					+ uid + " and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public String getLBName(String lbuuid) {
		Session session = null;
		String result = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select lbName from LB where lbUuid='"
					+ lbuuid + "'";
			Query query = session.createQuery(queryString);
			List<String> list = query.list();
			result = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * @author hty
	 * @param lbUuid
	 * @param alarmUuid
	 */
	public void updateAlarm(String lbUuid, String alarmUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update LB set alarmUuid=:alarmUuid where lbUuid =:lbUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			query.setString("lbUuid", lbUuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isLNotExistAlarm(String alarmUuid) {
		boolean result = true;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from LB where alarmUuid = :alarmUuid and lbStatus >0";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			List<LB> lbList = query.list();
			if (lbList.size() > 0) {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	public boolean setLBHostUuid(String uuid, String hostUuid) {
		boolean result = false;
		LB lb = this.getLB(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			lb.setHostUuid(hostUuid);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(lb);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
