package com.oncecloud.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

@Component
public class DHCPDAO {

	private Constant constant;

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	private OverViewDAO overViewDAO;

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public boolean addDHCPPool(String prefix, int start, int end, Date date) {
		boolean result = true;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
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
			Connection connection = this.getConstant().getConnectionNoTransactional(1);
			boolean bindResult = Host.bindIpMac(connection, total.toString());
			if (bindResult) {
				for (DHCP dhcp : dhcpList) {
					session.save(dhcp);
					this.getOverViewDAO().updateOverViewfieldNoTransaction(
							"viewDhcp", true);
				}
				session.getTransaction().commit();
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public int countAllDHCP(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(DHCP.class)
					.add(Restrictions
							.like("dhcpIp", search, MatchMode.ANYWHERE))
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

	public boolean deleteDHCP(String ip, String mac) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			DHCP dhcp = this.doGetDHCP(session, mac);
			if (dhcp != null) {
				session.delete(dhcp);
				result = true;
			}
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewDhcp",
					false);
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

	private DHCP doGetDHCP(Session session, String dhcpMac) {
		DHCP dhcp;
		Criteria criteria = session.createCriteria(DHCP.class).add(
				Restrictions.eq("dhcpMac", dhcpMac));
		dhcp = (DHCP) criteria.uniqueResult();
		return dhcp;
	}

	public DHCP getDHCP(String dhcpMac) {
		DHCP dhcp = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			dhcp = this.doGetDHCP(session, dhcpMac);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return dhcp;
	}

	@SuppressWarnings("unchecked")
	public synchronized DHCP getFreeDHCP(String tenantUuid, int depenType) {
		DHCP dhcp = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(DHCP.class).add(
					Restrictions.isNull("tenantUuid"));
			List<DHCP> dhcpList = criteria.list();
			if (dhcpList.size() > 0) {
				int selectedIndex = new Random().nextInt(dhcpList.size());
				dhcp = dhcpList.get(selectedIndex);
				dhcp.setTenantUuid(tenantUuid);
				dhcp.setDepenType(depenType);
				session.update(dhcp);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return dhcp;
	}

	@SuppressWarnings("unchecked")
	public List<DHCP> getOnePageDHCPList(int page, int limit, String search) {
		List<DHCP> dhcpList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(DHCP.class)
					.add(Restrictions
							.like("dhcpIp", search, MatchMode.ANYWHERE))
					.addOrder(Order.desc("tenantUuid"))
					.addOrder(Order.asc("dhcpIp")).setFirstResult(startPos)
					.setMaxResults(limit);
			dhcpList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return dhcpList;
	}

	public boolean ipExist(String dhcpIp) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Criteria criteria = session.createCriteria(DHCP.class).add(
					Restrictions.eq("dhcpIp", dhcpIp));
			DHCP dhcp = (DHCP) criteria.uniqueResult();
			result = (dhcp != null);
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public synchronized boolean returnDHCP(String dhcpMac) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			DHCP dhcp = this.doGetDHCP(session, dhcpMac);
			if (dhcp != null) {
				dhcp.setTenantUuid(null);
				dhcp.setDepenType(null);
				session.update(dhcp);
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
