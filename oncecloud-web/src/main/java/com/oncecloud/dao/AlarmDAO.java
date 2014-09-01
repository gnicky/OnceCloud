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

	public Alarm getAlarm(String alarmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Alarm.class).add(
					Restrictions.eq("alarmUuid", alarmUuid));
			Alarm alarm = (Alarm) criteria.uniqueResult();
			session.getTransaction().commit();
			return alarm;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public boolean updateAlarm(Alarm alarm) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.update(alarm);
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

	public boolean removeAlarm(Alarm alarm) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.delete(alarm);
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

	@SuppressWarnings("unchecked")
	public List<Alarm> getOnePageList(int userId, int pageIndex,
			int itemPerPage, String keyword) {
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
			List<Alarm> list = criteria.list();
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

	public int countAlarmList(int userId, String keyword) {
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
			int count = ((Number) criteria.uniqueResult()).intValue();
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

	public boolean addAlarm(String alarmUuid, String alarmName,
			Integer alarmType, Integer alarmIsalarm, Integer alarmTouch,
			Integer alarmPeriod, int userId) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Date alarmDate = new Date();
			Alarm alarm = new Alarm(alarmUuid, alarmName, alarmType, alarmDate,
					alarmIsalarm, alarmTouch, alarmPeriod, userId);
			session.save(alarm);
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

	public boolean updateName(String alarmUuid, String newName,
			String description) {
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

	public boolean updatePeriod(String alarmUuid, int alarmPeriod) {
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
}