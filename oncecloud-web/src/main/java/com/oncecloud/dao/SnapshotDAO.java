package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
	 * 获取主机快照列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getOnePageVMSnapshotList(int userId, int page, int limit,
			String search) {
		List<Object> vmSnapshotList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vmSnapshotList;
	}

	/**
	 * 获取硬盘快照列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @param offside
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getOnePageVolumeSnapshotList(int userId, int page, int limit,
			String search, int offside) {
		List<Object> volumeSnapshotList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "select ss.snapshotVolume, ol.volumeName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
					+ "from Snapshot ss, Volume ol where ss.snapshotVolume=ol.volumeUuid "
					+ "group by ss.snapshotVolume, ol.volumeName having ss.snapshotVolume in "
					+ "(select v.volumeUuid from Volume v where v.volumeUID = :userId "
					+ "and v.volumeName like :search) order by ol.createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			volumeSnapshotList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return volumeSnapshotList;
	}

	@SuppressWarnings("unchecked")
	public Object getOneVmSnapshot(String vmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select ss.snapshotVm, ov.vmName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
				+ "from Snapshot ss, OCVM ov "
				+ "where ss.snapshotVm=ov.vmUuid "
				+ "group by ss.snapshotVm, ov.vmName "
				+ "having ss.snapshotVm=:vmuuid ";
		Query query = session.createQuery(queryString);
		query.setString("vmuuid", vmUuid);
		List<Object> VMSnapshot = query.list();
		session.close();
		return VMSnapshot.get(0);
	}

	@SuppressWarnings("unchecked")
	public Object getOneVolumeSnapshot(String volumeUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select ss.snapshotVolume, ol.volumeName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
				+ "from Snapshot ss, Volume ol "
				+ "where ss.snapshotVolume=ol.volumeUuid "
				+ "group by ss.snapshotVolume, ol.volumeName "
				+ "having ss.snapshotVolume=:volumeuuid";
		Query query = session.createQuery(queryString);
		query.setString("volumeuuid", volumeUuid);
		List<Object> VolumeSnapshot = query.list();
		session.close();
		return VolumeSnapshot.get(0);
	}

	@SuppressWarnings("unchecked")
	public boolean ifNewChain(String uuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "from Snapshot where snapshotVm=:uuid or snapshotVolume=:uuid";
		Query query = session.createQuery(queryString);
		query.setString("uuid", uuid);
		List<Snapshot> snapshotList = query.list();
		session.close();
		if (snapshotList.size() == 0) {
			return true;
		} else {
			return false;
		}
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
			session = this.getSessionHelper().openMainSession();
			String queryString1 = "select count(distinct ss.snapshotVm) "
					+ "from Snapshot ss where ss.snapshotVm in "
					+ "(select vm.vmUuid from OCVM vm where vm.vmUID= :userId "
					+ "and vm.vmName like :search')";
			Query query1 = session.createQuery(queryString1);
			query1.setInteger("userId", userId);
			query1.setString("search", "%" + search + "%");
			int vmCount = ((Number) query1.iterate().next()).intValue();
			String queryString2 = "select count(distinct ss.snapshotVolume) "
					+ "from Snapshot ss where ss.snapshotVolume in "
					+ "(select v.volumeUuid from Volume v where v.volumeUID = :userId"
					+ " and v.volumeName like :search')";
			Query query2 = session.createQuery(queryString2);
			query2.setInteger("userId", userId);
			query2.setString("search", "%" + search + "%");
			int volumeCount = ((Number) query2.iterate().next()).intValue();
			count = vmCount + volumeCount;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return count;
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
			session = this.getSessionHelper().openMainSession();
			String queryString1 = "select count(distinct ss.snapshotVm) "
					+ "from Snapshot ss where ss.snapshotVm in "
					+ "(select vm.vmUuid from OCVM vm where vm.vmUID= :userId "
					+ "and vm.vmName like :search')";
			Query query = session.createQuery(queryString1);
			query.setInteger("userId", userId);
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

	public void insertSnapshot(String snapshotId, String snapshotName,
			int snapshotSize, Date backupDate, String snapshotVm,
			String snapshotVolume, boolean newChain, int userId) {
		Session session = null;
		Transaction tx = null;
		try {
			Snapshot snapshot = new Snapshot(snapshotId, snapshotName,
					snapshotSize, backupDate, snapshotVm, snapshotVolume);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(snapshot);
			if (newChain) {
				this.getQuotaDAO().updateQuotaField(session, userId,
						"quotaSnapshot", 1, true);
			}
			tx.commit();
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
	}

	@SuppressWarnings("unchecked")
	public Snapshot getSnapshot(String snapshotId) {
		Snapshot ss = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Snapshot where snapshotId = :snapshotId";
			Query query = session.createQuery(queryString);
			query.setString("snapshotId", snapshotId);
			List<Snapshot> ssList = query.list();
			if (ssList.size() == 1) {
				ss = ssList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return ss;
	}

	public void deleteOneSnapshot(int userId, String snapshotId) {
		Snapshot ss = this.getSnapshot(snapshotId);
		String vmUuid = ss.getSnapshotVm();
		String volumeUuid = ss.getSnapshotVolume();
		int vmSize = getVmSnapshotSize(vmUuid);
		int volSize = getVolumeSnapshotSize(volumeUuid);
		int nsize = vmSize + volSize;
		Session session = null;
		Transaction tx = null;
		try {
			if (ss != null) {
				session = this.getSessionHelper().openMainSession();
				tx = session.beginTransaction();
				session.delete(ss);
				if (nsize == 1) {
					this.getQuotaDAO().updateQuotaField(session, userId,
							"quotaSnapshot", 1, false);
				}
				tx.commit();
			}
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
	}

	public void deleteVmSnapshot(String vmUuid, int userId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete from Snapshot where snapshotVm= '"
					+ vmUuid + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaField(session, userId,
					"quotaSnapshot", 1, false);
			tx.commit();
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
	}

	public void deleteVolumeSnapshot(String volumeUuid, int userId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete from Snapshot where snapshotVolume= '"
					+ volumeUuid + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaField(session, userId,
					"quotaSnapshot", 1, false);
			tx.commit();
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
	}

	public Date getRecentVmSnapshotDate(String vmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select max(ss.backupDate) " + "from Snapshot ss "
				+ "group by ss.snapshotVm " + "having ss.snapshotVm='" + vmUuid
				+ "'";
		Query query = session.createQuery(queryString);
		if (query.list().size() == 0) {
			session.close();
			return null;
		}
		Date date = (Date) query.iterate().next();
		session.close();
		return date;
	}

	public Date getRecentVolumeSnapshotDate(String volumeUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select max(ss.backupDate) " + "from Snapshot ss "
				+ "group by ss.snapshotVolume " + "having ss.snapshotVolume='"
				+ volumeUuid + "'";
		Query query = session.createQuery(queryString);
		if (query.list().size() == 0) {
			session.close();
			return null;
		}
		Date date = (Date) query.iterate().next();
		session.close();
		return date;
	}

	@SuppressWarnings("unchecked")
	public List<Snapshot> getVmSnapshotList(String vmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "from Snapshot where snapshotVm = '" + vmUuid
				+ "' order by backupDate desc";
		Query query = session.createQuery(queryString);
		List<Snapshot> VMSnapshotList = query.list();
		session.close();
		return VMSnapshotList;
	}

	@SuppressWarnings("unchecked")
	public List<Snapshot> getVolumeSnapshotList(String volumeUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "from Snapshot where snapshotVolume = '"
				+ volumeUuid + "' order by backupDate desc";
		Query query = session.createQuery(queryString);
		List<Snapshot> VolumeSnapshotList = query.list();
		session.close();
		return VolumeSnapshotList;
	}

	@SuppressWarnings("unchecked")
	public int getVmSnapshotSize(String vmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "select sum(ss.snapshotSize) from Snapshot ss where ss.snapshotVm=:uuid";
		Query query = session.createQuery(queryString);
		query.setString("uuid", vmUuid);
		List<Object> objlist = query.list();
		Long re = (Long) objlist.get(0);
		if (re == null) {
			return 0;
		} else {
			return re.intValue();
		}
	}

	@SuppressWarnings("unchecked")
	public int getVolumeSnapshotSize(String volumeUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "select sum(ss.snapshotSize) from Snapshot ss where ss.snapshotVolume=:uuid";
		Query query = session.createQuery(queryString);
		query.setString("uuid", volumeUuid);
		List<Object> objlist = query.list();
		Long re = (Long) objlist.get(0);
		if (re == null) {
			return 0;
		} else {
			return re.intValue();
		}
	}
}
