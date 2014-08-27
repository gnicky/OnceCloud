package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.AlarmRule;
import com.oncecloud.helper.SessionHelper;

@Component
public class AlarmRuleDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public AlarmRule getAlarmRule(String ruleAUuid) {
		Session session = null;
		AlarmRule alarmRule = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from AlarmRule where ruleAUuid =:ruleAUuid";
			Query query = session.createQuery(queryString);
			query.setString("ruleAUuid", ruleAUuid);
			if (query.list().size() == 1) {
				alarmRule = (AlarmRule) query.list().get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return alarmRule;
	}

	public boolean addRule(String ruleAUuid, Integer ruleAType,
			Integer ruleAThreshold, String ruleAAlarmUuid, Integer ruleAPeriod) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().openMainSession();
			AlarmRule rule = new AlarmRule(ruleAUuid, ruleAType,
					ruleAThreshold, ruleAAlarmUuid, ruleAPeriod);
			tx = session.beginTransaction();
			session.save(rule);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	public boolean removeAlarmRule(AlarmRule alarmrule) {
		boolean result = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.delete(alarmrule);
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

	public boolean removeRules(String alarmUuid) {
		boolean result = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete AlarmRule ar where ar.ruleAAlarmUuid=:alarmUuid";
			Query query = session.createQuery(queryString);
			query.setParameter("alarmUuid", alarmUuid);
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

	public boolean updateRule(String ruleAUuid, int ruleAPeriod,
			int ruleAThreshold, int ruleAType) {
		boolean result = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			AlarmRule alarmRule = getAlarmRule(ruleAUuid);
			if (ruleAPeriod > 0)
				alarmRule.setRuleAPeriod(ruleAPeriod);
			if (ruleAThreshold > 0)
				alarmRule.setRuleAThreshold(ruleAThreshold);
			if (ruleAType > 0)
				alarmRule.setRuleAType(ruleAType);
			session.update(alarmRule);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;
		} finally {
			if (session != null) {
				session.close();
			}

		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<AlarmRule> getOnePageList(int page, int limit,
			String ruleAAlarmUuid) {
		List<AlarmRule> alarmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from AlarmRule where ruleAAlarmUuid=:ruleAAlarmUuid ";
			Query query = session.createQuery(queryString);
			query.setString("ruleAAlarmUuid", ruleAAlarmUuid);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			alarmList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return alarmList;
	}

	public int countAlarmList(String ruleAAlarmUuid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from AlarmRule where ruleAAlarmUuid =:ruleAAlarmUuid";
			Query query = session.createQuery(queryString);
			query.setString("ruleAAlarmUuid", ruleAAlarmUuid);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

}
