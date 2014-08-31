package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Vnet;
import com.oncecloud.helper.SessionHelper;

/**
 * @author xpx hehai hty
 * @version 2014/08/23
 */
@Component
public class VnetDAO {
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

	private final static int VNETMIN = 2;
	private final static int VNETMAX = 4096;

	@SuppressWarnings("unchecked")
	public Vnet getVnet(String vnetUuid) {
		Vnet vnet = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Vnet where vnetUuid = :vnetUuid";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetUuid);
			List<Vnet> vnetList = query.list();
			if (vnetList.size() == 1) {
				vnet = vnetList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vnet;
	}

	@SuppressWarnings("unchecked")
	public List<Vnet> getOnePageVnetList(int userId, int page, int limit,
			String search) {
		List<Vnet> vnetList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Vnet where vnetUID = " + userId
					+ " and vnetName like '%" + search
					+ "%' order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vnetList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vnetList;
	}

	public int countAllVnetList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Vnet where vnetUID= "
					+ uid + " and vnetName like '%" + search + "%' ";
			Query query = session.createQuery(queryString);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return count;
	}

	public boolean createVnet(String uuid, int userId, String name, String desc) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().openMainSession();
			Vnet vnet = new Vnet(uuid, userId, name, desc, null, null, null,
					null, 1, null, new Date());
			vnet.setVnetID(getFreeVnetID());
			tx = session.beginTransaction();
			session.save(vnet);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaVlan",
					1, true);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Integer getFreeVnetID() {
		Integer freeVnet = 0;
		List<Integer> usedVnetList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select vnetID from Vnet";
			Query query = session.createQuery(queryString);
			usedVnetList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		for (Integer i = VNETMIN; i <= VNETMAX; i++) {
			if (!usedVnetList.contains(i)) {
				freeVnet = i;
				break;
			}
		}
		return freeVnet;
	}

	public boolean removeVnet(int userId, String uuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "delete from Vnet where vnetUuid=:uuid";
			Query query = session.createQuery(queryString);
			tx = session.beginTransaction();
			query.setString("uuid", uuid);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaVlan",
					1, false);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public boolean setVlan2VM(String vnetUuid, String vmUuid, String vmIp) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCVM set vmVlan=:vnetUuid,vmIp=:ip where vmUuid =:vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetUuid);
			query.setString("ip", vnetUuid);
			query.setString("vmUuid", vmUuid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public boolean setDhcpStatus(String vnetUuid, int state) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Vnet set dhcpStatus=" + state
					+ " where vnetUuid ='" + vnetUuid + "'";
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
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public boolean updateVnet(String vnetuuid, String newName,
			String description) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Vnet set vnetName=:name, vnetDesc=:desc where vnetUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", vnetuuid);
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
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public String getVnetName(String vnetuuid) {
		Session session = null;
		String result = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select vnetName from Vnet where vnetUuid='"
					+ vnetuuid + "'";
			Query query = session.createQuery(queryString);
			List<String> list = query.list();
			if (1 == list.size()) {
				result = list.get(0);
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

	public boolean linkToRouter(String vnetuuid, String routerUuid,
			Integer net, Integer gate, Integer start, Integer end,
			Integer dhcpStatus, String mac) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Vnet set vnetNet=:net, vnetGate=:gate, vnetStart=:start, vnetEnd=:end, dhcpStatus=:status, vnetRouter=:routerid, routerMac=:routermac where vnetUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setInteger("net", net);
			query.setInteger("gate", gate);
			query.setInteger("start", start);
			query.setInteger("end", end);
			query.setInteger("status", dhcpStatus);
			query.setString("uuid", vnetuuid);
			query.setString("routerid", routerUuid);
			query.setString("routermac", mac);
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
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public boolean unlink(String vnetuuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Vnet set vnetNet=null, vnetGate=null, vnetStart=null, vnetEnd=null, dhcpStatus=0, vnetRouter=null where vnetUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", vnetuuid);
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
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public int countAbleNet(int userId, String routerid, Integer net) {
		Session session = null;
		Transaction tx = null;
		Integer count = 0;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "select count(*) from Vnet where vnetUID=:userid "
					+ " and vnetNet=:net and vnetRouter=:routerid";
			Query query = session.createQuery(queryString);
			query.setString("routerid", routerid);
			query.setInteger("userid", userId);
			query.setInteger("net", net);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		}
		return count;
	}

	/**
	 * @author hty
	 * @param routerUuid
	 * @return
	 * @time 2014/08/14
	 */
	@SuppressWarnings("unchecked")
	public List<Vnet> getVxnets(String routerUuid) {
		List<Vnet> vxnetsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Vnet where vnetRouter=:routerid ";
			Query query = session.createQuery(queryString);
			query.setString("routerid", routerUuid);
			vxnetsList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vxnetsList;
	}

	@SuppressWarnings("unchecked")
	public List<Vnet> getallVnetList(int userId) {
		List<Vnet> vxnetsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Vnet where vnetUID=:userid ";
			Query query = session.createQuery(queryString);
			query.setInteger("userid", userId);
			vxnetsList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vxnetsList;
	}
}
