package com.oncecloud.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.DatacenterDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.PoolDAO;
import com.oncecloud.dao.RackDAO;
import com.oncecloud.dao.StorageDAO;
import com.oncecloud.dao.SwitchDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.Datacenter;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCPool;
import com.oncecloud.entity.OverView;
import com.oncecloud.entity.Rack;
import com.oncecloud.entity.Storage;
import com.oncecloud.entity.Switch;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;

@Component
public class DatacenterManager {
	private DatacenterDAO datacenterDAO;
	private LogDAO logDAO;
	private OverViewDAO overViewDAO;
	private PoolDAO poolDAO;
	private HostDAO hostDAO;
	private StorageDAO storageDAO;
	private ImageDAO imageDAO;
	private VMDAO vmDAO;
	private RackDAO rackDAO;
	private SwitchDAO switchDAO;

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

	private OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	private void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

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

	private StorageDAO getStorageDAO() {
		return storageDAO;
	}

	@Autowired
	private void setStorageDAO(StorageDAO storageDAO) {
		this.storageDAO = storageDAO;
	}

	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private RackDAO getRackDAO() {
		return rackDAO;
	}

	@Autowired
	private void setRackDAO(RackDAO rackDAO) {
		this.rackDAO = rackDAO;
	}

	private SwitchDAO getSwitchDAO() {
		return switchDAO;
	}

	@Autowired
	private void setSwitchDAO(SwitchDAO switchDAO) {
		this.switchDAO = switchDAO;
	}

	public JSONArray createDatacenter(String dcName, String dcLocation,
			String dcDesc, int userid) {
		Date startTime = new Date();
		JSONArray qaArray = new JSONArray();
		Datacenter result = this.getDatacenterDAO().createDatacenter(dcName,
				dcLocation, dcDesc);
		if (result != null) {
			JSONObject tObj = new JSONObject();
			tObj.put("dcname", Utilities.encodeText(dcName));
			tObj.put("dcid", result.getDcUuid());
			tObj.put("dclocation", Utilities.encodeText(dcLocation));
			tObj.put("dcdesc", Utilities.encodeText(dcDesc));
			tObj.put("createdate", Utilities.formatTime(result.getCreateDate()));
			qaArray.put(tObj);
		}
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.数据中心.toString(), dcName));
		if (result != null) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.数据中心.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.数据中心.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return qaArray;
	}

	public JSONArray getDatacenterList(int page, int limit, String search) {
		JSONArray qaArray = new JSONArray();
		int totalNum = this.getDatacenterDAO().countAllDatacenter(search);
		List<Datacenter> dcList = this.getDatacenterDAO().getOnePageDCList(
				page, limit, search);
		qaArray.put(totalNum);
		if (dcList != null) {
			for (Datacenter dc : dcList) {
				JSONObject tObj = new JSONObject();
				tObj.put("dcname", Utilities.encodeText(dc.getDcName()));
				String dcUuid = dc.getDcUuid();
				tObj.put("dcid", dcUuid);
				tObj.put("dclocation", Utilities.encodeText(dc.getDcLocation()));
				tObj.put("dcdesc", Utilities.encodeText(dc.getDcDesc()));
				tObj.put("createdate", Utilities.formatTime(dc.getCreateDate()));
				List<Integer> volumeList = this.getDCVolume(dcUuid);
				tObj.put("totalcpu", volumeList.get(0));
				tObj.put("totalmem", volumeList.get(1));
				qaArray.put(tObj);
			}
		}
		return qaArray;
	}

	public JSONArray getDatacenterAllList() {
		JSONArray qaArray = new JSONArray();
		List<Datacenter> dcList = this.getDatacenterDAO().getAllPageDCList();
		if (dcList != null) {
			for (Datacenter dc : dcList) {
				JSONObject tObj = new JSONObject();
				tObj.put("dcname", Utilities.encodeText(dc.getDcName()));
				tObj.put("dcid", dc.getDcUuid());
				qaArray.put(tObj);
			}
		}
		return qaArray;
	}

	public JSONArray deleteDatacenter(String dcId, String dcName, int userid) {
		Date startTime = new Date();
		JSONArray qaArray = new JSONArray();
		boolean result = this.getDatacenterDAO().deleteDatacenter(dcId);
		JSONObject tObj = new JSONObject();
		tObj.put("result", result);
		qaArray.put(tObj);
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.数据中心.toString(), dcName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.数据中心.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.数据中心.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return qaArray;
	}

	public JSONArray getOverview() {
		JSONArray qaArray = new JSONArray();
		OverView ov = this.getOverViewDAO().getOverViewTotal();
		JSONObject tObj = new JSONObject();
		tObj.put("viewId", ov.getViewId());
		tObj.put("viewDc", ov.getViewDc());
		tObj.put("viewDhcp", ov.getViewDhcp());
		tObj.put("viewFirewall", ov.getViewFirewall());
		tObj.put("viewImage", ov.getViewImage());
		tObj.put("viewOutip", ov.getViewOutip());
		tObj.put("viewPool", ov.getViewPool());
		tObj.put("viewRack", ov.getViewRack());
		tObj.put("viewServer", ov.getViewServer());
		tObj.put("viewSr", ov.getViewSr());
		tObj.put("viewVm", ov.getViewVm());
		qaArray.put(tObj);
		return qaArray;
	}

	public void update(String dcUuid, String dcName, String dcLocation,
			String dcDesc, int userid) {
		boolean result = this.getDatacenterDAO().updateDatacenter(dcUuid,
				dcName, dcLocation, dcDesc);
		if (result) {
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess("数据中心更新成功"));
		} else {
			MessagePush
					.pushMessage(userid, Utilities.stickyToError("数据中心更新失败"));
		}
	}

	public List<Integer> getDCVolume(String dcUuid) {
		int totalCpu = 0;
		int totalMemory = 0;
		List<OCPool> poolList = this.getPoolDAO().getPoolListOfDC(dcUuid);
		if (poolList != null) {
			for (OCPool pool : poolList) {
				List<Integer> poolVolume = getPoolVolume(pool.getPoolUuid());
				totalCpu += poolVolume.get(0);
				totalMemory += poolVolume.get(1);
			}
		}
		List<Integer> volumeList = new ArrayList<Integer>();
		volumeList.add(totalCpu);
		volumeList.add(totalMemory);
		return volumeList;
	}

	public List<Integer> getPoolVolume(String poolUuid) {
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

	public JSONArray getPoolList(String dcid) {
		JSONArray jsonArrayPool = new JSONArray();
		List<OCPool> poolList = this.getPoolDAO().getPoolListOfDC(dcid);
		if (poolList != null) {
			for (OCPool pool : poolList) {
				JSONObject tpool = new JSONObject();
				tpool.put("poolobj", pool.toJsonString());

				List<Storage> srlist = this.getStorageDAO()
						.getStorageListByHostId(pool.getPoolMaster());
				JSONArray jsonArrayStorage = new JSONArray();
				if (srlist != null) {
					for (Storage sr : srlist)
						jsonArrayStorage.put(sr.toJsonString());
				}
				tpool.put("storagelist", jsonArrayStorage);

				JSONArray jsonArrayServer = new JSONArray();
				List<OCHost> serverList = this.getHostDAO().getHostListOfPool(
						pool.getPoolUuid());
				for (OCHost host : serverList) {
					JSONObject thost = new JSONObject();
					thost.put("hostobj", host.toJsonString());
					thost.put("imagecount",
							this.getImageDAO().countByHost(host.getHostUuid()));
					thost.put("vmcount",
							this.getVmDAO().countByHost(host.getHostUuid()));
					jsonArrayServer.put(thost);
				}

				tpool.put("serverlist", jsonArrayServer);
				jsonArrayPool.put(tpool);
			}

		}
		return jsonArrayPool;
	}

	public JSONArray getRackList(String dcid) {
		JSONArray jsonArrayRack = new JSONArray();
		List<Rack> rackList = this.getRackDAO().getRackListOfDC(dcid);
		if (rackList != null) {
			for (Rack rack : rackList) {
				JSONObject track = new JSONObject();
				track.put("rackobj", rack.toJsonString());

				List<Storage> srlist = this.getStorageDAO()
						.getStorageListOfRack(rack.getRackUuid());
				JSONArray jsonArrayStorage = new JSONArray();
				if (srlist != null) {
					for (Storage sr : srlist)
						jsonArrayStorage.put(sr.toJsonString());
				}
				track.put("storagelist", jsonArrayStorage);

				List<Switch> switchlist = this.getSwitchDAO().getSwitchOfRack(
						rack.getRackUuid());
				JSONArray jsonArraySwitch = new JSONArray();
				if (switchlist != null) {
					for (Switch sw : switchlist)
						jsonArraySwitch.put(sw.toJsonString());
				}
				track.put("switchlist", jsonArraySwitch);

				JSONArray jsonArrayServer = new JSONArray();
				List<OCHost> serverList = this.getHostDAO().getHostListOfRack(
						rack.getRackUuid());
				for (OCHost host : serverList) {
					JSONObject thost = new JSONObject();
					thost.put("hostobj", host.toJsonString());
					thost.put("imagecount",
							this.getImageDAO().countByHost(host.getHostUuid()));
					thost.put("vmcount",
							this.getVmDAO().countByHost(host.getHostUuid()));
					jsonArrayServer.put(thost);
				}

				track.put("serverlist", jsonArrayServer);
				jsonArrayRack.put(track);
			}

		}
		return jsonArrayRack;
	}
}
