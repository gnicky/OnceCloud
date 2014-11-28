package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.Alarm;

public interface AlarmDAO {
	public abstract boolean addAlarm(String alarmUuid, String alarmName,
			Integer alarmType, Integer alarmIsalarm, Integer alarmTouch,
			Integer alarmPeriod, int userId);

	public abstract int countAlarmList(int userId, String keyword);

	public abstract Alarm getAlarm(String alarmUuid);

	public abstract List<Alarm> getOnePageList(int userId, int pageIndex,
			int itemPerPage, String keyword);

	public abstract boolean removeAlarm(Alarm alarm);

	public abstract boolean updateAlarm(Alarm alarm);

	public abstract boolean updateName(String alarmUuid, String newName,
			String description);

	public abstract boolean updatePeriod(String alarmUuid, int alarmPeriod);

}
