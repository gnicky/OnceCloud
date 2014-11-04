package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.LogDAO;
import com.oncecloud.entity.OCLog;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.log.LogConstant;

@Component("LogDAO")
public class LogDAOImpl implements LogDAO {

	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}
	
	public List<OCLog> getLogList(int logUID, int logStatus, int start, int num) {
		List<OCLog> logList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(OCLog.class).add(
					Restrictions.eq("logUID", logUID));
			if (logStatus != LogConstant.logStatus.全部.ordinal()) {
				criteria = criteria
						.add(Restrictions.eq("logStatus", logStatus));
			}
			criteria = criteria.addOrder(Order.desc("logId"))
					.setFirstResult(start).setMaxResults(num);
			logList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return logList;
	}

	public OCLog insertLog(int logUID, int logObject, int logAction,
			int logStatus, String logInfo, Date logTime, int logElapse) {
		OCLog log = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			log = new OCLog();
			log.setLogUID(logUID);
			log.setLogObject(logObject);
			log.setLogAction(logAction);
			log.setLogStatus(logStatus);
			log.setLogInfo(logInfo);
			log.setLogTime(logTime);
			log.setLogElapse(logElapse);
			session.save(log);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return log;
	}

}
