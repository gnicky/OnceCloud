package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.DHCP;

public interface DHCPDAO {

	public abstract int countAllDHCP(String search);

	public abstract boolean deleteDHCP(String ip, String mac);

//	public abstract DHCP getDHCP(String dhcpMac);
//
//	public abstract DHCP getFreeDHCP(String tenantUuid, int depenType);

	public abstract List<DHCP> getOnePageDHCPList(int page, int limit,
			String search);

//	public abstract List<DHCP> getDHCPList();
//
//	public abstract boolean ipExist(String dhcpIp);

	public abstract boolean returnDHCP(String dhcpMac);

	public abstract void saveDHCP(DHCP dhcp);
	
	public abstract boolean ipExist(String dhcpIp);
	
}
