package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
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

	/**
	 * 获取问题或答复
	 * 
	 * @param qaId
	 * @return
	 */
	public QA getQuestion(int qaId) {
		QA qa = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session.createQuery("from QA where qaId = :qaId");
			query.setInteger("qaId", qaId);
			qa = (QA) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return qa;
	}

	/**
	 * 获取一页问题列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QA> getOnePageQuestionList(int userId, int page, int limit,
			String search) {
		List<QA> qaList = null;
		User user = this.getUserDAO().getUser(userId);
		if (user != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				int startPos = (page - 1) * limit;
				String queryStr = "from QA where qaTitle like :search and qaUID = "
						+ userId + " and qaStatus < 2 order by qaId desc";
				if (user.getUserLevel() == 0) {
					queryStr = "from QA where qaTitle like :search and qaStatus < 2 order by qaId desc";
				}
				Query query = session.createQuery(queryStr);
				query.setString("search", "%" + search + "%");
				query.setFirstResult(startPos);
				query.setMaxResults(limit);
				qaList = query.list();
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return qaList;
	}

	/**
	 * 获取答复列表
	 * 
	 * @param qaTid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAnswerList(int qaTid) {
		List<Object> qaList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("select user.userName, qa.qaContent, qa.qaStatus, "
							+ "qa.qaTime from QA qa, User user where qa.qaUID = user.userId "
							+ "and qa.qaTid = :qaTid order by qa.qaId");
			query.setInteger("qaTid", qaTid);
			qaList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return qaList;
	}

	/**
	 * 获取问题总数
	 * 
	 * @param qaUID
	 * @param search
	 * @return
	 */
	public int countAllQuestionList(int qaUID, String search) {
		int count = 0;
		User user = this.getUserDAO().getUser(qaUID);
		if (user != null) {
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				Query query = null;
				String queryStr = "select count(*) from QA where qaTitle like :search "
						+ "and qaUID = " + qaUID + " and qaStatus < 2";
				if (user.getUserLevel() == 0) {
					queryStr = "select count(*) from QA where qaTitle like :search and qaStatus < 2";
				}
				query = session.createQuery(queryStr);
				query.setString("search", "%" + search + "%");
				count = ((Number) query.uniqueResult()).intValue();
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return count;
	}

	/**
	 * 添加问题
	 * 
	 * @param qaUID
	 * @param qaTitle
	 * @param qaContent
	 * @param qaTime
	 * @return
	 */
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(question);
			session.getTransaction().commit();
			id = question.getQaId();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return id;
	}

	/**
	 * 添加答复
	 * 
	 * @param qaUID
	 * @param qaContent
	 * @param qaTid
	 * @param qaStatus
	 * @param qaTime
	 * @return
	 */
	public int insertAnswer(int qaUID, String qaContent, int qaTid,
			int qaStatus, Date qaTime) {
		int id = 0;
		QA target = this.getQuestion(qaTid);
		target.setQaReply(target.getQaReply() + 1);
		QA question = new QA();
		question.setQaUID(qaUID);
		question.setQaContent(qaContent);
		question.setQaTid(qaTid);
		question.setQaStatus(qaStatus);
		question.setQaTime(qaTime);
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(question);
			session.update(target);
			session.getTransaction().commit();
			id = question.getQaId();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return id;
	}

	/**
	 * 关闭问题
	 * 
	 * @param qaId
	 * @return
	 */
	public boolean closeQuestion(int qaId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("update QA set qaStatus = 0 where qaId = :qaId");
			query.setInteger("qaId", qaId);
			query.executeUpdate();
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
