package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.DatacenterDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.RackDAO;
import com.oncecloud.entity.Datacenter;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.Rack;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.RackManager;
import com.oncecloud.message.MessagePush;

@Component("RackManager")
public class RackManagerImpl implements RackManager {

	private RackDAO rackDAO;
	private DatacenterDAO datacenterDAO;
	private LogDAO logDAO;
	private OverViewDAO overViewDAO;
	private MessagePush messagePush;

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

	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public JSONArray createRack(String rackName, String dcid, String rackDesc,
			int userid) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		Rack result = this.getRackDAO().createRack(rackName, dcid, rackDesc);
		boolean overview = this.getOverViewDAO().updateOverViewfield(
				"viewRack", true);
		if (result != null && overview) {
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
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
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
		result = this.getOverViewDAO().updateOverViewfield("viewRack", false);
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
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
			this.getMessagePush().deleteRow(userid, rackId);
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.机架.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void update(String rackId, String rackName, String rackDesc,
			String dcid, int userid) {
		boolean result = this.getRackDAO().updateRack(rackId, rackName,
				rackDesc, dcid);
		// push message
		if (result) {
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess("机架更新成功"));
		} else {
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToError("机架更新失败"));
		}
	}

	public JSONArray getRackAllList() {
		List<Rack> rackList = this.getRackDAO().getRackList();
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
