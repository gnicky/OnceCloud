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
@Table(name = "oc_host")
public class Host {
	private UUID uuid;
	private String password;
	private String name;
	private String description;
	private String ip;
	private int memory;
	private int cores;
	private String kernelVersion;
	private String xenVersion;
	private Status status;
	private Pool pool;
	private Rack rack;
	private Date createDate;

	@Id
	@Column(name = "host_uuid")
	public UUID getUuid() {
		return uuid;
	}

	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Column(name = "host_pwd")
	public String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "host_name")
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Column(name = "host_desc")
	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "host_ip")
	public String getIp() {
		return ip;
	}

	protected void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "host_mem")
	public int getMemory() {
		return memory;
	}

	protected void setMemory(int memory) {
		this.memory = memory;
	}

	@Column(name = "host_cpu")
	public int getCores() {
		return cores;
	}

	protected void setCores(int cores) {
		this.cores = cores;
	}

	@Column(name = "kernel_version")
	public String getKernelVersion() {
		return kernelVersion;
	}

	protected void setKernelVersion(String kernelVersion) {
		this.kernelVersion = kernelVersion;
	}

	@Column(name = "xen_version")
	public String getXenVersion() {
		return xenVersion;
	}

	protected void setXenVersion(String xenVersion) {
		this.xenVersion = xenVersion;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "host_status")
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "pool_uuid")
	public Pool getPool() {
		return pool;
	}

	protected void setPool(Pool pool) {
		this.pool = pool;
	}

	@ManyToOne
	@JoinColumn(name = "rack_uuid")
	public Rack getRack() {
		return rack;
	}

	public void setRack(Rack rack) {
		this.rack = rack;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	protected void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	protected Host() {

	}
}
