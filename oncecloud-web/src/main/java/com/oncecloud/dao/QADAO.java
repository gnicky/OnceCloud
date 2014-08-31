package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.QA;
import com.oncecloud.entity.User;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/05/09
 */
@Component
public class QADAO {
	private SessionHelper sessionHelper;
	private UserDAO userDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@SuppressWarnings("unchecked")
	public List<QA> getOnePageQuestionList(int qaUID, int page, int limit,
			String search) {
		User user = this.getUserDAO().getUser(qaUID);
		Session session = null;
		List<QA> qaList = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = null;
			String queryStr = "from QA where qaTitle like '%" + search
					+ "%' and qaUID = " + qaUID
					+ " and qaStatus < 2 order by qaId desc";
			if (user.getUserLevel() == 0) {
				queryStr = "from QA where qaTitle like '%" + search
						+ "%' and qaStatus < 2 order by qaId desc";
			}
			query = session.createQuery(queryStr);
			int startPos = (page - 1) * limit;
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			qaList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return qaList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAnswerList(int qaTid) {
		Session session = null;
		List<Object> qaList = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = null;
			query = session
					.createQuery("select user.userName, qa.qaContent, qa.qaStatus, qa.qaTime from QA qa, User user where qa.qaUID = user.userId and qa.qaTid = :qaTid");
			query.setInteger("qaTid", qaTid);
			qaList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return qaList;
	}

	public int insertQuestion(int qaUID, String qaTitle, String qaContent,
			Date qaTime) {
		int id = 0;
		QA question = new QA();
		question.setQaUID(qaUID);
		question.setQaTitle(qaTitle);
		question.setQaContent(qaContent);
		question.setQaStatus(1);
		question.setQaReply(0);
		question.setQaTime(qaTime);
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(question);
			tx.commit();
			id = question.getQaId();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return id;
	}

	public int insertAnswer(int qaUID, String qaContent, int qaTid,
			int qaStatus, Date qaTime) {
		int id = 0;
		QA target = getQuestion(qaTid);
		target.setQaReply(target.getQaReply() + 1);
		QA question = new QA();
		question.setQaUID(qaUID);
		question.setQaContent(qaContent);
		question.setQaTid(qaTid);
		question.setQaStatus(qaStatus);
		question.setQaTime(qaTime);
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(question);
			session.update(target);
			tx.commit();
			id = question.getQaId();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return id;
	}

	public int countAllQuestionList(int qaUID, String search) {
		User user = this.getUserDAO().getUser(qaUID);
		int result = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = null;
			String queryStr = "select count(*) from QA where qaTitle like '%"
					+ search + "%' and qaUID = " + qaUID + " and qaStatus < 2";
			if (user.getUserLevel() == 0) {
				queryStr = "select count(*) from QA where qaTitle like :search and qaStatus < 2";
			}
			query = session.createQuery(queryStr);
			result = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public QA getQuestion(int qaId) {
		QA qa = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			Query query = null;
			query = session.createQuery("from QA where qaId = :qaId");
			query.setInteger("qaId", qaId);
			List<QA> qaList = query.list();
			if (qaList != null && qaList.size() == 1) {
				qa = qaList.get(0);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return qa;
	}

	@SuppressWarnings("deprecation")
	public boolean closeQuestion(int qaId) {
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			Query query = session
					.createQuery("update QA qa set qa.qaStatus = 0 where qa.qaId = :qaId");
			query.setLockMode("qa", LockMode.UPGRADE);
			query.setInteger("qaId", qaId);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}
}
