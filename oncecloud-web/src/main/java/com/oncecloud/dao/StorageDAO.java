package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
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

	/**
	 * 获取存储
	 * 
	 * @param srUuid
	 * @return
	 */
	public Storage getStorage(String srUuid) {
		Storage sr = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Storage where srUuid = :srUuid");
			query.setString("srUuid", srUuid);
			sr = (Storage) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return sr;
	}

	/**
	 * 获取存储大小
	 * 
	 * @param hostUuid
	 * @return
	 */
	public int getStorageSize(String hostUuid) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from HostSR where hostUuid = :hostUuid";
			Query query = session.createQuery(queryString);
			query.setString("hostUuid", hostUuid);
			total = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return total;
	}

	/**
	 * 获取一页存储列表
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
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from Storage where srName like :search and srstatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			srList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return srList;
	}

	/**
	 * 获取机架存储列表
	 * 
	 * @param rackUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Storage> getStorageListOfRack(String rackUuid) {
		List<Storage> srList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Storage where rackuuid = :rackUuid "
							+ "and srstatus = 1");
			query.setString("rackUuid", rackUuid);
			srList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return srList;
	}

	/**
	 * 获取主机存储列表
	 * 
	 * @param hostUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Storage> getStorageListOfHost(String hostUuid) {
		List<Storage> srList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Storage where srUuid in (select s.srUuid from HostSR s where s.hostUuid = :hostUuid) and srstatus = 1");
			query.setString("hostUuid", hostUuid);
			srList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return srList;
	}

	/**
	 * 获取对应目标IP的存储
	 * 
	 * @param address
	 * @return
	 */
	public Storage getStorageOfAddress(String address) {
		Session session = null;
		Storage sr = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Storage where srAddress = :address "
							+ "and srstatus = 1");
			sr = (Storage) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return sr;
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
			session.beginTransaction();
			String queryString = "select count(*) from Storage where srName like :search and srstatus = 1 ";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
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

	/**
	 * 获取挂载该存储的服务器数目
	 * 
	 * @param srUuid
	 * @return
	 */
	public int countHostsOfStorage(String srUuid) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from HostSR where srUuid = :srUuid";
			Query query = session.createQuery(queryString);
			query.setString("srUuid", srUuid);
			total = ((Number) query.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return total;
	}

	/**
	 * 创建存储
	 * 
	 * @param srname
	 * @param srAddress
	 * @param srDesc
	 * @param srType
	 * @param srDir
	 * @param rackid
	 * @return
	 */
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(storage);
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewSr",
					true);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return storage;
	}

	/**
	 * 删除存储
	 * 
	 * @param storageId
	 * @return
	 */
	public boolean removeStorage(String storageId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Storage set srstatus = 0 where srUuid = :storageId";
			Query query = session.createQuery(queryString);
			query.setString("storageId", storageId);
			query.executeUpdate();
			this.getOverViewDAO().updateOverViewfieldNoTransaction("viewSr",
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

	/**
	 * 更新存储
	 * 
	 * @param srId
	 * @param srName
	 * @param srDesc
	 * @param rackId
	 * @return
	 */
	public boolean updateSR(String srId, String srName, String srDesc,
			String rackId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("update Storage set srName= :srName, "
							+ "srDesc= :srDesc, rackuuid= :rackId where srUuid = :srId");
			query.setString("srName", srName);
			query.setString("srDesc", srDesc);
			query.setString("srId", srId);
			query.setString("rackId", rackId);
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
}
