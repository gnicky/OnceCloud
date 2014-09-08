package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.OCVM;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.main.NoVNC;
import com.oncecloud.message.MessagePush;

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
	private MessagePush messagePush;

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

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	/**
	 * 获取主机
	 * 
	 * @param vmUuid
	 * @return
	 */
	public OCVM getVM(String vmUuid) {
		OCVM vm = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from OCVM where vmUuid = :vmUuid");
			query.setString("vmUuid", vmUuid);
			vm = (OCVM) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vm;
	}

	/**
	 * 获取主机名称
	 * 
	 * @param vmUuid
	 * @return
	 */
	public String getVmName(String vmUuid) {
		String name = "";
		OCVM vm = getVM(vmUuid);
		if (vm != null) {
			name = getVM(vmUuid).getVmName();
		}
		return name;
	}

	/**
	 * 获取使用中的主机
	 * 
	 * @param vmUuid
	 * @return
	 */
	public OCVM getAliveVM(String vmUuid) {
		OCVM vm = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from OCVM where vmUuid = :vmUuid and vmStatus = 1");
			query.setString("vmUuid", vmUuid);
			vm = (OCVM) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vm;
	}

	/**
	 * 获取一页用户主机列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageVMs(int userId, int page, int limit,
			String search) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from OCVM where vmUID = :userId and vmName like :search and vmStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vmList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmList;
	}

	/**
	 * 获取一页管理员主机列表
	 * 
	 * @param page
	 * @param limit
	 * @param host
	 * @param importance
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageVMsOfAdmin(int page, int limit, String host,
			int importance) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
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
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmList;
	}

	/**
	 * 获取一页未设置监控警告的主机列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageVMsWithoutAlarm(int page, int limit,
			String search, int userId) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from OCVM where vmUID = :userId and vmName like :search "
					+ "and vmStatus = 1 and alarmUuid is null order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vmList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmList;
	}

	/**
	 * 获取一页没有绑定公网IP的主机列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getOnePageVMsWithoutEIP(int page, int limit,
			String search, int userId) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "select vm from OCVM vm where vm.vmUID = :userId "
					+ "and vm.vmName like :search and vm.vmStatus = 1 and vm.vmVlan is null and vm.vmUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID = :uid "
					+ "and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setInteger("uid", userId);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			vmList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmList;
	}

	/**
	 * 获取对应监控警告的主机列表
	 * 
	 * @param vmUID
	 * @param alarmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getVMsOfAlarm(int vmUID, String alarmUuid) {
		List<OCVM> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCVM where vmUID = :vmUID and vmStatus = 1 and alarmUuid = :alarmUuid order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("vmUID", vmUID);
			query.setString("alarmUuid", alarmUuid);
			vmList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmList;
	}

	/**
	 * 获取对应私有网络的主机列表
	 * 
	 * @param vmVlan
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OCVM> getVMsOfVnet(String vmVlan) {
		List<OCVM> list = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from OCVM where vmVlan = :vmVlan and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("vmVlan", vmVlan);
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

	/**
	 * 获取简单用户主机列表
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getSimpleVMList(int userId) {
		List<Object[]> vmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select vmUuid, vmName, vmIP from OCVM where vmUID = :userId"
					+ "and vmStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			vmList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return vmList;
	}

	/**
	 * 获取用户主机总数
	 * 
	 * @param search
	 * @param vmUID
	 * @return
	 */
	public int countVMs(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVM where vmUID = :userId and vmName like :search and vmStatus = 1";
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

	/**
	 * 获取管理员主机总数
	 * 
	 * @param host
	 * @param importance
	 * @return
	 */
	public int countVMsOfAdmin(String host, int importance) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
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
	 * 获取用户的主机总数
	 * 
	 * @param userId
	 * @return
	 */
	public int countVMsOfUser(int userId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVM where vmUID = :userId"
					+ "and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
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
	 * 获取未设置监控警告的主机总数
	 * 
	 * @param search
	 * @param userId
	 * @return
	 */
	public int countVMsWithoutAlarm(String search, int userId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVM where vmUID = :userId and vmName like :search "
					+ "and vmStatus = 1 and alarmUuid is null";
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

	/**
	 * 获取未绑定公网IP的主机总数
	 * 
	 * @param search
	 * @param userId
	 * @return
	 */
	public int countVMsWithoutEIP(String search, int userId) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVM vm where vm.vmUID = :userId "
					+ "and vm.vmName like :search and vm.vmStatus = 1 and vm.vmUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID = :uid "
					+ "and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setInteger("uid", userId);
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
	 * 获取指定私有网络的主机总数
	 * 
	 * @param vnetUuid
	 * @return
	 */
	public int countVMsOfVnet(String vnetUuid) {
		int count = -1;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVM where vmVlan = :vnetUuid and vmStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("vnetUuid", vnetUuid);
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
	 * 获取指定服务器的主机总数
	 * 
	 * @param hostUuid
	 * @return
	 */
	public int countVMsOfHost(String hostUuid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from OCVM where vmStatus != 0 and hostUuid = :hostUuid";
			Query query = session.createQuery(queryString);
			query.setString("hostUuid", hostUuid);
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
	 * 预创建虚拟机
	 * 
	 * @param vmUuid
	 * @param vmPWD
	 * @param vmUID
	 * @param vmName
	 * @param vmPlatform
	 * @param vmMac
	 * @param vmMem
	 * @param vmCpu
	 * @param vmPower
	 * @param vmStatus
	 * @param createDate
	 * @return
	 */
	public boolean preCreateVM(String vmUuid, String vmPWD, Integer vmUID,
			String vmName, Integer vmPlatform, String vmMac, Integer vmMem,
			Integer vmCpu, Integer vmPower, Integer vmStatus, Date createDate) {
		Session session = null;
		boolean result = false;
		try {
			OCVM vm = new OCVM(vmUuid, vmPWD, vmUID, vmName, vmPlatform, vmMac,
					vmMem, vmCpu, vmPower, vmStatus, createDate);
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(vm);
			this.getQuotaDAO().updateQuotaFieldNoTransaction(vmUID, "quotaVM",
					1, true);
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
	 * 删除主机
	 * 
	 * @param userId
	 * @param vmUuid
	 */
	public void removeVM(int userId, String vmUuid) {
		OCVM toDelete = this.getVM(vmUuid);
		if (toDelete != null) {
			Session session = null;
			try {
				toDelete.setVmStatus(0);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(toDelete);
				this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
						"quotaVM", 1, false);
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	/**
	 * 更新主机
	 * 
	 * @param userId
	 * @param vmUuid
	 * @param vmPWD
	 * @param vmPower
	 * @param hostUuid
	 * @param ip
	 * @return
	 */
	public boolean updateVM(int userId, String vmUuid, String vmPWD,
			int vmPower, String hostUuid, String ip) {
		boolean result = false;
		OCVM vm = this.getVM(vmUuid);
		if (vm != null) {
			Session session = null;
			try {
				String firewallId = this.getFirewallDAO()
						.getDefaultFirewall(userId).getFirewallId();
				vm.setVmPWD(vmPWD);
				vm.setVmPower(vmPower);
				vm.setHostUuid(hostUuid);
				vm.setVmIP(ip);
				vm.setVmFirewall(firewallId);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(vm);
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
	 * 更新主机备份时间
	 * 
	 * @param vmUuid
	 * @param date
	 */
	public void updateBackupDate(String vmUuid, Date date) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set backupDate = :date where vmUuid = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			query.setTimestamp("date", date);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 更新主机防火墙
	 * 
	 * @param vmUuid
	 * @param firewallId
	 */
	public void updateFirewall(String vmUuid, String firewallId) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set vmFirewall = :firewallId where vmUuid = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			query.setString("firewallId", firewallId);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 更新主机监控警告
	 * 
	 * @param vmUuid
	 * @param alarmUuid
	 */
	public boolean updateAlarm(String vmUuid, String alarmUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set alarmUuid = :alarmUuid where vmUuid = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("alarmUuid", alarmUuid);
			query.setString("vmUuid", vmUuid);
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
	 * 更新主机名称和描述
	 * 
	 * @param vmUuid
	 * @param vmName
	 * @param vmDesc
	 */
	public void updateName(String vmUuid, String vmName, String vmDesc) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set vmName = :vmName, vmDesc = :vmDesc where vmUuid = :vmUuid";
			Query query = session.createQuery(queryString);
			query.setString("vmName", vmName);
			query.setString("vmUuid", vmUuid);
			query.setString("vmDesc", vmDesc);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 更新主机状态
	 * 
	 * @param vmUuid
	 * @param hostUuid
	 * @param power
	 * @return
	 */
	public boolean updateVMStatus(String vmUuid, String hostUuid, int power) {
		boolean result = false;
		OCVM vm = this.getVM(vmUuid);
		if (vm != null) {
			Session session = null;
			try {
				vm.setHostUuid(hostUuid);
				vm.setVmStatus(1);
				vm.setVmPower(power);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(vm);
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

	public boolean updateVMImportance(String vmUuid, int vmImportance) {
		boolean result = false;
		OCVM vm = this.getVM(vmUuid);
		if (vm != null) {
			Session session = null;
			try {
				vm.setVmImportance(vmImportance);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(vm);
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
	 * 更新主机电源状态和所在服务器
	 * 
	 * @param session
	 * @param uuid
	 * @param power
	 * @param hostUuid
	 */
	public void updatePowerAndHostNoTransaction(String uuid, int power,
			String hostUuid) {
		Session session = this.getSessionHelper().getMainSession();
		String queryString = "update OCVM set vmPower = :power, hostUuid = :hostUuid where vmUuid = :uuid";
		Query query = session.createQuery(queryString);
		query.setInteger("power", power);
		query.setString("hostUuid", hostUuid);
		query.setString("uuid", uuid);
		query.executeUpdate();
	}

	/**
	 * 更新主机私有网络
	 * 
	 * @param vmuuid
	 * @param vnetid
	 * @return
	 */
	public boolean updateVMVlan(String vmuuid, String vnetid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set vmIP = null, vmVlan = :vnetid where vmUuid = :vmid";
			Query query = session.createQuery(queryString);
			query.setString("vnetid", vnetid);
			query.setString("vmid", vmuuid);
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
	 * 更新主机电源状态
	 * 
	 * @param uuid
	 * @param powerStatus
	 * @return
	 */
	public boolean updatePowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		OCVM vm = this.getVM(uuid);
		if (vm != null) {
			Session session = null;
			try {
				vm.setVmPower(powerStatus);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(vm);
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
	 * 更新主机所在服务器
	 * 
	 * @param uuid
	 * @param hostUuid
	 * @return
	 */
	public boolean updateHostUuid(String uuid, String hostUuid) {
		boolean result = false;
		OCVM vm = this.getVM(uuid);
		Session session = null;
		try {
			vm.setHostUuid(hostUuid);
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.update(vm);
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
	 * 主机一致性删除
	 * 
	 * @param hostUuid
	 * @param vmUuid
	 */
	public void syncDelVMOperate(String hostUuid, String vmUuid) {
		OCVM vm = this.getVM(vmUuid);
		if (vm != null) {
			Session session = null;
			try {
				if (hostUuid.equals(vm.getHostUuid())) {
					vm.setVmStatus(0);
					session = this.getSessionHelper().getMainSession();
					session.beginTransaction();
					session.update(vm);
					session.getTransaction().commit();
					this.getFeeDAO().destoryVM(new Date(), vmUuid);
					NoVNC.deleteToken(vmUuid.substring(0, 8));
					this.getMessagePush().deleteRow(vm.getVmUID(), vmUuid);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	/**
	 * 主机回到基础网络
	 * 
	 * @param vmuuid
	 * @param ip
	 * @return
	 */
	public boolean returnToBasicNetwork(String vmuuid, String ip) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "update OCVM set vmIP= :ip, vmVlan = null where vmUuid = :vmid";
			Query query = session.createQuery(queryString);
			query.setString("ip", ip);
			query.setString("vmid", vmuuid);
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
	 * 是否有主机具有该监控警告
	 * 
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
			Query query = session
					.createQuery("from OCVM where alarmUuid = :alarmUuid and vmStatus = 1");
			query.setString("alarmUuid", alarmUuid);
			List<OCVM> vmList = query.list();
			session.getTransaction().commit();
			if (vmList.size() > 0) {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
}
