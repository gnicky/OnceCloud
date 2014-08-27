package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Alarm where alarmUuid =:alarmUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			return (Alarm) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean updateAlarm(Alarm alarm) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSessionHelper().openMainSession();
			transaction = session.beginTransaction();
			session.update(alarm);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean removeAlarm(Alarm alarm) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSessionHelper().openMainSession();
			transaction = session.beginTransaction();
			session.delete(alarm);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Alarm> getOnePageList(int page, int limit, int alarmUid,
			String search) {
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Alarm alarm where alarm.alarmUid =:alarmUid and alarm.alarmName like '%"
					+ search + "%' order by alarm.alarmDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("alarmUid", alarmUid);
			return (List<Alarm>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public int countAlarmList(String search, int alarmUid) {
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Alarm where alarmUid=:alarmUid and alarmName like '%"
					+ search + "%'";
			Query query = session.createQuery(queryString);
			query.setInteger("alarmUid", alarmUid);
			return ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean addAlarm(String alarmUuid, String alarmName,
			Integer alarmType, Integer alarmIsalarm, Integer alarmTouch,
			Integer alarmPeriod, int userId) {
		Transaction transaction = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			transaction = session.beginTransaction();
			Date alarmDate = new Date();
			Alarm alram = new Alarm(alarmUuid, alarmName, alarmType, alarmDate,
					alarmIsalarm, alarmTouch, alarmPeriod, userId);
			session.save(alram);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean updateName(String alarmUuid, String newName,
			String description) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSessionHelper().openMainSession();
			transaction = session.beginTransaction();
			String queryString = "update Alarm set alarmName=:name, alarmDesc=:desc where alarmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", alarmUuid);
			query.setString("desc", description);
			query.executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean updatePeriod(String alarmUuid, int alarmPeriod) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSessionHelper().openMainSession();
			transaction = session.beginTransaction();
			String queryString = "update Alarm set alarmPeriod=:alarmPeriod where alarmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("uuid", alarmUuid);
			query.setInteger("alarmPeriod", alarmPeriod);
			query.executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}