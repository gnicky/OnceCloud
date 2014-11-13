package com.oncecloud.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.HostDAO;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCPool;
import com.oncecloud.entity.Storage;
import com.oncecloud.helper.SessionHelper;

@Component("HostDAO")
public class HostDAOImpl implements HostDAO {

	private SessionHelper sessionHelper;
	
	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}
	
	public int countAllHostList(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCHost where hostName like '%"
					+ search + "%' and hostStatus = 1";
			Query query = session.createQuery(queryString);
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
/*
	@SuppressWarnings("unchecked")
	public List<OCHost> getAllHost() {
		List<OCHost> list = null;
		Session session = null;
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
	}*/

	@SuppressWarnings("unchecked")
	public OCHost getHost(String hostUuid) {
		OCHost host = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session.createQuery("from OCHost where hostUuid = '"
					+ hostUuid + "'");
			List<OCHost> hostList = query.list();
			if (hostList.size() == 1) {
				host = hostList.get(0);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return host;
	}

	@SuppressWarnings("unchecked")
	public OCHost getHostNoTransactional(String hostUuid) {
		OCHost host = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session.createQuery("from OCHost where hostUuid = '"
					+ hostUuid + "'");
			List<OCHost> hostList = query.list();
			if (hostList.size() == 1) {
				host = hostList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return host;
	}

/*	@SuppressWarnings("unchecked")
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

	public OCHost getHostFromIp(String hostIp) {
		OCHost host = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from OCHost where hostIP = :hostIp and hostStatus = 1");
			query.setString("hostIp", hostIp);
			host = (OCHost) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return host;
	}
*/
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
		List<OCHost> hostList = null;
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
/*
	@SuppressWarnings("unchecked")
	public List<OCHost> getOnePageLoadHostList(int page, int limit,
			String search, String sruuid) {
		List<OCHost> hostList = null;
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
	public List<Storage> getSROfHost(String hostUuid) {
		List<Storage> storageList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select sr from Storage sr where sr.srUuid in (select hs.srUuid from HostSR hs where hs.hostUuid='"
					+ hostUuid + "')";
			Query query = session.createQuery(queryString);
			storageList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return storageList;
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

	public boolean setPool(String hostUuid, String poolUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCHost set poolUuid = :poolUuid where hostUuid = :hostUuid";
			Query query = session.createQuery(queryString);
			query.setString("hostUuid", hostUuid);
			query.setString("poolUuid", poolUuid);
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

	public boolean unbindSr(String hostUuid, String srUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete from HostSR where hostUuid=:hid and srUuid=:sid";
			Query query = session.createQuery(queryString);
			query.setString("hid", hostUuid);
			query.setString("sid", srUuid);
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

	public boolean saveHost(OCHost host) {
		boolean result = false;
		if (host != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.save(host);
				this.getOverViewDAO().updateOverViewfieldNoTransaction(
						"viewServer", true);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	public boolean updateHost(String hostId, String hostName, String hostDesc,
			String rackUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String querysString = "update OCHost set hostName=:name,"
					+ "hostDesc=:desc,rackUuid=:rackid where hostUuid =:hostid";
			Query query = session.createQuery(querysString);
			query.setString("name", hostName);
			query.setString("desc", hostDesc);
			query.setString("rackid", rackUuid);
			query.setString("hostid", hostId);
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

	public boolean deleteHost(String hostId) {
		boolean result = false;
		OCHost host = this.getHost(hostId);
		if (host != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				this.deleteHostSR(hostId);
				session.delete(host);
				this.getOverViewDAO().updateOverViewfieldNoTransaction(
						"viewServer", false);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}
	
	public void deleteHostSR(String hostId) {
		Session session = null;
		try {
			session = getSessionHelper().getMainSession();
			Query query = session
				.createQuery("delete from HostSR where hostUuid = :hostId");
			query.setString("hostId", hostId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public boolean eject(OCHost host, String poolUuid, String masterUuid) {
		boolean result = false;
		Session session = null;
		OCPool pool = this.getPoolDAO().getPool(poolUuid);
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			host.setPoolUuid(null);
			session.update(host);
			if (host.getHostUuid().equals(masterUuid)) {
				pool.setPoolMaster(null);
				session.update(pool);
			}
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

	public boolean updatePoolMaster(OCPool pool, OCHost targetHost) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			targetHost.setPoolUuid(pool.getPoolUuid());
			pool.setPoolMaster(targetHost.getHostUuid());
			session.update(targetHost);
			session.update(pool);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;

	}*/
}
