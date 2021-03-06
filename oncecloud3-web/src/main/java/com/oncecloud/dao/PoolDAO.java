package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.OCPool;

public interface PoolDAO {

	/**
	 * 获取资源池
	 * 
	 * @param poolUuid
	 * @return
	 */
	public abstract OCPool getPool(String poolUuid);

	public abstract OCPool getPoolNoTransactional(String poolUuid);

	/**
	 * 获取随机资源池
	 * 
	 * @return
	 */
	public abstract String getRandomPool();

	/**
	 * 获取一页资源池列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract List<OCPool> getOnePagePoolList(int page, int limit,
			String search);

	/**
	 * 获取所有资源池列表
	 * 
	 * @return
	 */
	public abstract List<OCPool> getPoolList();

	/**
	 * 获取数据中心的资源池列表
	 * 
	 * @param dcUuid
	 * @return
	 */
	public abstract List<OCPool> getPoolListOfDC(String dcUuid);

	public abstract OCPool getPoolByMaster(String poolMaster);

	/**
	 * 获取资源池总数
	 * 
	 * @param search
	 * @return
	 */
	public abstract int countAllPoolList(String search);

	/**
	 * 创建资源池
	 * 
	 * @param poolName
	 * @param poolDesc
	 * @param dcuuid
	 * @return
	 */
	public abstract OCPool createPool(String poolName, String poolDesc,
			String dcuuid);

	/**
	 * 删除资源池
	 * 
	 * @param poolId
	 * @return
	 */
	public abstract boolean deletePool(String poolId);

//	/**
//	 * 添加资源池到数据中心
//	 * 
//	 * @param poolUuid
//	 * @param dcUuid
//	 * @return
//	 */
//	public abstract boolean bindPool(String poolUuid, String dcUuid);
//
//	/**
//	 * 从数据中心中删除资源池
//	 * 
//	 * @param poolUuid
//	 * @param dcUuid
//	 * @return
//	 */
//	public abstract boolean unbindPool(String poolId);

	/**
	 * 更新资源池
	 * 
	 * @param poolId
	 * @param poolName
	 * @param poolDesc
	 * @param dcuuid
	 * @return
	 */
	public abstract boolean updatePool(String poolId, String poolName,
			String poolDesc, String dcUuid);
	
	public abstract boolean update(OCPool pool);
}
