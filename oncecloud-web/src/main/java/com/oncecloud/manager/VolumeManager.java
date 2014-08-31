package com.oncecloud.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.VDI;
import com.once.xenapi.VM;
import com.oncecloud.dao.FeeDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VolumeDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.Quota;
import com.oncecloud.entity.Volume;
import com.oncecloud.entity.VolumeStatus;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class VolumeManager {
	private VolumeDAO volumeDAO;
	private VMDAO vmDAO;
	private FeeDAO feeDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private Constant constant;

	private VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	private void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	/**
	 * 获取用户硬盘列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getVolumeList(int userId, int page, int limit,
			String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getVolumeDAO().countAllVolumeList(userId, search);
		List<Volume> volumeList = this.getVolumeDAO().getOnePageVolumeList(
				userId, page, limit, search);
		ja.put(totalNum);
		if (volumeList != null) {
			for (int i = 0; i < volumeList.size(); i++) {
				JSONObject jo = new JSONObject();
				Volume volume = volumeList.get(i);
				jo.put("volumeid", volume.getVolumeUuid());
				jo.put("volumename",
						Utilities.encodeText(volume.getVolumeName()));
				String vmuuid = volume.getVolumeDependency();
				if (vmuuid != null) {
					jo.put("volumedepen", vmuuid);
					jo.put("depenname",
							Utilities.encodeText(this.getVmDAO().getVmName(
									vmuuid)));
					jo.put("isused", true);
				} else {
					jo.put("volumedepen", "");
					jo.put("depenname", "");
					jo.put("isused", false);
				}
				jo.put("volState", volume.getVolumeStatus());
				jo.put("volumesize", volume.getVolumeSize());
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed(volume.getCreateDate()));
				jo.put("createdate", timeUsed);
				if (volume.getBackupDate() == null) {
					jo.put("backupdate", "");
				} else {
					timeUsed = Utilities.encodeText(Utilities.dateToUsed(volume
							.getBackupDate()));
					jo.put("backupdate", timeUsed);
				}
				ja.put(jo);
			}
		}
		return ja;
	}

	public void createVolume(int userId, String volUuid, String volName,
			int volSize) {
		boolean result = false;
		String showId = "vol-" + volUuid.substring(0, 8);
		Date startTime = new Date();
		try {
			boolean preCreate = this.getVolumeDAO().preCreateVolume(volUuid,
					volName, userId, volSize, startTime,
					VolumeStatus.STATUS_CREATE);
			if (preCreate) {
				Connection c = this.getConstant().getConnection(userId);
				VDI vdi = VDI.createDataDisk(c, volUuid, (long) volSize);
				if (vdi != null) {
					this.getVolumeDAO().setVolumeStatus(volUuid,
							VolumeStatus.STATUS_FREE);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(startTime);
					calendar.add(Calendar.MINUTE, 60);
					Date endDate = calendar.getTime();
					this.getFeeDAO().insertFeeVolume(userId, startTime,
							endDate, Constant.VOLUME_PRICE * (double) volSize,
							1, volUuid, volName);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result == false) {
				this.getVolumeDAO().deleteVolume(userId, volUuid);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.硬盘.toString(), showId));
		infoArray.put(Utilities.createLogInfo("容量", volSize + " GB"));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, volUuid, "running", "可用");
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.deleteRow(userId, volUuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void deleteVolume(int userId, String volUuid) {
		boolean result = false;
		String showId = "vol-" + volUuid.substring(0, 8);
		Date startTime = new Date();
		Connection c = null;
		try {
			c = this.getConstant().getConnection(userId);
			boolean preDelete = this.getVolumeDAO().setVolumeStatus(volUuid,
					VMManager.POWER_DESTROY);
			if (preDelete) {
				boolean deleteResult = VDI.deleteDataDisk(c, volUuid);
				if (deleteResult) {
					Date endTime = new Date();
					this.getFeeDAO().deleteVolume(endTime, volUuid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.getVolumeDAO().deleteVolume(userId, volUuid);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.硬盘.toString(), showId));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.deleteRow(userId, volUuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, volUuid, "running", "可用");
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void bindVolume(int userId, String volUuid, String vmUuid) {
		boolean result = false;
		String showId = "vol-" + volUuid.substring(0, 8);
		Date startTime = new Date();
		Connection c = null;
		String vmName = null;
		try {
			c = this.getConstant().getConnection(userId);
			boolean preBind = this.getVolumeDAO().setVolumeStatus(volUuid,
					VolumeStatus.STATUS_MOUNTING);
			if (preBind) {
				boolean bindResult = VM.createDataVBD(c, vmUuid, volUuid);
				if (bindResult) {
					this.getVolumeDAO().addDependency(volUuid, vmUuid);
					vmName = this.getVmDAO().getVmName(vmUuid);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result == false) {
				this.getVolumeDAO().setVolumeStatus(volUuid,
						VolumeStatus.STATUS_FREE);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.硬盘.toString(), showId));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.加载.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, volUuid, "using", "使用中");
			MessagePush.editRowStatusForBindVolume(userId, volUuid, vmUuid,
					vmName);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.加载.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, volUuid, "running", "可用");
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void unbindVolume(int userId, String volUuid) {
		boolean result = false;
		String showId = "vol-" + volUuid.substring(0, 8);
		Date startTime = new Date();
		Connection c = null;
		try {
			c = this.getConstant().getConnection(userId);
			boolean preUnbind = this.getVolumeDAO().setVolumeStatus(volUuid,
					VolumeStatus.STATUS_UNMOUNTING);
			if (preUnbind) {
				String vmUuid = this.getVolumeDAO().getVolume(volUuid)
						.getVolumeDependency();
				boolean unbindResult = VM.deleteDataVBD(c, vmUuid, volUuid);
				if (unbindResult) {
					this.getVolumeDAO().emptyDependency(volUuid);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result == false) {
				this.getVolumeDAO().setVolumeStatus(volUuid,
						VolumeStatus.STATUS_MOUNTED);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.硬盘.toString(), showId));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.卸载.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, volUuid, "running", "可用");
			MessagePush.editRowStatusForUnbindVolume(userId, volUuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.硬盘.ordinal(),
					LogConstant.logAction.卸载.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, volUuid, "using", "使用中");
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public String getQuota(int userId, int count, int size) {
		String quota = "ok";
		Quota qt = this.getQuotaDAO().getQuotaTotal(userId);
		Quota qu = this.getQuotaDAO().getQuotaUsed(userId);
		int freeC = qt.getQuotaDiskN() - qu.getQuotaDiskN();
		int freeS = qt.getQuotaDiskS() - qu.getQuotaDiskS();
		if (freeC < count || freeS < size) {
			quota = freeC + ":" + freeS;
		}
		return quota;
	}

	/**
	 * 获取硬盘详细信息
	 * 
	 * @param volUuid
	 * @return
	 */
	public JSONObject getVolumeDetail(String volUuid) {
		JSONObject jo = new JSONObject();
		Volume volume = this.getVolumeDAO().getVolume(volUuid);
		if (volume != null) {
			jo.put("volumeName", Utilities.encodeText(volume.getVolumeName()));
			jo.put("volumeUID", volume.getVolumeUID());
			jo.put("volumeSize", volume.getVolumeSize());
			jo.put("volumeDependency",
					(null == volume.getVolumeDependency()) ? "&nbsp;" : volume
							.getVolumeDependency());
			jo.put("volumeDescription",
					(null == volume.getVolumeDescription()) ? "&nbsp;"
							: Utilities.encodeText(volume
									.getVolumeDescription()));
			jo.put("createDate", Utilities.formatTime(volume.getCreateDate()));
			String timeUsed = Utilities.encodeText(Utilities.dateToUsed(volume
					.getCreateDate()));
			if (volume.getBackupDate() == null) {
				jo.put("backupDate", "&nbsp;");
			} else {
				String tUsed = Utilities.encodeText(Utilities.dateToUsed(volume
						.getBackupDate()));
				jo.put("backupDate", tUsed);
			}
			jo.put("useDate", timeUsed);
			jo.put("volumeStatus", volume.getVolumeStatus());
		}
		return jo;
	}

	public JSONArray getAbledVolumeList(int userId) {
		JSONArray ja = new JSONArray();
		List<Volume> volumeList = this.getVolumeDAO().getAbledVolumes(userId);
		if (volumeList != null) {
			for (int i = 0; i < volumeList.size(); i++) {
				JSONObject jo = new JSONObject();
				Volume volume = volumeList.get(i);
				jo.put("volumeId", volume.getVolumeUuid());
				jo.put("volumeName",
						Utilities.encodeText(volume.getVolumeName()));
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray getVolumeListByVM(String vmUuid) {
		JSONArray ja = new JSONArray();
		List<Volume> volumeList = this.getVolumeDAO().getVolListByVM(vmUuid);
		if (volumeList != null) {
			for (int i = 0; i < volumeList.size(); i++) {
				JSONObject jo = new JSONObject();
				Volume volume = volumeList.get(i);
				jo.put("volumeId", volume.getVolumeUuid());
				jo.put("volumeName",
						Utilities.encodeText(volume.getVolumeName()));
				jo.put("volumeSize", volume.getVolumeSize());
				ja.put(jo);
			}
		}
		return ja;
	}
}
