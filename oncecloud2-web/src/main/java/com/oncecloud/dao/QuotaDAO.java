package com.oncecloud.dao;

import com.oncecloud.entity.Quota;

public interface QuotaDAO {

	/**
	 * 获取已用的配额
	 * 
	 * @param userId
	 * @return
	 */
	public abstract Quota getQuotaUsed(int userId);

	/**
	 * 获取全部的配额
	 * 
	 * @param userId
	 * @return
	 */
	public abstract Quota getQuotaTotal(int userId);

	/**
	 * 更新配额（不包含事务）
	 * 
	 * @param userId
	 * @param filedName
	 * @param size
	 * @param isadd
	 */
	public abstract void updateQuota(int userId,
			String filedName, int size, boolean isadd);

	/**
	 * 初始化配额（不包含事务）
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public abstract void initQuota(Integer userId);

	/**
	 * 更新配额
	 * 
	 * @param newQuota
	 * @return
	 */
	public abstract boolean updateQuota(Quota newQuota);
}
