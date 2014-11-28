package com.oncecloud.dao.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.entity.Firewall;
import com.oncecloud.entity.Rule;
import com.oncecloud.helper.SessionHelper;

@Component("FirewallDAO")
public class FirewallDAOImpl implements FirewallDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public int countAllFirewallList(String search, int userId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Firewall where firewallUID = :userId "
					+ "and firewallName like :search";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
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

	public int countAllRuleList(String search, String firewallId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) from Rule where ruleFirewall = :firewallId "
					+ "and ruleName like :search order by ruleName";
			Query query = session.createQuery(queryString);
			query.setString("firewallId", firewallId);
			query.setString("search", "%" + search + "%");
			count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}
/*
	public void createDefaultFirewallNoTransaction(Integer userId)
			throws Exception {
		Session session = this.getSessionHelper().getMainSession();
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
*/
	public boolean deleteAllRuleOfFirewall(String firewallId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete Rule where ruleFirewall=:id";
			Query query = session.createQuery(queryString);
			query.setString("id", firewallId);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean deleteFirewall(Integer userId, String firewallId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete Firewall where firewallId=:id";
			Query query = session.createQuery(queryString);
			query.setString("id", firewallId);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean deleteRule(String ruleId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "delete Rule where ruleId= :ruleId";
			Query query = session.createQuery(queryString);
			query.setString("ruleId", ruleId);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	// /获取可用防火墙
	@SuppressWarnings("unchecked")
	public List<Firewall> getabledfirewalls(int uid) {
		List<Firewall> firewallList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Firewall where firewallUID=" + uid
					+ " and isDefault != 2 order by isDefault desc";
			Query query = session.createQuery(queryString);
			firewallList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return firewallList;
	}

	public Firewall getDefaultFirewall(int userId) {
		Firewall firewall = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Firewall where firewallUID = :userId and isDefault = 1";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			firewall = (Firewall) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return firewall;
	}

	public Firewall getFirewall(String firewallId) {
		Firewall firewall = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Firewall where firewallId = :firewallId";
			Query query = session.createQuery(queryString);
			query.setString("firewallId", firewallId);
			firewall = (Firewall) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return firewall;
	}

	@SuppressWarnings("unchecked")
	public List<Firewall> getOnePageFirewallList(int page, int limit,
			String search, int userId) {
		List<Firewall> firewallList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from Firewall where firewallUID = :userId"
					+ " and firewallName like :search and isDefault <2 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			firewallList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return firewallList;
	}

	@SuppressWarnings("unchecked")
	public List<Rule> getOnePageRuleList(int page, int limit, String search,
			String firewallId) {
		List<Rule> ruleList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "";
			queryString = "from Rule where ruleFirewall = :firewallId "
					+ "and ruleName like :search order by ruleName";
			Query query = session.createQuery(queryString);
			query.setString("firewallId", firewallId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			ruleList = query.list();
			session.getTransaction().commit();
			return ruleList;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return ruleList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getRSListOfFirewall(String firewallId) {
		List<Object> rsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString1 = "select vmUuid, vmIP from OCVM where vmFirewall = :firewallId and vmStatus = 1 and vmIP != null";
			Query query1 = session.createQuery(queryString1);
			query1.setString("firewallId", firewallId);
			rsList = query1.list();
			String queryString2 = "select lbUuid, lbIP from LB where firewallUuid = :firewallId and lbStatus = 1";
			Query query2 = session.createQuery(queryString2);
			query2.setString("firewallId", firewallId);
			rsList.addAll(query2.list());
			String queryString3 = "select routerUuid, routerIP from Router where firewallUuid = :firewallId and routerStatus = 1";
			Query query3 = session.createQuery(queryString3);
			query3.setString("firewallId", firewallId);
			rsList.addAll(query3.list());
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rsList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getRSListOfFirewallOnlyInnerRoute(String firewallId) {
		List<Object> rsList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString1 = "select routerUuid, routerIP from Router where innerFirewallUuid = :firewallId and routerStatus = 1";
			Query query1 = session.createQuery(queryString1);
			query1.setString("firewallId", firewallId);
			rsList = query1.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rsList;
	}

	public Rule getRule(String ruleId) {
		Rule rule = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "from Rule where ruleId='" + ruleId + "'";
			Query query = session.createQuery(queryString);
			rule = (Rule) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return rule;
	}

	@SuppressWarnings("unchecked")
	public List<Rule> getRuleList(String firewallId) {
		List<Rule> ruleList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Rule where ruleFirewall= :firewallId and ruleState = 1 order by rulePriority";
			Query query = session.createQuery(queryString);
			query.setString("firewallId", firewallId);
			ruleList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return ruleList;
	}

	public int getRuleSize(String firewallId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "";
			queryString = "select count(*) from Rule where ruleFirewall='"
					+ firewallId + "'";
			Query query = session.createQuery(queryString);
			count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}
/*
	@SuppressWarnings("unchecked")
	public JSONObject getSimpleFWList(int userId) {
		JSONObject jo = new JSONObject();
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
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
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return jo;
	}
*/
	public boolean insertFirewall(String firewallId, String firewallName,
			int firewallUID, Date createDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Firewall firewall = new Firewall(firewallId, firewallName,
					firewallUID, createDate, 1, 0);
			session.save(firewall);
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	// /cyh 插入用于路由器内部的防火墙 默认值状态 为2 ，在防火墙列表中，也不显示出来
	public boolean insertFirewallForinnerRoute(String firewallId,
			String firewallName, int firewallUID, Date createDate) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Firewall firewall = new Firewall(firewallId, firewallName,
					firewallUID, createDate, 1, 2);
			session.save(firewall);
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean insertRule(String ruleId, String ruleName,
			Integer rulePriority, String ruleProtocol, Integer ruleStartPort,
			Integer ruleEndPort, Integer ruleState, String ruleIp,
			String ruleFirewall) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Rule rule = new Rule(ruleId, ruleName, rulePriority, ruleProtocol,
					ruleStartPort, ruleEndPort, ruleState, ruleIp, ruleFirewall);
			session.save(rule);
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean updateConfirm(String firewallId, int isConfirm) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Firewall set isConfirm = :confirm where firewallId = :firewallId";
			Query query = session.createQuery(queryString);
			query.setInteger("confirm", isConfirm);
			query.setString("firewallId", firewallId);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean updateRuleState(String ruleId, int state) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update Rule set ruleState=:state where ruleId=:id";
			Query query = session.createQuery(queryString);
			query.setInteger("state", state);
			query.setString("id", ruleId);
			query.executeUpdate();
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
}
