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

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageVmSnapshotList(int page, int limit,
			String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "";
		queryString = "select ss.snapshotVm, ov.vmName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
				+ "from Snapshot ss, OCVM ov "
				+ "where ss.snapshotVm=ov.vmUuid "
				+ "group by ss.snapshotVm, ov.vmName "
				+ "having ss.snapshotVm in "
				+ "(select vm.vmUuid from OCVM vm where vm.vmUID="
				+ uid
				+ " and vm.vmName like '%"
				+ search
				+ "%') "
				+ "order by ov.createDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<Object> VMSnapshotList = query.list();
		session.close();
		return VMSnapshotList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageVolumeSnapshotList(int page, int limit,
			String search, int uid, int offside) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit + offside;
		String queryString = "";
		queryString = "select ss.snapshotVolume, ol.volumeName, count(*), sum(ss.snapshotSize), max(ss.backupDate) "
				+ "from Snapshot ss, Volume ol "
				+ "where ss.snapshotVolume=ol.volumeUuid "
				+ "group by ss.snapshotVolume, ol.volumeName "
				+ "having ss.snapshotVolume in "
				+ "(select v.volumeUuid from Volume v where v.volumeUID="
				+ uid
				+ " and v.volumeName like '%"
				+ search
				+ "%') "
				+ "order by ol.createDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<Object> VolumeSnapshotList = query.list();
		session.close();
		return VolumeSnapshotList;
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

	public int countAllSnapshotList(String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";

		queryString = "select count(distinct ss.snapshotVm) "
				+ "from Snapshot ss " + "where ss.snapshotVm in "
				+ "(select vm.vmUuid from OCVM vm where vm.vmUID=" + uid
				+ " and vm.vmName like '%" + search + "%')";
		Query query = session.createQuery(queryString);
		int vmCount = ((Number) query.iterate().next()).intValue();

		queryString = "select count(distinct ss.snapshotVolume) "
				+ "from Snapshot ss " + "where ss.snapshotVolume in "
				+ "(select v.volumeUuid from Volume v where v.volumeUID=" + uid
				+ " and v.volumeName like '%" + search + "%')";
		Query query1 = session.createQuery(queryString);
		int volumeCount = ((Number) query1.iterate().next()).intValue();

		session.close();
		return vmCount + volumeCount;
	}

	public int countVmSnapshotList(String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";

		queryString = "select count(distinct ss.snapshotVm) "
				+ "from Snapshot ss " + "where ss.snapshotVm in "
				+ "(select vm.vmUuid from OCVM vm where vm.vmUID=" + uid
				+ " and vm.vmName like '%" + search + "%')";
		Query query = session.createQuery(queryString);
		int vmCount = ((Number) query.iterate().next()).intValue();

		session.close();
		return vmCount;
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
			if (session != null) {
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
			if (session != null) {
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
			if (session != null) {
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
			if (session != null) {
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
			if (session != null) {
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
