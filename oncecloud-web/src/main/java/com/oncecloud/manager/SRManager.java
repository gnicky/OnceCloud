package com.oncecloud.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.SR;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.HostSRDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.RackDAO;
import com.oncecloud.dao.StorageDAO;
import com.oncecloud.entity.HostSR;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.Storage;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author hehai
 * @version 2014/08/26
 */
@Component
public class SRManager {
	private StorageDAO storageDAO;
	private HostDAO hostDAO;
	private HostSRDAO hostSRDAO;
	private LogDAO logDAO;
	private RackDAO rackDAO;
	private MessagePush messagePush;
	private Constant constant;

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private StorageDAO getStorageDAO() {
		return storageDAO;
	}

	@Autowired
	private void setStorageDAO(StorageDAO storageDAO) {
		this.storageDAO = storageDAO;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	private HostSRDAO getHostSRDAO() {
		return hostSRDAO;
	}

	@Autowired
	private void setHostSRDAO(HostSRDAO hostSRDAO) {
		this.hostSRDAO = hostSRDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private RackDAO getRackDAO() {
		return rackDAO;
	}

	@Autowired
	private void setRackDAO(RackDAO rackDAO) {
		this.rackDAO = rackDAO;
	}

	public Constant getConstant() {
		return constant;
	}

	@Autowired
	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	@SuppressWarnings("deprecation")
	public boolean addStorage2Server(String srUuid, String hostUuid) {
		boolean result = false;
		try {
			Storage storage = this.getStorageDAO().getStorage(srUuid);
			OCHost targetHost = this.getHostDAO().getHost(hostUuid);
			Connection conn = new Connection("http://" + targetHost.getHostIP()
					+ ":" + HostManager.DEFAULT_PORT, HostManager.DEFAULT_USER,
					targetHost.getHostPwd());
			boolean checkResult = Host.checkSR(conn, storage.getSrAddress(),
					storage.getSrdir(), storage.getSrtype());
			if (checkResult == true) {
				boolean activeResult = Host.activeSR(conn,
						storage.getDiskuuid(), storage.getIsouuid(),
						storage.getHauuid(), storage.getSrdir(),
						storage.getSrtype());
				if (activeResult == true) {
					HostSR hsr = this.getHostSRDAO().createHostSR(hostUuid,
							srUuid);
					if (hsr != null) {
						result = true;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkSREquals(String masterHost, String targetHost) {
		Set<String> masterSR = this.getHostSRDAO().getSRList(masterHost);
		Set<String> hostSR = this.getHostSRDAO().getSRList(targetHost);
		if (masterSR.size() != hostSR.size()) {
			return false;
		} else {
			masterSR.retainAll(hostSR);
			if (masterSR.size() != hostSR.size()) {
				return false;
			} else {
				return true;
			}
		}
	}

	public JSONArray addStorage(int userId, String srname, String srAddress,
			String srDesc, String srType, String srDir, String rackId,
			String rackName) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		Storage result = this.getStorageDAO().createStorage(srname, srAddress,
				srDesc, srType, srDir, rackId);
		if (result != null) {
			JSONObject tObj = new JSONObject();
			tObj.put("srname", Utilities.encodeText(result.getSrName()));
			tObj.put("srid", result.getSrUuid());
			tObj.put("srAddress", result.getSrAddress());
			tObj.put("createDate", Utilities.formatTime(result.getCreateDate()));
			tObj.put("srType", result.getSrtype());
			tObj.put("srDir", result.getSrdir());
			tObj.put("srDesc", Utilities.encodeText(result.getSrDesc()));
			tObj.put("srsize", 0);
			tObj.put("rackid", rackId);
			tObj.put("rackname", Utilities.encodeText(rackName));
			ja.put(tObj);
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.存储.toString(), srname));
		if (result != null) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.存储.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.存储.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	/**
	 * 获取存储列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getStorageList(int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getStorageDAO().countAllStorageList(search);
		ja.put(totalNum);
		List<Storage> srList = this.getStorageDAO().getOnePageStorageList(page,
				limit, search);
		if (srList != null) {
			for (Storage sr : srList) {
				JSONObject tObj = new JSONObject();
				String srUuid = sr.getSrUuid();
				tObj.put("srname", Utilities.encodeText(sr.getSrName()));
				tObj.put("srid", srUuid);
				tObj.put("srAddress", sr.getSrAddress());
				tObj.put("createDate", Utilities.formatTime(sr.getCreateDate()));
				tObj.put("srType", sr.getSrtype());
				tObj.put("srDir", sr.getSrdir());
				tObj.put("srsize",
						this.getStorageDAO().countHostsOfStorage(srUuid));
				tObj.put("srDesc", Utilities.encodeText(sr.getSrDesc()));
				String rackid = sr.getRackuuid();
				if (rackid == null) {
					tObj.put("rackid", "");
					tObj.put("rackname", "");
				} else {
					tObj.put("rackid", rackid);
					tObj.put(
							"rackname",
							Utilities.encodeText(this.getRackDAO()
									.getRack(rackid).getRackName()));
				}
				ja.put(tObj);
			}
		}
		return ja;
	}

	public JSONArray deleteStorage(int userId, String srId, String srName) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		boolean result = this.getStorageDAO().removeStorage(srId);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.存储.toString(), srName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.存储.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.存储.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray load2Server(int userId, String srUuid, String hostUuid) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		boolean result = this.addStorage2Server(srUuid, hostUuid);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.存储.toString(),
				"sr-" + srUuid.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.服务器.toString(),
				"host-" + hostUuid.substring(0, 8)));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.存储.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.存储.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray getStorageByAddress(String address) {
		JSONArray ja = new JSONArray();
		Storage sr = this.getStorageDAO().getStorageOfAddress(address);
		JSONObject jo = new JSONObject();
		if (sr != null) {
			jo.put("exist", true);
		} else {
			jo.put("exist", false);
		}
		ja.put(jo);
		return ja;
	}

	public void updateStorage(int userId, String srId, String srName,
			String srDesc, String rackId) {
		boolean result = this.getStorageDAO().updateSR(srId, srName, srDesc,
				rackId);
		// push message
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("存储更新成功"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("存储更新失败"));
		}
	}
	
	public JSONArray getRealSRList(String poolUuid) {
		JSONArray ja = new JSONArray();
		Connection conn = null;
		try {
			conn = this.getConstant().getConnectionFromPool(poolUuid);
			Map<SR, SR.Record> srList = SR.getAllRecords(conn);
			for (SR sr : srList.keySet()) {
				SR.Record record = srList.get(sr);		
				String srType = record.type;
				if (true) {
					JSONObject jo = new JSONObject();
					jo.put("type", srType);
					jo.put("uuid", record.uuid);
					jo.put("name", record.nameLabel);
					ja.put(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}
}
