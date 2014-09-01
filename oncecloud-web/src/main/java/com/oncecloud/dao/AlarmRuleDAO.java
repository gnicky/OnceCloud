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

	private AlarmRule doGetAlarmRule(String ruleAUuid, Session session) {
		AlarmRule alarmRule;
		Criteria criteria = session.createCriteria(AlarmRule.class).add(
				Restrictions.eq("ruleAUuid", ruleAUuid));
		alarmRule = (AlarmRule) criteria.uniqueResult();
		return alarmRule;
	}

	public AlarmRule getAlarmRule(String ruleAUuid) {
		AlarmRule alarmRule = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			alarmRule = this.doGetAlarmRule(ruleAUuid, session);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return alarmRule;
	}

	@SuppressWarnings("unchecked")
	public List<AlarmRule> getOnePageList(int page, int limit,
			String ruleAAlarmUuid) {
		List<AlarmRule> alarmRuleList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session.createCriteria(AlarmRule.class)
					.add(Restrictions.eq("ruleAAlarmUuid", ruleAAlarmUuid))
					.setFirstResult(startPos).setMaxResults(limit);
			alarmRuleList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return alarmRuleList;
	}

	public int countAlarmList(String ruleAAlarmUuid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(AlarmRule.class)
					.add(Restrictions.eq("ruleAAlarmUuid", ruleAAlarmUuid))
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

	public boolean addRule(String ruleAUuid, Integer ruleAType,
			Integer ruleAThreshold, String ruleAAlarmUuid, Integer ruleAPeriod) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			AlarmRule rule = new AlarmRule(ruleAUuid, ruleAType,
					ruleAThreshold, ruleAAlarmUuid, ruleAPeriod);
			session.save(rule);
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

	public boolean updateRule(String ruleAUuid, int ruleAPeriod,
			int ruleAThreshold, int ruleAType) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			AlarmRule alarmRule = this.doGetAlarmRule(ruleAUuid, session);
			if (alarmRule != null) {
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

	public boolean removeAlarmRule(AlarmRule alarmrule) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.delete(alarmrule);
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

	@SuppressWarnings("unchecked")
	public boolean removeRules(String alarmUuid) {
		boolean result = false;
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
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

}
