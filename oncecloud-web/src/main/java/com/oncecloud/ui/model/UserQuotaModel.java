package com.oncecloud.ui.model;

import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;

public class UserQuotaModel {
    private double cpuMoney;
    private double memMoney;
    private double netMoney;
    private double diskMoney;
    private double allMoney;
    
	public double getCpuMoney() {
		return cpuMoney;
	}
	public void setCpuMoney(double cpuMoney) {
		this.cpuMoney = cpuMoney;
	}
	public double getMemMoney() {
		return memMoney;
	}
	public void setMemMoney(double memMoney) {
		this.memMoney = memMoney;
	}
	public double getNetMoney() {
		return netMoney;
	}
	public void setNetMoney(double netMoney) {
		this.netMoney = netMoney;
	}
	public double getDiskMoney() {
		return diskMoney;
	}
	public void setDiskMoney(double diskMoney) {
		this.diskMoney = diskMoney;
	}
	public double getAllMoney() {
		return memMoney+netMoney+diskMoney+cpuMoney;
	}
	public void setAllMoney(double allMoney) {
		this.allMoney = allMoney;
	}
    
 
    
}
