package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_alarm")
public class Alarm {
	private String alarmUuid;
	private String alarmName;
	// 0暂停 1活跃
	private Integer alarmStatus;
	// 0主机 1公网ip 2路由器 3负载均衡监听器HTTP 4负载均衡监听器HTTP5 5负载均衡监听器TCP 6负载均衡后端HTTP
	// 7负载均衡后端TCP
	private Integer alarmType;
	// 0否 1是
	private Integer alarmModify;
	private Date alarmDate;
	// 0否 1是
	private Integer alarmIsalarm;
	// 0资源变为警告时 1资源恢复正常时 2资源变为警告时和资源恢复正常时
	private Integer alarmTouch;
	// 1分钟或5分钟
	private Integer alarmPeriod;
	private Integer alarmUid;
	private String alarmDesc;

	public Alarm() {

	}

	public Alarm(String alarmUuid, String alarmName, Integer alarmType,
			Date alarmDate, Integer alarmIsalarm, Integer alarmTouch,
			Integer alarmPeriod, Integer alarmUid) {
		this.alarmUuid = alarmUuid;
		this.alarmName = alarmName;
		this.alarmStatus = 1;
		this.alarmType = alarmType;
		this.alarmModify = 1;
		this.alarmDate = alarmDate;
		this.alarmIsalarm = alarmIsalarm;
		this.alarmTouch = alarmTouch;
		this.alarmPeriod = alarmPeriod;
		this.alarmUid = alarmUid;
	}

	@Id
	@Column(name = "alarm_uuid")
	public String getAlarmUuid() {
		return alarmUuid;
	}

	public void setAlarmUuid(String alarmUuid) {
		this.alarmUuid = alarmUuid;
	}

	@Column(name = "alarm_name")
	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	@Column(name = "alarm_status")
	public Integer getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(Integer alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	@Column(name = "alarm_type")
	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	@Column(name = "alarm_modify")
	public Integer getAlarmModify() {
		return alarmModify;
	}

	public void setAlarmModify(Integer alarmModify) {
		this.alarmModify = alarmModify;
	}

	@Column(name = "alarm_date")
	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}

	@Column(name = "alarm_isalarm")
	public Integer getAlarmIsalarm() {
		return alarmIsalarm;
	}

	public void setAlarmIsalarm(Integer alarmIsalarm) {
		this.alarmIsalarm = alarmIsalarm;
	}

	@Column(name = "alarm_touch")
	public Integer getAlarmTouch() {
		return alarmTouch;
	}

	public void setAlarmTouch(Integer alarmTouch) {
		this.alarmTouch = alarmTouch;
	}

	@Column(name = "alarm_period")
	public Integer getAlarmPeriod() {
		return alarmPeriod;
	}

	public void setAlarmPeriod(Integer alarmPeriod) {
		this.alarmPeriod = alarmPeriod;
	}

	@Column(name = "alarm_uid")
	public Integer getAlarmUid() {
		return alarmUid;
	}

	public void setAlarmUid(Integer alarmUid) {
		this.alarmUid = alarmUid;
	}

	@Column(name = "alarm_desc")
	public String getAlarmDesc() {
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	@Override
	public String toString() {
		return "Alarm [alarmUuid=" + alarmUuid + " alarmName=" + alarmName
				+ " alarmStatus=" + alarmStatus + " alarmType=" + alarmType
				+ " alarmModify=" + alarmModify + " alarmDate=" + alarmDate
				+ " alarmIsalarm=" + alarmIsalarm + " alarmTouch=" + alarmTouch
				+ " alarmPeriod=" + alarmPeriod + " alarmUid=" + alarmUid
				+ "alarmDesc=" + alarmDesc + "]";
	}

}
