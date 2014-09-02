package com.oncecloud.newentity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "oc_alarm")
public class Alarm {
	private UUID uuid;
	private String name;
	private AlarmStatus status;
	private AlarmType type;
	private boolean modified;
	private Date date;
	private boolean notify;
	private AlarmTouchStatus touch;
	private int period;
	private User user;
	private String description;

	@Id
	@Column(name = "alarm_uuid")
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Column(name = "alarm_name")
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "alarm_status")
	public AlarmStatus getStatus() {
		return status;
	}

	protected void setStatus(AlarmStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "alarm_type")
	public AlarmType getType() {
		return type;
	}

	protected void setType(AlarmType type) {
		this.type = type;
	}

	@Column(name = "alarm_modify")
	public boolean isModified() {
		return modified;
	}

	protected void setModified(boolean modified) {
		this.modified = modified;
	}

	@Column(name = "alarm_date")
	public Date getDate() {
		return date;
	}

	protected void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "alarm_isalarm")
	public boolean isNotify() {
		return notify;
	}

	protected void setNotify(boolean notify) {
		this.notify = notify;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "alarm_touch")
	public AlarmTouchStatus getTouch() {
		return touch;
	}

	protected void setTouch(AlarmTouchStatus touch) {
		this.touch = touch;
	}

	@Column(name = "alarm_period")
	public int getPeriod() {
		return period;
	}

	protected void setPeriod(int period) {
		this.period = period;
	}

	@ManyToOne
	@JoinColumn(name = "alarm_uid")
	public User getUser() {
		return user;
	}

	protected void setUser(User user) {
		this.user = user;
	}

	@Column(name = "alarm_desc")
	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	protected Alarm() {

	}

}
