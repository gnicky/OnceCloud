package com.oncecloud.newentity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "oc_rule")
public class Rule {
	private UUID uuid;
	private String name;
	private int priority;
	private String protocol;
	private Integer startPort;
	private Integer endPort;
	private Status status;
	private String targetIP;
	private Firewall firewall;

	@Id
	@Type(type = "uuid-char")
	@Column(name = "rule_id")
	public UUID getUuid() {
		return uuid;
	}

	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Column(name = "rule_name")
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Column(name = "rule_priority")
	public int getPriority() {
		return priority;
	}

	protected void setPriority(int priority) {
		this.priority = priority;
	}

	@Column(name = "rule_protocol")
	public String getProtocol() {
		return protocol;
	}

	protected void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Column(name = "rule_sport")
	public Integer getStartPort() {
		return startPort;
	}

	protected void setStartPort(Integer startPort) {
		this.startPort = startPort;
	}

	@Column(name = "rule_eport")
	public Integer getEndPort() {
		return endPort;
	}

	protected void setEndPort(Integer endPort) {
		this.endPort = endPort;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "rule_state")
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "rule_ip")
	public String getTargetIP() {
		return targetIP;
	}

	protected void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}

	@ManyToOne
	@JoinColumn(name = "rule_firewall")
	public Firewall getFirewall() {
		return firewall;
	}

	protected void setFirewall(Firewall firewall) {
		this.firewall = firewall;
	}

	protected Rule() {

	}

	public Rule(UUID uuid, String name, int priority, String protocol,
			Integer startPort, Integer endPort, Status status, String targetIP,
			Firewall firewall) {
		this.setUuid(uuid);
		this.setName(name);
		this.setPriority(priority);
		this.setProtocol(protocol);
		this.setStartPort(startPort);
		this.setEndPort(endPort);
		this.setStatus(status);
		this.setTargetIP(targetIP);
		this.setFirewall(firewall);
	}
}
