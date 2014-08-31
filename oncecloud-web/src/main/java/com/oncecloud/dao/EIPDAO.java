package com.oncecloud.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.EIP;
import com.oncecloud.helper.SessionHelper;

@Component
public class EIPDAO {
	private SessionHelper sessionHelper;
	private QuotaDAO quotaDAO;
	private OverViewDAO overViewDAO;

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

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	@SuppressWarnings({ "unchecked" })
	public synchronized EIP applyEip(String eipName, int userId,
			int eipBandwidth, Date createDate, String eipUuid) {
		EIP eip = null;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from EIP where eipUID = null");
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<EIP> eipList = query.list();
			eip = eipList.get(0);
			eip.setEipName(eipName);
			eip.setEipUuid(eipUuid);
			eip.setEipUID(userId);
			eip.setEipBandwidth(eipBandwidth);
			eip.setCreateDate(createDate);
			session.update(eip);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaIP", 1,
					true);
			this.getQuotaDAO().updateQuotaField(session, userId,
					"quotaBandwidth", eipBandwidth, true);
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
		return eip;
	}

	@SuppressWarnings("unchecked")
	public List<EIP> getOnePageEipList(int userId, int page, int limit,
			String search) {
		List<EIP> eipList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from EIP where eipUID = :userId and eipName like '%:search%' order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", search);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			eipList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
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
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "";
		queryString = "from EIP where eipUID=:eipUID and eipName like '%"
				+ search + "%' and alarmUuid is null order by createDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		query.setInteger("eipUID", eipUID);
		List<EIP> eipList = query.list();
		session.close();
		return eipList;
	}

	public int countAllEipList(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "";
			queryString = "select count(*) from EIP where eipUID = :userId and eipName like '%:search%'";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", search);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
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
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from EIP where eipUID=:eipUID and eipName like '%"
				+ search + "%' and alarmUuid is null ";
		Query query = session.createQuery(queryString);
		query.setInteger("eipUID", eipUID);
		return ((Number) query.iterate().next()).intValue();
	}

	@SuppressWarnings("unchecked")
	public void abandonEip(String eipIp, int userId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from EIP where eipIp = :ip");
			query.setString("ip", eipIp);
			List<EIP> eipList = query.list();
			EIP eip = eipList.get(0);
			if (eip.getEipUID() == userId) {
				int bandwith = eip.getEipBandwidth();
				eip.setEipBandwidth(null);
				eip.setEipDependency(null);
				eip.setEipDescription(null);
				eip.setEipName(null);
				eip.setEipUID(null);
				session.update(eip);
				this.getQuotaDAO().updateQuotaField(session, userId, "quotaIP",
						1, false);
				this.getQuotaDAO().updateQuotaField(session, userId,
						"quotaBandwidth", bandwith, false);
				tx.commit();
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
	}

	@SuppressWarnings("unchecked")
	public EIP getEip(String eipIp) {
		Session session = this.getSessionHelper().openMainSession();
		Query query = session.createQuery("from EIP where eipIp=:ip");
		query.setString("ip", eipIp);
		List<EIP> eipList = query.list();
		return eipList.get(0);
	}

	@SuppressWarnings("unchecked")
	public String getEipId(String eip) {
		String eipid = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select eipUuid from EIP where eipIp =:eip";
			Query query = session.createQuery(queryString);
			query.setString("eip", eip);
			List<Object> objlist = query.list();
			if (objlist.size() == 1) {
				eipid = (String) objlist.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return eipid;
	}

	@SuppressWarnings("unchecked")
	public EIP getEipByUuid(String eipUuid) {
		Session session = this.getSessionHelper().openMainSession();
		Query query = session.createQuery("from EIP where eipUuid=:Uuid");
		query.setString("Uuid", eipUuid);
		List<EIP> eipList = query.list();
		return eipList.get(0);
	}

	public void bindEip(String eipIp, String dependencyUuid, int type) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("update EIP set eipDependency=:uuid,depenType=:type where eipIp=:ip");
		query.setString("uuid", dependencyUuid);
		query.setInteger("type", type);
		query.setString("ip", eipIp);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void unBindEip(String eipIp) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("update EIP set eipDependency=null,depenType=null where eipIp=:ip");
		query.setString("ip", eipIp);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public String getEipIp(String dependencyUuid) {
		String eipIp = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session
					.createQuery("select eipIp from EIP where eipDependency=:dependency");
			query.setString("dependency", dependencyUuid);
			List<Object> dependList = query.list();
			if (dependList != null && dependList.size() == 1) {
				eipIp = (String) dependList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return eipIp;
	}

	public boolean changeBandwidth(int userId, EIP eipObj, int size) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			int origin = eipObj.getEipBandwidth();
			eipObj.setEipBandwidth(size);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(eipObj);
			if (size > origin) {
				this.getQuotaDAO().updateQuotaField(session, userId,
						"quotaBandwidth", size - origin, true);
			} else {
				this.getQuotaDAO().updateQuotaField(session, userId,
						"quotaBandwidth", origin - size, false);
			}
			tx.commit();
			result = true;
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

	public boolean addEIP(String prefix, int start, int end, Date date,
			int eiptype, String eipInterface) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			for (int i = start; i <= end; i++) {
				String currentIp = prefix + i;
				boolean check = ipExist(currentIp);
				if (check == false) {
					EIP eip = new EIP();
					eip.setEipIp(currentIp);
					eip.setEipType(eiptype);
					eip.setEipUuid(UUID.randomUUID().toString());
					eip.setEipInterface(eipInterface);
					eip.setCreateDate(date);
					session.save(eip);
					this.getOverViewDAO().updateOverViewfield(session,
							"viewOutip", true);
				}
			}
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.commit();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean ipExist(String eIp) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from EIP where eipIp = :eIp";
			Query query = session.createQuery(queryString);
			query.setString("eIp", eIp);
			List<EIP> eipList = query.list();
			if (eipList.size() == 1) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isNotExistAlarm(String alarmUuid) {
		boolean result = true;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from EIP where alarmUuid = :alarmUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			List<EIP> eipList = query.list();
			if (eipList.size() > 0) {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	public int countAllEipListNoUserid(String searchStr) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from EIP where eipIp like '%"
				+ searchStr + "%'";
		Query query = session.createQuery(queryString);
		return ((Number) query.iterate().next()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<EIP> getOnePageEIPListNoUserid(int page, int limit,
			String searchStr) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "";
		queryString = "from EIP where eipIp like '%" + searchStr
				+ "%' order by eipDependency desc, eipUID desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<EIP> eipList = query.list();
		session.close();
		return eipList;
	}

	public boolean deleteEIP(String ip, String uuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "delete from EIP where eipUuid = '" + uuid
					+ "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			this.getOverViewDAO().updateOverViewfield(session, "viewOutip",
					false);
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

	/**
	 * @param eipuuid
	 * @param newName
	 * @param description
	 * @author xpx 2014-7-8
	 */
	public void updateName(String eipip, String newName, String description) {
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Transaction tx = session.beginTransaction();
			String queryString = "update EIP set eipName=:name,eipDescription=:desc where eipIp=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", eipip);
			query.setString("desc", description);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	// 获取可用公网IP
	@SuppressWarnings("unchecked")
	public List<EIP> getableeips(int uid) {
		Session session = null;
		List<EIP> list = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session
					.createQuery("from EIP where eipDependency is null and eipUID ="
							+ uid);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	/**
	 * @author hty
	 * @param eipip
	 * @param alarmUuid
	 */
	public void updateAlarm(String eipUuid, String alarmUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Transaction tx = session.beginTransaction();
			String queryString = "update EIP set alarmUuid=:alarmUuid where eipUuid=:eipUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			query.setString("eipUuid", eipUuid);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EIP> getAllListAlarm(int eipUID, String alarmUuid) {
		List<EIP> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from EIP where eipUID =:eipUID and alarmUuid =:alarmUuid order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("eipUID", eipUID);
			query.setString("alarmUuid", alarmUuid);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}
}
