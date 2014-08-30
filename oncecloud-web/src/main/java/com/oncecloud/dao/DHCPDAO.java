package com.oncecloud.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.oncecloud.entity.DHCP;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;

/**
 * @author hehai
 * @version 2014/06/26
 */
@Component
public class DHCPDAO {
	private SessionHelper sessionHelper;
	private OverViewDAO overViewDAO;
	private Constant constant;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	/*
	 * from prefix.start -> prefix.end
	 */
	public boolean addDHCPPool(String prefix, int start, int end, Date date) {
		boolean result = true;
		Session session = null;
		Transaction tx = null;
		Connection c = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			c = this.getConstant().getConnection(1);
			List<DHCP> dhcpList = new ArrayList<DHCP>();
			JSONObject total = new JSONObject();
			JSONArray ipMacArray = new JSONArray();
			for (int i = start; i <= end; i++) {
				String currentIp = prefix + i;
				boolean check = ipExist(currentIp);
				if (check == false) {
					DHCP dhcp = new DHCP();
					String mac = Utilities.randomMac();
					dhcp.setDhcpMac(mac);
					dhcp.setDhcpIp(currentIp);
					dhcp.setCreateDate(date);
					JSONObject ipMacObj = new JSONObject();
					ipMacObj.put("ipAddress", currentIp);
					ipMacObj.put("hardwareAddress", mac);
					ipMacArray.put(ipMacObj);
					dhcpList.add(dhcp);
				}
			}
			total.put("hosts", ipMacArray);
			boolean bindResult = Host.bindIpMac(c, total.toString());
			if (bindResult) {
				for (DHCP dhcp : dhcpList) {
					session.save(dhcp);
					this.getOverViewDAO().updateOverViewfield(session,
							"viewDhcp", true);
				}
				result = true;
				tx.commit();
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			if (tx != null) {
				tx.commit();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	public synchronized DHCP getFreeDHCP(String tenantUuid, int depenType) {
		DHCP dhcp = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session
					.createQuery("from DHCP where tenantUuid = null order by rand()");
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<DHCP> iplist = query.list();
			if (iplist.size() == 1) {
				dhcp = iplist.get(0);
				tx = session.beginTransaction();
				dhcp.setTenantUuid(tenantUuid);
				dhcp.setDepenType(depenType);
				session.update(dhcp);
				tx.commit();
			}
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
		return dhcp;
	}

	@SuppressWarnings("unchecked")
	public DHCP getDHCP(String dhcpMac) {
		DHCP dhcp = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from DHCP where dhcpMac = :dhcpMac";
			Query query = session.createQuery(queryString);
			query.setString("dhcpMac", dhcpMac);
			List<DHCP> dhcpList = query.list();
			if (dhcpList.size() == 1) {
				dhcp = dhcpList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dhcp;
	}

	@SuppressWarnings("unchecked")
	public boolean ipExist(String dhcpIp) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from DHCP where dhcpIp = :dhcpIp";
			Query query = session.createQuery(queryString);
			query.setString("dhcpIp", dhcpIp);
			List<DHCP> dhcpList = query.list();
			if (dhcpList.size() == 1) {
				result = true;
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

	public synchronized boolean returnDHCP(String dhcpMac) {
		DHCP dhcp = this.getDHCP(dhcpMac);
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		try {
			if (dhcp != null) {
				dhcp.setTenantUuid(null);
				dhcp.setDepenType(null);
				session = this.getSessionHelper().openMainSession();
				tx = session.beginTransaction();
				session.update(dhcp);
				tx.commit();
				result = true;
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DHCP> getOnePageDHCPList(int page, int limit, String search) {
		List<DHCP> dcList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from DHCP where dhcpIp like '%" + search
					+ "%' order by tenantUuid desc, dhcpIp";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			dcList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dcList;
	}

	public int countAllDHCP(String search) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from DHCP where dhcpIp like '%"
					+ search + "%'";
			Query query = session.createQuery(queryString);
			total = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return total;
	}

	public boolean deleteDHCP(String ip, String mac) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			DHCP dhcp = this.getDHCP(mac);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.delete(dhcp);
			this.getOverViewDAO().updateOverViewfield(session, "viewDhcp",
					false);
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}
}
