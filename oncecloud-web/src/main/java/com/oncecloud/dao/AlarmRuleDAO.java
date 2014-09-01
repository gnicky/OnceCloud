package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(AlarmRule.class).add(
					Restrictions.eq("ruleAUuid", ruleAUuid));
			AlarmRule alarmRule = (AlarmRule) criteria.uniqueResult();
			session.getTransaction().commit();
			return alarmRule;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public boolean addRule(String ruleAUuid, Integer ruleAType,
			Integer ruleAThreshold, String ruleAAlarmUuid, Integer ruleAPeriod) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			AlarmRule rule = new AlarmRule(ruleAUuid, ruleAType,
					ruleAThreshold, ruleAAlarmUuid, ruleAPeriod);
			session.save(rule);
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

	public boolean removeAlarmRule(AlarmRule alarmrule) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.delete(alarmrule);
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
	public boolean removeRules(String alarmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(AlarmRule.class).add(
					Restrictions.eq("ruleAAlarmUuid", alarmUuid));
			List<AlarmRule> rules = criteria.list();
			for (AlarmRule alarmRule : rules) {
				session.delete(alarmRule);
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

	public boolean updateRule(String ruleAUuid, int ruleAPeriod,
			int ruleAThreshold, int ruleAType) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			AlarmRule alarmRule = getAlarmRule(ruleAUuid);
			if (ruleAPeriod > 0) {
				alarmRule.setRuleAPeriod(ruleAPeriod);
			}
			if (ruleAThreshold > 0) {
				alarmRule.setRuleAThreshold(ruleAThreshold);
			}
			if (ruleAType > 0) {
				alarmRule.setRuleAType(ruleAType);
			}
			session.update(alarmRule);
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
	public List<AlarmRule> getOnePageList(int page, int limit,
			String ruleAAlarmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session.createCriteria(AlarmRule.class)
					.add(Restrictions.eq("ruleAAlarmUuid", ruleAAlarmUuid))
					.setFirstResult(startPos).setMaxResults(limit);
			List<AlarmRule> list = criteria.list();
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

	public int countAlarmList(String ruleAAlarmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(AlarmRule.class)
					.add(Restrictions.eq("ruleAAlarmUuid", ruleAAlarmUuid))
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

}
