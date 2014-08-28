package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.OCVM;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.main.NoVNC;
import com.oncecloud.main.Utilities;

/**
 * @author hehai hty
 * @version 2014/08/24
 */
@Component
public class VMDAO {
	private SessionHelper sessionHelper;
	private QuotaDAO quotaDAO;
	private FirewallDAO firewallDAO;
	private FeeDAO feeDAO;

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

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	public OCVM getVM(String vmUuid) {
		OCVM vm = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session
					.createQuery("from OCVM where vmUuid = :vmUuid");
			query.setString("vmUuid", vmUuid);
			vm = (OCVM) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vm;
	}

	public OCVM getAliveVM(String vmUuid) {
		OCVM vm = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session
					.createQuery("from OCVM where vmUuid = :vmUuid and vmStatus = 1");
			query.setString("vmUuid", vmUuid);
			vm = (OCVM) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vm;
	}

	public int countVMOfUser(int userId) {
		long size = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Query query = session
					.createQuery("select count(*) from OCVM where vmUID = "
							+ userId + " and vmStatus = 1");
			size = (Long) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			size = 0;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return (int) size;
	}

	@SuppressWarnings("unchecked")
	public boolean isExist(String vmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		Query query = session.createQuery("from OCVM where vmUuid = '" + vmUuid
				+ "' and vmStatus=1");
		List<OCVM> vmList = query.list();
		session.close();
		if (vmList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isNotExistAlarm(String alarmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		Query query = session
				.createQuery("from OCVM where alarmUuid =:alarmUuid and vmStatus=1");
		List<OCVM> vmList = query.list();
		query.setString("alarmUuid", alarmUuid);
		session.close();
		if (vmList.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean preCreateVM(String vmUuid, String vmPWD, Integer vmUID,
			String vmName, Integer vmPlatform, String vmMac, Integer vmMem,
			Integer vmCpu, Integer vmPower, Integer vmStatus, Date createDate) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().openMainSession();
			OCVM vm = new OCVM(vmUuid, vmPWD, vmUID, vmName, vmPlatform, vmMac,
					vmMem, vmCpu, vmPower, vmStatus, createDate);
			tx = session.beginTransaction();
			session.save(vm);
			this.getQuotaDAO().updateQuotaField(session, vmUID, "quotaVM", 1,
					true);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	public boolean updateVM(int userId, String vmUuid, String vmPWD,
			int vmPower, String hostUuid, String ip) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			OCVM vm = this.getVM(vmUuid);
			String firewallId = this.getFirewallDAO()
					.getDefaultFirewall(userId).getFirewallId();
			vm.setVmPWD(vmPWD);
			vm.setVmPower(vmPower);
			vm.setHostUuid(hostUuid);
			vm.setVmIP(ip);
			vm.setVmFirewall(firewallId);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(vm);
			tx.commit();
			result = true;
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
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageVmList(int page, int limit, String search,
			int uid) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from OCVM where vmUID = " + uid
					+ " and vmName like '%" + search
					+ "%' and vmStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vmList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
	}

	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageAdminVmList(int page, int limit, String host,
			int importance) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			Criteria criteria = session.createCriteria(OCVM.class);
			criteria.add(Restrictions.ne("vmStatus", 0));
			criteria.setFirstResult(startPos);
			criteria.setMaxResults(limit);
			if (!host.equals("all")) {
				criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria.add(Restrictions.eq("vmImportance", importance));
			}
			vmList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
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
	public List<OCVM> getOnePageVmListAlarm(int page, int limit, String search,
			int vmUID) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from OCVM where vmUID =:vmUID and vmName like '%"
					+ search
					+ "%' and vmStatus = 1 and alarmUuid is null order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			query.setInteger("vmUID", vmUID);
			vmList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
	}

	public int countAllVMList(String search, int vmUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from OCVM where vmUID =:vmUID and vmName like '%"
					+ search + "%' and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setInteger("vmUID", vmUID);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	public int countAllAdminVMList(String host, int importance) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Criteria criteria = session.createCriteria(OCVM.class);
			criteria.add(Restrictions.ne("vmStatus", 0));
			if (!host.equals("all")) {
				criteria.add(Restrictions.eq("hostUuid", host));
			}
			if (importance != 6) {
				criteria.add(Restrictions.eq("vmImportance", importance));
			}
			criteria.setProjection(Projections.rowCount());
			count = Integer.parseInt(criteria.uniqueResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	public int countVMOfVnet(String vnetUuid) {
		int count = -1;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from OCVM where vmVlan = :vnetUuid and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetUuid);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
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
	public int countAllVMListAlarm(String search, int vmUID) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from OCVM where vmUID =:vmUID and vmName like '%"
					+ search + "%' and vmStatus = 1 and alarmUuid is null";
			Query query = session.createQuery(queryString);
			query.setInteger("vmUID", vmUID);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	public void updateBackupDate(String vmuuid, Date date) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update OCVM set backupDate=:date where vmUuid='"
				+ vmuuid + "'";
		Query query = session.createQuery(queryString);
		query.setTimestamp("date", date);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void removeVM(int userId, String vmUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			OCVM toDelete = this.getVM(vmUuid);
			toDelete.setVmStatus(0);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(toDelete);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaVM", 1,
					false);
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
	public int getVmPowerState(String vmuuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "select vmPower from OCVM where vmUuid='" + vmuuid
				+ "'";
		Query query = session.createQuery(queryString);
		List<Integer> list = query.list();
		session.close();
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public String getVmName(String vmuuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "select vmName from OCVM where vmUuid='" + vmuuid
				+ "'";
		Query query = session.createQuery(queryString);
		List<String> list = query.list();
		session.close();
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageVMsWithoutEIP(int page, int limit,
			String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		int startPos = (page - 1) * limit;
		String queryString = "";
		queryString = "select vm from OCVM vm where vm.vmUID=" + uid
				+ " and vm.vmName like '%" + search
				+ "%' and vm.vmStatus=1 and vm.vmUuid not in "
				+ "(select eip.eipDependency from EIP eip where eip.eipUID="
				+ uid + " and eip.eipDependency is not null)";
		Query query = session.createQuery(queryString);
		query.setFirstResult(startPos);
		query.setMaxResults(limit);
		List<OCVM> vms = query.list();
		session.close();
		return vms;
	}

	public int countVMsWithoutEIP(String search, int uid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from OCVM vm where vm.vmUID=" + uid
				+ " and vm.vmName like '%" + search
				+ "%' and vm.vmStatus=1 and vm.vmUuid not in "
				+ "(select eip.eipDependency from EIP eip where eip.eipUID="
				+ uid + " and eip.eipDependency is not null)";
		Query query = session.createQuery(queryString);
		int count = ((Number) query.iterate().next()).intValue();
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public String getVmIp(String vmUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "select vmIP from OCVM where vmUuid='" + vmUuid
				+ "'";
		Query query = session.createQuery(queryString);
		List<String> list = query.list();
		return list.get(0);
	}

	public void updateVMIP(String vmuuid, String ip) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update OCVM set vmIP='" + ip
				+ "' where vmUuid ='" + vmuuid + "'";
		Query query = session.createQuery(queryString);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void emptyVMEipIp(String vmuuid) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update EIP set eipDependency=null where eipDependency ='"
				+ vmuuid + "'";
		Query query = session.createQuery(queryString);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateFirewall(String vmuuid, String firewallId) {
		Session session = this.getSessionHelper().openMainSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update OCVM set vmFirewall=:fid where vmUuid ='"
				+ vmuuid + "'";
		Query query = session.createQuery(queryString);
		query.setString("fid", firewallId);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateName(String uuid, String newName, String description) {
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			Transaction tx = session.beginTransaction();
			String queryString = "update OCVM set vmName=:name,vmDesc=:desc where vmUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", newName);
			query.setString("uuid", uuid);
			query.setString("desc", description);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean setVMPowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		OCVM vm = this.getVM(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			vm.setVmPower(powerStatus);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(vm);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean setVMHostUuid(String uuid, String hostUuid) {
		boolean result = false;
		OCVM vm = this.getVM(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			vm.setHostUuid(hostUuid);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.update(vm);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean syncDelVMOperate(String hostUuid, String vmUuid) {
		OCVM vm = this.getVM(vmUuid);
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			if (vm != null) {
				if (hostUuid.equals(vm.getHostUuid())) {
					vm.setVmStatus(0);
					session = this.getSessionHelper().openMainSession();
					tx = session.beginTransaction();
					session.update(vm);
					tx.commit();
					this.getFeeDAO().destoryVM(new Date(), vmUuid);
					NoVNC.deleteToken(vmUuid.substring(0, 8));
					MessagePush.deleteRow(vm.getVmUID(), vmUuid);
				}
				result = true;
			} else {
				result = true;
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	public int countByHost(String hostUuid) {
		Session session = this.getSessionHelper().openMainSession();
		String queryString = "";
		queryString = "select count(*) from OCVM where vmStatus!=0 and hostUuid='"
				+ hostUuid + "'";
		Query query = session.createQuery(queryString);
		int count = ((Number) query.iterate().next()).intValue();
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getSimpleVMList(int userId) {
		List<Object[]> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select vmUuid,vmName,vmIP from OCVM where vmUID = "
					+ userId + " and vmStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			vmList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getOnePageVMWithoutVnet(int page, int limit,
			String search, int userId) {
		JSONArray vmList = new JSONArray();
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int count = countVMWithoutVnet(session, userId);
			vmList.put(count);
			int startPos = (page - 1) * limit;
			String queryString = "select vmUuid,vmName from OCVM where vmUID = "
					+ userId
					+ "and vmName like '%"
					+ search
					+ "%' and vmStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			List<Object[]> resultList = query.list();
			for (Object[] item : resultList) {
				JSONObject itemjo = new JSONObject();
				itemjo.put("vmid", item[0]);
				itemjo.put("vmname", Utilities.encodeText((String) item[1]));
				vmList.put(itemjo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			vmList = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
	}

	public Integer countVMWithoutVnet(Session session, int userId) {
		Integer count = 0;
		try {
			String queryString = "select count(*) from OCVM where vmUID = "
					+ userId + " and vmStatus = 1";
			Query query = session.createQuery(queryString);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public void emptyVMIp(String vmuuid) {
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCVM set vmIP=null, vmVlan=null where vmUuid ='"
					+ vmuuid + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if (session != null) {
				session.close();

			}
		}
	}

	public boolean updateVMVlan(String vmuuid, String vnetid) {
		boolean result = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCVM set vmIP=null, vmVlan=:vnetid where vmUuid =:vmid";
			Query query = session.createQuery(queryString);
			query.setString("vnetid", vnetid);
			query.setString("vmid", vmuuid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;
		} finally {
			if (session != null) {
				session.close();
			}

		}
		return result;
	}

	public boolean returnToBasicNetwork(String vmuuid, String ip) {
		boolean result = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCVM set vmIP=:ip, vmVlan=null where vmUuid =:vmid";
			Query query = session.createQuery(queryString);
			query.setString("ip", ip);
			query.setString("vmid", vmuuid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;
		} finally {
			if (session != null) {
				session.close();
			}

		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<OCVM> getVxnetsList(String vmVlan) {
		List<OCVM> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from OCVM where vmVlan= :vmVlan and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("vmVlan", vmVlan);
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

	/**
	 * @author hty
	 * @param vmuuid
	 * @param alarmUuid
	 * @return
	 */
	public boolean updateAlarm(String vmuuid, String alarmUuid) {
		boolean result = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update OCVM set alarmUuid=:alarmUuid where vmUuid =:vmid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			query.setString("vmid", vmuuid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;
		} finally {
			if (session != null) {
				session.close();
			}

		}
		return result;
	}

	/**
	 * @author hty
	 * @param alarmUuid
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getAllListAlarm(int vmUID, String alarmUuid) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from OCVM where vmUID =:vmUID and vmStatus = 1 and alarmUuid =:alarmUuid order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("vmUID", vmUID);
			query.setString("alarmUuid", alarmUuid);
			vmList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
	}

	@SuppressWarnings("unchecked")
	public List<OCVM> getTotalVMList() {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from OCVM where vmStatus = 1";
			Query query = session.createQuery(queryString);
			vmList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return vmList;
	}

	public static void updatePowerAndHost(Session session, String uuid,
			int power, String hostUuid) {
		String queryString = "update OCVM set vmPower = :power, hostUuid = :hostUuid where vmUuid = :uuid";
		Query query = session.createQuery(queryString);
		query.setInteger("power", power);
		query.setString("hostUuid", hostUuid);
		query.setString("uuid", uuid);
		query.executeUpdate();
	}

	public boolean updateVMStatus(String vmUuid, String hostUuid, int power) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			OCVM vm = this.getVM(vmUuid);
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			vm.setHostUuid(hostUuid);
			vm.setVmStatus(1);
			vm.setVmPower(power);
			session.update(vm);
			tx.commit();
			result = true;
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
		return result;
	}
}