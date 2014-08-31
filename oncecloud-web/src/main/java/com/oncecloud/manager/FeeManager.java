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

	/**
	 * 获取用户计费列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @param type
	 * @return
	 */
	public JSONArray getFeeList(int userId, int page, int limit, String search,
			String type) {
		JSONArray ja = new JSONArray();
		int totalNum = 0;
		List<Object> feeObjList = new ArrayList<Object>();
		if (type.equals("instance")) {
			totalNum = this.getFeeDAO().countAllFeeVMList(search, userId);
			feeObjList = this.getFeeDAO().getOnePageFeeVMList(page, limit,
					search, userId);
		} else if (type.equals("volume")) {
			totalNum = this.getFeeDAO().countAllFeeVolumeList(search, userId);
			feeObjList = this.getFeeDAO().getOnePageFeeVolumeList(page, limit,
					search, userId);
		} else if (type.equals("snapshot")) {
			totalNum = this.getFeeDAO().countAllFeeSnapshotList(search, userId);
			feeObjList = this.getFeeDAO().getOnePageFeeSnapshotList(page,
					limit, search, userId);
		} else if (type.equals("image")) {
			totalNum = this.getFeeDAO().countAllFeeImageList(search, userId);
			feeObjList = this.getFeeDAO().getOnePageFeeImageList(page, limit,
					search, userId);
		} else if (type.equals("eip")) {
			totalNum = this.getFeeDAO().countAllFeeEipList(search, userId);
			feeObjList = this.getFeeDAO().getOnePageFeeEipList(page, limit,
					search, userId);
		}
		ja.put(totalNum);
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

	public JSONArray feeGetDetailList(int page, int limit, String search,
			String type, String uuid, User user) {
		JSONArray ja = new JSONArray();
		int totalNum = 0;
		List<Object> feeObjList = new ArrayList<Object>();
		if (type.equals("instance")) {
			totalNum = this.getFeeDAO().countFeeVMDetailList(user.getUserId(),
					uuid);
			feeObjList = this.getFeeDAO().getFeeVMDetailList(page, limit,
					search, user.getUserId(), uuid);
		} else if (type.equals("volume")) {
			totalNum = this.getFeeDAO().countFeeVolumeDetailList(
					user.getUserId(), uuid);
			feeObjList = this.getFeeDAO().getFeeVolumeDetailList(page, limit,
					search, user.getUserId(), uuid);
		} else if (type.equals("snapshot")) {
			totalNum = this.getFeeDAO().countFeeSnapshotDetailList(
					user.getUserId(), uuid);
			feeObjList = this.getFeeDAO().getFeeSnapshotDetailList(page, limit,
					search, user.getUserId(), uuid);
		} else if (type.equals("image")) {
			totalNum = this.getFeeDAO().countFeeImageDetailList(
					user.getUserId(), uuid);
			feeObjList = this.getFeeDAO().getFeeImageDetailList(page, limit,
					search, user.getUserId(), uuid);
		} else if (type.equals("eip")) {
			totalNum = this.getFeeDAO().countFeeEipDetailList(user.getUserId(),
					uuid);
			feeObjList = this.getFeeDAO().getFeeEipDetailList(page, limit,
					search, user.getUserId(), uuid);
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

	/**
	 * 用户计费清单
	 * 
	 * @param userId
	 * @return
	 */
	public JSONObject getFeeSummary(int userId) {
		double instanceFee = this.getFeeDAO().getVmTotalFee(userId);
		double volumeFee = this.getFeeDAO().getVolumeTotalFee(userId);
		double snapshotFee = this.getFeeDAO().getSnapshotTotalFee(userId);
		double imageFee = this.getFeeDAO().getImageTotalFee(userId);
		double eipFee = this.getFeeDAO().getEipTotalFee(userId);
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

	/**
	 * 用户计费查询
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @param type
	 * @param month
	 * @return
	 */
	public JSONArray getQueryList(int userId, int page, int limit,
			String search, String type, String month) {
		JSONArray ja = new JSONArray();
		Date thisMonth = Utilities.String2Month(month);
		Date nextMonth = Utilities.AddMonthForDate(thisMonth, 1);
		int totalNum = 0;
		List<Object> feeObjList = new ArrayList<Object>();
		if (type.equals("instance")) {
			totalNum = this.getFeeDAO().countFeeVMDetailList(userId, thisMonth,
					nextMonth);
			feeObjList = this.getFeeDAO().getFeeVMDetailList(page, limit,
					search, userId, thisMonth, nextMonth);
		} else if (type.equals("volume")) {
			totalNum = this.getFeeDAO().countFeeVolumeDetailList(userId,
					thisMonth, nextMonth);
			feeObjList = this.getFeeDAO().getFeeVolumeDetailList(page, limit,
					search, userId, thisMonth, nextMonth);
		} else if (type.equals("snapshot")) {
			totalNum = this.getFeeDAO().countFeeSnapshotDetailList(userId,
					thisMonth, nextMonth);
			feeObjList = this.getFeeDAO().getFeeSnapshotDetailList(page, limit,
					search, userId, thisMonth, nextMonth);
		} else if (type.equals("image")) {
			totalNum = this.getFeeDAO().countFeeImageDetailList(userId,
					thisMonth, nextMonth);
			feeObjList = this.getFeeDAO().getFeeImageDetailList(page, limit,
					search, userId, thisMonth, nextMonth);
		} else if (type.equals("eip")) {
			totalNum = this.getFeeDAO().countFeeEipDetailList(userId,
					thisMonth, nextMonth);
			feeObjList = this.getFeeDAO().getFeeEipDetailList(page, limit,
					search, userId, thisMonth, nextMonth);
		}
		ja.put(totalNum);
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
