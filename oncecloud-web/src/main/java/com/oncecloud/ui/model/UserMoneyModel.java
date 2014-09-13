package com.oncecloud.ui.model;

public class UserMoneyModel {
	private int userId;
	private String userName;
	private double networkFee;
	private double powerFee;
	private double hostFee;
	private double storageFee;
	private double netEquipmentFee;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getNetworkFee() {
		return networkFee;
	}
	public void setNetworkFee(double networkFee) {
		this.networkFee = networkFee;
	}
	public double getPowerFee() {
		return powerFee;
	}
	public void setPowerFee(double powerFee) {
		this.powerFee = powerFee;
	}
	public double getHostFee() {
		return hostFee;
	}
	public void setHostFee(double hostFee) {
		this.hostFee = hostFee;
	}
	public double getStorageFee() {
		return storageFee;
	}
	public void setStorageFee(double storageFee) {
		this.storageFee = storageFee;
	}
	public double getNetEquipmentFee() {
		return netEquipmentFee;
	}
	public void setNetEquipmentFee(double netEquipmentFee) {
		this.netEquipmentFee = netEquipmentFee;
	}
	
	
}
