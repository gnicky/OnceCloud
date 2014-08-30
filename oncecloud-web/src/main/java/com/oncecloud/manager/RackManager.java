package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.DatacenterDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.RackDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.Datacenter;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.Rack;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;

/**
 * @author hehai yly
 * @version 2014/08/23
 */
@Component
public class RackManager {
	private RackDAO rackDAO;
	private DatacenterDAO datacenterDAO;
	private LogDAO logDAO;

	private RackDAO getRackDAO() {
		return rackDAO;
	}

	@Autowired
	private void setRackDAO(RackDAO rackDAO) {
		this.rackDAO = rackDAO;
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

	public JSONArray createRack(String rackName, String dcid, String rackDesc,
			int userid) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		Rack result = this.getRackDAO().createRack(rackName, dcid, rackDesc);
		if (result != null) {
			JSONObject jo = new JSONObject();
			jo.put("rackname", Utilities.encodeText(result.getRackName()));
			jo.put("rackdesc", Utilities.encodeText(result.getRackDesc()));
			jo.put("rackid", result.getRackUuid());
			jo.put("createdate", Utilities.formatTime(result.getCreateDate()));
			String dcId = result.getDcUuid();
			if (dcId == null) {
				dcId = "";
				jo.put("dcname", "");
			} else {
				Datacenter dc = this.getDatacenterDAO().getDatacenter(dcId);
				jo.put("dcname", Utilities.encodeText(dc.getDcName()));
			}
			jo.put("dcid", dcid);
			ja.put(jo);
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.机架.toString(), rackName));
		if (result != null) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray getRackList(int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getRackDAO().countAllRackList(search);
		ja.put(totalNum);
		List<Rack> rackList = this.getRackDAO().getOnePageRackList(page, limit,
				search);
		if (rackList != null) {
			for (Rack result : rackList) {
				JSONObject jo = new JSONObject();
				jo.put("rackname", Utilities.encodeText(result.getRackName()));
				jo.put("rackid", result.getRackUuid());
				jo.put("rackdesc", Utilities.encodeText(result.getRackDesc()));
				jo.put("createdate",
						Utilities.formatTime(result.getCreateDate()));
				String dcId = result.getDcUuid();
				if (dcId == null) {
					dcId = "";
					jo.put("dcname", "");
				} else {
					Datacenter dc = this.getDatacenterDAO().getDatacenter(dcId);
					jo.put("dcname", Utilities.encodeText(dc.getDcName()));
				}
				jo.put("dcid", dcId);
				ja.put(jo);
			}
		}
		return ja;
	}

	public void deleteRack(String rackId, String rackName, int userid) {
		Date startTime = new Date();
		boolean result = this.getRackDAO().deleteRack(rackId);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.机架.toString(), rackName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
			MessagePush.deleteRow(userid, rackId);
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
	}

	public JSONArray bind(String rackId, String dcId, int userid) {
		Date startTime = new Date();
		boolean result = this.getRackDAO().bindDatacenter(rackId, dcId);
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		Datacenter dc = this.getDatacenterDAO().getDatacenter(dcId);
		String dcName = dc.getDcName();
		jo.put("dcname", Utilities.encodeText(dcName));
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.机架.toString(),
				"rack-" + rackId.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.数据中心.toString(), dcName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray unbind(String rackId, int userid) {
		Date startTime = new Date();
		JSONArray ja = new JSONArray();
		boolean result = this.getRackDAO().unbindDatacenter(rackId);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.机架.toString(),
				"rack-" + rackId.substring(0, 8)));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.移除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.移除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public void update(String rackId, String rackName, String rackDesc,
			String dcid, int userid) {
		boolean result = this.getRackDAO().updateRack(rackId, rackName,
				rackDesc, dcid);
		// push message
		if (result) {
			MessagePush
					.pushMessage(userid, Utilities.stickyToSuccess("机架更新成功"));
		} else {
			MessagePush.pushMessage(userid, Utilities.stickyToError("机架更新失败"));
		}
	}

	public JSONArray getRackAllList() {
		List<Rack> rackList = this.getRackDAO().getAllPageRackList();
		JSONArray ja = new JSONArray();
		for (Rack result : rackList) {
			JSONObject jo = new JSONObject();
			jo.put("rackname", Utilities.encodeText(result.getRackName()));
			jo.put("rackid", result.getRackUuid());
			ja.put(jo);
		}
		return ja;
	}
}
