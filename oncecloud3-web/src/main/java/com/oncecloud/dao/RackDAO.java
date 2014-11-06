package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.Rack;

public interface RackDAO {
//	/**
//	 * 获取机架
//	 * 
//	 * @param rackUuid
//	 * @return
//	 */
//	public abstract Rack getRack(String rackUuid);

	/**
	 * 获取一页机架列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract List<Rack> getOnePageRackList(int page, int limit,
			String search);

//	/**
//	 * 获取全部机架列表
//	 * 
//	 * @return
//	 */
//	public abstract List<Rack> getRackList();

	/**
	 * 获取数据中心的机架列表
	 * 
	 * @param dcUuid
	 * @return
	 */
	public abstract List<Rack> getRackListOfDC(String dcUuid);

	/**
	 * 获取机架总数
	 * 
	 * @param search
	 * @return
	 */
	public abstract int countAllRackList(String search);

	/**
	 * 创建机架
	 * 
	 * @param rackName
	 * @param dcId
	 * @param rackDesc
	 * @return
	 */
	public abstract Rack createRack(String rackName, String dcId,
			String rackDesc);

	/**
	 * 删除机架
	 * 
	 * @param rackId
	 * @return
	 */
	public abstract boolean deleteRack(String rackId);

//	/**
//	 * 添加机架到数据中心
//	 * 
//	 * @param rackId
//	 * @param dcId
//	 * @return
//	 */
//	public abstract boolean bindDatacenter(String rackId, String dcId);
//
//	/**
//	 * 从数据中心中删除机架
//	 * 
//	 * @param rackId
//	 * @return
//	 */
//	public abstract boolean unbindDatacenter(String rackId);
//
//	/**
//	 * 更新机架
//	 * 
//	 * @param rackId
//	 * @param rackName
//	 * @param rackDesc
//	 * @param dcid
//	 * @return
//	 */
//	public abstract boolean updateRack(String rackUuid, String rackName,
//			String rackDesc, String dcUuid);
}
