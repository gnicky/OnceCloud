package com.oncecloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.Storage;
import com.oncecloud.helper.SessionHelper;

@Component
public class HostDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public int countAllHostList(String search) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCHost where hostName like '%"
					+ search + "%' and hostStatus = 1";
			Query query = session.createQuery(queryString);
			int total = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return total;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<OCHost> getAllHost() {
		Session session = null;
		List<OCHost> list = new ArrayList<OCHost>();
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCHost where hostStatus = 1";
			Query query = session.createQuery(queryString);
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

	@SuppressWarnings("unchecked")
	public OCHost getHost(String hostUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			OCHost host = null;
			Query query = session.createQuery("from OCHost where hostUuid = '"
					+ hostUuid + "'");
			List<OCHost> hostList = query.list();
			if (hostList.size() == 1) {
				host = hostList.get(0);
			}
			session.getTransaction().commit();
			return host;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<OCHost> getHostForImage() {
		List<OCHost> hostList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCHost where hostStatus = 1";
			Query query = session.createQuery(queryString);
			hostList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return hostList;
	}

	@SuppressWarnings("unchecked")
	public OCHost getHostFromIp(String hostIp) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			OCHost host = null;
			Query query = session.createQuery("from OCHost where hostIP = '"
					+ hostIp + "' and hostStatus = 1");
			List<OCHost> hostList = query.list();
			if (hostList.size() == 1) {
				host = hostList.get(0);
			}
			session.getTransaction().commit();
			return host;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<OCHost> getHostListOfPool(String poolUuid) {
		List<OCHost> hostList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCHost where poolUuid = :poolUuid and hostStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("poolUuid", poolUuid);
			hostList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return hostList;
	}

	@SuppressWarnings("unchecked")
	public List<OCHost> getHostListOfRack(String rackUuid) {
		List<OCHost> hostList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCHost where rackUuid = :rackUuid and hostStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("rackUuid", rackUuid);
			hostList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return hostList;
	}

	@SuppressWarnings("unchecked")
	public List<OCHost> getOnePageHostList(int page, int limit, String search) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from OCHost where hostName like '%" + search
					+ "%' and hostStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			List<OCHost> hostList = query.list();
			session.getTransaction().commit();
			return hostList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<OCHost> getOnePageLoadHostList(int page, int limit,
			String search, String sruuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from OCHost where hostName like '%"
					+ search
					+ "%' and hostStatus = 1 and hostUuid not in (select hs.hostUuid from HostSR hs where hs.srUuid = '"
					+ sruuid + "') order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			List<OCHost> hostList = query.list();
			session.getTransaction().commit();
			return hostList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Storage> getSROfHost(String hostUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select sr from Storage sr where sr.srUuid in (select hs.srUuid from HostSR hs where hs.hostUuid='"
					+ hostUuid + "')";
			Query query = session.createQuery(queryString);
			List<Storage> storageList = query.list();
			session.getTransaction().commit();
			return storageList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public boolean isSameSr(Set<String> sr1, Set<String> sr2) {
		if (sr1.size() != sr2.size()) {
			return false;
		} else {
			sr1.retainAll(sr1);
			if (sr1.size() == sr2.size()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void setPool(String hostUuid, String poolUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCHost set poolUuid = :poolUuid where hostUuid = :hostUuid";
			Query query = session.createQuery(queryString);
			query.setString("hostUuid", hostUuid);
			query.setString("poolUuid", poolUuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public boolean unbindSr(String hostUuid, String srUuid) {
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "delete from HostSR where hostUuid=:hid and srUuid=:sid";
			Query query = session.createQuery(queryString);
			query.setString("hid", hostUuid);
			query.setString("sid", srUuid);
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

	public boolean updateHost(String hostId, String hostName, String hostDesc,
			String rackUuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String querysString = "update OCHost set hostName=:name,"
					+ "hostDesc=:desc,rackUuid=:rackid where hostUuid =:hostid";
			Query query = session.createQuery(querysString);
			query.setString("name", hostName);
			query.setString("desc", hostDesc);
			query.setString("rackid", rackUuid);
			query.setString("hostid", hostId);
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
}
