package com.oncecloud.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Types;
import com.once.xenapi.VDI;
import com.once.xenapi.VM;
import com.oncecloud.dao.FeeDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.SnapshotDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VolumeDAO;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.Snapshot;
import com.oncecloud.entity.Volume;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.SnapshotManager;
import com.oncecloud.manager.VMManager;
import com.oncecloud.message.MessagePush;

@Component("SnapshotManager")
public class SnapshotManagerImpl implements SnapshotManager {
	private SnapshotDAO snapshotDAO;
	private FeeDAO feeDAO;
	private VMDAO vmDAO;
	private VolumeDAO volumeDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private MessagePush messagePush;

	private Constant constant;

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private SnapshotDAO getSnapshotDAO() {
		return snapshotDAO;
	}

	@Autowired
	private void setSnapshotDAO(SnapshotDAO snapshotDAO) {
		this.snapshotDAO = snapshotDAO;
	}

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	private void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
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
	 * 获取备份链列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getSnapshotList(int userId, int page, int limit,
			String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getSnapshotDAO().countAllSnapshotList(userId,
				search);
		int vmNum = this.getSnapshotDAO().countVMSnapshotList(userId, search);
		int offside = limit - vmNum % limit;
		ja.put(totalNum);
		List<Object> vmSnapshotList = new ArrayList<Object>();
		List<Object> volumeSnapshotList = new ArrayList<Object>();
		if (page <= vmNum / limit) {
			vmSnapshotList = this.getSnapshotDAO().getOnePageVMSnapshotList(
					userId, page, limit, search);
		} else if (page == vmNum / limit + 1) {
			vmSnapshotList = this.getSnapshotDAO().getOnePageVMSnapshotList(
					userId, page, limit, search);
			volumeSnapshotList = this
					.getSnapshotDAO()
					.getOnePageVolumeSnapshotList(userId, 1, offside, search, 0);
		} else {
			volumeSnapshotList = this.getSnapshotDAO()
					.getOnePageVolumeSnapshotList(userId,
							page - vmNum / limit - 2, limit, search, offside);
		}
		if (vmSnapshotList != null) {
			for (int i = 0; i < vmSnapshotList.size(); i++) {
				JSONObject jo = new JSONObject();
				Object[] obj = (Object[]) vmSnapshotList.get(i);
				jo.put("resourceUuid", (String) obj[0]);
				jo.put("resourceName", Utilities.encodeText((String) obj[1]));
				jo.put("resourceType", "instance");
				jo.put("snapshotCount", ((Long) obj[2]).intValue());
				jo.put("snapshotSize", ((Long) obj[3]).intValue());
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed((Date) obj[4]));
				jo.put("backupDate", timeUsed);
				ja.put(jo);
			}
		}
		if (volumeSnapshotList != null) {
			for (int i = 0; i < volumeSnapshotList.size(); i++) {
				JSONObject jo = new JSONObject();
				Object[] obj = (Object[]) volumeSnapshotList.get(i);
				jo.put("resourceUuid", (String) obj[0]);
				jo.put("resourceName", Utilities.encodeText((String) obj[1]));
				jo.put("resourceType", "volume");
				jo.put("snapshotCount", ((Long) obj[2]).intValue());
				jo.put("snapshotSize", ((Long) obj[3]).intValue());
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed((Date) obj[4]));
				jo.put("backupDate", timeUsed);
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject createSnapshot(int userId, String snapshotId,
			String snapshotName, String resourceUuid, String resourceType) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
		snapshotId = snapshotId + "_" + sdf.format(startTime);
		boolean newChain = this.getSnapshotDAO().ifNewChain(resourceUuid);
		if (resourceType.equals("instance")) {
			VM vm = Types.toVM(resourceUuid);
			try {
				Connection c = this.getConstant().getConnection(userId);
				if (vm.snapshot(c, snapshotId)) {
					this.getSnapshotDAO().insertSnapshot(snapshotId,
							snapshotName, 1, startTime, resourceUuid, null, userId);
					if (newChain) {
						this.getQuotaDAO().updateQuota(userId,
								"quotaSnapshot", 1, true);
					}
					Date endDate = new Date();
					this.getFeeDAO().deleteSnapshot(endDate, resourceUuid);
					String vmName = this.getVmDAO().getVmName(resourceUuid);
					double snapshotPrice = this.getSnapshotDAO()
							.getVmSnapshotSize(resourceUuid)
							* Constant.SNAPSHOT_PRICE;
					this.getFeeDAO().insertFeeSnapshot(userId, endDate,
							Utilities.AddMinuteForDate(endDate, 60),
							snapshotPrice, 1, resourceUuid, vmName);
					Date maxdate = this.getSnapshotDAO()
							.getRecentVmSnapshotDate(resourceUuid);
					this.getVmDAO().updateBackupDate(resourceUuid, maxdate);
					jo.put("isSuccess", true);
					String timeUsed = Utilities.encodeText(Utilities
							.dateToUsed(maxdate));
					jo.put("backupDate", timeUsed);
				} else {
					jo.put("isSuccess", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jo.put("isSuccess", false);
			}
		} else if (resourceType.equals("volume")) {
			VDI vdi = Types.toVDI(resourceUuid);
			try {
				Connection c = this.getConstant().getConnection(userId);
				if (vdi.snapshot(c, snapshotId)) {
					this.getSnapshotDAO().insertSnapshot(snapshotId,
							snapshotName, 1, startTime, null, resourceUuid, userId);
					if (newChain) {
						this.getQuotaDAO().updateQuota(userId,
								"quotaSnapshot", 1, true);
					}
					Date endDate = new Date();
					this.getFeeDAO().deleteSnapshot(endDate, resourceUuid);
					String volumeName = this.getVolumeDAO()
							.getVolume(resourceUuid).getVolumeName();
					double snapshotPrice = this.getSnapshotDAO()
							.getVolumeSnapshotSize(resourceUuid)
							* Constant.SNAPSHOT_PRICE;
					this.getFeeDAO().insertFeeSnapshot(userId, endDate,
							Utilities.AddMinuteForDate(endDate, 60),
							snapshotPrice, 1, resourceUuid, volumeName);
					Date maxdate = this.getSnapshotDAO()
							.getRecentVolumeSnapshotDate(resourceUuid);
					this.getVolumeDAO().updateBackupDate(resourceUuid, maxdate);
					String timeUsed = Utilities.encodeText(Utilities
							.dateToUsed(maxdate));
					jo.put("backupDate", timeUsed);
					jo.put("isSuccess", true);
				} else {
					jo.put("isSuccess", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jo.put("isSuccess", false);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.备份.toString(),
				"bk-" + snapshotId.substring(0, 8)));
		if (resourceType.equals("instance")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.主机.toString(),
					"i-" + resourceUuid.substring(0, 8)));
		} else {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.硬盘.toString(),
					"vol-" + resourceUuid.substring(0, 8)));
		}
		if (jo.getBoolean("isSuccess") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject deleteSnapshotSeries(int userId, String resourceUuid,
			String resourceType) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		if (resourceType.equals("instance")) {
			VDI vdi = Types.toVDI(resourceUuid);
			try {
				Connection c = this.getConstant().getConnection(userId);
				if (vdi.destroyAllSnapshots(c)) {
					this.getSnapshotDAO()
							.deleteVmSnapshot(resourceUuid, userId);
					this.getQuotaDAO().updateQuota(userId, "quotaSnapshot", 1, false);
					Date endDate = new Date();
					this.getFeeDAO().deleteSnapshot(endDate, resourceUuid);
					this.getVmDAO().updateBackupDate(resourceUuid, null);
					jo.put("result", true);
				} else {
					jo.put("result", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jo.put("result", false);
			}
		} else if (resourceType.equals("volume")) {
			VDI vdi = Types.toVDI(resourceUuid);
			try {
				Connection c = this.getConstant().getConnection(userId);
				if (vdi.destroyAllSnapshots(c)) {
					this.getSnapshotDAO().deleteVolumeSnapshot(resourceUuid,
							userId);
					this.getQuotaDAO().updateQuota(userId, "quotaSnapshot", 1, false);
					Date endDate = new Date();
					this.getFeeDAO().deleteSnapshot(endDate, resourceUuid);
					this.getVolumeDAO().updateBackupDate(resourceUuid, null);
					jo.put("result", true);
				} else {
					jo.put("result", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jo.put("result", false);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.备份链.toString(),
				"bk-" + resourceUuid.substring(0, 8)));
		if (resourceType.equals("instance")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.主机.toString(),
					"i-" + resourceUuid.substring(0, 8)));
		} else {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.硬盘.toString(),
					"vol-" + resourceUuid.substring(0, 8)));
		}
		if (jo.getBoolean("result") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份链.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份链.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONArray getDetailList(String resourceUuid, String resourceType) {
		JSONArray ja = new JSONArray();
		List<Snapshot> snapshotList = new ArrayList<Snapshot>();
		if (resourceType.equals("instance")) {
			snapshotList = this.getSnapshotDAO()
					.getVmSnapshotList(resourceUuid);
		} else if (resourceType.equals("volume")) {
			snapshotList = this.getSnapshotDAO().getVolumeSnapshotList(
					resourceUuid);
		}
		if (snapshotList != null) {
			for (int i = 0; i < snapshotList.size(); i++) {
				JSONObject jo = new JSONObject();
				Snapshot ss = snapshotList.get(i);
				jo.put("snapshotId", ss.getSnapshotId());
				jo.put("snapshotName",
						Utilities.encodeText(ss.getSnapshotName()));
				jo.put("snapshotSize", ss.getSnapshotSize());
				jo.put("backupDate", ss.getBackupDate());
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject rollbackSnapshot(int userId, String snapshotId,
			String resourceUuid, String resourceType) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		if (resourceType.equals("instance")) {
			if (this.getVmDAO().getAliveVM(resourceUuid) != null) {
				try {
					Connection c = this.getConstant().getConnection(userId);
					VM vm = Types.toVM(resourceUuid);
					if (this.getVmDAO().getVM(resourceUuid).getVmPower() == 1) {
						vm.hardShutdown(c);
						this.getVmDAO().updatePowerStatus(resourceUuid, 0);
					}
					if (vm.rollback(c, snapshotId)) {
						jo.put("exist", true);
						jo.put("result", true);
					} else {
						jo.put("exist", true);
						jo.put("result", false);
					}
				} catch (Exception e) {
					e.printStackTrace();
					jo.put("exist", true);
					jo.put("result", false);
				}
			} else {
				jo.put("exist", false);
				jo.put("result", false);
			}
		} else if (resourceType.equals("volume")) {
			if (this.getVolumeDAO().isExist(resourceUuid)) {
				Volume volume = this.getVolumeDAO().getVolume(resourceUuid);
				if (volume.getVolumeDependency() == null) {
					try {
						Connection c = this.getConstant().getConnection(userId);
						VDI vdi = Types.toVDI(resourceUuid);
						if (vdi.rollback(c, snapshotId)) {
							jo.put("exist", true);
							jo.put("result", true);
						} else {
							jo.put("exist", true);
							jo.put("result", false);
						}
					} catch (Exception e) {
						e.printStackTrace();
						jo.put("exist", true);
						jo.put("result", false);
					}
				} else {
					String dependenVm = volume.getVolumeDependency();
					try {
						Connection c = this.getConstant().getConnection(userId);
						if (this.getVmDAO().getVM(resourceUuid).getVmPower() == 1) {
							VM vm = Types.toVM(dependenVm);
							vm.hardShutdown(c);
							this.getVmDAO().updatePowerStatus(dependenVm, 0);
						}
						VDI vdi = Types.toVDI(resourceUuid);
						if (vdi.rollback(c, snapshotId)) {
							jo.put("exist", true);
							jo.put("result", true);
						} else {
							jo.put("exist", true);
							jo.put("result", false);
						}
					} catch (Exception e) {
						e.printStackTrace();
						jo.put("exist", true);
						jo.put("result", false);
					}
				}
			} else {
				jo.put("exist", false);
				jo.put("result", false);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.备份.toString(),
				"ss-" + resourceUuid.substring(0, 8)));
		if (resourceType.equals("instance")) {
			if (jo.getBoolean("exist") == true) {
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.主机.toString(), "i-"
								+ resourceUuid.substring(0, 8)));
			} else {
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.主机.toString(), "不存在"));
			}
		} else {
			if (jo.getBoolean("exist") == true) {
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.硬盘.toString(), "vol-"
								+ resourceUuid.substring(0, 8)));
			} else {
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.硬盘.toString(), "不存在"));
			}
		}
		if (jo.getBoolean("result") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份.ordinal(),
					LogConstant.logAction.回滚.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份.ordinal(),
					LogConstant.logAction.回滚.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject deleteSnapshot(int userId, String snapshotId,
			String resourceUuid, String resourceType) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		if (resourceType.equals("instance")) {
			VDI vdi = Types.toVDI(resourceUuid);
			try {
				Connection c = this.getConstant().getConnection(userId);
				if (vdi.destroySnapshot(c, snapshotId)) {
					Snapshot ss = this.getSnapshotDAO().getSnapshot(snapshotId);
					String vmUuid = ss.getSnapshotVm();
					int vmSize = this.getSnapshotDAO().getVmSnapshotSize(vmUuid);
					if (vmSize == 1) {
						this.getQuotaDAO().updateQuota(userId, "quotaSnapshot", 1, false);
					}
					this.getSnapshotDAO().deleteOneSnapshot(ss);
					Date endDate = new Date();
					this.getFeeDAO().deleteSnapshot(endDate, resourceUuid);
					double snapshotPrice = this.getSnapshotDAO()
							.getVmSnapshotSize(resourceUuid)
							* Constant.SNAPSHOT_PRICE;
					if (snapshotPrice > 0) {
						String vmName = this.getVmDAO().getVmName(resourceUuid);
						this.getFeeDAO().insertFeeSnapshot(userId, endDate,
								Utilities.AddMinuteForDate(endDate, 60),
								snapshotPrice, 1, resourceUuid, vmName);
					}

					Date maxdate = this.getSnapshotDAO()
							.getRecentVmSnapshotDate(resourceUuid);
					this.getVmDAO().updateBackupDate(resourceUuid, maxdate);
					jo.put("result", true);
				} else {
					jo.put("result", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jo.put("result", false);
			}
		} else if (resourceType.equals("volume")) {
			VDI vdi = Types.toVDI(resourceUuid);
			try {
				Connection c = this.getConstant().getConnection(userId);
				if (vdi.destroySnapshot(c, snapshotId)) {
					Snapshot ss = this.getSnapshotDAO().getSnapshot(snapshotId);
					String volumeUuid = ss.getSnapshotVolume();
					int volSize = this.getSnapshotDAO().getVolumeSnapshotSize(volumeUuid);
					if (volSize == 1) {
						this.getQuotaDAO().updateQuota(userId, "quotaSnapshot", 1, false);
					}
					this.getSnapshotDAO().deleteOneSnapshot(ss);
					Date endDate = new Date();
					this.getFeeDAO().deleteSnapshot(endDate, resourceUuid);
					double snapshotPrice = this.getSnapshotDAO()
							.getVolumeSnapshotSize(resourceUuid)
							* Constant.SNAPSHOT_PRICE;
					if (snapshotPrice > 0) {
						String volumeName = this.getVolumeDAO()
								.getVolume(resourceUuid).getVolumeName();
						this.getFeeDAO().insertFeeSnapshot(userId, endDate,
								Utilities.AddMinuteForDate(endDate, 60),
								snapshotPrice, 1, resourceUuid, volumeName);
					}

					Date maxdate = this.getSnapshotDAO()
							.getRecentVolumeSnapshotDate(resourceUuid);
					this.getVolumeDAO().updateBackupDate(resourceUuid, maxdate);
					jo.put("result", true);
				} else {
					jo.put("result", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jo.put("result", false);
			}
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.备份.toString(),
				"ss-" + snapshotId.substring(0, 8)));
		if (resourceType.equals("instance")) {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.主机.toString(),
					"i-" + resourceUuid.substring(0, 8)));
		} else {
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.硬盘.toString(),
					"vol-" + resourceUuid.substring(0, 8)));
		}
		if (jo.getBoolean("result") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.备份.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject getBasicList(int userId, String resourceUuid,
			String resourceType) {
		JSONObject jo = new JSONObject();
		Object[] obj = null;
		if (resourceType.equals("instance")) {
			obj = (Object[]) this.getSnapshotDAO().getOneVmSnapshot(
					resourceUuid);
		} else if (resourceType.equals("volume")) {
			obj = (Object[]) this.getSnapshotDAO().getOneVolumeSnapshot(
					resourceUuid);
		}
		jo.put("resourceName", Utilities.encodeText((String) obj[1]));
		jo.put("snapshotCount", ((Long) obj[2]).intValue());
		jo.put("snapshotSize", ((Long) obj[3]).intValue());
		String timeUsed = Utilities.encodeText(Utilities
				.dateToUsed((Date) obj[4]));
		jo.put("backupDate", timeUsed);
		jo.put("backStatus", ((Integer) obj[5]).intValue());
		return jo;
	}

	public String getQuota(int userId, int count) {
		String result = "ok";
		int free = this.getQuotaDAO().getQuotaTotal(userId).getQuotaSnapshot()
				- this.getQuotaDAO().getQuotaUsed(userId).getQuotaSnapshot();
		if (free < count) {
			result = String.valueOf(free);
		}
		return result;
	}
}
