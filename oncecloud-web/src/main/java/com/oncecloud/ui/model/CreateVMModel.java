package com.oncecloud.ui.model;

public class CreateVMModel {
     private String imageUuid;
     
     private int cpu;
     
     private double memory;
     
     private String vmName;
     
     private String password;
     
     private String vmUuid;
     
	public String getImageUuid() {
		return imageUuid;
	}
	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public double getMemory() {
		return memory;
	}
	public void setMemory(double memory) {
		this.memory = memory;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVmUuid() {
		return vmUuid;
	}
	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}
     
     
}
