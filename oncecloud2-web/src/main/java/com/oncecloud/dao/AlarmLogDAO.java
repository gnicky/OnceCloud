package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.AlarmLog;
import com.oncecloud.entity.performance.Cpu;
import com.oncecloud.entity.performance.Memory;
import com.oncecloud.entity.performance.Vbd;

public interface AlarmLogDAO {
	//插入告警日志
	public abstract boolean addAlarmLog(String uuid, String alarmObject,
			String alarmType, String alarmDesc, int userId);

	public abstract int countAlarmLogList(int userId, String keyword);

	public abstract List<AlarmLog> getOnePageList(int userId, int pageIndex,
			int itemPerPage, String keyword);

	public abstract AlarmLog getAlarmLog(String alarmlogUuid);

	public abstract boolean removeAlarmLog(String alarmlogUuid);

	public abstract boolean updateAlarm(AlarmLog alarm);

	public abstract boolean updateStatus(String alarmUuid, int alarmStatus);

	public abstract List<Cpu> getCheckCPUList();

	public abstract boolean checkOneCPU(String uuid, Integer cpuId, double d);

	public abstract List<Vbd> getCheckVbdList();

	//// d是kbps 数值
	public abstract boolean checkOneVbd(String uuid, String vbdId, double d,
			String isread);

	public abstract List<Memory> getCheckMemoryList();

	///d 空闲内存 小于多少 数值
	public abstract boolean checkOneMem(String uuid, double d);

}
