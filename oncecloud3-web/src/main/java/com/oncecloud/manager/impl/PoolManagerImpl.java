package com.oncecloud.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.VM;
import com.oncecloud.dao.DatacenterDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.PoolDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCPool;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.HostManager;
import com.oncecloud.manager.PoolManager;
import com.oncecloud.message.MessagePush;

@Component("PoolManager")
public class PoolManagerImpl implements PoolManager {

	private PoolDAO poolDAO;
	private HostDAO hostDAO;
	private DatacenterDAO datacenterDAO;
	private LogDAO logDAO;
	private OverViewDAO overViewDAO;
	private VMDAO vmDAO;
	private RouterDAO routerDAO;
	private LBDAO lbDAO;
	
	private MessagePush messagePush;
	
	private PoolDAO getPoolDAO() {
		return poolDAO;
	}

	@Autowired
	private void setPoolDAO(PoolDAO poolDAO) {
		this.poolDAO = poolDAO;
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

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}
	
	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	public RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	public void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	public LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	public void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}
	
	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public JSONArray createPool(String poolName, String poolDesc,
			String dcUuid, String dcName, int userId) {
		Date startTime = new Date();
		OCPool result = this.getPoolDAO()
				.createPool(poolName, poolDesc, dcUuid);
		boolean overview = this.getOverViewDAO().updateOverViewfield("viewPool",
				true);
		JSONArray ja = new JSONArray();
		if (result != null && overview) {
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
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
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
				List<Integer> volumeList = this.getPoolVolume(poolUuid);
				jo.put("totalcpu", volumeList.get(0));
				jo.put("totalmem", volumeList.get(1));
				ja.put(jo);
			}
		}
		return ja;
	}
	
	private List<Integer> getPoolVolume(String poolUuid) {
		int totalCpu = 0;
		int totalMemory = 0;
		List<OCHost> hostList = this.getHostDAO().getHostListOfPool(poolUuid);
		if (hostList != null) {
			for (OCHost host : hostList) {
				totalCpu += host.getHostCpu();
				totalMemory += host.getHostMem();
			}
		}
		List<Integer> volumeList = new ArrayList<Integer>();
		volumeList.add(totalCpu);
		volumeList.add(totalMemory);
		return volumeList;
	}

	public JSONArray deletePool(String poolId, String poolName, int userId) {
		Date startTime = new Date();
		boolean result = this.getPoolDAO().deletePool(poolId);
		result = this.getOverViewDAO().updateOverViewfield("viewPool", false);
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
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.资源池.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public void updatePool(String poolUuid, String poolName, String poolDesc,
			String dcUuid, int userId) {
		boolean result = this.getPoolDAO().updatePool(poolUuid, poolName,
				poolDesc, dcUuid);
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("资源池更新成功"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("资源池更新失败"));
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
					String name = vmRecord.nameLabel;
					if (name.contains("i-")) {
						srList.add(new SimpleRecord(vmRecord.uuid,
								vmRecord.powerState.toString(),
								vmRecord.residentOn.toWireString(), "instance"));
					} else if (name.contains("rt-")) {
						srList.add(new SimpleRecord(vmRecord.uuid,
								vmRecord.powerState.toString(),
								vmRecord.residentOn.toWireString(), "router"));
					} else if (name.contains("lb-")) {
						srList.add(new SimpleRecord(vmRecord.uuid,
								vmRecord.powerState.toString(),
								vmRecord.residentOn.toWireString(),
								"loadbalance"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			srList = null;
		}
		if (srList != null) {
			try {
				for (SimpleRecord sr : srList) {
					String uuid = sr.getUuid();
					String powerStatus = sr.powerStatus;
					String hostUuid = sr.hostUuid;
					int power = powerStatus.equals("Running") ? 1 : 0;
					String type = sr.getType();
					if (type.equals("instance")) {
						this.getVmDAO().updatePowerAndHost(uuid,
								power, hostUuid);
					} else if (type.equals("router")) {
						this.getRouterDAO().updatePowerAndHost(
								uuid, power, hostUuid);
					} else if (type.equals("loadbalance")) {
						this.getLbDAO().updatePowerAndHost(uuid,
								power, hostUuid);
					}
				}
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// push message
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("资源池状态已保持一致"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("资源池状态未保持一致"));
		}
	}

	public JSONObject getPoolHa(String poolUuid) {
		// TODO Auto-generated method stub
		OCPool ocpool = this.getPoolDAO().getPool(poolUuid);
		OCHost ochost = this.getHostDAO().getHost(ocpool.getPoolMaster());
		JSONObject jsonobject =new JSONObject();
		jsonobject.put("hapath", ocpool.getHaPath());
		jsonobject.put("masterip", ochost.getHostIP());
		return jsonobject;
	}
	
	public String StartHa(String poolUuid,String masterIP,String haPath) {
		// TODO Auto-generated method stub
		saveHaPath(poolUuid,haPath);
		
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://127.0.0.1/haPool/start");
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("poolUUID", poolUuid),
					new NameValuePair("masterIP",masterIP), new NameValuePair("haPath", haPath)};
			post.setRequestBody(data);
			client.executeMethod(post);
			String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
			System.out.println(result); // 打印返回消息状态
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "失败";
		} finally {
			post.releaseConnection();
		}
	}
	
	public String StopHa(String poolUuid,String masterIP,String haPath) {
		// TODO Auto-generated method stub
		saveHaPath(poolUuid,haPath);
		
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://127.0.0.1/haPool/stop");
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("poolUUID", poolUuid)};
			post.setRequestBody(data);
			client.executeMethod(post);
			String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
			System.out.println(result); // 打印返回消息状态
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "失败";
		} finally {
			post.releaseConnection();
		}
	}
	
	private boolean saveHaPath(String poolUuid,String haPath)
	{
		OCPool ocpool = this.getPoolDAO().getPool(poolUuid);
		ocpool.setHaPath(haPath);
		this.getPoolDAO().update(ocpool);
		return true;
	}
}

class SimpleRecord {
	String uuid;
	String powerStatus;
	String hostUuid;
	String type;

	public SimpleRecord(String uuid, String powerStatus, String hostUuid,
			String type) {
		this.uuid = uuid;
		this.powerStatus = powerStatus;
		this.hostUuid = hostUuid;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SimpleRecord [uuid=" + uuid + ", powerStatus=" + powerStatus
				+ ", hostUuid=" + hostUuid + ", type=" + type + "]";
	}
}

