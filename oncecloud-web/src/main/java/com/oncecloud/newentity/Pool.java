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
@Table(name = "oc_pool")
public class Pool {
	private UUID uuid;
	private String name;
	private String description;
	private Host master;
	private Datacenter datacenter;
	private Status status;
	private Date createDate;

	@Id
	@Column(name = "pool_uuid")
	public UUID getUuid() {
		return uuid;
	}

	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Column(name = "pool_name")
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Column(name = "pool_desc")
	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "pool_master")
	public Host getMaster() {
		return master;
	}

	protected void setMaster(Host master) {
		this.master = master;
	}

	@ManyToOne
	@JoinColumn(name = "dc_uuid")
	public Datacenter getDatacenter() {
		return datacenter;
	}

	protected void setDatacenter(Datacenter datacenter) {
		this.datacenter = datacenter;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "pool_status")
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

	protected Pool() {

	}
}
