package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.FeeEip;
import com.oncecloud.entity.FeeImage;
import com.oncecloud.entity.FeeSnapshot;
import com.oncecloud.entity.FeeVM;
import com.oncecloud.entity.FeeVolume;
import com.oncecloud.helper.Helper;
import com.oncecloud.helper.SessionHelper;

@Component
public class FeeDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public void insertFeeVM(Integer vmUID, Date startDate, Date endDate,
			Double vmPrice, Integer vmState, String vmUuid, String vmName) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			FeeVM feeVM = new FeeVM(vmUID, startDate, endDate, vmPrice,
					vmState, vmUuid, vmName);
			feeVM.setVmExpense();
			Transaction tx = session.beginTransaction();
			session.save(feeVM);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void destoryVM(Date endDate, String vmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Transaction tx = session.beginTransaction();
			String queryStr = "from FeeVM fv where fv.vmUuid=:uuid and fv.vmState<>0";
			Query query = session.createQuery(queryStr);
			query.setString("uuid", vmUuid);
			List<FeeVM> feeVMList = query.list();
			if (feeVMList.size() == 1) {
				FeeVM feeVM = feeVMList.get(0);
				feeVM.setEndDate(endDate);
				feeVM.setVmExpense();
				feeVM.setVmState(0);
				session.update(feeVM);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void insertFeeVolume(Integer volumeUID, Date startDate,
			Date endDate, Double volumePrice, Integer volumeState,
			String volumeUuid, String volumeName) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			FeeVolume feeVolume = new FeeVolume(volumeUID, startDate, endDate,
					volumePrice, volumeState, volumeUuid, volumeName);
			feeVolume.setVolumeExpense();
			Transaction tx = session.beginTransaction();
			session.save(feeVolume);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteVolume(Date endDate, String volumeUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Transaction tx = session.beginTransaction();
			String queryStr = "from FeeVolume fv where fv.volumeUuid=:uuid and fv.volumeState<>0";
			Query query = session.createQuery(queryStr);
			query.setString("uuid", volumeUuid);
			List<FeeVolume> feeVolumeList = query.list();
			if (feeVolumeList.size() == 1) {
				FeeVolume feeVolume = feeVolumeList.get(0);
				feeVolume.setEndDate(endDate);
				feeVolume.setVolumeExpense();
				feeVolume.setVolumeState(0);
				session.update(feeVolume);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void insertFeeSnapshot(Integer snapshotUID, Date startDate,
			Date endDate, Double snapshotPrice, Integer snapshotState,
			String vmUuid, String vmName) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			FeeSnapshot feeSnapshot = new FeeSnapshot(snapshotUID, startDate,
					endDate, snapshotPrice, snapshotState, vmUuid, vmName);
			feeSnapshot.setSnapshotExpense();
			Transaction tx = session.beginTransaction();
			session.save(feeSnapshot);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void deleteSnapshot(Date endDate, String vmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Transaction tx = session.beginTransaction();
			String queryStr = "from FeeSnapshot fs where fs.vmUuid=:uuid and fs.snapshotState<>0";
			Query query = session.createQuery(queryStr);
			query.setString("uuid", vmUuid);
			List<FeeSnapshot> feeSnapshotList = query.list();
			if (feeSnapshotList.size() > 0) {
				FeeSnapshot feeSnapshot = feeSnapshotList.get(0);
				feeSnapshot.setEndDate(endDate);
				feeSnapshot.setSnapshotExpense();
				feeSnapshot.setSnapshotState(0);
				session.update(feeSnapshot);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void insertFeeEip(Integer eipUID, Date startDate, Date endDate,
			Double eipPrice, Integer eipState, String eipUuid, String eipName) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			FeeEip feeEip = new FeeEip(eipUID, startDate, endDate, eipPrice,
					eipState, eipUuid, eipName);
			feeEip.setEipExpense();
			Transaction tx = session.beginTransaction();
			session.save(feeEip);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void abandonEip(Date endDate, String eipUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Transaction tx = session.beginTransaction();
			String queryStr = "from FeeEip fe where fe.eipUuid=:uuid and fe.eipState<>0";
			Query query = session.createQuery(queryStr);
			query.setString("uuid", eipUuid);
			List<FeeEip> feeEipList = query.list();
			if (feeEipList.size() == 1) {
				FeeEip feeEip = feeEipList.get(0);
				feeEip.setEndDate(endDate);
				feeEip.setEipExpense();
				feeEip.setEipState(0);
				session.update(feeEip);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void insertFeeImage(Integer imageUID, Date startDate, Date endDate,
			Double imagePrice, Integer imageState, String imageUuid,
			String imageName) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			FeeImage feeImage = new FeeImage(imageUID, startDate, endDate,
					imagePrice, imageState, imageUuid, imageName);
			feeImage.setImageExpense();
			Transaction tx = session.beginTransaction();
			session.save(feeImage);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteImage(Date endDate, String imageUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Transaction tx = session.beginTransaction();
			String queryStr = "from FeeImage fi where fi.imageUuid=:uuid and fi.imageState<>0";
			Query query = session.createQuery(queryStr);
			query.setString("uuid", imageUuid);
			List<FeeImage> feeImageList = query.list();
			if (feeImageList.size() == 1) {
				FeeImage feeImage = feeImageList.get(0);
				feeImage.setEndDate(endDate);
				feeImage.setImageExpense();
				feeImage.setImageState(0);
				session.update(feeImage);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageFeeVMList(int page, int limit, String search,
			int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			queryString = "select sum(fv.vmExpense), fv1.vmName, fv.vmUuid, fv1.vmState, fv1.vmPrice, min(fv.startDate) "
					+ "from FeeVM fv, FeeVM fv1 "
					+ "where fv.vmUuid=fv1.vmUuid and fv.vmUID=:uid "
					+ "group by fv.vmUuid, fv1.vmUuid, fv1.endDate "
					+ "having fv1.endDate=max(fv.endDate) "
					+ "order by max(fv.endDate) desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllFeeVMList(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fv.vmUuid) "
					+ "from FeeVM fv " + "where fv.vmUID=:uid ";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageFeeVolumeList(int page, int limit,
			String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			queryString = "select sum(fv.volumeExpense), fv.volumeName, fv.volumeUuid, fv1.volumeState, fv1.volumePrice, min(fv.startDate) "
					+ "from FeeVolume fv, FeeVolume fv1 "
					+ "where fv.volumeUuid=fv1.volumeUuid and fv.volumeUID=:uid "
					+ "group by fv.volumeUuid, fv1.volumeUuid, fv1.endDate "
					+ "having fv1.endDate=max(fv.endDate) "
					+ "order by max(fv.endDate) desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllFeeVolumeList(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fv.volumeUuid) "
					+ "from FeeVolume fv " + "where fv.volumeUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageFeeImageList(int page, int limit,
			String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			queryString = "select sum(fi.imageExpense), fi.imageName, fi.imageUuid, fi1.imageState, fi1.imagePrice, min(fi.startDate) "
					+ "from FeeImage fi, FeeImage fi1 "
					+ "where fi.imageUuid=fi1.imageUuid and fi.imageUID=:uid "
					+ "group by fi.imageUuid, fi1.imageUuid, fi1.endDate "
					+ "having fi1.endDate=max(fi.endDate) "
					+ "order by max(fi.endDate) desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllFeeImageList(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fi.imageUuid) "
					+ "from FeeImage fi " + "where fi.imageUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageFeeEipList(int page, int limit,
			String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			queryString = "select sum(fe.eipExpense), fe.eipName, fe.eipUuid, fe1.eipState, fe1.eipPrice, min(fe.startDate) "
					+ "from FeeEip fe, FeeEip fe1 "
					+ "where fe.eipUuid=fe1.eipUuid and fe.eipUID=:uid "
					+ "group by fe.eipUuid, fe1.eipUuid, fe1.endDate "
					+ "having fe1.endDate=max(fe.endDate) "
					+ "order by max(fe.endDate) desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllFeeEipList(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fe.eipUuid) "
					+ "from FeeEip fe " + "where fe.eipUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getOnePageFeeSnapshotList(int page, int limit,
			String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			queryString = "select sum(fs.snapshotExpense), fs.vmName, fs.vmUuid, fs1.snapshotState, fs1.snapshotPrice, min(fs.startDate) "
					+ "from FeeSnapshot fs, FeeSnapshot fs1 "
					+ "where fs.vmUuid=fs1.vmUuid and fs.snapshotUID=:uid "
					+ "group by fs.vmUuid, fs1.vmUuid, fs1.endDate "
					+ "having fs1.endDate=max(fs.endDate) "
					+ "order by max(fs.endDate) desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllFeeSnapshotList(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fs.vmUuid) "
					+ "from FeeSnapshot fs " + "where fs.snapshotUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeVMDetailList(int page, int limit, String search,
			int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fv.startDate, fv.endDate, fv.vmExpense, fv.vmPrice from FeeVM fv where fv.vmUuid=:uuid and fv.vmUID=:uid order by fv.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeVMDetailList(int page, int limit, String search,
			int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fv.vmUuid, fv.startDate, fv.endDate, fv.vmExpense, fv.vmPrice "
					+ "from FeeVM fv where fv.vmUID=:uid "
					+ "and fv.startDate>=:start and fv.startDate<:end order by fv.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeVolumeDetailList(int page, int limit,
			String search, int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fv.startDate, fv.endDate, fv.volumeExpense, fv.volumePrice from FeeVolume fv where fv.volumeUuid=:uuid and fv.volumeUID=:uid order by fv.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeVolumeDetailList(int page, int limit,
			String search, int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fv.volumeUuid, fv.startDate, fv.endDate, fv.volumeExpense, fv.volumePrice "
					+ "from FeeVolume fv where fv.volumeUID=:uid and "
					+ "fv.startDate>=:start and fv.startDate<:end order by fv.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeImageDetailList(int page, int limit,
			String search, int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fi.startDate, fi.endDate, fi.imageExpense, fi.imagePrice from FeeImage fi where fi.imageUuid=:uuid and fi.imageUID=:uid order by fi.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeImageDetailList(int page, int limit,
			String search, int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fi.imageUuid, fi.startDate, fi.endDate, fi.imageExpense, fi.imagePrice "
					+ "from FeeImage fi where fi.imageUID=:uid "
					+ "and fi.startDate>=:start and fi.startDate<:end order by fi.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeEipDetailList(int page, int limit, String search,
			int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fe.startDate, fe.endDate, fe.eipExpense, fe.eipPrice from FeeEip fe where fe.eipUuid=:uuid and fe.eipUID=:uid order by fe.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeEipDetailList(int page, int limit, String search,
			int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fe.eipUuid, fe.startDate, fe.endDate, fe.eipExpense, fe.eipPrice "
					+ "from FeeEip fe "
					+ "where fe.eipUID=:uid "
					+ "and fe.startDate>=:start and fe.startDate<:end order by fe.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeSnapshotDetailList(int page, int limit,
			String search, int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fs.startDate, fs.endDate, fs.snapshotExpense, fs.snapshotPrice from FeeSnapshot fs where fs.vmUuid=:uuid and fs.snapshotUID=:uid order by fs.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getFeeSnapshotDetailList(int page, int limit,
			String search, int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select fs.vmUuid, fs.startDate, fs.endDate, fs.snapshotExpense, fs.snapshotPrice "
					+ "from FeeSnapshot fs where fs.snapshotUID=:uid "
					+ "and fs.startDate>=:start and fs.startDate<:end order by fs.startDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			List<Object> ObjList = query.list();
			session.getTransaction().commit();
			return ObjList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countFeeVMDetailList(int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) " + "from FeeVM fv "
					+ "where fv.vmUID=:uid and fv.vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeVolumeDetailList(int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) " + "from FeeVolume fv "
					+ "where fv.volumeUID=:uid and fv.volumeUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeEipDetailList(int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) " + "from FeeEip fe "
					+ "where fe.eipUID=:uid and fe.eipUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeSnapshotDetailList(int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) " + "from FeeSnapshot fs "
					+ "where fs.snapshotUID=:uid and fs.vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeImageDetailList(int uid, String uuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "";
			queryString = "select count(*) " + "from FeeImage fi "
					+ "where fi.imageUID=:uid and fi.imageUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			int count = ((Number) query.iterate().next()).intValue();
			session.close();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeVMDetailList(int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) "
					+ "from FeeVM fv "
					+ "where fv.vmUID=:uid and fv.startDate>=:start and fv.startDate<:end";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeVolumeDetailList(int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) "
					+ "from FeeVolume fv "
					+ "where fv.volumeUID=:uid and fv.startDate>=:start and fv.startDate<:end";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeEipDetailList(int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) "
					+ "from FeeEip fe "
					+ "where fe.eipUID=:uid and fe.startDate>=:start and fe.startDate<:end";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeSnapshotDetailList(int uid, Date startMonth,
			Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) "
					+ "from FeeSnapshot fs "
					+ "where fs.snapshotUID=:uid and fs.startDate>=:start and fs.startDate<:end";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public int countFeeImageDetailList(int uid, Date startMonth, Date endMonth) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) "
					+ "from FeeImage fi "
					+ "where fi.imageUID=:uid and fi.startDate>=:start and fi.startDate<:end";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			query.setTimestamp("start", startMonth);
			query.setTimestamp("end", endMonth);
			int count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public double getVmTotalFee(int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select sum(fv.vmExpense) from FeeVM fv where fv.vmUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			Number number = (Number) query.iterate().next();
			session.getTransaction().commit();
			if (number != null) {
				return number.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0.0;
		}
	}

	public double getVolumeTotalFee(int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select sum(fv.volumeExpense) from FeeVolume fv where fv.volumeUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			Number number = (Number) query.iterate().next();
			session.getTransaction().commit();
			if (number != null) {
				return number.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0.0;
		}
	}

	public double getEipTotalFee(int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select sum(fe.eipExpense) from FeeEip fe where fe.eipUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			Number number = (Number) query.iterate().next();
			session.getTransaction().commit();
			if (number != null) {
				return number.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0.0;
		}
	}

	public double getImageTotalFee(int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select sum(fi.imageExpense) from FeeImage fi where fi.imageUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			Number number = (Number) query.iterate().next();
			session.getTransaction().commit();
			if (number != null) {
				return number.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0.0;
		}
	}

	public double getSnapshotTotalFee(int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select sum(fs.snapshotExpense) from FeeSnapshot fs where fs.snapshotUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
			Number number = (Number) query.iterate().next();
			session.getTransaction().commit();
			if (number != null) {
				return number.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0.0;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void updateAliveVmEndDate(Date nowDate) {
		List<FeeVM> feeVmList = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "from FeeVM fv where fv.vmState<>0 and fv.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			feeVmList = query.list();
			for (int i = 0; i < feeVmList.size(); i++) {
				FeeVM feeVM = feeVmList.get(0);
				Date nowEndDate = feeVM.getEndDate();
				long miliDis = nowDate.getTime() - nowEndDate.getTime();
				int addHourNum = (int) Math.ceil(miliDis / (1000 * 60 * 60.0));
				Date updateEndDate = Helper.addMinuteForDate(nowEndDate,
						60 * addHourNum);
				feeVM.setEndDate(updateEndDate);
				feeVM.setVmExpense();
				session.update(feeVM);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void updateAliveVolumeEndDate(Date nowDate) {
		List<FeeVolume> feeVolumeList = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "from FeeVolume fv where fv.volumeState<>0 and fv.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			feeVolumeList = query.list();
			for (int i = 0; i < feeVolumeList.size(); i++) {
				FeeVolume feeVolume = feeVolumeList.get(0);
				Date nowEndDate = feeVolume.getEndDate();
				long miliDis = nowDate.getTime() - nowEndDate.getTime();
				int addHourNum = (int) Math.ceil(miliDis / (1000 * 60 * 60.0));
				Date updateEndDate = Helper.addMinuteForDate(nowEndDate,
						60 * addHourNum);
				feeVolume.setEndDate(updateEndDate);
				feeVolume.setVolumeExpense();
				session.update(feeVolume);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void updateAliveEipEndDate(Date nowDate) {
		List<FeeEip> feeEipList = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "from FeeEip fe where fe.eipState<>0 and fe.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			feeEipList = query.list();
			for (int i = 0; i < feeEipList.size(); i++) {
				FeeEip feeEip = feeEipList.get(0);
				Date nowEndDate = feeEip.getEndDate();
				long miliDis = nowDate.getTime() - nowEndDate.getTime();
				int addHourNum = (int) Math.ceil(miliDis / (1000 * 60 * 60.0));
				Date updateEndDate = Helper.addMinuteForDate(nowEndDate,
						60 * addHourNum);
				feeEip.setEndDate(updateEndDate);
				feeEip.setEipExpense();
				session.update(feeEip);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void updateAliveImageEndDate(Date nowDate) {
		List<FeeImage> feeImageList = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "from FeeImage fi where fi.imageState<>0 and fi.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			feeImageList = query.list();
			for (int i = 0; i < feeImageList.size(); i++) {
				FeeImage feeImage = feeImageList.get(0);
				Date nowEndDate = feeImage.getEndDate();
				long miliDis = nowDate.getTime() - nowEndDate.getTime();
				int addHourNum = (int) Math.ceil(miliDis / (1000 * 60 * 60.0));
				Date updateEndDate = Helper.addMinuteForDate(nowEndDate,
						60 * addHourNum);
				feeImage.setEndDate(updateEndDate);
				feeImage.setImageExpense();
				session.update(feeImage);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void updateAliveSnapshotEndDate(Date nowDate) {
		List<FeeSnapshot> feeSnapshotList = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "from FeeSnapshot fs where fs.snapshotState<>0 and fs.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			feeSnapshotList = query.list();
			for (int i = 0; i < feeSnapshotList.size(); i++) {
				FeeSnapshot feeSnapshot = feeSnapshotList.get(0);
				Date nowEndDate = feeSnapshot.getEndDate();
				long miliDis = nowDate.getTime() - nowEndDate.getTime();
				int addHourNum = (int) Math.ceil(miliDis / (1000 * 60 * 60.0));
				Date updateEndDate = Helper.addMinuteForDate(nowEndDate,
						60 * addHourNum);
				feeSnapshot.setEndDate(updateEndDate);
				feeSnapshot.setSnapshotExpense();
				session.update(feeSnapshot);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * @param vmuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 */
	public void updateVmName(String vmuuid, String newName) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "";
			queryString = "update FeeVM fv set fv.vmName=:name where fv.vmState<>0 and fv.vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", vmuuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * @param volumeuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 */
	public void updateVolumeName(String volumeuuid, String newName) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update FeeVolume set volumeName=:name where volumeState<>0 and volumeUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", volumeuuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * @param eipuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 */
	public void updateEipName(String eipuuid, String newName) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update FeeEip set eipName=:name where eipState<>0 and eipUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", eipuuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * @param vmUuid
	 * @param newName
	 * @author xpx 2014-7-8
	 */
	public void updateSnapshotVMName(String vmUuid, String newName) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update FeeSnapshot set vmName=:name where snapshotState<>0 and vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", vmUuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * @param imageuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 */
	public void updateImageName(String imageuuid, String newName) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update FeeImage set imageName=:name where imageState<>0 and imageUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", imageuuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}
}
