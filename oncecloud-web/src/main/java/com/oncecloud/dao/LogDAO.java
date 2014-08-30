package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OCLog;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.log.LogConstant;

/**
 * @author hehai
 * @version 2014/05/06
 */
@Component
public class LogDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<OCLog> getLogList(int logUID, int logStatus, int start, int num) {
		Session session = null;
		List<OCLog> logList = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = null;
			if (logStatus == LogConstant.logStatus.全部.ordinal()) {
				query = session
						.createQuery("from OCLog where logUID = :logUID order by logId desc");
				query.setInteger("logUID", logUID);
			} else {
				query = session
						.createQuery("from OCLog where logUID = :logUID and logStatus = :logStatus order by logId desc");
				query.setInteger("logUID", logUID);
				query.setInteger("logStatus", logStatus);
			}

			query.setFirstResult(start);
			query.setMaxResults(num);
			logList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return logList;
	}

	public OCLog insertLog(int logUID, int logObject, int logAction,
			int logStatus, String logInfo, Date logTime, int logElapse) {
		OCLog log = new OCLog();
		log.setLogUID(logUID);
		log.setLogObject(logObject);
		log.setLogAction(logAction);
		log.setLogStatus(logStatus);
		log.setLogInfo(logInfo);
		log.setLogTime(logTime);
		log.setLogElapse(logElapse);
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(log);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return log;
	}
}
