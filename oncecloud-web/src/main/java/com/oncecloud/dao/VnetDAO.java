package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
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

	public Vnet getVnet(String vnetUuid) {
		Vnet vnet = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Vnet where vnetUuid = :vnetUuid";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetUuid);
			vnet = (Vnet) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vnet;
	}

	/**
	 * 获取用户私有网络列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vnet> getOnePageVnetList(int userId, int page, int limit,
			String search) {
		List<Vnet> vnetList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from Vnet where vnetUID = :userId and vnetName like :search order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vnetList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vnetList;
	}

	/**
	 * 获取用户私有网络总数
	 * 
	 * @param userId
	 * @param search
	 * @return
	 */
	public int countAllVnetList(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Vnet where vnetUID = :usrId and vnetName like :search";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
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

	public boolean createVnet(String uuid, int userId, String name, String desc) {
		Session session = null;
		boolean result = false;
		try {
			Vnet vnet = new Vnet(uuid, userId, name, desc, null, null, null,
					null, 1, null, new Date());
			vnet.setVnetID(getFreeVnetID());
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.beginTransaction();
			session.save(vnet);
			this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
					"quotaVlan", 1, true);
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

	@SuppressWarnings("unchecked")
	public int getFreeVnetID() {
		int freeVnet = 0;
		List<Integer> usedVnetList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select vnetID from Vnet";
			Query query = session.createQuery(queryString);
			usedVnetList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete from Vnet where vnetUuid = :uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
					"quotaVlan", 1, false);
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

	public boolean setVlan2VM(String vnetUuid, String vmUuid, String vmIp) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set vmVlan=:vnetUuid,vmIp=:ip where vmUuid =:vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetUuid);
			query.setString("ip", vnetUuid);
			query.setString("vmUuid", vmUuid);
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

	public boolean setDhcpStatus(String vnetUuid, int state) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Vnet set dhcpStatus = :state "
					+ "where vnetUuid = :vnetUuid";
			Query query = session.createQuery(queryString);
			query.setInteger("state", state);
			query.setString("vnetUuid", vnetUuid);
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

	public boolean updateVnet(String vnetuuid, String newName,
			String description) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Vnet set vnetName=:name, vnetDesc=:desc where vnetUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", vnetuuid);
			query.setString("desc", description);
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

	public String getVnetName(String vnetuuid) {
		String result = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select vnetName from Vnet where vnetUuid = :vnetUuid";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetuuid);
			result = (String) query.list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean linkToRouter(String vnetuuid, String routerUuid,
			Integer net, Integer gate, Integer start, Integer end,
			Integer dhcpStatus, String mac) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Vnet set vnetNet = :net, vnetGate = :gate, "
					+ "vnetStart = :start, vnetEnd = :end, dhcpStatus = :status, "
					+ "vnetRouter = :routerid, routerMac = :routermac where vnetUuid = :uuid";
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

	public boolean unlink(String vnetuuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Vnet set vnetNet = null, vnetGate = null, "
					+ "vnetStart = null, vnetEnd = null, dhcpStatus = 0, vnetRouter = null "
					+ "where vnetUuid = :uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", vnetuuid);
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

	public int countAbleNet(int userId, String routerid, Integer net) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Vnet where vnetUID = :userid "
					+ " and vnetNet = :net and vnetRouter = :routerid";
			Query query = session.createQuery(queryString);
			query.setString("routerid", routerid);
			query.setInteger("userid", userId);
			query.setInteger("net", net);
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

	@SuppressWarnings("unchecked")
	public List<Vnet> getVxnets(String routerUuid) {
		List<Vnet> vxnetsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Vnet where vnetRouter = :routerid";
			Query query = session.createQuery(queryString);
			query.setString("routerid", routerUuid);
			vxnetsList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vxnetsList;
	}

	@SuppressWarnings("unchecked")
	public List<Vnet> getallVnetList(int userId) {
		List<Vnet> vxnetsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Vnet where vnetUID = :userid";
			Query query = session.createQuery(queryString);
			query.setInteger("userid", userId);
			vxnetsList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vxnetsList;
	}
}
