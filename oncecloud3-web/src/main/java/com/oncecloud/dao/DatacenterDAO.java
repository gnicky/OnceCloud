package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.Datacenter;

public interface DatacenterDAO {

	public abstract int countAllDatacenter(String search);

	public abstract Datacenter createDatacenter(String dcName,
			String dcLocation, String dcDesc);

	public abstract boolean deleteDatacenter(String dcUuid);

//	public abstract List<Datacenter> getAllPageDCList();
	/**
	 * 非本模块使用，获取dc信息
	 * @param dcUuid
	 * @return
	 */
	public abstract Datacenter getDatacenter(String dcUuid);

	public abstract List<Datacenter> getOnePageDCList(int page, int limit,
			String search);

	public abstract boolean updateDatacenter(String dcUuid, String dcName,
			String dcLocation, String dcDesc);

}