package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

///告警记录表
@Entity
@Table(name = "alarmlog")
public class AlarmLog {
     
	public AlarmLog(){}
	
	private String uuid;
	
	// 告警对象
	private String alarmObject;
	
	/// 告警类型 cpu，内存，io
	private String alarmType;
	
	//0 是未读，1 是已读
	private Integer alarmStatus;
	
	//告警描述
	private String alarmDesc;
	
	//关联的用户id
	private Integer alarmUid;
	
	private Date alarmDate;
	
	//备留属性1 
	private String alarmAtt1;
	
	//备留属性2
    private String alarmAtt2;
    
	public AlarmLog(String uuid,String alarmType,String alarmObject,Integer alarmStatus,String alarmDesc,Integer alarmUid,Date alarmDate){
		this.uuid = uuid;
		this.alarmType = alarmType;
		this.alarmObject = alarmObject;
		this.alarmStatus = alarmStatus;
		this.alarmDesc = alarmDesc;
		this.alarmUid = alarmUid;
		this.alarmDate = alarmDate;
		
	}

	@Id
	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "alarm_object")
	public String getAlarmObject() {
		return alarmObject;
	}

	public void setAlarmObject(String alarmObject) {
		this.alarmObject = alarmObject;
	}

	@Column(name = "alarm_type")
	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	@Column(name = "alarm_status")
	public Integer getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(Integer alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	@Column(name = "alarm_desc")
	public String getAlarmDesc() {
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	@Column(name = "alarm_uid")
	public Integer getAlarmUid() {
		return alarmUid;
	}

	public void setAlarmUid(Integer alarmUid) {
		this.alarmUid = alarmUid;
	}

	@Column(name = "alarm_date")
	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}

	@Column(name = "alarm_att1")
	public String getAlarmAtt1() {
		return alarmAtt1;
	}

	public void setAlarmAtt1(String alarmAtt1) {
		this.alarmAtt1 = alarmAtt1;
	}

	@Column(name = "alarm_att2")
	public String getAlarmAtt2() {
		return alarmAtt2;
	}

	public void setAlarmAtt2(String alarmAtt2) {
		this.alarmAtt2 = alarmAtt2;
	}

	@Override
	public String toString() {
		return "AlarmLog [uuid=" + uuid + ", alarmObject=" + alarmObject
				+ ", alarmType=" + alarmType + ", alarmStatus=" + alarmStatus
				+ ", alarmDesc=" + alarmDesc + ", alarmUid=" + alarmUid
				+ ", alarmDate=" + alarmDate + "]";
	}
	
}
