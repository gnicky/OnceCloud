package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Snapshot;
import com.oncecloud.helper.SessionHelper;

/**
 * @author yly
 * @version 2014/04/04
 */
@Component
public class SnapshotDAO {
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

	/**
	 * 获取快照
	 * 
	 * @param snapshotId
	 * @return
	 */
	public Snapshot getSnapshot(String snapshotId) {
		Snapshot ss = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from Snapshot where snapshotId = :snapshotId";
			Query query = session.createQuery(queryString);
			query.setString("snapshotId", snapshotId);
			ss = (Snapshot) query.uniqueResult();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return ss;
	}

	/**
	 * 获取主机的备份链
	 * 
	 * @param vmUuid
	 * @return
	 */
	public Object getOneVmSnapshot(String vmUuid) {
		Object object = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select ss.snapshotVm, ov.vmName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
					+ "from Snapshot ss, OCVM ov "
					+ "where ss.snapshotVm = ov.vmUuid "
					+ "group by ss.snapshotVm, ov.vmName "
					+ "having ss.snapshotVm = :vmuuid ";
			Query query = session.createQuery(queryString);
			query.setString("vmuuid", vmUuid);
			object = query.list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return object;
	}

	/**
	 * 获取硬盘的备份链
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public Object getOneVolumeSnapshot(String volumeUuid) {
		Object object = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select ss.snapshotVolume, ol.volumeName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
					+ "from Snapshot ss, Volume ol "
					+ "where ss.snapshotVolume=ol.volumeUuid "
					+ "group by ss.snapshotVolume, ol.volumeName "
					+ "having ss.snapshotVolume=:volumeuuid";
			Query query = session.createQuery(queryString);
			query.setString("volumeuuid", volumeUuid);
			object = query.list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return object;
	}

	/**
	 * 获取一页主机备份链列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getOnePageVMSnapshotList(int userId, int page,
			int limit, String search) {
		List<Object> vmSnapshotList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select ss.snapshotVm, ov.vmName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
					+ "from Snapshot ss, OCVM ov where ss.snapshotVm = ov.vmUuid "
					+ "group by ss.snapshotVm, ov.vmName having ss.snapshotVm in "
					+ "(select vm.vmUuid from OCVM vm where vm.vmUID = :userId "
					+ "and vm.vmName like :search) order by ov.createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vmSnapshotList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmSnapshotList;
	}

	/**
	 * 获取一页硬盘备份链列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @param offside
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getOnePageVolumeSnapshotList(int userId, int page,
			int limit, String search, int offside) {
		List<Object> volumeSnapshotList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select ss.snapshotVolume, ol.volumeName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
					+ "from Snapshot ss, Volume ol where ss.snapshotVolume = ol.volumeUuid "
					+ "group by ss.snapshotVolume, ol.volumeName having ss.snapshotVolume in "
					+ "(select v.volumeUuid from Volume v where v.volumeUID = :userId "
					+ "and v.volumeName like :search) order by ol.createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			volumeSnapshotList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volumeSnapshotList;
	}

	/**
	 * 获取虚拟机快照列表
	 * 
	 * @param vmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Snapshot> getVmSnapshotList(String vmUuid) {
		List<Snapshot> snapshotList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Snapshot where snapshotVm = :vmUuid order by backupDate desc";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			snapshotList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return snapshotList;
	}

	/**
	 * 获取硬盘快照列表
	 * 
	 * @param volumeUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Snapshot> getVolumeSnapshotList(String volumeUuid) {
		List<Snapshot> snapshotList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Snapshot where snapshotVolume = :volumeUuid order by backupDate desc";
			Query query = session.createQuery(queryString);
			query.setString("volumeUuid", volumeUuid);
			snapshotList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return snapshotList;
	}

	/**
	 * 获取最近的主机快照时间
	 * 
	 * @param vmUuid
	 * @return
	 */
	public Date getRecentVmSnapshotDate(String vmUuid) {
		Date date = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select max(ss.backupDate) "
					+ "from Snapshot ss group by ss.snapshotVm "
					+ "having ss.snapshotVm = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			date = (Date) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return date;
	}

	/**
	 * 获取最近的硬盘快照时间
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public Date getRecentVolumeSnapshotDate(String volumeUuid) {
		Date date = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select max(ss.backupDate) "
					+ "from Snapshot ss group by ss.snapshotVolume "
					+ "having ss.snapshotVolume = :volumeUuid";
			Query query = session.createQuery(queryString);
			query.setString("volumeUuid", volumeUuid);
			date = (Date) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return date;
	}

	/**
	 * 获取主机快照数目
	 * 
	 * @param vmUuid
	 * @return
	 */
	public int getVmSnapshotSize(String vmUuid) {
		int size = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select sum(ss.snapshotSize) from Snapshot ss where ss.snapshotVm = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			size = (Integer) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return size;
	}

	/**
	 * 获取硬盘快照数目
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public int getVolumeSnapshotSize(String volumeUuid) {
		int size = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select sum(ss.snapshotSize) from Snapshot ss where ss.snapshotVolume = :volumeUuid";
			Query query = session.createQuery(queryString);
			query.setString("volumeUuid", volumeUuid);
			size = (Integer) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return size;
	}

	/**
	 * 获取主机备份链总数
	 * 
	 * @param userId
	 * @param search
	 * @return
	 */
	public int countVMSnapshotList(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString1 = "select count(distinct ss.snapshotVm) "
					+ "from Snapshot ss where ss.snapshotVm in "
					+ "(select vm.vmUuid from OCVM vm where vm.vmUID= :userId "
					+ "and vm.vmName like :search')";
			Query query = session.createQuery(queryString1);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			count = (Integer) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * 获取备份链总数
	 * 
	 * @param search
	 * @param uid
	 * @return
	 */
	public int countAllSnapshotList(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString1 = "select count(distinct ss.snapshotVm) "
					+ "from Snapshot ss where ss.snapshotVm in "
					+ "(select vm.vmUuid from OCVM vm where vm.vmUID= :userId "
					+ "and vm.vmName like :search')";
			Query query1 = session.createQuery(queryString1);
			query1.setInteger("userId", userId);
			query1.setString("search", "%" + search + "%");
			int vmCount = (Integer) query1.uniqueResult();
			String queryString2 = "select count(distinct ss.snapshotVolume) "
					+ "from Snapshot ss where ss.snapshotVolume in "
					+ "(select v.volumeUuid from Volume v where v.volumeUID = :userId "
					+ "and v.volumeName like :search')";
			Query query2 = session.createQuery(queryString2);
			query2.setInteger("userId", userId);
			query2.setString("search", "%" + search + "%");
			int volumeCount = (Integer) query2.uniqueResult();
			count = vmCount + volumeCount;
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * 判断是否是新的备份链
	 * 
	 * @param uuid
	 * @return
	 */
	public boolean ifNewChain(String uuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from Snapshot where snapshotVm = :uuid or snapshotVolume = :uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			int size = query.list().size();
			if (size == 0) {
				result = true;
			}
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	/**
	 * 创建新的快照
	 * 
	 * @param snapshotId
	 * @param snapshotName
	 * @param snapshotSize
	 * @param backupDate
	 * @param snapshotVm
	 * @param snapshotVolume
	 * @param newChain
	 * @param userId
	 */
	public void insertSnapshot(String snapshotId, String snapshotName,
			int snapshotSize, Date backupDate, String snapshotVm,
			String snapshotVolume, boolean newChain, int userId) {
		Session session = null;
		try {
			Snapshot snapshot = new Snapshot(snapshotId, snapshotName,
					snapshotSize, backupDate, snapshotVm, snapshotVolume);
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(snapshot);
			if (newChain) {
				this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
						"quotaSnapshot", 1, true);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 删除快照
	 * 
	 * @param userId
	 * @param snapshotId
	 */
	public void deleteOneSnapshot(int userId, String snapshotId) {
		Snapshot ss = this.getSnapshot(snapshotId);
		if (ss != null) {
			String vmUuid = ss.getSnapshotVm();
			String volumeUuid = ss.getSnapshotVolume();
			int vmSize = getVmSnapshotSize(vmUuid);
			int volSize = getVolumeSnapshotSize(volumeUuid);
			int nsize = vmSize + volSize;
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.delete(ss);
				if (nsize == 1) {
					this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
							"quotaSnapshot", 1, false);
				}
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	/**
	 * 删除主机的全部快照
	 * 
	 * @param vmUuid
	 * @param userId
	 */
	public void deleteVmSnapshot(String vmUuid, int userId) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete from Snapshot where snapshotVm = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
					"quotaSnapshot", 1, false);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 删除硬盘的全部快照
	 * 
	 * @param volumeUuid
	 * @param userId
	 */
	public void deleteVolumeSnapshot(String volumeUuid, int userId) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete from Snapshot where snapshotVolume = :volumeUuid";
			Query query = session.createQuery(queryString);
			query.setString("volumeUuid", volumeUuid);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
					"quotaSnapshot", 1, false);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}
}
