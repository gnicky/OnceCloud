package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.performance.Cpu;
import com.oncecloud.entity.performance.Memory;
import com.oncecloud.entity.performance.Pif;
import com.oncecloud.entity.performance.Vbd;
import com.oncecloud.entity.performance.Vif;
import com.oncecloud.helper.SessionHelper;

@Component
public class PerformanceDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllCPUTime430() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Cpu";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllCPUTime4Oneday() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Cpu1d";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllCPUTime4Onemonth() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Cpu1m";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllCPUTime4Sixhours() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Cpu6h";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllCPUTime4Twoweeks() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Cpu2w";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllMemoryTime430() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Memory";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllMemoryTime4Oneday() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Memory1d";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllMemoryTime4Onemonth() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Memory1m";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllMemoryTime4Sixhours() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Memory6h";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllMemoryTime4Twoweeks() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Memory2w";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllPifTime430() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Pif";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllPifTime4Oneday() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Pif1d";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllPifTime4Onemonth() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Pif1m";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllPifTime4Sixhours() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Pif6h";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllPifTime4Twoweeks() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Pif2w";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVbdTime430() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vbd";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVbdTime4Oneday() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vbd1d";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVbdTime4Onemonth() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vbd1m";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVbdTime4Sixhours() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vbd6h";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVbdTime4Twoweeks() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vbd2w";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVifTime430() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vif";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVifTime4Oneday() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vif1d";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVifTime4Onemonth() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vif1m";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVifTime4Sixhours() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vif6h";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllVifTime4Twoweeks() {
		List<Long> timeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "select distinct time from Vif2w";
			Query query = session.createQuery(queryString);
			timeList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return timeList;
	}

	@SuppressWarnings("unchecked")
	public List<Cpu> getCpuList(String uuid) {
		List<Cpu> cpuList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Cpu30min where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			cpuList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return cpuList;
	}

	@SuppressWarnings("unchecked")
	public List<Cpu> getCpuList4Oneday(String uuid) {
		List<Cpu> cpuList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Cpu1d where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			cpuList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return cpuList;
	}

	@SuppressWarnings("unchecked")
	public List<Cpu> getCpuList4Onemonth(String uuid) {
		List<Cpu> cpuList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Cpu1m where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			cpuList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return cpuList;
	}

	@SuppressWarnings("unchecked")
	public List<Cpu> getCpuList4Sixhours(String uuid) {
		List<Cpu> cpuList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Cpu6h where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			cpuList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return cpuList;
	}

	@SuppressWarnings("unchecked")
	public List<Cpu> getCpuList4Twoweeks(String uuid) {
		List<Cpu> cpuList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Cpu2w where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			cpuList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return cpuList;
	}

	@SuppressWarnings("unchecked")
	public List<Memory> getMemoryList(String uuid) {
		List<Memory> memoryList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Memory30min where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			memoryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return memoryList;
	}

	@SuppressWarnings("unchecked")
	public List<Memory> getMemoryList4Oneday(String uuid) {
		List<Memory> memoryList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Memory1d where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			memoryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return memoryList;
	}

	@SuppressWarnings("unchecked")
	public List<Memory> getMemoryList4Onemonth(String uuid) {
		List<Memory> memoryList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Memory1m where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			memoryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return memoryList;
	}

	@SuppressWarnings("unchecked")
	public List<Memory> getMemoryList4Sixhours(String uuid) {
		List<Memory> memoryList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Memory6h where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			memoryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return memoryList;
	}

	@SuppressWarnings("unchecked")
	public List<Memory> getMemoryList4Twoweeks(String uuid) {
		List<Memory> memoryList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Memory2w where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			memoryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return memoryList;
	}

	@SuppressWarnings("unchecked")
	public List<Pif> getPifList(String uuid) {
		List<Pif> pifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Pif30min where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			pifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return pifList;
	}

	@SuppressWarnings("unchecked")
	public List<Pif> getPifList4Oneday(String uuid) {
		List<Pif> pifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Pif1d where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			pifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return pifList;
	}

	@SuppressWarnings("unchecked")
	public List<Pif> getPifList4Onemonth(String uuid) {
		List<Pif> pifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Pif1m where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			pifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return pifList;
	}

	@SuppressWarnings("unchecked")
	public List<Pif> getPifList4Sixhours(String uuid) {
		List<Pif> pifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Pif6h where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			pifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return pifList;
	}

	@SuppressWarnings("unchecked")
	public List<Pif> getPifList4Twoweeks(String uuid) {
		List<Pif> pifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Pif2w where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			pifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return pifList;
	}

	@SuppressWarnings("unchecked")
	public List<Vbd> getVbdList(String uuid) {
		List<Vbd> vbdList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vbd30min where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vbdList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vbdList;
	}

	@SuppressWarnings("unchecked")
	public List<Vbd> getVbdList4Oneday(String uuid) {
		List<Vbd> vbdList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vbd1d where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vbdList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vbdList;
	}

	@SuppressWarnings("unchecked")
	public List<Vbd> getVbdList4Onemonth(String uuid) {
		List<Vbd> vbdList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vbd1m where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vbdList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vbdList;
	}

	@SuppressWarnings("unchecked")
	public List<Vbd> getVbdList4Sixhours(String uuid) {

		List<Vbd> vbdList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vbd6h where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vbdList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vbdList;
	}

	@SuppressWarnings("unchecked")
	public List<Vbd> getVbdList4Twoweeks(String uuid) {
		List<Vbd> vbdList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vbd2w where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vbdList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vbdList;
	}

	@SuppressWarnings("unchecked")
	public List<Vif> getVifList(String uuid) {
		List<Vif> vifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vif30min where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vifList;
	}

	@SuppressWarnings("unchecked")
	public List<Vif> getVifList4Oneday(String uuid) {
		List<Vif> vifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vif1d where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vifList;
	}

	@SuppressWarnings("unchecked")
	public List<Vif> getVifList4Onemonth(String uuid) {
		List<Vif> vifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vif1m where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vifList;
	}

	@SuppressWarnings("unchecked")
	public List<Vif> getVifList4Sixhours(String uuid) {
		List<Vif> vifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vif6h where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vifList;
	}

	@SuppressWarnings("unchecked")
	public List<Vif> getVifList4Twoweeks(String uuid) {
		List<Vif> vifList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getPerformaceSession();
			String queryString = "from Vif2w where uuid=:id order by time";
			Query query = session.createQuery(queryString);
			query.setString("id", uuid);
			vifList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return vifList;
	}
}
