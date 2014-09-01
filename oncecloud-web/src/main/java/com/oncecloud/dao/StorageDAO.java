package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Storage;
import com.oncecloud.helper.SessionHelper;

/**
 * @author cyh
 * @version 2014/06/28
 */
@Component
public class StorageDAO {
	private SessionHelper sessionHelper;
	private OverViewDAO overViewDAO;

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

	@SuppressWarnings("unchecked")
	public Storage getStorage(String srUuid) {
		Session session = this.getSessionHelper().getMainSession();
		Storage sr = null;
		Query query = session.createQuery("from Storage where srUuid = '"
				+ srUuid + "'");
		List<Storage> srList = query.list();
		if (srList.size() == 1) {
			sr = srList.get(0);
		}
		session.close();
		return sr;
	}

	public Storage createStorage(String srname, String srAddress,
			String srDesc, String srType, String srDir, String rackid) {
		Storage storage = new Storage();
		storage.setSrUuid(UUID.randomUUID().toString());
		storage.setSrName(srname);
		storage.setSrDesc(srDesc);
		storage.setSrAddress(srAddress.trim());
		storage.setSrtype(srType);
		storage.setSrdir(srDir);
		storage.setSrstatus(1);
		storage.setCreateDate(new Date());
		storage.setDiskuuid(UUID.randomUUID().toString());
		storage.setIsouuid(UUID.randomUUID().toString());
		storage.setHauuid(UUID.randomUUID().toString());
		storage.setRackuuid(rackid);
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.save(storage);
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewSr", true);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return storage;
	}

	public int countStorage(int storageId) {
		long size = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session
					.createQuery("select count(*) from Storage where srUuid = "
							+ storageId + " and srstatus = 1");
			size = (Long) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			size = 0;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (int) size;
	}

	@SuppressWarnings("unchecked")
	public boolean isExist(String srName) {
		Session session = this.getSessionHelper().getMainSession();
		Query query = session.createQuery("from Storage where srName = '"
				+ srName + "' and srstatus = 1");
		List<Storage> srList = query.list();
		session.close();
		if (srList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取存储列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Storage> getOnePageStorageList(int page, int limit,
			String search) {
		List<Storage> srList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Storage where srName like :search and srstatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			srList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return srList;
	}

	/**
	 * 获取存储总数
	 * 
	 * @param search
	 * @return
	 */
	public int countAllStorageList(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select count(*) from Storage where srName like :search and srstatus = 1 ";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
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

	public int getStorageSize(String hostUuid) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select count(*) from HostSR where hostUuid = :hostUuid";
			Query query = session.createQuery(queryString);
			query.setString("hostUuid", hostUuid);
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

	public int getHostSizeOfStorage(String srUuid) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select count(*) from HostSR where srUuid = :srUuid";
			Query query = session.createQuery(queryString);
			query.setString("srUuid", srUuid);
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

	public boolean removeStorage(String storageId) {
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update Storage set srstatus = 0 where srUuid='"
					+ storageId + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewSr", false);
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

	/* 用于判断ip地址是否已经存在 */
	@SuppressWarnings("unchecked")
	public Storage getStorageByAddress(String address) {
		Session session = null;
		Storage sr = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session
					.createQuery("from Storage where srAddress = '"
							+ address.trim() + "' and srstatus = 1");
			List<Storage> srList = query.list();
			System.out.print(srList.size());
			if (srList.size() == 1) {
				sr = srList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return sr;
	}

	@SuppressWarnings("unchecked")
	public List<Storage> getStorageListByHostId(String poolMaster) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session
					.createQuery("from Storage where srUuid in (select s.srUuid from HostSR s where s.hostUuid= '"
							+ poolMaster.trim() + "') and srstatus = 1");
			List<Storage> srList = query.list();
			return srList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Storage> getStorageListOfRack(String rackUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session.createQuery("from Storage where rackuuid = '"
					+ rackUuid + "' and srstatus = 1");
			List<Storage> srList = query.list();
			return srList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}

	public boolean updateSR(String srid, String srName, String srDesc,
			String rackid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			Query query = session
					.createQuery("update Storage set srName=:name,"
							+ "srDesc=:desc,rackuuid=:rackid where srUuid =:srid");
			query.setString("name", srName);
			query.setString("desc", srDesc);
			query.setString("srid", srid);
			query.setString("rackid", rackid);
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
}
