package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_exc")
public class OCHException {

	private Integer excId;
	private Integer excUid;
	private String excClassName;
	private String excFunName;
	private String excException;
	private Date excDate;
	private String excArgs;
	
	@Id
	@Column(name = "exc_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getExcId() {
		return excId;
	}

	public void setExcId(Integer excId) {
		this.excId = excId;
	}

	@Column(name = "exc_uid")
	public Integer getExcUid() {
		return excUid;
	}

	public void setExcUid(Integer excUid) {
		this.excUid = excUid;
	}

	@Column(name = "exc_classname")
	public String getExcClassName() {
		return excClassName;
	}

	public void setExcClassName(String excClassName) {
		this.excClassName = excClassName;
	}

	@Column(name = "exc_funname")
	public String getExcFunName() {
		return excFunName;
	}

	public void setExcFunName(String excFunName) {
		this.excFunName = excFunName;
	}

	@Column(name = "exc_exception")
	public String getExcException() {
		return excException;
	}

	public void setExcException(String excException) {
		this.excException = excException;
	}
	
	@Column(name = "exc_date")
	public Date getExcDate() {
		return excDate;
	}

	public void setExcDate(Date excDate) {
		this.excDate = excDate;
	}

	@Column(name = "exc_args")
	public String getExcArgs() {
		return excArgs;
	}

	public void setExcArgs(String excArgs) {
		this.excArgs = excArgs;
	}

}
