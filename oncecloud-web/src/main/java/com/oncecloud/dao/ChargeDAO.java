package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
		ChargeRecord chargeRecord = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			chargeRecord = new ChargeRecord(recordId, recordBill, recordType,
					recordDate, recordUID);
			chargeRecord.setRecordState(state);
			session.save(chargeRecord);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return chargeRecord;
	}

	@SuppressWarnings("unchecked")
	public List<ChargeRecord> getOnePageChargeRecord(int page, int limit,
			int userid) {
		List<ChargeRecord> chargeRecordList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session.createCriteria(ChargeRecord.class)
					.add(Restrictions.eq("recordUID", userid))
					.add(Restrictions.gt("recordState", 0))
					.addOrder(Order.desc("recordDate"))
					.setFirstResult(startPos).setMaxResults(limit);
			chargeRecordList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return chargeRecordList;
	}

	public int countChargeRecord(int userid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(ChargeRecord.class)
					.add(Restrictions.eq("recordUID", userid))
					.add(Restrictions.gt("recordState", 0))
					.setProjection(Projections.rowCount());
			count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	public boolean updateChargeRecord(String recordId, Integer recordstate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(ChargeRecord.class).add(
					Restrictions.eq("recordId", recordId));
			ChargeRecord chargeRecord = (ChargeRecord) criteria.uniqueResult();
			if (chargeRecord != null) {
				chargeRecord.setRecordState(recordstate);
				session.update(chargeRecord);
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

	public ChargeRecord getChargeRecord(String recordId) {
		ChargeRecord chargeRecord = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(ChargeRecord.class).add(
					Restrictions.eq("recordId", recordId));
			chargeRecord = (ChargeRecord) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return chargeRecord;
	}
}
