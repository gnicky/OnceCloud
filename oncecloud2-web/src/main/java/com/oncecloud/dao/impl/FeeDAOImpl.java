package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.FeeDAO;
import com.oncecloud.entity.FeeEip;
import com.oncecloud.entity.FeeImage;
import com.oncecloud.entity.FeeSnapshot;
import com.oncecloud.entity.FeeVM;
import com.oncecloud.entity.FeeVolume;
import com.oncecloud.helper.Helper;
import com.oncecloud.helper.SessionHelper;

@Component("FeeDAO")
public class FeeDAOImpl implements FeeDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

/*	public boolean abandonEip(Date endDate, String eipUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(FeeEip.class);
			criteria.add(Restrictions.eq("eipUuid", eipUuid));
			criteria.add(Restrictions.ne("eipState", 0));
			FeeEip feeEip = (FeeEip) criteria.uniqueResult();
			if (feeEip != null) {
				feeEip.setEndDate(endDate);
				feeEip.setEipExpense();
				feeEip.setEipState(0);
				session.update(feeEip);
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

	public int countAllFeeEipList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fe.eipUuid) "
					+ "from FeeEip fe " + "where fe.eipUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
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

	public int countAllFeeImageList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fi.imageUuid) "
					+ "from FeeImage fi " + "where fi.imageUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
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

	public int countAllFeeSnapshotList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fs.vmUuid) "
					+ "from FeeSnapshot fs " + "where fs.snapshotUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
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

	public int countAllFeeVMList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fv.vmUuid) "
					+ "from FeeVM fv " + "where fv.vmUID=:uid ";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
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

	public int countAllFeeVolumeList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(distinct fv.volumeUuid) "
					+ "from FeeVolume fv " + "where fv.volumeUID=:uid";
			Query query = session.createQuery(queryString);
			query.setInteger("uid", uid);
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

	public int countFeeEipDetailList(int uid, Date startMonth, Date endMonth) {
		int count = 0;
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

	public int countFeeEipDetailList(int uid, String uuid) {
		int count = 0;
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

	public int countFeeImageDetailList(int uid, Date startMonth, Date endMonth) {
		int count = 0;
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

	public int countFeeImageDetailList(int uid, String uuid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "";
			queryString = "select count(*) " + "from FeeImage fi "
					+ "where fi.imageUID=:uid and fi.imageUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", uuid);
			query.setInteger("uid", uid);
			count = ((Number) query.iterate().next()).intValue();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	public int countFeeSnapshotDetailList(int uid, Date startMonth,
			Date endMonth) {
		int count = 0;
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

	public int countFeeSnapshotDetailList(int uid, String uuid) {
		int count = 0;
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

	public int countFeeVMDetailList(int uid, Date startMonth, Date endMonth) {
		int count = 0;
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

	public int countFeeVMDetailList(int uid, String uuid) {
		int count = 0;
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

	public int countFeeVolumeDetailList(int uid, Date startMonth, Date endMonth) {
		int count = 0;
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

	public int countFeeVolumeDetailList(int uid, String uuid) {
		int count = 0;
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

	@SuppressWarnings({ "unchecked" })
	public boolean deleteSnapshot(Date endDate, String vmUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
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

	public boolean deleteVolume(Date endDate, String volumeUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(FeeVolume.class)
					.add(Restrictions.eq("volumeUuid", volumeUuid))
					.add(Restrictions.ne("volumeState", 0));
			FeeVolume feeVolume = (FeeVolume) criteria.uniqueResult();
			if (feeVolume != null) {
				feeVolume.setEndDate(endDate);
				feeVolume.setVolumeExpense();
				feeVolume.setVolumeState(0);
				session.update(feeVolume);
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

	public boolean destoryVM(Date endDate, String vmUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(FeeVM.class)
					.add(Restrictions.eq("vmUuid", vmUuid))
					.add(Restrictions.ne("vmState", 0));
			FeeVM feeVM = (FeeVM) criteria.uniqueResult();
			if (feeVM != null) {
				feeVM.setEndDate(endDate);
				feeVM.setVmExpense();
				feeVM.setVmState(0);
				session.update(feeVM);
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

	public double getEipTotalFee(int uid) {
		double result = 0.0;
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
				result = number.doubleValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<FeeEip> getFeeEipDetailList(int page, int limit, String search,
			int uid, Date startMonth, Date endMonth) {
		List<FeeEip> feeEipDetailList = null;
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
			feeEipDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeEipDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeEip> getFeeEipDetailList(int page, int limit, String search,
			int uid, String uuid) {
		List<FeeEip> feeEipDetailList = null;
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
			feeEipDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeEipDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeImage> getFeeImageDetailList(int page, int limit,
			String search, int uid, Date startMonth, Date endMonth) {
		List<FeeImage> feeImageDetailList = null;
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
			feeImageDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeImageDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeImage> getFeeImageDetailList(int page, int limit,
			String search, int uid, String uuid) {
		List<FeeImage> feeImageDetailList = null;
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
			feeImageDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeImageDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeSnapshot> getFeeSnapshotDetailList(int page, int limit,
			String search, int uid, Date startMonth, Date endMonth) {
		List<FeeSnapshot> feeSnapshotDetailList = null;
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
			feeSnapshotDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeSnapshotDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeSnapshot> getFeeSnapshotDetailList(int page, int limit,
			String search, int uid, String uuid) {
		List<FeeSnapshot> feeSnapshotDetailList = null;
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
			feeSnapshotDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeSnapshotDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeVM> getFeeVMDetailList(int page, int limit, String search,
			int uid, Date startMonth, Date endMonth) {
		List<FeeVM> feeVMDetailList = null;
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
			feeVMDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeVMDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeVM> getFeeVMDetailList(int page, int limit, String search,
			int uid, String uuid) {
		List<FeeVM> feeVMDetailList = null;
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
			feeVMDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeVMDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeVolume> getFeeVolumeDetailList(int page, int limit,
			String search, int uid, Date startMonth, Date endMonth) {
		List<FeeVolume> feeVolumeDetailList = null;
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
			feeVolumeDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeVolumeDetailList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeVolume> getFeeVolumeDetailList(int page, int limit,
			String search, int uid, String uuid) {
		List<FeeVolume> feeVolumeDetailList = null;
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
			feeVolumeDetailList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeVolumeDetailList;
	}

	public double getImageTotalFee(int uid) {
		double result = 0.0;
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
				result = number.doubleValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<FeeEip> getOnePageFeeEipList(int page, int limit,
			String search, int uid) {
		List<FeeEip> feeEipList = null;
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
			feeEipList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeEipList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeImage> getOnePageFeeImageList(int page, int limit,
			String search, int uid) {
		List<FeeImage> feeImageList = null;
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
			feeImageList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeImageList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeSnapshot> getOnePageFeeSnapshotList(int page, int limit,
			String search, int uid) {
		List<FeeSnapshot> feeSnapshotList = null;
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
			feeSnapshotList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeSnapshotList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeVM> getOnePageFeeVMList(int page, int limit, String search,
			int uid) {
		List<FeeVM> feeVMList = null;
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
			feeVMList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeVMList;
	}

	@SuppressWarnings("unchecked")
	public List<FeeVolume> getOnePageFeeVolumeList(int page, int limit,
			String search, int uid) {
		List<FeeVolume> feeVolumeList = null;
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
			feeVolumeList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return feeVolumeList;
	}

	public double getSnapshotTotalFee(int uid) {
		double result = 0.0;
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
				result = number.doubleValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public double getVmTotalFee(int uid) {
		double result = 0.0;
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
				result = number.doubleValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public double getVolumeTotalFee(int uid) {
		double result = 0.0;
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
				result = number.doubleValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean insertFeeEip(Integer eipUID, Date startDate, Date endDate,
			Double eipPrice, Integer eipState, String eipUuid, String eipName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			FeeEip feeEip = new FeeEip(eipUID, startDate, endDate, eipPrice,
					eipState, eipUuid, eipName);
			session.save(feeEip);
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

	public boolean insertFeeSnapshot(Integer snapshotUID, Date startDate,
			Date endDate, Double snapshotPrice, Integer snapshotState,
			String vmUuid, String vmName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			FeeSnapshot feeSnapshot = new FeeSnapshot(snapshotUID, startDate,
					endDate, snapshotPrice, snapshotState, vmUuid, vmName);
			feeSnapshot.setSnapshotExpense();
			session.save(feeSnapshot);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
*/
	public boolean insertFeeVM(Integer vmUID, Date startDate, Date endDate,
			Double vmPrice, Integer vmState, String vmUuid, String vmName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			FeeVM feeVM = new FeeVM(vmUID, startDate, endDate, vmPrice,
					vmState, vmUuid, vmName);
			feeVM.setVmExpense();
			session.save(feeVM);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
/*
	public boolean insertFeeVolume(Integer volumeUID, Date startDate,
			Date endDate, Double volumePrice, Integer volumeState,
			String volumeUuid, String volumeName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			FeeVolume feeVolume = new FeeVolume(volumeUID, startDate, endDate,
					volumePrice, volumeState, volumeUuid, volumeName);
			feeVolume.setVolumeExpense();
			session.save(feeVolume);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	public boolean updateAliveEipEndDate(Date nowDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from FeeEip fe where fe.eipState<>0 and fe.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			List<FeeEip> feeEipList = query.list();
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

	@SuppressWarnings({ "unchecked" })
	public boolean updateAliveImageEndDate(Date nowDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from FeeImage fi where fi.imageState<>0 and fi.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			List<FeeImage> feeImageList = query.list();
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

	@SuppressWarnings({ "unchecked" })
	public boolean updateAliveSnapshotEndDate(Date nowDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from FeeSnapshot fs where fs.snapshotState<>0 and fs.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			List<FeeSnapshot> feeSnapshotList = query.list();
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

	@SuppressWarnings({ "unchecked" })
	public boolean updateAliveVmEndDate(Date nowDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from FeeVM fv where fv.vmState<>0 and fv.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			List<FeeVM> feeVmList = query.list();
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

	@SuppressWarnings({ "unchecked" })
	public boolean updateAliveVolumeEndDate(Date nowDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from FeeVolume fv where fv.volumeState<>0 and fv.endDate<:date";
			Query query = session.createQuery(queryString);
			query.setTimestamp("date", nowDate);
			List<FeeVolume> feeVolumeList = query.list();
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

	*//**
	 * @param eipuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 *//*
	public boolean updateEipName(String eipuuid, String newName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update FeeEip set eipName=:name where eipState<>0 and eipUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", eipuuid);
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

	*//**
	 * @param imageuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 *//*
	public boolean updateImageName(String imageuuid, String newName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update FeeImage set imageName=:name where imageState<>0 and imageUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", imageuuid);
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

	*//**
	 * @param vmUuid
	 * @param newName
	 * @author xpx 2014-7-8
	 *//*
	public boolean updateSnapshotVMName(String vmUuid, String newName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update FeeSnapshot set vmName=:name where snapshotState<>0 and vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", vmUuid);
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

	*//**
	 * @param vmuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 *//*
	public boolean updateVmName(String vmuuid, String newName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "update FeeVM fv set fv.vmName=:name where fv.vmState<>0 and fv.vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", vmuuid);
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

	*//**
	 * @param volumeuuid
	 * @param newName
	 * @author xpx 2014-7-8
	 *//*
	public boolean updateVolumeName(String volumeuuid, String newName) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update FeeVolume set volumeName=:name where volumeState<>0 and volumeUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", volumeuuid);
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
	}*/
}
