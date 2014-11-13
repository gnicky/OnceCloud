package com.oncecloud.manager;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.AlarmDAO;
import com.oncecloud.dao.AlarmLogDAO;
import com.oncecloud.dao.AlarmRuleDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.AlarmLog;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.performance.Cpu;
import com.oncecloud.entity.performance.Memory;
import com.oncecloud.entity.performance.Vbd;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author cyh
 * @version 2014/11/04
 */
@Component
public class AlarmLogManager {
	private AlarmLogDAO alarmLogDAO;
	private AlarmDAO alarmDAO;
	private AlarmRuleDAO alarmRuleDAO;
	private VMDAO vmDAO;
	private EIPDAO eipDAO;
	private RouterDAO routerDAO;
	private LBDAO lbDAO;
	private HostDAO hostDAO;
	private MessagePush messagePush;
	private LogDAO logDAO;
	
	

	public LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	public void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	private AlarmLogDAO getAlarmLogDAO() {
		return alarmLogDAO;
	}

	@Autowired
	private void setAlarmLogDAO(AlarmLogDAO alarmLogDAO) {
		this.alarmLogDAO = alarmLogDAO;
	}

	private AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	@Autowired
	private void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	private AlarmRuleDAO getAlarmRuleDAO() {
		return alarmRuleDAO;
	}

	@Autowired
	private void setAlarmRuleDAO(AlarmRuleDAO alarmRuleDAO) {
		this.alarmRuleDAO = alarmRuleDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	private void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public void checkCPU() {
		// TODO Auto-generated method stub

		// 1 查询cpu_30min 表，找到需要监控的对象
		List<Cpu> checklist = this.getAlarmLogDAO().getCheckCPUList();
		if (checklist != null && checklist.size()>0) {
			for (Object item : checklist) {
				Cpu cpuitem = (Cpu)item;
				if (this.getAlarmLogDAO().checkOneCPU(cpuitem.getUuid(),
						cpuitem.getCpuId(), 0.2)) {
					String alarmDesc = "";
					int userId = 1;
					String uuid = UUID.randomUUID().toString();
					OCVM ocvm = this.vmDAO.getVM(cpuitem.getUuid());
					if (ocvm != null) {
						userId = ocvm.getVmUID();
						alarmDesc = "虚拟主机 i-"
								+ cpuitem.getUuid().substring(0, 8) + ",CPU"
								+ cpuitem.getCpuId() + "超过预设阀值，发生告警!";
						// /说明是虚拟机
					} else {
						// /说明是物理机.物理机不需要查询了
						alarmDesc = "物理主机 host-"
								+ cpuitem.getUuid().substring(0, 8) + ",CPU"
								+ cpuitem.getCpuId() + "超过预设阀值，发生告警!";

					}
					// /告警
					this.alarmLogDAO.addAlarmLog(uuid, cpuitem.getUuid(),
							"CPU", alarmDesc, userId);
					this.getMessagePush().pushMessage(userId,
							Utilities.stickyToError(alarmDesc));
				}
			}
		}
	}

	public void checkIO() {
		// TODO Auto-generated method stub
		// 查询vbd_30min 表，找到需要监控的对象
		List<Vbd> checklist = this.getAlarmLogDAO().getCheckVbdList();
		if (checklist != null && checklist.size()>0) {
			for (Vbd item : checklist) {
				Vbd vbditem = (Vbd)item;
				if (this.getAlarmLogDAO().checkOneVbd(vbditem.getUuid(),
						vbditem.getVbdId(), 0.8, "read")) {
					String alarmDesc = "";
					int userId = 1;
					String uuid = UUID.randomUUID().toString();
					OCVM ocvm = this.vmDAO.getVM(vbditem.getUuid());
					if (ocvm != null) {
						userId = ocvm.getVmUID();
						alarmDesc = "虚拟主机 i-"
								+ vbditem.getUuid().substring(0, 8) + ",硬盘："
								+ vbditem.getVbdId() + "读操作 超过预设阀值，发生告警!";
						// /说明是虚拟机
					} else {
						// /说明是物理机.物理机不需要查询了
						alarmDesc = "物理主机 host-"
								+ vbditem.getUuid().substring(0, 8) + ",硬盘："
								+ vbditem.getVbdId() + "读操作 超过预设阀值，发生告警!";

					}
					// /告警
					this.alarmLogDAO.addAlarmLog(uuid, vbditem.getUuid(), "硬盘",
							alarmDesc, userId);
					this.getMessagePush().pushMessage(userId,
							Utilities.stickyToError(alarmDesc));
				}

				if (this.getAlarmLogDAO().checkOneVbd(vbditem.getUuid(),
						vbditem.getVbdId(), 0.8, "write")) {
					String alarmDesc = "";
					int userId = 1;
					String uuid = UUID.randomUUID().toString();
					OCVM ocvm = this.vmDAO.getVM(vbditem.getUuid());
					if (ocvm != null) {
						userId = ocvm.getVmUID();
						alarmDesc = "虚拟主机 i-"
								+ vbditem.getUuid().substring(0, 8) + ",硬盘："
								+ vbditem.getVbdId() + "写操作 超过预设阀值，发生告警!";
						// /说明是虚拟机
					} else {
						// /说明是物理机.物理机不需要查询了
						alarmDesc = "物理主机 host-"
								+ vbditem.getUuid().substring(0, 8) + ",硬盘："
								+ vbditem.getVbdId() + "写操作 超过预设阀值，发生告警!";

					}
					// /告警
					this.alarmLogDAO.addAlarmLog(uuid, vbditem.getUuid(), "硬盘",
							alarmDesc, userId);
					this.getMessagePush().pushMessage(userId,
							Utilities.stickyToError(alarmDesc));
				}
			}
		}
	}

	// /内存 只有物理机内存 Kb
	public void checkMem() {
		// TODO Auto-generated method stub
		// 1 查询mem_30min 表，找到需要监控的对象
		List<Memory> checklist = this.getAlarmLogDAO()
				.getCheckMemoryList();
		if (checklist != null && checklist.size()>0) {
			for (Object item : checklist) {
				Memory memitem= (Memory)item;
				// /小于 512M
				if (this.getAlarmLogDAO().checkOneMem(memitem.getUuid(),
						1024 * 1024 * 2)) {
					String alarmDesc = "";
					int userId = 1;
					String uuid = UUID.randomUUID().toString();
					OCVM ocvm = this.vmDAO.getVM(memitem.getUuid());
					if (ocvm != null) {
						userId = ocvm.getVmUID();
						alarmDesc = "虚拟主机 i-"
								+ memitem.getUuid().substring(0, 8)
								+ ",内存超过预设阀值，发生告警!";
						// /说明是虚拟机
					} else {
						// /说明是物理机.物理机不需要查询了
						alarmDesc = "物理主机 host-"
								+ memitem.getUuid().substring(0, 8)
								+ ",内存超过预设阀值，发生告警!";

					}
					// /告警
					this.alarmLogDAO.addAlarmLog(uuid, memitem.getUuid(), "内存",
							alarmDesc, userId);
					this.getMessagePush().pushMessage(userId,
							Utilities.stickyToError(alarmDesc));
				}
			}
		}
	}

	public JSONArray getAlarmLogList(int userId, int userLevel, int page,
			int limit, String search) {
		// TODO Auto-generated method stub
		JSONArray ja = new JSONArray();
		int totalNum = this.getAlarmLogDAO().countAlarmLogList(userId, search);
		List<AlarmLog> alarmLogList = this.getAlarmLogDAO().getOnePageList(userId, page, limit, search);
		ja.put(totalNum);
		if (alarmLogList != null) {
			for (AlarmLog alarmLog : alarmLogList) {
				JSONObject jo = new JSONObject();
				jo.put("alarmlogID", alarmLog.getUuid());
				jo.put("alarmObject", alarmLog.getAlarmObject());
				jo.put("alarmType", Utilities.encodeText(alarmLog.getAlarmType()));
				jo.put("alarmtatus", alarmLog.getAlarmStatus());
				jo.put("createDate",Utilities.formatTime(alarmLog.getAlarmDate()));
				jo.put("alarmDesc", Utilities.encodeText(alarmLog.getAlarmDesc()));
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject deleteAlarmLog(Integer userId, String alarmLogId) {
		// TODO Auto-generated method stub
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		boolean result = this.getAlarmLogDAO().removeAlarmLog(alarmLogId);
		jo.put("result", result);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.告警日志.toString(), ""));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.告警日志.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.告警日志.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject updateAlarmLog(Integer userId, String alarmLogId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
			JSONObject jo = new JSONObject();
			Date startTime = new Date();
			boolean result = this.getAlarmLogDAO().updateStatus(alarmLogId,1);
			jo.put("result", result);
			// write log and push message
			Date endTime = new Date();
			int elapse = Utilities.timeElapse(startTime, endTime);
			JSONArray infoArray = new JSONArray();
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.告警日志.toString(), ""));
			if (result) {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.告警日志.ordinal(),
						LogConstant.logAction.标为已读.ordinal(),
						LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
						startTime, elapse);
				this.getMessagePush().pushMessage(userId,
						Utilities.stickyToSuccess(log.toString()));
			} else {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.告警日志.ordinal(),
						LogConstant.logAction.标为已读.ordinal(),
						LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
						startTime, elapse);
				this.getMessagePush().pushMessage(userId,
						Utilities.stickyToError(log.toString()));
			}
			return jo;
	}

}
