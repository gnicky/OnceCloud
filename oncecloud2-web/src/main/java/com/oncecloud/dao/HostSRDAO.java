package com.oncecloud.dao;

import java.util.Set;

import com.oncecloud.entity.HostSR;

public interface HostSRDAO {
	
	public abstract HostSR createHostSR(String hostUuid, String srUuid);

	public abstract Set<String> getSRList(String hostUuid);

	public abstract Set<String> getHostList(String srUuid);

}
