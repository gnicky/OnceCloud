package com.oncecloud.newentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "oc_quota")
public class Quota {
	private int id;
	private User user;
	private int elasticIPLimit;
	private int instanceLimit;
	private int snapshotLimit;
	private int imageLimit;
	private int diskCountLimit;
	private int diskStorageLimit;
	private int sshKeyLimit;
	private int firewallLimit;
	private int routerLimit;
	private int vlanLimit;
	private int loadBalancerLimit;
	private int bandwidthLimit;
	private int memoryLimit;
	private int cpuCoreLimit;
	private QuotaType type;

	@Id
	@Column(name = "quota_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "quota_uid")
	public User getUser() {
		return user;
	}

	protected void setUser(User user) {
		this.user = user;
	}

	@Column(name = "quota_ip")
	public int getElasticIPLimit() {
		return elasticIPLimit;
	}

	protected void setElasticIPLimit(int elasticIPLimit) {
		this.elasticIPLimit = elasticIPLimit;
	}

	@Column(name = "quota_vm")
	public int getInstanceLimit() {
		return instanceLimit;
	}

	protected void setInstanceLimit(int instanceLimit) {
		this.instanceLimit = instanceLimit;
	}

	@Column(name = "quota_snapshot")
	public int getSnapshotLimit() {
		return snapshotLimit;
	}

	protected void setSnapshotLimit(int snapshotLimit) {
		this.snapshotLimit = snapshotLimit;
	}

	@Column(name = "quota_image")
	public int getImageLimit() {
		return imageLimit;
	}

	protected void setImageLimit(int imageLimit) {
		this.imageLimit = imageLimit;
	}

	@Column(name = "quota_disk_n")
	public int getDiskCountLimit() {
		return diskCountLimit;
	}

	protected void setDiskCountLimit(int diskCountLimit) {
		this.diskCountLimit = diskCountLimit;
	}

	@Column(name = "quota_disk_s")
	public int getDiskStorageLimit() {
		return diskStorageLimit;
	}

	protected void setDiskStorageLimit(int diskStorageLimit) {
		this.diskStorageLimit = diskStorageLimit;
	}

	@Column(name = "quota_ssh")
	public int getSshKeyLimit() {
		return sshKeyLimit;
	}

	protected void setSshKeyLimit(int sshKeyLimit) {
		this.sshKeyLimit = sshKeyLimit;
	}

	@Column(name = "quota_firewall")
	public int getFirewallLimit() {
		return firewallLimit;
	}

	protected void setFirewallLimit(int firewallLimit) {
		this.firewallLimit = firewallLimit;
	}

	@Column(name = "quota_route")
	public int getRouterLimit() {
		return routerLimit;
	}

	protected void setRouterLimit(int routerLimit) {
		this.routerLimit = routerLimit;
	}

	@Column(name = "quota_vlan")
	public int getVlanLimit() {
		return vlanLimit;
	}

	protected void setVlanLimit(int vlanLimit) {
		this.vlanLimit = vlanLimit;
	}

	@Column(name = "quota_loadbalance")
	public int getLoadBalancerLimit() {
		return loadBalancerLimit;
	}

	protected void setLoadBalancerLimit(int loadBalancerLimit) {
		this.loadBalancerLimit = loadBalancerLimit;
	}

	@Column(name = "quota_bandwidth")
	public int getBandwidthLimit() {
		return bandwidthLimit;
	}

	protected void setBandwidthLimit(int bandwidthLimit) {
		this.bandwidthLimit = bandwidthLimit;
	}

	@Column(name = "quota_memory")
	public int getMemoryLimit() {
		return memoryLimit;
	}

	protected void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	@Column(name = "quota_cpu")
	public int getCpuCoreLimit() {
		return cpuCoreLimit;
	}

	protected void setCpuCoreLimit(int cpuCoreLimit) {
		this.cpuCoreLimit = cpuCoreLimit;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "quota_type")
	public QuotaType getType() {
		return type;
	}

	protected void setType(QuotaType type) {
		this.type = type;
	}

	protected Quota() {

	}

	public Quota(User user, int elasticIPLimit, int instanceLimit,
			int snapshotLimit, int imageLimit, int diskCountLimit,
			int diskStorageLimit, int sshKeyLimit, int firewallLimit,
			int routerLimit, int vlanLimit, int loadBalancerLimit,
			int bandwidthLimit, int memoryLimit, int cpuCoreLimit,
			QuotaType type) {
		this.setUser(user);
		this.setElasticIPLimit(elasticIPLimit);
		this.setInstanceLimit(instanceLimit);
		this.setSnapshotLimit(snapshotLimit);
		this.setImageLimit(imageLimit);
		this.setDiskCountLimit(diskCountLimit);
		this.setDiskStorageLimit(diskStorageLimit);
		this.setSshKeyLimit(sshKeyLimit);
		this.setFirewallLimit(firewallLimit);
		this.setRouterLimit(routerLimit);
		this.setVlanLimit(vlanLimit);
		this.setLoadBalancerLimit(loadBalancerLimit);
		this.setBandwidthLimit(bandwidthLimit);
		this.setMemoryLimit(memoryLimit);
		this.setCpuCoreLimit(cpuCoreLimit);
		this.setType(type);
	}
}
