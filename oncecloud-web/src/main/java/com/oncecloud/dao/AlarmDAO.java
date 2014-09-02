package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Alarm;
import com.oncecloud.helper.SessionHelper;

@Component
public class AlarmDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public boolean addAlarm(String alarmUuid, String alarmName,
			Integer alarmType, Integer alarmIsalarm, Integer alarmTouch,
			Integer alarmPeriod, int userId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Date alarmDate = new Date();
			Alarm alarm = new Alarm(alarmUuid, alarmName, alarmType, alarmDate,
					alarmIsalarm, alarmTouch, alarmPeriod, userId);
			session.save(alarm);
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

	public int countAlarmList(int userId, String keyword) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Alarm.class)
					.add(Restrictions.eq("alarmUid", userId))
					.add(Restrictions.like("alarmName", keyword,
							MatchMode.ANYWHERE))
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

	public Alarm getAlarm(String alarmUuid) {
		Alarm alarm = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Alarm.class).add(
					Restrictions.eq("alarmUuid", alarmUuid));
			alarm = (Alarm) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return alarm;
	}

	@SuppressWarnings("unchecked")
	public List<Alarm> getOnePageList(int userId, int pageIndex,
			int itemPerPage, String keyword) {
		List<Alarm> alarmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPosition = (pageIndex - 1) * itemPerPage;
			Criteria criteria = session
					.createCriteria(Alarm.class)
					.add(Restrictions.eq("alarmUid", userId))
					.add(Restrictions.like("alarmName", keyword,
							MatchMode.ANYWHERE))
					.addOrder(Order.desc("alarmDate"))
					.setFirstResult(startPosition).setMaxResults(itemPerPage);
			alarmList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return alarmList;
	}

	public boolean removeAlarm(Alarm alarm) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.delete(alarm);
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

	public boolean updateAlarm(Alarm alarm) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.update(alarm);
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

	public boolean updateName(String alarmUuid, String newName,
			String description) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Alarm.class).add(
					Restrictions.eq("alarmUuid", alarmUuid));
			Alarm alarm = (Alarm) criteria.uniqueResult();
			if (alarm != null) {
				alarm.setAlarmName(newName);
				alarm.setAlarmDesc(description);
				session.update(alarm);
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

	public boolean updatePeriod(String alarmUuid, int alarmPeriod) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Alarm.class).add(
					Restrictions.eq("alarmUuid", alarmUuid));
			Alarm alarm = (Alarm) criteria.uniqueResult();
			if (alarm != null) {
				alarm.setAlarmPeriod(alarmPeriod);
				session.update(alarm);
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
}