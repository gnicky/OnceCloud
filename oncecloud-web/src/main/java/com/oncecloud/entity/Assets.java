package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oncecloud.main.Utilities;

/**
 * @author cyh
 * @version 2014/09/12
 */
@Entity
@Table(name = "assets")
public class Assets {
	private Integer id;
	private String assetsName;
	private Integer assetsNum;
	private double assetsUnitPrice;
	private double assetsPrice;
	private double assetsPermonth;
	private String assersIcon;
	
	public Assets() {
	}

	@Id
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "assets_name")
	public String getAssetsName() {
		return assetsName;
	}

	public void setAssetsName(String assetsName) {
		this.assetsName = assetsName;
	}

	@Column(name = "assets_num")
	public Integer getAssetsNum() {
		return assetsNum;
	}

	public void setAssetsNum(Integer assetsNum) {
		this.assetsNum = assetsNum;
	}

	@Column(name = "assets_unitprice")
	public double getAssetsUnitPrice() {
		return assetsUnitPrice;
	}

	public void setAssetsUnitPrice(double assetsUnitPrice) {
		this.assetsUnitPrice = assetsUnitPrice;
	}

	@Column(name = "assets_price")
	public double getAssetsPrice() {
		return assetsPrice;
	}

	public void setAssetsPrice(double assetsPrice) {
		this.assetsPrice = assetsPrice;
	}

	@Column(name = "assets_permonth")
	public double getAssetsPermonth() {
		return assetsPermonth;
	}

	public void setAssetsPermonth(double assetsPermonth) {
		this.assetsPermonth = assetsPermonth;
	}

	@Column(name = "assers_icon")
	public String getAssersIcon() {
		return assersIcon;
	}

	public void setAssersIcon(String assersIcon) {
		this.assersIcon = assersIcon;
	}



}
