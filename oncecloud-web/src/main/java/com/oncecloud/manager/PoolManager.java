package com.oncecloud.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.once.xenapi.Connection;
import com.once.xenapi.VM;
import com.oncecloud.dao.DatacenterDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.PoolDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.Datacenter;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCPool;
import com.oncecloud.helper.SessionHelper;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;

/**
 * @author hehai
 * @version 2014/08/23
 */
public class PoolManager {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private PoolDAO poolDAO;
	private LogDAO logDAO;
	private HostDAO hostDAO;
	private DatacenterDAO datacenterDAO;
	private VMDAO vmDAO;
	private DatacenterManager datacenterManager;

	private PoolDAO getPoolDAO() {
		return poolDAO;
	}

	@Autowired
	private void setPoolDAO(PoolDAO poolDAO) {
		this.poolDAO = poolDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	private DatacenterDAO getDatacenterDAO() {
		return datacenterDAO;
	}

	@Autowired
	private void setDatacenterDAO(DatacenterDAO datacenterDAO) {
		this.datacenterDAO = datacenterDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private DatacenterManager getDatacenterManager() {
		return datacenterManager;
	}

	@Autowired
	private void setDatacenterManager(DatacenterManager datacenterManager) {
		this.datacenterManager = datacenterManager;
	}

	public JSONArray createPool(String poolName, String poolDesc,
			String dcUuid, String dcName, int userId) {
		Date startTime = new Date();
		OCPool result = this.getPoolDAO()
				.createPool(poolName, poolDesc, dcUuid);
		JSONArray ja = new JSONArray();
		if (result != null) {
			JSONObject jo = new JSONObject();
			jo.put("poolname", Utilities.encodeText(result.getPoolName()));
			jo.put("poolid", result.getPoolUuid());
			jo.put("dcname", Utilities.encodeText(dcName));
			jo.put("pooldesc", Utilities.encodeText(result.getPoolDesc()));
			jo.put("dcuuid", dcUuid);
			String poolMaster = result.getPoolMaster();
			if (poolMaster == null) {
				poolMaster = "";
			}
			jo.put("poolmaster", poolMaster);
			jo.put("createdate", Utilities.formatTime(result.getCreateDate()));
			ja.put(jo);
		}
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.资源池.toString(), poolName));
		if (result != null) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray getPoolList(int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getPoolDAO().countAllPoolList(search);
		ja.put(totalNum);
		List<OCPool> poolList = this.getPoolDAO().getOnePagePoolList(page,
				limit, search);
		if (poolList != null) {
			for (OCPool result : poolList) {
				JSONObject jo = new JSONObject();
				jo.put("poolname", Utilities.encodeText(result.getPoolName()));
				jo.put("pooldesc", Utilities.encodeText(result.getPoolDesc()));
				String poolUuid = result.getPoolUuid();
				jo.put("poolid", poolUuid);
				String poolMaster = result.getPoolMaster();
				if (poolMaster == null) {
					jo.put("poolmaster", "");
					jo.put("mastername", "");
				} else {
					jo.put("poolmaster", poolMaster);
					jo.put("mastername",
							Utilities.encodeText(this.getHostDAO()
									.getHost(poolMaster).getHostName()));
				}
				String dcuuid = result.getDcUuid();
				if (dcuuid == null) {
					jo.put("dcname", "");
					jo.put("dcuuid", "");
				} else {
					jo.put("dcuuid", dcuuid);
					jo.put("dcname",
							Utilities.encodeText(this.getDatacenterDAO()
									.getDatacenter(dcuuid).getDcName()));
				}
				jo.put("createdate",
						Utilities.formatTime(result.getCreateDate()));
				List<Integer> volumeList = this.getDatacenterManager()
						.getPoolVolume(poolUuid);
				jo.put("totalcpu", volumeList.get(0));
				jo.put("totalmem", volumeList.get(1));
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray deletePool(String poolId, String poolName, int userId) {
		Date startTime = new Date();
		boolean result = this.getPoolDAO().deletePool(poolId);
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.资源池.toString(), poolName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public void bind(String poolId, String dcid, int userId) {
		Date startTime = new Date();
		boolean result = this.getPoolDAO().bindPool(poolId, dcid);
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		Datacenter dc = this.getDatacenterDAO().getDatacenter(dcid);
		String dcName = dc.getDcName();
		jo.put("dcname", Utilities.encodeText(dcName));
		ja.put(jo);
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.资源池.toString(),
				"pool-" + poolId.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.数据中心.toString(), dcName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void unbind(String poolId, int userId) {
		Date startTime = new Date();
		JSONArray ja = new JSONArray();
		boolean result = this.getPoolDAO().unbindPool(poolId);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.资源池.toString(),
				"pool-" + poolId.substring(0, 8)));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.移除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.移除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void updatePool(String poolUuid, String poolName, String poolDesc,
			String dcUuid, int userId) {
		boolean result = this.getPoolDAO().updatePool(poolUuid, poolName,
				poolDesc, dcUuid);
		if (result) {
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess("资源池更新成功"));
		} else {
			MessagePush.pushMessage(userId, Utilities.stickyToError("资源池更新失败"));
		}
	}

	public JSONArray getAllPool() {
		JSONArray ja = new JSONArray();
		List<OCPool> poolList = this.getPoolDAO().getPoolList();
		if (poolList != null) {
			for (OCPool pool : poolList) {
				JSONObject jo = new JSONObject();
				jo.put("poolname", Utilities.encodeText(pool.getPoolName()));
				jo.put("poolid", pool.getPoolUuid());
				ja.put(jo);
			}
		}
		return ja;
	}

	@SuppressWarnings("deprecation")
	public void keepAccordance(int userId, String poolUuid) {
		boolean result = false;
		List<SimpleRecord> srList = new ArrayList<SimpleRecord>();
		try {
			OCHost master = this.getHostDAO().getHost(
					this.getPoolDAO().getPool(poolUuid).getPoolMaster());
			Connection conn = new Connection("http://" + master.getHostIP()
					+ ":9363", HostManager.DEFAULT_USER, master.getHostPwd());
			Map<VM, VM.Record> map = VM.getAllRecords(conn);
			for (VM thisVM : map.keySet()) {
				VM.Record vmRecord = map.get(thisVM);
				if (!vmRecord.isControlDomain && !vmRecord.isATemplate) {
					srList.add(new SimpleRecord(vmRecord.uuid,
							vmRecord.powerState.toString(), vmRecord.residentOn
									.toWireString()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			srList = null;
		}
		if (srList != null) {
			Session session = null;
			Transaction tx = null;
			try {
				session = this.getSessionHelper().openMainSession();
				tx = session.beginTransaction();
				for (SimpleRecord sr : srList) {
					String uuid = sr.getUuid();
					String powerStatus = sr.powerStatus;
					String hostUuid = sr.hostUuid;
					int power = powerStatus.equals("Running") ? 1 : 0;
					this.getVmDAO().updatePowerAndHost(session, uuid, power,
							hostUuid);
				}
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
		}
		// push message
		if (result) {
			MessagePush.pushMessage(userId, "资源池状态已保持一致");
		} else {
			MessagePush.pushMessage(userId, "资源池状态未保持一致");
		}
	}
}

class SimpleRecord {
	String uuid;
	String powerStatus;
	String hostUuid;

	public SimpleRecord(String uuid, String powerStatus, String hostUuid) {
		this.uuid = uuid;
		this.powerStatus = powerStatus;
		this.hostUuid = hostUuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPowerStatus() {
		return powerStatus;
	}

	public void setPowerStatus(String powerStatus) {
		this.powerStatus = powerStatus;
	}

	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

	@Override
	public String toString() {
		return "SimpleRecord [uuid=" + uuid + ", powerStatus=" + powerStatus
				+ ", hostUuid=" + hostUuid + "]";
	}
}
