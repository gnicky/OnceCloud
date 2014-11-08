package com.oncecloud.manager;

import org.json.JSONArray;


public interface PoolManager {
//
//	public abstract RouterDAO getRouterDAO();
//
//	public abstract void setRouterDAO(RouterDAO routerDAO);
//
//	public abstract LBDAO getLbDAO();
//
//	public abstract void setLbDAO(LBDAO lbDAO);

	public abstract JSONArray createPool(String poolName, String poolDesc,
			String dcUuid, String dcName, int userId);

	public abstract JSONArray getPoolList(int page, int limit, String search);

	public abstract JSONArray deletePool(String poolId, String poolName,
			int userId);

//	public abstract void bind(String poolId, String dcid, int userId);
//
//	public abstract JSONArray unbind(String poolId, int userId);

	public abstract void updatePool(String poolUuid, String poolName,
			String poolDesc, String dcUuid, int userId);

//	public abstract JSONArray getAllPool();

	public abstract void keepAccordance(int userId, String poolUuid);

}
