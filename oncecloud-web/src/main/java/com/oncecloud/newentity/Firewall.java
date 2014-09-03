package com.oncecloud.newentity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "oc_firewall")
public class Firewall {

	private UUID uuid;
	private String name;
	private User user;
	private Date createDate;
	private boolean confirm;
	private boolean defaultFirewall;

	@Id
	@Type(type = "uuid-char")
	@Column(name = "firewall_id")
	public UUID getUuid() {
		return uuid;
	}

	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Column(name = "firewall_name")
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "firewall_uid")
	public User getUser() {
		return user;
	}

	protected void setUser(User user) {
		this.user = user;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	protected void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "firewall_confirm")
	public boolean isConfirm() {
		return confirm;
	}

	protected void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	@Column(name = "is_default")
	public boolean isDefaultFirewall() {
		return defaultFirewall;
	}

	protected void setDefaultFirewall(boolean defaultFirewall) {
		this.defaultFirewall = defaultFirewall;
	}

	protected Firewall() {

	}

	public Firewall(UUID uuid, String name, User user, Date createDate,
			boolean confirm, boolean defaultFirewall) {
		this.setUuid(uuid);
		this.setName(name);
		this.setUser(user);
		this.setCreateDate(createDate);
		this.setConfirm(confirm);
		this.setDefaultFirewall(defaultFirewall);
	}
}