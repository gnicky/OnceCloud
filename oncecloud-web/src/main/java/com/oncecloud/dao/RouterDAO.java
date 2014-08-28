package com.oncecloud.dao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Router;
import com.oncecloud.helper.SessionHelper;

/**
 * @author xpx hehai hty
 * @version 2014/08/23
 */
@Component
public class RouterDAO {
	private SessionHelper sessionHelper;
	private QuotaDAO quotaDAO;

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

	public Router getRouter(String routerUuid) {
		Router router = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Router where routerUuid = :routerUuid";
			Query query = session.createQuery(queryString);
			query.setString("routerUuid", routerUuid);
			router = (Router) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return router;
	}

	public Router getAliveRouter(String routerUuid) {
		Router router = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Router where routerUuid = :routerUuid and routerStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("routerUuid", routerUuid);
			router = (Router) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return router;
	}

	@SuppressWarnings("unchecked")
	public List<Router> getOnePageRouterList(int page, int limit,
			String search, int uid) {
		List<Router> routerList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Router where routerUID = " + uid
					+ " and routerName like '%" + search
					+ "%' and routerStatus > 0 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			routerList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return routerList;
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
	public List<Router> getOnePageRouterListAlarm(int page, int limit,
			String search, int routerUID) {
		List<Router> routerList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Router where routerUID =:routerUID and routerName like '%"
					+ search
					+ "%' and routerStatus > 0 and alarmUuid is null order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("routerUID", routerUID);
			routerList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return routerList;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Router> getAllListAlarm(int routerUID, String alarmUuid) {
		List<Router> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Router where routerUID =:routerUID and routerStatus > 0 and alarmUuid =:alarmUuid order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("routerUID", routerUID);
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

	public int countAllRouterList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Router where routerUID= "
					+ uid
					+ " and routerName like '%"
					+ search
					+ "%' and routerStatus > 0";
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

	/**
	 * @author hty
	 * @param search
	 * @param uid
	 * @return
	 */
	public int countAllRouterListAlarm(String search, int routerUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Router where routerUID=:routerUID and routerName like '%"
					+ search + "%' and routerStatus > 0 and alarmUuid is null";
			Query query = session.createQuery(queryString);
			query.setInteger("routerUID", routerUID);
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
			Criteria criteria = session.createCriteria(Router.class);
			criteria.add(Restrictions.ne("routerStatus", 0));
			if (!host.equals("all")) {
				criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria.add(Restrictions.eq("routerImportance", importance));
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
	public List<Router> getOnePageAdminVmList(int page, int limit, String host,
			int importance) {
		List<Router> rtList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			Criteria criteria = session.createCriteria(Router.class);
			criteria.add(Restrictions.ne("routerStatus", 0));
			criteria.setFirstResult(startPos);
			criteria.setMaxResults(limit);
			if (!host.equals("all")) {
				criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria.add(Restrictions.eq("routerImportance", importance));
			}
			rtList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return rtList;
	}

	public boolean preCreateRouter(String uuid, String pwd, int userId,
			String name, String mac, int capacity, String fwuuid, int power,
			int status, Date createDate) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().openMainSession();
			Router router = new Router(uuid, pwd, userId, name, mac, capacity,
					power, status, fwuuid, createDate);
			tx = session.beginTransaction();
			session.save(router);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaRoute",
					1, true);
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

	public void updateRouter(int userId, String uuid, String pwd, int power,
			String firewallId, String hostUuid, String ip) {
		Session session = null;
		Transaction tx = null;
		try {
			Router router = this.getRouter(uuid);
			router.setRouterPWD(pwd);
			router.setRouterPower(power);
			router.setHostUuid(hostUuid);
			router.setRouterIP(ip);
			router.setFirewallUuid(firewallId);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(router);
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

	public void removeRouter(int userId, String uuid) {
		Session session = null;
		Transaction tx = null;
		try {
			Router toDelete = this.getRouter(uuid);
			toDelete.setRouterStatus(0);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(toDelete);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaRoute",
					1, false);
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

	public boolean setRouterPowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		Router router = this.getRouter(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			router.setRouterPower(powerStatus);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(router);
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

	public boolean setRouterStatus(String routerUuid, int state) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Router set routerStatus=" + state
					+ " where routerUuid ='" + routerUuid + "'";
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

	public void updateFirewall(String routerUuid, String firewallId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Router set firewallUuid=:fid where routerUuid ='"
					+ routerUuid + "'";
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

	public boolean updateName(String routeruuid, String newName,
			String description) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Router set routerName=:name, routerDesc=:desc where routerUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", routeruuid);
			query.setString("desc", description);
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
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Router> getOnePageRoutersWithoutEip(int page, int limit,
			String search, int uid) {
		List<Router> routerList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Router where routerUID = "
					+ uid
					+ " and routerName like '%"
					+ search
					+ "%' and routerStatus <>0 and routerUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID="
					+ uid + " and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			routerList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return routerList;
	}

	public int countRoutersWithoutEIP(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Router where routerUID= "
					+ uid
					+ " and routerName like '%"
					+ search
					+ "%' and routerStatus <> 0 and routerUuid not in "
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
	public String getRouterName(String routeruuid) {
		Session session = null;
		String result = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select routerName from Router where routerUuid='"
					+ routeruuid + "'";
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
	 * @param uuid
	 * @param name
	 * @param desc
	 * @param capacity
	 * @param fwuuid
	 * @return
	 * @author xpx 2014-7-26
	 */
	public boolean updateRouter(String uuid, String name, String desc,
			int capacity, String fwuuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Router set routerName=:name, routerDesc=:desc,routerCapacity=:capacity,firewallUuid=:fwuuid where routerUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", name);
			query.setString("uuid", uuid);
			query.setString("desc", desc);
			query.setInteger("capacity", capacity);
			query.setString("fwuuid", desc);
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
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getRoutersForVnet(int uid) {
		JSONArray routerList = new JSONArray();
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select routerUuid,routerName from Router where routerUID = "
					+ uid + " and routerStatus <>0 ";
			Query query = session.createQuery(queryString);
			List<Object[]> resultList = query.list();
			for (Object[] item : resultList) {
				JSONObject itemjo = new JSONObject();
				itemjo.put("uuid", (String) item[0]);
				itemjo.put(
						"rtname",
						URLEncoder.encode((String) item[1], "utf-8").replace(
								"+", "%20"));
				routerList.put(itemjo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			routerList = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return routerList;
	}

	/**
	 * @author hty
	 * @param routerUuid
	 * @param alarmUuid
	 */
	public void updateAlarm(String routerUuid, String alarmUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Router set alarmUuid=:alarmUuid where routerUuid =:routerUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			query.setString("routerUuid", routerUuid);
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
	public boolean isNotExistAlarm(String alarmUuid) {
		boolean result = true;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Router where alarmUuid = :alarmUuid and routerStatus >0";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			List<Router> routerList = query.list();
			if (routerList.size() > 0) {
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

	public boolean setRouterHostUuid(String uuid, String hostUuid) {
		boolean result = false;
		Router rt = getRouter(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			rt.setHostUuid(hostUuid);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(rt);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
