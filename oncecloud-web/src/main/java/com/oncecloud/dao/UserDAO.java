package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.User;
import com.oncecloud.helper.HashHelper;
import com.oncecloud.helper.SessionHelper;

@Component
public class UserDAO {
	private SessionHelper sessionHelper;
	private HashHelper hashHelper;
	private QuotaDAO quotaDAO;
	private PoolDAO poolDAO;
	private FirewallDAO firewallDAO;
	private ChargeDAO chargeDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private HashHelper getHashHelper() {
		return hashHelper;
	}

	@Autowired
	private void setHashHelper(HashHelper hashHelper) {
		this.hashHelper = hashHelper;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	private PoolDAO getPoolDAO() {
		return poolDAO;
	}

	@Autowired
	private void setPoolDAO(PoolDAO poolDAO) {
		this.poolDAO = poolDAO;
	}

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private ChargeDAO getChargeDAO() {
		return chargeDAO;
	}

	@Autowired
	private void setChargeDAO(ChargeDAO chargeDAO) {
		this.chargeDAO = chargeDAO;
	}

	@SuppressWarnings("unchecked")
	public List<User> getOnePageUserList(int page, int limit, String search) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "from User where userName like '%"
				+ search
				+ "%' and userId != 1 and userStatus = 1 order by userDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<User> userList = query.list();
		session.close();
		return userList;
	}

	public int countAllUserList(String search) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "select count(*) from User where userName like '%"
				+ search + "%' and userId != 1 and userStatus = 1";
		Query query = session.createQuery(queryString);
		return ((Number) query.iterate().next()).intValue();
	}

	@SuppressWarnings("unchecked")
	public User getUser(String userName) {
		Session session = this.getSessionHelper().openMainSession();
		User user = null;
		Query query = session.createQuery("from User where userName = '"
				+ userName + "'");
		List<User> userList = query.list();
		if (userList.size() == 1) {
			user = userList.get(0);
		}
		session.close();
		return user;
	}

	@SuppressWarnings("unchecked")
	public User getUser(int userId) {
		Session session = this.getSessionHelper().openMainSession();
		User user = null;
		Query query = session.createQuery("from User where userId = :userId");
		query.setInteger("userId", userId);
		List<User> userList = query.list();
		if (userList.size() == 1) {
			user = userList.get(0);
		}
		session.close();
		return user;
	}

	public boolean disableUser(int userId) {
		User delUser = getUser(userId);
		boolean result = false;
		if (delUser != null) {
			delUser.setUserStatus(0);
			Session session = null;
			Transaction tx = null;
			try {
				session = this.getSessionHelper().openMainSession();
				tx = session.beginTransaction();
				session.update(delUser);
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
		}
		return result;
	}

	public void insertUser(String userName, String userPass, String userMail,
			String userPhone, String userCompany, int userLevel, Date userDate) {
		Session session = null;
		Transaction tx = null;
		try {
			User user = new User();
			user.setUserName(userName);
			user.setUserPass(this.getHashHelper().md5Hash(userPass));
			user.setUserMail(userMail);
			user.setUserPhone(userPhone);
			user.setUserCompany(userCompany);
			user.setUserLevel(userLevel);
			user.setUserStatus(1);
			user.setUserDate(userDate);
			user.setUserBalance(0.0);
			String allocatePool = this.getPoolDAO().getRandomPool();
			user.setUserAllocate(allocatePool);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(user);
			int userId = user.getUserId();
			this.getQuotaDAO().initQuota(session, userId);
			this.getFirewallDAO().createDefaultFirewall(session, userId);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void updatePwd(String userName, String newPwd) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from User where userName = '"
				+ userName + "'");
		List<User> userList = query.list();
		if (userList.size() == 1) {
			User user = userList.get(0);
			user.setUserPass(this.getHashHelper().md5Hash(newPwd));
			session.update(user);
			tx.commit();
			session.close();
			return;
		}
		session.close();
	}

	public boolean applyVoucher(int userId, int voucher) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			User user = this.getUser(userId);
			if (user.getUserLevel() == 2) {
				user.setUserVoucher(voucher);
				user.setUserLevel(1);
				session.update(user);
				tx.commit();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public boolean confirmVoucher(int userId) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			User user = this.getUser(userId);
			if (user.getUserLevel() == 1) {
				int voucher = user.getUserVoucher();
				user.setUserBalance(user.getUserBalance()
						+ user.getUserVoucher());
				user.setUserVoucher(null);
				Date date = new Date();
				String uuid = UUID.randomUUID().toString();
				session.update(user);
				tx.commit();
				this.getChargeDAO().createChargeRecord(uuid, (double) voucher,
						1, date, userId, 1);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean denyVoucher(int userid) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from User where userId = '" + userid
				+ "'");
		List<User> userList = query.list();
		if (userList.size() == 1) {
			User user = userList.get(0);
			if (user.getUserLevel() == 1) {
				user.setUserVoucher(null);
				user.setUserLevel(2);
				session.update(user);
				tx.commit();
				session.close();
				return true;
			} else {
				session.close();
				return false;
			}
		}
		session.close();
		return false;
	}

	public boolean updateUser(Integer userid, String userName, String userMail,
			String userPhone, String userCompany, int userLevel) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			Query query = session
					.createQuery("update User set userName=:name, userMail=:mail,userPhone=:phone,userCompany=:com,userLevel=:level where userId =:id");
			query.setString("name", userName);
			query.setString("mail", userMail);
			query.setString("phone", userPhone);
			query.setString("com", userCompany);
			query.setInteger("level", userLevel);
			query.setInteger("id", userid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			result = false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean updateBalance(int userid, int bill) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from User where userId = '" + userid
				+ "'");
		List<User> userList = query.list();
		if (userList.size() == 1) {
			User user = userList.get(0);
			if (user.getUserLevel() == 1) {
				user.setUserBalance(user.getUserBalance() + bill);
				session.update(user);
				tx.commit();
				session.close();
				return true;
			} else {
				session.close();
				return false;
			}
		}
		session.close();
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<User> getCompanyUserList(String searchStr) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "from User where userName like '%"
				+ searchStr
				+ "%' and userLevel != 0 and userStatus = 1 order by userDate desc";
		Query query = session.createQuery(queryString);
		List<User> userList = query.list();
		session.close();
		return userList;
	}
}
