package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.ChargeRecord;
import com.oncecloud.helper.SessionHelper;

@Component
public class ChargeDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public ChargeRecord createChargeRecord(String recordId, Double recordBill,
			Integer recordType, Date recordDate, Integer recordUID,
			Integer state) {
		ChargeRecord cr = null;
		Session session = null;
		Transaction tx = null;
		try {
			cr = new ChargeRecord(recordId, recordBill, recordType, recordDate,
					recordUID);
			cr.setRecordState(state);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(cr);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return cr;
	}

	@SuppressWarnings("unchecked")
	public List<ChargeRecord> getOnePageChargeRecord(int page, int limit,
			int userid) {
		List<ChargeRecord> crList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from ChargeRecord where recordUID=:userid and recordState > 0 order by recordDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userid", userid);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			crList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return crList;
	}

	public int countChargeRecord(int userid) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from ChargeRecord where recordUID=:userid and recordState > 0 ";
			Query query = session.createQuery(queryString);
			query.setInteger("userid", userid);
			total = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return total;
	}

	public boolean updateChargeRecord(String recordId, Integer recordstate) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update ChargeRecord set recordState="
					+ recordstate + " where recordId ='" + recordId + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			tx.commit();
			result = true;
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
		return result;
	}

	@SuppressWarnings("unchecked")
	public ChargeRecord getChargeRecord(String recordId) {
		List<ChargeRecord> crList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from ChargeRecord where recordId='"
					+ recordId + "'";
			Query query = session.createQuery(queryString);
			crList = query.list();
			if (crList.size() > 0) {
				return crList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}
}
