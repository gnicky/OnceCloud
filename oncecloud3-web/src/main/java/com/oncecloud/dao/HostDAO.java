package com.oncecloud.dao;

import java.util.List;
import java.util.Set;

import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCPool;
import com.oncecloud.entity.Storage;

public interface HostDAO {

	public abstract int countAllHostList(String search);

//	public abstract List<OCHost> getAllHost();

	public abstract OCHost getHost(String hostUuid);

	public abstract OCHost getHostNoTransactional(String hostUuid);

//	public abstract List<OCHost> getHostForImage();
//
//	public abstract OCHost getHostFromIp(String hostIp);

	public abstract List<OCHost> getHostListOfPool(String poolUuid);

	public abstract List<OCHost> getHostListOfRack(String rackUuid);

	public abstract List<OCHost> getOnePageHostList(int page, int limit,
			String search);

//	public abstract List<OCHost> getOnePageLoadHostList(int page, int limit,
//			String search, String sruuid);
//
//	public abstract List<Storage> getSROfHost(String hostUuid);
//
//	public abstract boolean isSameSr(Set<String> sr1, Set<String> sr2);
//
//	public abstract boolean setPool(String hostUuid, String poolUuid);
//
//	public abstract boolean unbindSr(String hostUuid, String srUuid);
//
//	public abstract boolean saveHost(OCHost host);
//
//	public abstract boolean updateHost(String hostId, String hostName,
//			String hostDesc, String rackUuid);
//
//	public abstract boolean deleteHost(String hostId);
//
//	public abstract void deleteHostSR(String hostId);
//
//	public abstract boolean eject(OCHost host, String poolUuid,
//			String masterUuid);
//
//	public abstract boolean updatePoolMaster(OCPool pool, OCHost targetHost);

}
