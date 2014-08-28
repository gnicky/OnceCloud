package com.oncecloud.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.FeeDAO;
import com.oncecloud.entity.User;
import com.oncecloud.main.Utilities;

@Component
public class FeeManager {
	private FeeDAO feeDAO;

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	public JSONArray feeGetList(int page, int limit, String searchStr,
			String type, User user) {
		JSONArray ja = new JSONArray();
		int totalNum = 0;
		List<Object> feeObjList = new ArrayList<Object>();
		if (type.equals("instance")) {
			totalNum = this.getFeeDAO().countAllFeeVMList(searchStr,
					user.getUserId());
			feeObjList = this.getFeeDAO().getOnePageFeeVMList(page, limit,
					searchStr, user.getUserId());
		} else if (type.equals("volume")) {
			totalNum = this.getFeeDAO().countAllFeeVolumeList(searchStr,
					user.getUserId());
			feeObjList = this.getFeeDAO().getOnePageFeeVolumeList(page, limit,
					searchStr, user.getUserId());
		} else if (type.equals("snapshot")) {
			totalNum = this.getFeeDAO().countAllFeeSnapshotList(searchStr,
					user.getUserId());
			feeObjList = this.getFeeDAO().getOnePageFeeSnapshotList(page,
					limit, searchStr, user.getUserId());
		} else if (type.equals("image")) {
			totalNum = this.getFeeDAO().countAllFeeImageList(searchStr,
					user.getUserId());
			feeObjList = this.getFeeDAO().getOnePageFeeImageList(page, limit,
					searchStr, user.getUserId());
		} else if (type.equals("eip")) {
			totalNum = this.getFeeDAO().countAllFeeEipList(searchStr,
					user.getUserId());
			feeObjList = this.getFeeDAO().getOnePageFeeEipList(page, limit,
					searchStr, user.getUserId());
		}
		JSONObject tn = new JSONObject();
		tn.put("totalpage", totalNum);
		ja.put(tn);
		for (int i = 0; i < feeObjList.size(); i++) {
			JSONObject jo = new JSONObject();
			Object[] obj = (Object[]) feeObjList.get(i);
			jo.put("feeExpense", (Double) obj[0]);
			jo.put("feeName", Utilities.encodeText((String) obj[1]));
			jo.put("resourceId", (String) obj[2]);
			jo.put("feeState", (Integer) obj[3]);
			jo.put("feePrice", (Double) obj[4]);
			jo.put("feeCreateDate", Utilities.formatTime((Date) obj[5]));
			ja.put(jo);
		}
		return ja;
	}

	public JSONArray feeGetDetailList(int page, int limit, String searchStr,
			String type, String uuid, User user) {
		JSONArray ja = new JSONArray();
		int totalNum = 0;
		List<Object> feeObjList = new ArrayList<Object>();
		if (type.equals("instance")) {
			totalNum = this.getFeeDAO().countFeeVMDetailList(user.getUserId(),
					uuid);
			feeObjList = this.getFeeDAO().getFeeVMDetailList(page, limit,
					searchStr, user.getUserId(), uuid);
		} else if (type.equals("volume")) {
			totalNum = this.getFeeDAO().countFeeVolumeDetailList(
					user.getUserId(), uuid);
			feeObjList = this.getFeeDAO().getFeeVolumeDetailList(page, limit,
					searchStr, user.getUserId(), uuid);
		} else if (type.equals("snapshot")) {
			totalNum = this.getFeeDAO().countFeeSnapshotDetailList(
					user.getUserId(), uuid);
			feeObjList = this.getFeeDAO().getFeeSnapshotDetailList(page, limit,
					searchStr, user.getUserId(), uuid);
		} else if (type.equals("image")) {
			totalNum = this.getFeeDAO().countFeeImageDetailList(
					user.getUserId(), uuid);
			feeObjList = this.getFeeDAO().getFeeImageDetailList(page, limit,
					searchStr, user.getUserId(), uuid);
		} else if (type.equals("eip")) {
			totalNum = this.getFeeDAO().countFeeEipDetailList(user.getUserId(),
					uuid);
			feeObjList = this.getFeeDAO().getFeeEipDetailList(page, limit,
					searchStr, user.getUserId(), uuid);
		}
		JSONObject tn = new JSONObject();
		tn.put("totalpage", totalNum);
		ja.put(tn);
		for (int i = 0; i < feeObjList.size(); i++) {
			JSONObject jo = new JSONObject();
			Object[] obj = (Object[]) feeObjList.get(i);
			jo.put("feeStartDate", Utilities.formatTime((Date) obj[0]));
			jo.put("feeEndDate", Utilities.formatTime((Date) obj[1]));
			jo.put("feeExpense", (Double) obj[2]);
			jo.put("feePrice", (Double) obj[3]);
			ja.put(jo);
		}
		return ja;
	}

	public JSONObject feeInitfee(int userid) {
		double instanceFee = this.getFeeDAO().getVmTotalFee(userid);
		double volumeFee = this.getFeeDAO().getVolumeTotalFee(userid);
		double snapshotFee = this.getFeeDAO().getSnapshotTotalFee(userid);
		double imageFee = this.getFeeDAO().getImageTotalFee(userid);
		double eipFee = this.getFeeDAO().getEipTotalFee(userid);
		double totalFee = instanceFee + volumeFee + snapshotFee + imageFee
				+ eipFee;
		JSONObject jo = new JSONObject();
		jo.put("totalFee", totalFee);
		jo.put("instanceFee", instanceFee);
		jo.put("volumeFee", volumeFee);
		jo.put("snapshotFee", snapshotFee);
		jo.put("imageFee", imageFee);
		jo.put("eipFee", eipFee);
		return jo;
	}

	public JSONArray feeQuerylist(int userid, int page, int limit,
			String searchStr, String type, String monthStr) {
		Date month = Utilities.String2Month(monthStr);
		Date nextMonth = Utilities.AddMonthForDate(month, 1);
		JSONArray ja = new JSONArray();
		int totalNum = 0;
		List<Object> feeObjList = new ArrayList<Object>();
		if (type.equals("instance")) {
			totalNum = this.getFeeDAO().countFeeVMDetailList(userid, month,
					nextMonth);
			feeObjList = this.getFeeDAO().getFeeVMDetailList(page, limit,
					searchStr, userid, month, nextMonth);
		} else if (type.equals("volume")) {
			totalNum = this.getFeeDAO().countFeeVolumeDetailList(userid, month,
					nextMonth);
			feeObjList = this.getFeeDAO().getFeeVolumeDetailList(page, limit,
					searchStr, userid, month, nextMonth);
		} else if (type.equals("snapshot")) {
			totalNum = this.getFeeDAO().countFeeSnapshotDetailList(userid,
					month, nextMonth);
			feeObjList = this.getFeeDAO().getFeeSnapshotDetailList(page, limit,
					searchStr, userid, month, nextMonth);
		} else if (type.equals("image")) {
			totalNum = this.getFeeDAO().countFeeImageDetailList(userid, month,
					nextMonth);
			feeObjList = this.getFeeDAO().getFeeImageDetailList(page, limit,
					searchStr, userid, month, nextMonth);
		} else if (type.equals("eip")) {
			totalNum = this.getFeeDAO().countFeeEipDetailList(userid, month,
					nextMonth);
			feeObjList = this.getFeeDAO().getFeeEipDetailList(page, limit,
					searchStr, userid, month, nextMonth);
		}
		JSONObject tn = new JSONObject();
		tn.put("totalpage", totalNum);
		ja.put(tn);
		for (int i = 0; i < feeObjList.size(); i++) {
			JSONObject jo = new JSONObject();
			Object[] obj = (Object[]) feeObjList.get(i);
			jo.put("feeId", (String) obj[0]);
			jo.put("feeStartDate", (Date) obj[1]);
			jo.put("feeEndDate", (Date) obj[2]);
			jo.put("feeExpense", (Double) obj[3]);
			jo.put("feePrice", (Double) obj[4]);
			ja.put(jo);
		}
		return ja;
	}

}
