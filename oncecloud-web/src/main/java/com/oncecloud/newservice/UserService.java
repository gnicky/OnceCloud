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

}
