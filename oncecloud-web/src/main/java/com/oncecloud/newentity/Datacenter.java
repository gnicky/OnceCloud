package com.oncecloud.newentity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "oc_datacenter")
public class Datacenter {
	private UUID uuid;
	private String name;
	private String location;
	private String description;
	private Status status;
	private Date createDate;

	@Id
	@Type(type = "uuid-char")
	@Column(name = "dc_uuid")
	public UUID getUuid() {
		return uuid;
	}

	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Column(name = "dc_name")
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Column(name = "dc_location")
	public String getLocation() {
		return location;
	}

	protected void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "dc_desc")
	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "dc_status")
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	protected void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	protected Datacenter() {

	}
}