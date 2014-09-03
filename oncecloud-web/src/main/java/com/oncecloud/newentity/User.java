package com.oncecloud.newentity;

import java.util.Date;

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
@Table(name = "oc_user")
public class User {
	private int id;
	private String userName;
	private String password;
	private String email;
	private String telephone;
	private String company;
	private UserLevel level;
	private Status status;
	private Date createDate;
	private Pool pool;
	private Double balance;
	private Integer voucher;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	protected void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "user_pass")
	public String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "user_mail")
	public String getEmail() {
		return email;
	}

	protected void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "user_phone")
	public String getTelephone() {
		return telephone;
	}

	protected void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "user_company")
	public String getCompany() {
		return company;
	}

	protected void setCompany(String company) {
		this.company = company;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "user_level")
	public UserLevel getLevel() {
		return level;
	}

	protected void setLevel(UserLevel level) {
		this.level = level;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "user_status")
	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "user_date")
	public Date getCreateDate() {
		return createDate;
	}

	protected void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@ManyToOne
	@JoinColumn(name = "user_allocate")
	public Pool getPool() {
		return pool;
	}

	protected void setPool(Pool pool) {
		this.pool = pool;
	}

	@Column(name = "user_balance")
	public Double getBalance() {
		return balance;
	}

	protected void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "user_voucher")
	public Integer getVoucher() {
		return voucher;
	}

	protected void setVoucher(Integer voucher) {
		this.voucher = voucher;
	}

	protected User() {

	}

	public User(String userName, String password, String email,
			String telephone, String company, UserLevel level, Status status,
			Date createDate, Pool pool, Double balance, Integer voucher) {
		this.setUserName(userName);
		this.setPassword(password);
		this.setEmail(email);
		this.setTelephone(telephone);
		this.setCompany(company);
		this.setLevel(level);
		this.setStatus(status);
		this.setCreateDate(createDate);
		this.setPool(pool);
		this.setBalance(balance);
		this.setVoucher(voucher);
	}
}