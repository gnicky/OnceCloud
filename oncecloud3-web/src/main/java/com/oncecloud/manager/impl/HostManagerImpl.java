package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Pool;
import com.once.xenapi.Types;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.HostSRDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.PoolDAO;
import com.oncecloud.dao.RackDAO;
import com.oncecloud.dao.StorageDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCPool;
import com.oncecloud.entity.Storage;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.SSH;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.HostManager;
import com.oncecloud.manager.SRManager;
import com.oncecloud.message.MessagePush;

@Component("HostManager")
public class HostManagerImpl implements HostManager{
	private final static long MB = 1024 * 1024;
	
	private HostDAO hostDAO;
	private PoolDAO poolDAO;
	private RackDAO rackDAO;
	private StorageDAO storageDAO;
	private LogDAO logDAO;
	private OverViewDAO overViewDAO;
	private HostSRDAO hostSRDAO;
	private VMDAO vmDAO;
	
	private MessagePush messagePush;
	
	private SRManager srManager;
	
	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	private PoolDAO getPoolDAO() {
		return poolDAO;
	}

	@Autowired
	private void setPoolDAO(PoolDAO poolDAO) {
		this.poolDAO = poolDAO;
	}

	private RackDAO getRackDAO() {
		return rackDAO;
	}

	@Autowired
	private void setRackDAO(RackDAO rackDAO) {
		this.rackDAO = rackDAO;
	}


	private StorageDAO getStorageDAO() {
		return storageDAO;
	}

	@Autowired
	private void setStorageDAO(StorageDAO storageDAO) {
		this.storageDAO = storageDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}
	
	private HostSRDAO getHostSRDAO() {
		return hostSRDAO;
	}

	@Autowired
	private void setHostSRDAO(HostSRDAO hostSRDAO) {
		this.hostSRDAO = hostSRDAO;
	}
	
	public VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	public void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private SRManager getSrManager() {
		return srManager;
	}

	@Autowired
	private void setSrManager(SRManager srManager) {
		this.srManager = srManager;
	}

	public JSONArray createHost(String hostName, String hostPwd,
			String hostDesc, String hostIp, String rackUuid, String rackName,
			int userid) {
		Date startTime = new Date();
		JSONArray qaArray = new JSONArray();
		OCHost result = this.addHost(hostName, hostPwd, hostDesc, hostIp,
				rackUuid);
		if (result != null) {
			JSONObject tObj = new JSONObject();
			tObj.put("hostname", Utilities.encodeText(hostName));
			tObj.put("hostdesc", Utilities.encodeText(hostDesc));
			tObj.put("hostid", result.getHostUuid());
			tObj.put("hostip", hostIp);
			tObj.put("hostcpu", result.getHostCpu());
			tObj.put("hostmem", result.getHostMem());
			tObj.put("createdate", Utilities.formatTime(result.getCreateDate()));
			tObj.put("poolid", "");
			tObj.put("poolname", "");
			tObj.put("srsize", 0);
			tObj.put("rackUuid", rackUuid);
			tObj.put("rackName", Utilities.encodeText(rackName));
			qaArray.put(tObj);
		}
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.服务器.toString(), hostName));
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.地址.toString(), hostIp));
		if (result != null) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.服务器.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.服务器.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return qaArray;
	}

	public JSONArray getHostList(int page, int limit, String search) {
		JSONArray qaArray = new JSONArray();
		int totalNum = this.getHostDAO().countAllHostList(search);
		List<OCHost> hostList = this.getHostDAO().getOnePageHostList(page,
				limit, search);
		qaArray.put(totalNum);
		for (OCHost result : hostList) {
			JSONObject tObj = new JSONObject();
			tObj.put("hostname", Utilities.encodeText(result.getHostName()));
			tObj.put("hostdesc", Utilities.encodeText(result.getHostDesc()));
			String hostUuid = result.getHostUuid();
			tObj.put("hostid", hostUuid);
			tObj.put("hostip", result.getHostIP());
			tObj.put("hostcpu", result.getHostCpu());
			tObj.put("hostmem", result.getHostMem());
			tObj.put("createdate", Utilities.formatTime(result.getCreateDate()));
			String poolId = result.getPoolUuid();
			if (poolId == null) {
				tObj.put("poolid", "");
				tObj.put("poolname", "");
			} else {
				tObj.put("poolid", poolId);
				tObj.put(
						"poolname",
						Utilities.encodeText(this.getPoolDAO().getPool(poolId)
								.getPoolName()));
			}
			String rackUuid = result.getRackUuid();
			if (rackUuid == null || rackUuid == "") {
				tObj.put("rackUuid", "");
				tObj.put("rackName", "");
			} else {
				tObj.put("rackUuid", rackUuid);
				tObj.put(
						"rackName",
						Utilities.encodeText(this.getRackDAO()
								.getRack(rackUuid).getRackName()));
			}
			tObj.put("srsize", this.getStorageDAO().getStorageSize(hostUuid));
			qaArray.put(tObj);
		}
		return qaArray;
	}

	public JSONArray getHostLoadList(int page, int limit, String searchStr,
			String srUuid) {
		int totalNum = this.getHostDAO().countAllHostList(searchStr);
		JSONArray qaArray = new JSONArray();
		List<OCHost> hostList = this.getHostDAO().getOnePageLoadHostList(page,
				limit, searchStr, srUuid);
		qaArray.put(totalNum);
		for (OCHost result : hostList) {
			JSONObject tObj = new JSONObject();
			tObj.put("hostname", Utilities.encodeText(result.getHostName()));
			tObj.put("hostdesc", Utilities.encodeText(result.getHostDesc()));
			tObj.put("hostid", result.getHostUuid());
			String poolId = result.getPoolUuid();
			if (poolId == null) {
				poolId = "";
			}
			tObj.put("poolid", poolId);
			qaArray.put(tObj);
		}
		return qaArray;
	}

	public JSONArray getSrOfHost(String hostUuid) {
		List<Storage> srOfHost = this.getHostDAO().getSROfHost(hostUuid);
		JSONArray qaArray = new JSONArray();
		for (int i = 0; i < srOfHost.size(); i++) {
			Storage sr = srOfHost.get(i);
			JSONObject obj = new JSONObject();
			obj.put("sruuid", sr.getSrUuid());
			obj.put("srname", Utilities.encodeText(sr.getSrName()));
			obj.put("srip", sr.getSrAddress());
			obj.put("srdir", sr.getSrdir());
			obj.put("srtype", sr.getSrtype());
			qaArray.put(obj);
		}
		return qaArray;
	}

	public void unbindSr(String hostUuid, String srUuid, int userid) {
		if (this.getHostDAO().unbindSr(hostUuid, srUuid)) {
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess("解绑成功"));
		} else {
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToError("解绑失败"));
		}
	}

	public JSONArray isSameSr(String uuidJsonStr) {
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uuidJsonStr);
		JsonArray ja = je.getAsJsonArray();
		JSONArray qaArray = new JSONArray();
		boolean isSame = false;
		if (ja.size() > 0) {
			if (ja.size() > 1) {
				String firstUuid = ja.get(0).getAsString();
				List<Storage> srlist1 = this.getHostDAO().getSROfHost(firstUuid);
				Set<String> srSet1 = new HashSet<String>();
				for (Storage sr : srlist1) {
					srSet1.add(sr.getSrUuid());
				}
				for (int i = 1; i < ja.size(); i++) {
					String uuid = ja.get(i).getAsString();
					List<Storage> srlist2 = this.getHostDAO().getSROfHost(uuid);
					Set<String> srSet2 = new HashSet<String>();
					for (Storage sr : srlist2) {
						srSet2.add(sr.getSrUuid());
					}
					if (isSameSr(srSet1, srSet2)) {
						isSame = true;
						break;
					}
				}
				if (isSame) {
					JSONObject tObj1 = new JSONObject();
					tObj1.put("isSuccess", true);
					qaArray.put(tObj1);
				} else {
					JSONObject tObj = new JSONObject();
					tObj.put("isSuccess", false);
					qaArray.put(tObj);
				}
			} else {
				JSONObject tObj = new JSONObject();
				tObj.put("isSuccess", true);
				qaArray.put(tObj);
			}
		} else {
			JSONObject tObj = new JSONObject();
			tObj.put("isSuccess", false);
			qaArray.put(tObj);
		}
		return qaArray;
	}

	public JSONArray getTablePool(String uuidJsonStr) {
		JsonParser jp = new JsonParser();
		JSONArray qaArray = new JSONArray();
		JsonElement je = jp.parse(uuidJsonStr);
		JsonArray ja = je.getAsJsonArray();
		String hostUuid = ja.get(0).getAsString();
		List<Storage> srlist1 = this.getHostDAO().getSROfHost(hostUuid);
		Set<String> srSet1 = new HashSet<String>();
		for (Storage sr : srlist1) {
			srSet1.add(sr.getSrUuid());
		}
		List<OCPool> poolList = this.getPoolDAO().getPoolList();
		for (OCPool pool : poolList) {
			String poolUuid = pool.getPoolUuid();
			String poolName = pool.getPoolName();
			String poolMaster = pool.getPoolMaster();
			if (poolMaster != null) {
				List<Storage> poolsrlist = this.getHostDAO().getSROfHost(
						poolMaster);
				Set<String> poolsrSet = new HashSet<String>();
				for (Storage sr : poolsrlist) {
					poolsrSet.add(sr.getSrUuid());
				}
				if (isSameSr(srSet1, poolsrSet)) {
					JSONObject tObj = new JSONObject();
					tObj.put("pooluuid", poolUuid);
					tObj.put("poolname", Utilities.encodeText(poolName));
					tObj.put("hasmaster", 1);
					qaArray.put(tObj);
				}
			} else {
				JSONObject tObj = new JSONObject();
				tObj.put("pooluuid", poolUuid);
				tObj.put("poolname", Utilities.encodeText(poolName));
				tObj.put("hasmaster", 0);
				qaArray.put(tObj);
			}
		}
		return qaArray;
	}

	private boolean isSameSr(Set<String> sr1, Set<String> sr2) {
		if (sr1.size() != sr2.size()) {
			return false;
		} else {
			sr1.retainAll(sr2);
			if (sr1.size() == sr2.size()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public JSONArray deleteHost(String hostId, String hostName, int userid) {
		Date startTime = new Date();
		JSONArray qaArray = new JSONArray();
		boolean result = this.getHostDAO().deleteHost(hostId);
		result = this.getOverViewDAO().updateOverViewfield("viewServer", false);
		JSONObject tObj = new JSONObject();
		tObj.put("result", result);
		qaArray.put(tObj);
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.服务器.toString(), hostName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.服务器.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.服务器.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return qaArray;
	}

	public JSONArray queryAddress(String address) {
		OCHost query = this.getHostDAO().getHostFromIp(address);
		JSONObject tObj = new JSONObject();
		JSONArray qaArray = new JSONArray();
		if (query != null) {
			tObj.put("exist", true);
		} else {
			tObj.put("exist", false);
		}
		qaArray.put(tObj);
		return qaArray;
	}

	public JSONArray add2Pool(String uuidJsonStr, String hasMaster,
			String poolUuid, int userid) {
		Date startTime = new Date();
		JSONArray qaArray = new JSONArray();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uuidJsonStr);
		JsonArray ja = je.getAsJsonArray();
		if (hasMaster.equals("1")) {
			String pname = this.getPoolDAO().getPool(poolUuid).getPoolName();
			for (int i = 0; i < ja.size(); i++) {
				String hostUuid = ja.get(i).getAsString();
				boolean result = addHostToPool(hostUuid, poolUuid);

				Date endTime = new Date();
				int elapse = Utilities.timeElapse(startTime, endTime);
				JSONArray infoArray = new JSONArray();
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.服务器.toString(), "host-"
								+ hostUuid.substring(0, 8)));
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.资源池.toString(), pname));
				if (result) {
					JSONObject tObj = new JSONObject();
					tObj.put("huid", hostUuid);
					tObj.put("puid", poolUuid);
					tObj.put("pname", Utilities.encodeText(pname));
					qaArray.put(tObj);
					OCLog log = this.getLogDAO().insertLog(userid,
							LogConstant.logObject.服务器.ordinal(),
							LogConstant.logAction.加入.ordinal(),
							LogConstant.logStatus.成功.ordinal(),
							infoArray.toString(), startTime, elapse);
					this.getMessagePush().pushMessage(userid,
							Utilities.stickyToSuccess(log.toString()));
				} else {
					OCLog log = this.getLogDAO().insertLog(userid,
							LogConstant.logObject.服务器.ordinal(),
							LogConstant.logAction.加入.ordinal(),
							LogConstant.logStatus.失败.ordinal(),
							infoArray.toString(), startTime, elapse);
					this.getMessagePush().pushMessage(userid,
							Utilities.stickyToError(log.toString()));
				}
			}
		} else {
			String masterUuid = ja.get(0).getAsString();
			String pname = this.getPoolDAO().getPool(poolUuid).getPoolName();
			boolean result = createPool(masterUuid, poolUuid);
			Date endTime = new Date();
			int elapse = Utilities.timeElapse(startTime, endTime);
			JSONArray infoArray = new JSONArray();
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.服务器.toString(),
					"host-" + masterUuid.substring(0, 8)));
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.资源池.toString(), pname));
			if (result) {
				OCLog log = this.getLogDAO().insertLog(userid,
						LogConstant.logObject.主节点.ordinal(),
						LogConstant.logAction.创建.ordinal(),
						LogConstant.logStatus.成功.ordinal(),
						infoArray.toString(), startTime, elapse);
				this.getMessagePush().pushMessage(userid,
						Utilities.stickyToSuccess(log.toString()));

				JSONObject tObj1 = new JSONObject();
				tObj1.put("huid", ja.get(0).getAsString());
				tObj1.put("puid", poolUuid);
				tObj1.put("pname", Utilities.encodeText(pname));
				qaArray.put(tObj1);
				for (int i = 1; i < ja.size(); i++) {
					Date sTime = new Date();
					boolean addResult = addHostToPool(ja.get(i).getAsString(),
							poolUuid);
					Date eTime = new Date();
					int elap = Utilities.timeElapse(sTime, eTime);
					JSONArray infoArray1 = new JSONArray();
					infoArray1.put(Utilities.createLogInfo(
							LogConstant.logObject.服务器.toString(), "host-"
									+ masterUuid.substring(0, 8)));
					infoArray1.put(Utilities.createLogInfo(
							LogConstant.logObject.资源池.toString(), pname));
					if (addResult) {
						JSONObject tObj = new JSONObject();
						tObj.put("huid", ja.get(i).getAsString());
						tObj.put("puid", poolUuid);
						tObj.put("pname", Utilities.encodeText(pname));
						qaArray.put(tObj);
						OCLog log1 = this.getLogDAO().insertLog(userid,
								LogConstant.logObject.服务器.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.成功.ordinal(),
								infoArray.toString(), startTime, elap);
						this.getMessagePush().pushMessage(userid,
								Utilities.stickyToSuccess(log1.toString()));
					} else {
						OCLog log1 = this.getLogDAO().insertLog(userid,
								LogConstant.logObject.服务器.ordinal(),
								LogConstant.logAction.加入.ordinal(),
								LogConstant.logStatus.失败.ordinal(),
								infoArray.toString(), startTime, elap);
						this.getMessagePush().pushMessage(userid,
								Utilities.stickyToError(log1.toString()));
					}
				}
			} else {
				OCLog log = this.getLogDAO().insertLog(userid,
						LogConstant.logObject.主节点.ordinal(),
						LogConstant.logAction.创建.ordinal(),
						LogConstant.logStatus.失败.ordinal(),
						infoArray.toString(), startTime, elapse);
				this.getMessagePush().pushMessage(userid,
						Utilities.stickyToError(log.toString()));
			}
		}
		return qaArray;
	}

	public JSONArray r4Pool(String hostUuid, int userid) {
		JSONArray qaArray = new JSONArray();
		Date startTime = new Date();
		String poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		if (poolUuid != null) {
			boolean result = ejectHostFromPool(hostUuid);
			// write log and push message
			Date endTime = new Date();
			int elapse = Utilities.timeElapse(startTime, endTime);
			JSONArray infoArray = new JSONArray();
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.服务器.toString(),
					"host-" + hostUuid.substring(0, 8)));
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.资源池.toString(),
					"pool-" + poolUuid.substring(0, 8)));
			if (result) {
				JSONObject tObj = new JSONObject();
				tObj.put("isSuccess", true);
				qaArray.put(tObj);
				OCLog log = this.getLogDAO().insertLog(userid,
						LogConstant.logObject.服务器.ordinal(),
						LogConstant.logAction.离开.ordinal(),
						LogConstant.logStatus.成功.ordinal(),
						infoArray.toString(), startTime, elapse);
				this.getMessagePush().pushMessage(userid,
						Utilities.stickyToSuccess(log.toString()));
			} else {
				JSONObject tObj = new JSONObject();
				tObj.put("isSuccess", false);
				qaArray.put(tObj);
				OCLog log = this.getLogDAO().insertLog(userid,
						LogConstant.logObject.服务器.ordinal(),
						LogConstant.logAction.离开.ordinal(),
						LogConstant.logStatus.失败.ordinal(),
						infoArray.toString(), startTime, elapse);
				this.getMessagePush().pushMessage(userid,
						Utilities.stickyToError(log.toString()));
			}
		} else {
			JSONObject tObj = new JSONObject();
			tObj.put("isSuccess", false);
			qaArray.put(tObj);
		}
		return qaArray;
	}

	public JSONArray getOneHost(String hostid) {
		OCHost ochost = this.getHostDAO().getHost(hostid);
		JSONArray qaArray = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("hostName", Utilities.encodeText(ochost.getHostName()));
		jo.put("createDate", Utilities.formatTime(ochost.getCreateDate()));
		String timeUsed = Utilities.encodeText(Utilities.dateToUsed(ochost
				.getCreateDate()));
		jo.put("useDate", timeUsed);
		jo.put("hostCPU", ochost.getHostCpu());
		jo.put("hostMemory", ochost.getHostMem());
		jo.put("hostIP", ochost.getHostIP());
		jo.put("hostUuid", hostid);
		jo.put("hostPwd", ochost.getHostPwd());
		jo.put("hostDesc", Utilities.encodeText(ochost.getHostDesc()));
		jo.put("hostkernel", ochost.getKernelVersion());
		jo.put("hostXen", ochost.getXenVersion());
		String poolid = ochost.getPoolUuid();
		if (poolid == null || poolid == "") {
			jo.put("poolId", "");
			jo.put("poolName", "");
		} else {
			OCPool ocpool = this.getPoolDAO().getPool(poolid);
			jo.put("poolId", ocpool.getPoolUuid());
			jo.put("poolName", Utilities.encodeText(ocpool.getPoolName()));
		}
		qaArray.put(jo);
		Set<String> srlist = this.getHostSRDAO().getSRList(hostid);
		for (String sr : srlist) {
			JSONObject josr = new JSONObject();
			Storage srdao = this.getStorageDAO().getStorage(sr);
			josr.put("srId", srdao.getSrUuid());
			josr.put("srName", Utilities.encodeText(srdao.getSrName()));
			qaArray.put(josr);
		}
		return qaArray;
	}

	@SuppressWarnings("deprecation")
	private boolean addHostToPool(String hostUuid, String poolUuid) {
		boolean result = false;
		try {
			OCPool pool = this.getPoolDAO().getPool(poolUuid);
			String masterUuid = pool.getPoolMaster();
			if (masterUuid != null) {
				OCHost masterHost = this.getHostDAO().getHost(masterUuid);
				OCHost targetHost = this.getHostDAO().getHost(hostUuid);
				if (masterHost != null & targetHost != null) {
					boolean checkResult = this.getSrManager()
							.checkSREquals(masterUuid, hostUuid);
					if (checkResult == true) {
						Connection conn = new Connection("http://"
								+ targetHost.getHostIP() + ":9363",
								HostManager.DEFAULT_USER,
								targetHost.getHostPwd());
						Pool.join(conn, masterHost.getHostIP(),
								HostManager.DEFAULT_USER,
								masterHost.getHostPwd());
						this.getHostDAO().setPool(hostUuid, poolUuid);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private OCHost getPoolMaster(String poolUuid) {
		OCHost master = null;
		if (poolUuid != null) {
			OCPool pool = this.getPoolDAO().getPool(poolUuid);
			if (pool != null) {
				String masterUuid = pool.getPoolMaster();
				if (masterUuid != null) {
					master = this.getHostDAO().getHost(masterUuid);
				}
			}
		}
		return master;
	}

	@SuppressWarnings("deprecation")
	private boolean ejectHostFromPool(String hostUuid) {
		boolean result = false;
		OCHost host = this.getHostDAO().getHost(hostUuid);
		String poolUuid = host.getPoolUuid();
		OCHost master = this.getPoolMaster(poolUuid);
		String masterUuid = master.getHostUuid();
		try {
			if (host != null && poolUuid != null) {
				if (master != null) {
					Connection conn = new Connection("http://"
							+ master.getHostIP() + ":9363",
							HostManager.DEFAULT_USER, master.getHostPwd());
					Host ejectHost = Types.toHost(hostUuid);
					Pool.eject(conn, ejectHost);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == true) {
			OCPool pool = this.getPoolDAO().getPool(poolUuid);
			if (host.getHostUuid().equals(masterUuid)) {
				pool.setPoolMaster(null);
				this.getPoolDAO().update(pool);
			}
			host.setPoolUuid(null);
			result = this.getHostDAO().update(host);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private boolean createPool(String hostUuid, String poolUuid) {
		boolean result = false;
		try {
			OCHost targetHost = this.getHostDAO().getHost(hostUuid);
			OCPool pool = this.getPoolDAO().getPool(poolUuid);
			if (targetHost.getPoolUuid() == null && pool != null) {
				Connection conn = new Connection("http://"
						+ targetHost.getHostIP() + ":9363",
						HostManager.DEFAULT_USER, targetHost.getHostPwd());
				Pool.create(conn, poolUuid);
				targetHost.setPoolUuid(pool.getPoolUuid());
				pool.setPoolMaster(targetHost.getHostUuid());
				result = this.getHostDAO().update(targetHost);
				result = this.getPoolDAO().update(pool);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private OCHost addHost(String hostName, String hostPwd, String hostDesc,
			String hostIp, String rackUuid) {
		OCHost exist = this.getHostDAO().getHostFromIp(hostIp);
		if (exist != null) {
			return null;
		}
		OCHost host = null;
		try {
			Connection conn = new Connection("http://" + hostIp + ":"
					+ DEFAULT_PORT, DEFAULT_USER, hostPwd);
			// Add Host
			Map<Host, Host.Record> hostMap = Host.getAllRecords(conn);
			for (Host thisHost : hostMap.keySet()) {
				Host.Record hostRecord = hostMap.get(thisHost);
				String hostUuid = hostRecord.uuid;
				String hostPassword = hostPwd;
				int hostMem = (int) (hostRecord.memoryTotal / MB);
				int hostCpu = hostRecord.hostCPUs.size();
				String kernelVersion = hostRecord.softwareVersion.get("system")
						+ " " + hostRecord.softwareVersion.get("release");
				String xenVersion = "Xen "
						+ hostRecord.softwareVersion.get("Xen");
				int hostStatus = 1;
				Date current = new Date();
				host = new OCHost(hostUuid, hostPassword, hostName, hostDesc,
						hostIp, hostMem, hostCpu, kernelVersion, xenVersion,
						hostStatus, rackUuid, current);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (host != null) {
			this.getHostDAO().saveHost(host);
			this.getOverViewDAO().updateOverViewfield("viewServer", true);
		}
		return host;
	}

	public boolean updateHost(String hostId, String hostName, String hostDesc,
			String rackUuid) {
		return this.getHostDAO().updateHost(hostId, hostName, hostDesc,
				rackUuid);
	}

	public JSONArray getAllList() {
		JSONArray ja = new JSONArray();
		List<OCHost> list = this.getHostDAO().getAllHost();
		if (list != null) {
			for (OCHost oh : list) {
				JSONObject jo = new JSONObject();
				jo.put("hostName", oh.getHostName());
				jo.put("hostUuid", oh.getHostUuid());
				ja.put(jo);
			}
		}
		return ja;
	}
	
	public boolean recover(int userId, String ip, String username,
			String password, String content, String conid) {
		SSH ssh = new SSH(ip, username, password);
		if (!ssh.Connect()) {
			this.messagePush.pushMessageClose(userId, content, conid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("IP不存在，或者用户名密码不匹配"));
			return false;
		}
		try {
			int code = ssh.Command("/etc/init.d/xend restart");
			if (code != 0) {
				this.messagePush.pushMessageClose(userId, content, conid);
				this.getMessagePush().pushMessage(userId,
						Utilities.stickyToError("修复中出现未知错误"));
				return false;
			} else {
				this.messagePush.pushMessageClose(userId, content, conid);
				this.getMessagePush().pushMessage(userId,
						Utilities.stickyToSuccess("修复成功"));
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.messagePush.pushMessageClose(userId, content, conid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("修复中出现未知错误"));
			return false;
		}
	}

	public JSONArray getHostListForMigration(String vmuuid) {
		String hostuuid = this.getVmDAO().getVM(vmuuid).getHostUuid();
		OCHost ochost = this.getHostDAO().getHost(hostuuid);
		String pooluuid = ochost.getPoolUuid();
		List<OCHost> list = this.getHostDAO().getHostListOfPool(pooluuid);
		JSONArray ja = new JSONArray();
		if (list.size() > 0) {
			for (OCHost oh : list) {
				if (oh.getHostUuid().equals(hostuuid))
					continue;
				JSONObject jo = new JSONObject();
				jo.put("ip", oh.getHostIP());
				jo.put("uuid", oh.getHostUuid());
				ja.put(jo);
			}
		}
		return ja;
	}
}
