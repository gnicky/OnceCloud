package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.entity.EIP;
import com.oncecloud.helper.SessionHelper;

@Component("EIPDAO")
public class EIPDAOImpl implements EIPDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public int abandonEip(String eipIp, int userId) {
		Session session = null;
		int bandwith = -1;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class).add(
					Restrictions.eq("eipIp", eipIp));
			EIP eip = (EIP) criteria.uniqueResult();
			if (eip != null && eip.getEipUID() == userId) {
				bandwith = eip.getEipBandwidth();
				eip.setEipBandwidth(null);
				eip.setEipDependency(null);
				eip.setEipDescription(null);
				eip.setEipName(null);
				eip.setEipUID(null);
				session.update(eip);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return bandwith;
	}
/*
	public boolean addEIP(String prefix, int start, int end, Date date,
			int eiptype, String eipInterface) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			for (int i = start; i <= end; i++) {
				String currentIp = prefix + i;
				boolean check = this.ipExist(session, currentIp);
				if (check == false) {
					EIP eip = new EIP();
					eip.setEipIp(currentIp);
					eip.setEipType(eiptype);
					eip.setEipUuid(UUID.randomUUID().toString());
					eip.setEipInterface(eipInterface);
					eip.setCreateDate(date);
					session.save(eip);
					this.getOverViewDAO().updateOverViewfieldNoTransaction(
							"viewOutip", true);
					result = true;
				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
*/
	@SuppressWarnings({ "unchecked" })
	public synchronized EIP applyEip(String eipName, int userId,
			int eipBandwidth, Date createDate, String eipUuid) {
		EIP eip = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class)
					.add(Restrictions.isNull("eipUID")).setFirstResult(0)
					.setMaxResults(1);
			List<EIP> eipList = criteria.list();
			if (eipList.size() > 0) {
				eip = eipList.get(0);
				eip.setEipName(eipName);
				eip.setEipUuid(eipUuid);
				eip.setEipUID(userId);
				eip.setEipBandwidth(eipBandwidth);
				eip.setCreateDate(createDate);
				session.update(eip);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eip;
	}

	public boolean bindEip(String eipIp, String dependencyUuid, int type) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			EIP eip = this.doGetEip(session, eipIp);
			if (eip != null) {
				eip.setEipDependency(dependencyUuid);
				eip.setDepenType(type);
				session.update(eip);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean changeBandwidth(int userId, EIP eipObj, int size) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			eipObj.setEipBandwidth(size);
			session.update(eipObj);
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

	public int countAllEipList(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(EIP.class)
					.add(Restrictions.eq("eipUID", userId))
					.add(Restrictions.like("eipName", search,
							MatchMode.ANYWHERE))
					.setProjection(Projections.rowCount());
			count = ((Number) criteria.uniqueResult()).intValue();
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
	 * @author hty
	 * @param search
	 * @param uid
	 * @return
	 */
	public int countAllEipListAlarm(String search, int eipUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(EIP.class)
					.add(Restrictions.eq("eipUID", eipUID))
					.add(Restrictions.like("eipName", search,
							MatchMode.ANYWHERE))
					.add(Restrictions.isNull("alarmUuid"))
					.setProjection(Projections.rowCount());
			count = ((Number) criteria.uniqueResult()).intValue();
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
	public int countAllEipListNoUserid(String searchStr) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(EIP.class)
					.add(Restrictions.like("eipIp", searchStr,
							MatchMode.ANYWHERE))
					.setProjection(Projections.rowCount());
			count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	public boolean deleteEIP(String ip, String uuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class)
					.add(Restrictions.eq("eipIp", ip))
					.add(Restrictions.eq("eipUuid", uuid));
			EIP eip = (EIP) criteria.uniqueResult();
			if (eip != null) {
				session.delete(eip);
				this.getOverViewDAO().updateOverViewfieldNoTransaction(
						"viewOutip", false);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
*/
	private EIP doGetEip(Session session, String eipIp) {
		EIP eip;
		Criteria criteria = session.createCriteria(EIP.class).add(
				Restrictions.eq("eipIp", eipIp));
		eip = (EIP) criteria.uniqueResult();
		return eip;
	}

	// 获取可用公网IP
	@SuppressWarnings("unchecked")
	public List<EIP> getableeips(int uid) {
		List<EIP> eipList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class)
					.add(Restrictions.isNull("eipDependency"))
					.add(Restrictions.eq("eipUID", uid));
			eipList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipList;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EIP> getAllListAlarm(int eipUID, String alarmUuid) {
		List<EIP> eipList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class)
					.add(Restrictions.eq("eipUID", eipUID))
					.add(Restrictions.eq("alarmUuid", alarmUuid))
					.addOrder(Order.desc("createDate"));
			eipList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipList;
	}

	public EIP getEip(String eipIp) {
		EIP eip = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			eip = this.doGetEip(session, eipIp);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eip;
	}

	public String getEipId(String eip) {
		String eipId = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class).add(
					Restrictions.eq("eipIp", eip));
			EIP ip = (EIP) criteria.uniqueResult();
			if (ip != null) {
				eipId = ip.getEipUuid();
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipId;
	}

	public String getEipIp(String dependencyUuid) {
		String eipIp = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class).add(
					Restrictions.eq("eipDependency", dependencyUuid));
			EIP eip = (EIP) criteria.uniqueResult();
			if (eip != null) {
				eipIp = eip.getEipIp();
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipIp;
	}

	@SuppressWarnings("unchecked")
	public List<EIP> getOnePageEipList(int userId, int page, int limit,
			String search) {
		List<EIP> eipList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(EIP.class)
					.add(Restrictions.eq("eipUID", userId))
					.add(Restrictions.like("eipName", search,
							MatchMode.ANYWHERE))
					.addOrder(Order.desc("createDate"))
					.setFirstResult(startPos).setMaxResults(limit);
			eipList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipList;
	}

	/**
	 * @author hty
	 * @param page
	 * @param limit
	 * @param search
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EIP> getOnePageEipListAlarm(int page, int limit, String search,
			int eipUID) {
		List<EIP> eipList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(EIP.class)
					.add(Restrictions.eq("eipUID", eipUID))
					.add(Restrictions.like("eipName", search,
							MatchMode.ANYWHERE))
					.add(Restrictions.isNull("alarmUuid"))
					.addOrder(Order.desc("createDate"))
					.setFirstResult(startPos).setMaxResults(limit);
			eipList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipList;
	}
/*
	@SuppressWarnings("unchecked")
	public List<EIP> getOnePageEIPListNoUserid(int page, int limit,
			String searchStr) {
		List<EIP> eipList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(EIP.class)
					.add(Restrictions.like("eipIp", searchStr,
							MatchMode.ANYWHERE))
					.addOrder(Order.desc("eipDependency"))
					.addOrder(Order.desc("eipUID")).setFirstResult(startPos)
					.setMaxResults(limit);
			eipList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return eipList;
	}

	public boolean ipExist(Session session, String eIp) {
		Criteria criteria = session.createCriteria(EIP.class).add(
				Restrictions.eq("eipIp", eIp));
		return (criteria.uniqueResult() != null);
	}

	*//**
	 * @author hty
	 * @param alarmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isNotExistAlarm(String alarmUuid) {
		boolean result = true;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class).add(
					Restrictions.eq("alarmUuid", alarmUuid));
			List<EIP> eipList = criteria.list();
			if (eipList.size() > 0) {
				result = false;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean unBindEip(String eipIp) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			EIP eip = this.doGetEip(session, eipIp);
			if (eip != null) {
				eip.setEipDependency(null);
				eip.setDepenType(null);
				session.update(eip);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	/**
	 * @author hty
	 * @param eipip
	 * @param alarmUuid
	 */
	public boolean updateAlarm(String eipUuid, String alarmUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class).add(
					Restrictions.eq("eipUuid", eipUuid));
			EIP eip = (EIP) criteria.uniqueResult();
			if (eip != null) {
				eip.setAlarmUuid(alarmUuid);
				session.update(eip);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	/**
	 * @param eipuuid
	 * @param newName
	 * @param description
	 * @author xpx 2014-7-8
	 *//*
	public boolean updateName(String eipip, String newName, String description) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(EIP.class).add(
					Restrictions.eq("eipIp", eipip));
			EIP eip = (EIP) criteria.uniqueResult();
			if (eip != null) {
				eip.setEipName(newName);
				eip.setEipDescription(description);
				session.update(eip);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}*/
}
