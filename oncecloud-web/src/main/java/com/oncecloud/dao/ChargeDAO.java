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
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			ChargeRecord cr = new ChargeRecord(recordId, recordBill,
					recordType, recordDate, recordUID);
			cr.setRecordState(state);
			session.save(cr);
			session.getTransaction().commit();
			return cr;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ChargeRecord> getOnePageChargeRecord(int page, int limit,
			int userid) {
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
			List<ChargeRecord> list = criteria.list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countChargeRecord(int userid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(ChargeRecord.class)
					.add(Restrictions.eq("recordUID", userid))
					.add(Restrictions.gt("recordState", 0))
					.setProjection(Projections.rowCount());
			int total = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return total;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public boolean updateChargeRecord(String recordId, Integer recordstate) {
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
			}
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public ChargeRecord getChargeRecord(String recordId) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(ChargeRecord.class).add(
					Restrictions.eq("recordId", recordId));
			ChargeRecord chargeRecord = (ChargeRecord) criteria.uniqueResult();
			session.getTransaction().commit();
			return chargeRecord;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}
}
