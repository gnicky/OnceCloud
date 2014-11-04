package com.oncecloud.common.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.common.helper.HashHelper;
import com.oncecloud.common.helper.SessionHelper;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.User;

@Component("UserDAO")
public class UserDAOImpl implements UserDAO {
	private SessionHelper sessionHelper;
	private HashHelper hashHelper;

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

	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public User getUser(int userId) {
		User user = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from User where userId = :userId");
			query.setInteger("userId", userId);
			user = (User) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return user;
	}

	public User getUserNoTransactional(int userId) {
		User user = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			Query query = session
					.createQuery("from User where userId = :userId");
			query.setInteger("userId", userId);
			user = (User) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return user;
	}

	/**
	 * 获取用户（通过用户名）
	 * 
	 * @param userName
	 * @return
	 */
	public User getUser(String userName) {
		User user = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from User where userName = :userName and userStatus > 0");
			query.setString("userName", userName);
			user = (User) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return user;
	}

	/**
	 * 获取一页用户列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getOnePageUserList(int page, int limit, String search) {
		List<User> userList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from User where userName like :search and userId != 1 and userStatus = 1 order by userDate desc";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			userList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return userList;
	}

	/**
	 * 获取全部用户列表
	 * 
	 * @param searchStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getCompanyUserList(String search) {
		List<User> userList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from User where userName like :search "
					+ "and userLevel != 0 and userStatus = 1 order by userDate desc";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			userList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return userList;
	}

	/**
	 * 获取用户总数
	 * 
	 * @param search
	 * @return
	 */
	public int countAllUserList(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from User where userName like :search and userId != 1 and userStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("search", "%" + search + "%");
			count = ((Number) query.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * 添加用户
	 * 
	 * @param userName
	 * @param userPass
	 * @param userMail
	 * @param userPhone
	 * @param userCompany
	 * @param userLevel
	 * @param userDate
	 */
	public void insertUser(String userName, String userPass, String userMail,
			String userPhone, String userCompany, int userLevel, Date userDate, String poolUuid) {
		Session session = null;
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
			if (poolUuid.equals("0")){
				user.setUserAllocate(this.getPoolDAO().getRandomPool());
			} else {
				user.setUserAllocate(poolUuid);
			}
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(user);
			int userId = user.getUserId();
			this.getQuotaDAO().initQuotaNoTransaction(userId);
			this.getFirewallDAO().createDefaultFirewallNoTransaction(userId);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 禁用用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean disableUser(int userId) {
		boolean result = false;
		User delUser = getUser(userId);
		if (delUser != null) {
			delUser.setUserStatus(0);
			Session session = null;
			try {
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(delUser);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	/**
	 * 申请代金券
	 * 
	 * @param userId
	 * @param voucher
	 * @return
	 */
	public boolean applyVoucher(int userId, int voucher) {
		boolean result = false;
		User user = this.getUser(userId);
		if (user != null && user.getUserLevel() == 2) {
			Session session = null;
			try {
				user.setUserVoucher(voucher);
				user.setUserLevel(1);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(user);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	/**
	 * 确认代金券
	 * 
	 * @param userId
	 * @return
	 */
	public boolean confirmVoucher(int userId) {
		boolean result = false;
		User user = this.getUser(userId);
		if (user.getUserLevel() == 1) {
			Session session = null;
			try {
				int voucher = user.getUserVoucher();
				user.setUserBalance(user.getUserBalance()
						+ user.getUserVoucher());
				user.setUserVoucher(null);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				Date date = new Date();
				String uuid = UUID.randomUUID().toString();
				session.update(user);
				session.getTransaction().commit();
				this.getChargeDAO().createChargeRecord(uuid, (double) voucher,
						1, date, userId, 1);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	/**
	 * 拒绝代金券
	 * 
	 * @param userid
	 * @return
	 */
	public boolean denyVoucher(int userId) {
		boolean result = false;
		User user = this.getUser(userId);
		if (user != null && user.getUserLevel() == 1) {
			Session session = null;
			try {
				user.setUserVoucher(null);
				user.setUserLevel(2);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(user);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	/**
	 * 更新用户
	 * 
	 * @param userid
	 * @param userName
	 * @param userMail
	 * @param userPhone
	 * @param userCompany
	 * @param userLevel
	 * @return
	 */
	public boolean updateUser(Integer userid, String userName, String userMail,
			String userPhone, String userCompany, int userLevel) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("update User set userName= :name, userMail= :mail, "
							+ "userPhone = :phone, userCompany = :com, userLevel = :level "
							+ "where userId = :id");
			query.setString("name", userName);
			query.setString("mail", userMail);
			query.setString("phone", userPhone);
			query.setString("com", userCompany);
			query.setInteger("level", userLevel);
			query.setInteger("id", userid);
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

	/**
	 * 更新用户余额
	 * 
	 * @param userid
	 * @param bill
	 * @return
	 */
	public boolean updateBalance(int userId, double bill) {
		boolean result = false;
		User user = this.getUser(userId);
		if (user != null) {
			Session session = null;
			try {
				user.setUserBalance(user.getUserBalance() + bill);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(user);
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}
	
	public List<User> getUserList() {
		List<User> list = new ArrayList<User>();
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryStr = "from User";
			Query query = session.createQuery(queryStr);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return list;
	}

}
