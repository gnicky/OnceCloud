package com.oncecloud.newservice;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.helper.HashHelper;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.newentity.Firewall;
import com.oncecloud.newentity.Pool;
import com.oncecloud.newentity.Quota;
import com.oncecloud.newentity.QuotaType;
import com.oncecloud.newentity.Rule;
import com.oncecloud.newentity.Status;
import com.oncecloud.newentity.User;
import com.oncecloud.newentity.UserLevel;

@Component
public class UserService {
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

	public void initPlatform() {
		this.register("admin", "onceas", "admin@beyondcent.com", "12345678901",
				"BeyondCent", UserLevel.ADMINISTRATOR);
	}

	public boolean checkLogin(String userName, String password) {
		Session session = null;
		try {
			session = this.getSessionHelper().getTestSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("userName", userName));
			criteria.add(Restrictions.eq("password", this.getHashHelper()
					.md5Hash(password)));
			criteria.add(Restrictions.eq("status", Status.NORMAL));
			User user = (User) criteria.uniqueResult();
			session.getTransaction().commit();
			return (user != null);
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public User register(String userName, String password, String email,
			String telephone, String company, UserLevel level) {
		Session session = null;
		try {
			session = this.getSessionHelper().getTestSession();
			session.beginTransaction();
			User user = this.doCreateUser(session, userName, password, email,
					telephone, company, level);
			this.initQuotaForUser(session, user);
			this.initFirewallForUser(session, user);
			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	private void initFirewallForUser(Session session, User user) {
		Date createDate = new Date();
		Firewall initialFirewall = new Firewall(UUID.randomUUID(), "缺省防火墙",
				user, createDate, true, true);
		session.save(initialFirewall);

		Rule icmpRule = new Rule(UUID.randomUUID(), "", 1, "ICMP", null, null,
				Status.NORMAL, "", initialFirewall);
		session.save(icmpRule);

		Rule sshRule = new Rule(UUID.randomUUID(), "", 2, "TCP", 22, 22,
				Status.NORMAL, "", initialFirewall);
		session.save(sshRule);

		Rule rdpRule = new Rule(UUID.randomUUID(), "", 3, "TCP", 3389, 3389,
				Status.NORMAL, "", initialFirewall);
		session.save(rdpRule);
	}

	private void initQuotaForUser(Session session, User user) {
		Quota total = null;
		Quota used = new Quota(user, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				QuotaType.USED);
		if (user.getLevel() == UserLevel.ADMINISTRATOR) {
			int maxValue = 1000;
			total = new Quota(user, maxValue, maxValue, maxValue, maxValue,
					maxValue, maxValue, maxValue, maxValue, maxValue, maxValue,
					maxValue, maxValue, maxValue, maxValue, QuotaType.TOTAL);
		} else {
			total = new Quota(user, 2, 5, 5, 5, 10, 100, 10, 10, 2, 5, 3, 10,
					20, 10, QuotaType.TOTAL);
		}
		session.save(total);
		session.save(used);
	}

	private User doCreateUser(Session session, String userName,
			String password, String email, String telephone, String company,
			UserLevel level) {
		Status status = Status.NORMAL;
		Date createDate = new Date();
		Pool pool = this.getRandomPoolForUser(session);
		System.out.println(pool);
		Double balance = 0.0;
		Integer voucher = null;
		User user = new User(userName, this.getHashHelper().md5Hash(password),
				email, telephone, company, level, status, createDate, pool,
				balance, voucher);
		session.save(user);
		return user;
	}

	@SuppressWarnings("unchecked")
	private Pool getRandomPoolForUser(Session session) {
		Criteria criteria = session.createCriteria(Pool.class);
		criteria.add(Restrictions.eq("status", Status.NORMAL));
		List<Pool> poolList = criteria.list();
		Pool pool = null;
		if (poolList.size() > 0) {
			int randomIndex = new Random().nextInt(poolList.size());
			pool = poolList.get(randomIndex);
		}
		return pool;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserList(int page, int limit, String search) {
		Session session = null;
		try {
			session = this.getSessionHelper().getTestSession();
			session.beginTransaction();
			int firstResult = (page - 1) * limit;
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.like("userName", search,
					MatchMode.ANYWHERE));
			// Skip super administrator
			criteria.add(Restrictions.ne("id", 1));
			criteria.add(Restrictions.eq("status", Status.NORMAL));
			criteria.addOrder(Order.desc("createDate"));
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(limit);
			List<User> userList = criteria.list();
			session.getTransaction().commit();
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public double getBalance(int userId) {
		Session session = null;
		try {
			session = this.getSessionHelper().getTestSession();
			session.beginTransaction();
			User user = (User) session.get(User.class, userId);
			session.getTransaction().commit();
			if (user == null) {
				return 0;
			}
			return user.getBalance();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}
	//
	// private boolean userUpdate(Integer userid, String userName,
	// String userMail, String userPhone, String userCompany, String uLevel)
	// {
	// try {
	// if (userName == null || null == userid) {
	// logger.error("User Update: User [" + userName
	// + "] Failed: Null Exception");
	// return false;
	// }
	// int userLevel = Integer.valueOf(uLevel);
	// User user = this.getUserDAO().getUser(userid);
	// if (user == null) {
	// logger.error("User Update: User- [" + userid
	// + "] Failed: Not Exist");
	// return false;
	// }
	// boolean result = this.getUserDAO().updateUser(userid, userName,
	// userMail, userPhone, userCompany, userLevel);
	// if (!result) {
	// logger.error("User Update: User- [" + userid
	// + "] Failed: Update Failed");
	// return false;
	// }
	// logger.info("User Update To: User [" + userid + "] UserName ["
	// + userName + "] Mail [" + userMail + "] Telephone ["
	// + userPhone + "] Company [" + userCompany + "] UserLevel ["
	// + userLevel + "] Successful");
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
	//
	// public JSONArray doRegister(String userName, String userPassword,
	// String userEmail, String userTelephone)
	// throws UnsupportedEncodingException {
	// JSONArray ja = new JSONArray();
	// User result = this.userRegister(userName, userPassword, userEmail,
	// userTelephone, "", "2");
	// if (result != null) {
	// JSONObject tObj = new JSONObject();
	// tObj.put("username",
	// URLEncoder.encode(result.getUserName(), "utf-8"));
	// tObj.put("userid", result.getUserId());
	// tObj.put("usercom",
	// URLEncoder.encode(result.getUserCompany(), "utf-8"));
	// tObj.put("userdate", Utilities.formatTime(result.getUserDate()));
	// tObj.put("userlevel", result.getUserLevel());
	// tObj.put("usermail", result.getUserMail());
	// tObj.put("userphone", result.getUserPhone());
	// ja.put(tObj);
	// }
	// return ja;
	// }
	//
	// public JSONArray doQueryUser(String userName) {
	// JSONArray ja = new JSONArray();
	// User query = this.getUserDAO().getUser(userName);
	// JSONObject tObj = new JSONObject();
	// if (query != null) {
	// tObj.put("exist", true);
	// } else {
	// tObj.put("exist", false);
	// }
	// ja.put(tObj);
	// return ja;
	// }
	//
	// public JSONArray doCreateUser(String userName, String userPassword,
	// String userEmail, String userTelephone, String userCompany,
	// String userLevel, int userid) {
	// JSONArray ja = new JSONArray();
	// Date startTime = new Date();
	// User result = this.userRegister(userName, userPassword, userEmail,
	// userTelephone, userCompany, userLevel);
	// if (result != null) {
	// JSONObject tObj = new JSONObject();
	// tObj.put("username", Utilities.encodeText(result.getUserName()));
	// tObj.put("userid", result.getUserId());
	// tObj.put("usercom", Utilities.encodeText(result.getUserCompany()));
	// tObj.put("userdate", Utilities.formatTime(result.getUserDate()));
	// tObj.put("userlevel", result.getUserLevel());
	// tObj.put("usermail", result.getUserMail());
	// tObj.put("userphone", result.getUserPhone());
	// ja.put(tObj);
	// }
	// // write log and push message
	// Date endTime = new Date();
	// int elapse = Utilities.timeElapse(startTime, endTime);
	// JSONArray infoArray = new JSONArray();
	// infoArray.put(Utilities.createLogInfo(
	// LogConstant.logObject.用户.toString(), userName));
	// if (result != null) {
	// OCLog log = this.getLogDAO().insertLog(userid,
	// LogConstant.logObject.用户.ordinal(),
	// LogConstant.logAction.创建.ordinal(),
	// LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
	// startTime, elapse);
	// this.getMessagePush().pushMessage(userid,
	// Utilities.stickyToSuccess(log.toString()));
	// } else {
	// OCLog log = this.getLogDAO().insertLog(userid,
	// LogConstant.logObject.用户.ordinal(),
	// LogConstant.logAction.创建.ordinal(),
	// LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
	// startTime, elapse);
	// this.getMessagePush().pushMessage(userid,
	// Utilities.stickyToError(log.toString()));
	// }
	// return ja;
	// }
	//
	// public void doQuotaUpdate(int quotaid, int changerId, int eip, int
	// vm,
	// int bk, int img, int vol, int ssh, int fw, int rt, int vlan,
	// int lb, int disk, int bw, int mem, int cpu, int userId) {
	// Quota newQuota = new Quota(changerId, eip, vm, bk, img, vol, disk,
	// ssh,
	// fw, rt, vlan, lb, bw, mem, cpu, 0);
	// newQuota.setQuotaID(quotaid);
	// boolean result = this.getQuotaDAO().updateQuota(newQuota);
	// if (result) {
	// this.getMessagePush().pushMessage(userId,
	// Utilities.stickyToSuccess("资源配额更新成功"));
	// } else {
	// this.getMessagePush().pushMessage(userId,
	// Utilities.stickyToError("资源配额更新失败"));
	// }
	// }
	//
	// public JSONObject doGetUserQuota(int userId) {
	// Quota quotaU = this.getQuotaDAO().getQuotaUsed(userId);
	// Quota quotaT = this.getQuotaDAO().getQuotaTotal(userId);
	// JSONObject obj = new JSONObject();
	// obj.put("quotaU", new JSONObject(quotaU));
	// obj.put("quotaT", new JSONObject(quotaT));
	// return obj;
	// }
	//
	// public JSONObject doGetOneUser(int userId) {
	// User myuser = this.getUserDAO().getUser(userId);
	// JSONObject obj = new JSONObject();
	// obj.put("username", Utilities.encodeText(myuser.getUserName()));
	// obj.put("userid", myuser.getUserId());
	// obj.put("usercom", Utilities.encodeText(myuser.getUserCompany()));
	// obj.put("userdate", Utilities.formatTime(myuser.getUserDate()));
	// obj.put("userlevel", myuser.getUserLevel());
	// obj.put("usermail", myuser.getUserMail());
	// obj.put("userphone", myuser.getUserPhone());
	// obj.put("balance", myuser.getUserBalance());
	// return obj;
	// }
	//
	// public JSONArray doGetCompanyList() {
	// JSONArray ja = new JSONArray();
	// String searchStr = "";
	// List<User> userList =
	// this.getUserDAO().getCompanyUserList(searchStr);
	// for (User myuser : userList) {
	// JSONObject obj = new JSONObject();
	// obj.put("username", Utilities.encodeText(myuser.getUserName()));
	// obj.put("userid", myuser.getUserId());
	// obj.put("usercom", Utilities.encodeText(myuser.getUserCompany()));
	// obj.put("userlevel", myuser.getUserLevel());
	// obj.put("usermail", myuser.getUserMail());
	// obj.put("userphone", myuser.getUserPhone());
	// Quota quota = this.getQuotaDAO().getQuotaUsed(myuser.getUserId());
	// obj.put("usedVM", quota.getQuotaVM());
	// obj.put("usedDiskN", quota.getQuotaDiskN());
	// obj.put("usedDiskS", quota.getQuotaDiskS());
	// obj.put("usedIP", quota.getQuotaIP());
	// obj.put("usedBandwidth", quota.getQuotaBandwidth());
	// obj.put("usedCpu", quota.getQuotaCpu());
	// obj.put("usedQuotaMemory", quota.getQuotaMemory());
	// if (myuser.getUserVoucher() == null) {
	// obj.put("uservoucher", 0);
	// } else {
	// obj.put("uservoucher", myuser.getUserVoucher());
	// }
	// ja.put(obj);
	// }
	// return ja;
	// }
	//
	// public void doUpdateUser(int userId, int changeId, String userName,
	// String userEmail, String userTel, String userCom, String userLevel) {
	// boolean result = this.userUpdate(changeId, userName, userEmail,
	// userTel, userCom, userLevel);
	// if (result) {
	// this.getMessagePush().pushMessage(userId,
	// Utilities.stickyToSuccess("用户信息修改成功"));
	// } else {
	// this.getMessagePush().pushMessage(userId,
	// Utilities.stickyToError("用户信息修改失败"));
	// }
	// }
	//
	// public JSONObject doDeleteUser(int userId, int changeId, String
	// userName) {
	// JSONObject jo = new JSONObject();
	// Date startTime = new Date();
	// boolean result = this.getUserDAO().disableUser(changeId);
	// jo.put("result", result);
	// // write log and push message
	// Date endTime = new Date();
	// int elapse = Utilities.timeElapse(startTime, endTime);
	// JSONArray infoArray = new JSONArray();
	// infoArray.put(Utilities.createLogInfo(
	// LogConstant.logObject.用户.toString(), userName));
	// if (result) {
	// OCLog log = this.getLogDAO().insertLog(userId,
	// LogConstant.logObject.用户.ordinal(),
	// LogConstant.logAction.删除.ordinal(),
	// LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
	// startTime, elapse);
	// this.getMessagePush().pushMessage(userId,
	// Utilities.stickyToSuccess(log.toString()));
	// } else {
	// OCLog log = this.getLogDAO().insertLog(userId,
	// LogConstant.logObject.用户.ordinal(),
	// LogConstant.logAction.删除.ordinal(),
	// LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
	// startTime, elapse);
	// this.getMessagePush().pushMessage(userId,
	// Utilities.stickyToError(log.toString()));
	// }
	// return jo;
	// }
}
