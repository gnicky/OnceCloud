package com.oncecloud.dao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Firewall;
import com.oncecloud.entity.Rule;
import com.oncecloud.helper.SessionHelper;

@Component
public class FirewallDAO {
	private SessionHelper sessionHelper;
	private QuotaDAO quotaDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public void insertFirewall(String firewallId, String firewallName,
			int firewallUID, Date createDate) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			Firewall firewall = new Firewall(firewallId, firewallName,
					firewallUID, createDate, 1, 0);
			session.save(firewall);
			this.getQuotaDAO().updateQuotaField(session, firewallUID,
					"quotaFirewall", 1, true);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void insertRule(String ruleId, String ruleName,
			Integer rulePriority, String ruleProtocol, Integer ruleStartPort,
			Integer ruleEndPort, Integer ruleState, String ruleIp,
			String ruleFirewall) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		Rule rule = new Rule(ruleId, ruleName, rulePriority, ruleProtocol,
				ruleStartPort, ruleEndPort, ruleState, ruleIp, ruleFirewall);
		session.save(rule);
		tx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<Firewall> getOnePageFirewallList(int page, int limit,
			String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "";
		queryString = "from Firewall where firewallUID=" + uid
				+ " and firewallName like '%" + search
				+ "%' order by createDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<Firewall> firewallList = query.list();
		session.close();
		return firewallList;
	}

	public int countAllFirewallList(String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from Firewall where firewallUID=" + uid
				+ " and firewallName like '%" + search + "%'";
		Query query = session.createQuery(queryString);
		int count = ((Number) query.iterate().next()).intValue();
		session.close();
		return count;
	}

	public void updateConfirm(String firewallId, int isConfirm) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update Firewall set isConfirm=:confirm where firewallId=:id";
		Query query = session.createQuery(queryString);
		query.setInteger("confirm", isConfirm);
		query.setString("id", firewallId);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<Rule> getOnePageRuleList(int page, int limit, String search,
			String firewallId) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "";
		queryString = "from Rule where ruleFirewall='" + firewallId
				+ "' and ruleName like '%" + search + "%' order by ruleName";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<Rule> ruleList = query.list();
		session.close();
		return ruleList;
	}

	public int countAllRuleList(String search, String firewallId) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from Rule where ruleFirewall='"
				+ firewallId + "' and ruleName like '%" + search
				+ "%' order by ruleName";
		Query query = session.createQuery(queryString);
		int count = ((Number) query.iterate().next()).intValue();
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public Firewall getFirewall(String firewallId) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "from Firewall where firewallId='" + firewallId + "'";
		Query query = session.createQuery(queryString);
		List<Firewall> firewallList = query.list();
		session.close();
		return firewallList.get(0);
	}

	public void deleteRule(String ruleId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete Rule where ruleId= :ruleId";
			Query query = session.createQuery(queryString);
			query.setString("ruleId", ruleId);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void deleteAllRuleOfFirewall(String firewallId) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "delete Rule where ruleFirewall=:id";
		Query query = session.createQuery(queryString);
		query.setString("id", firewallId);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateRuleState(String ruleId, int state) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update Rule set ruleState=:state where ruleId=:id";
		Query query = session.createQuery(queryString);
		query.setInteger("state", state);
		query.setString("id", ruleId);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getRSListOfFirewall(String firewallId) {
		List<Object> rsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select vmUuid, vmIP from OCVM where vmFirewall = :firewallId and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("firewallId", firewallId);
			rsList = query.list();
			String queryString1 = "select lbUuid, lbIP from LB where firewallUuid = :firewallId and lbStatus = 1";
			Query query1 = session.createQuery(queryString1);
			query1.setString("firewallId", firewallId);
			rsList.addAll(query1.list());
			String queryString3 = "select routerUuid, routerIP from Router where firewallUuid = :firewallId and routerStatus = 1";
			Query query3 = session.createQuery(queryString3);
			query3.setString("firewallId", firewallId);
			rsList.addAll(query3.list());
			String queryString2 = "select databaseUuid, databaseIp from Database where databaseUuid = :firewallId and databaseStatus = 1";
			Query query2 = session.createQuery(queryString2);
			query2.setString("firewallId", firewallId);
			rsList.addAll(query2.list());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return rsList;
	}

	@SuppressWarnings("unchecked")
	public List<Rule> getRuleList(String firewallId) {
		List<Rule> ruleList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Rule where ruleFirewall= :firewallId and ruleState = 1 order by rulePriority";
			Query query = session.createQuery(queryString);
			query.setString("firewallId", firewallId);
			ruleList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return ruleList;
	}

	public void deleteFirewall(Integer userId, String firewallId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete Firewall where firewallId=:id";
			Query query = session.createQuery(queryString);
			query.setString("id", firewallId);
			query.executeUpdate();
			this.getQuotaDAO().updateQuotaField(session, userId,
					"quotaFirewall", 1, false);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Rule getRule(String ruleId) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "from Rule where ruleId='" + ruleId + "'";
		Query query = session.createQuery(queryString);
		List<Rule> ruleList = query.list();
		session.close();
		return ruleList.get(0);
	}

	public int getRuleSize(String firewallId) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from Rule where ruleFirewall='"
				+ firewallId + "'";
		Query query = session.createQuery(queryString);
		int count = ((Number) query.iterate().next()).intValue();
		session.close();
		return count;
	}

	public void createDefaultFirewall(Session session, Integer userId)
			throws Exception {
		if (session == null || !session.isOpen()) {
			throw new Exception("Session not open");
		}
		String firewallUuid = UUID.randomUUID().toString();
		String ruleIcmpId = UUID.randomUUID().toString();
		String rule22Id = UUID.randomUUID().toString();
		String rule3389Id = UUID.randomUUID().toString();
		Firewall fw = new Firewall(firewallUuid, "缺省防火墙", userId, new Date(),
				1, 1);
		session.save(fw);
		Rule ruleIcmp = new Rule(ruleIcmpId, "", 1, "ICMP", null, null, 1, "",
				firewallUuid);
		Rule rule22 = new Rule(rule22Id, "", 2, "TCP", 22, 22, 1, "",
				firewallUuid);
		Rule rule3389 = new Rule(rule3389Id, "", 3, "TCP", 3389, 3389, 1, "",
				firewallUuid);
		session.save(ruleIcmp);
		session.save(rule22);
		session.save(rule3389);
	}

	@SuppressWarnings("unchecked")
	public Firewall getDefaultFirewall(int userId) {
		Firewall firewall = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Firewall where firewallUID = :userId and isDefault = 1";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			List<Firewall> firewallList = query.list();
			firewall = firewallList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return firewall;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getSimpleFWList(int userId) {
		JSONObject jo = new JSONObject();
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select firewallId, firewallName from Firewall where firewallUID=:userid order by isDefault desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userid", userId);
			List<Object[]> resultList = query.list();
			JSONArray array = new JSONArray();
			JSONObject defaultItem = new JSONObject();
			defaultItem.put("uuid", (String) resultList.get(0)[0]);
			defaultItem.put("rtname",
					URLEncoder.encode((String) resultList.get(0)[1], "utf-8")
							.replace("+", "%20"));
			jo.put("first", defaultItem);

			for (int i = 1; i < resultList.size(); i++) {
				JSONObject itemjo = new JSONObject();
				itemjo.put("uuid", (String) resultList.get(i)[0]);
				itemjo.put(
						"rtname",
						URLEncoder.encode((String) resultList.get(i)[1],
								"utf-8").replace("+", "%20"));
				array.put(itemjo);
			}
			jo.put("list", array);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return jo;
	}

	// /获取可用防火墙
	@SuppressWarnings("unchecked")
	public List<Firewall> getabledfirewalls(int uid) {
		List<Firewall> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Firewall where firewallUID=" + uid
					+ " order by isDefault desc";
			Query query = session.createQuery(queryString);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}
