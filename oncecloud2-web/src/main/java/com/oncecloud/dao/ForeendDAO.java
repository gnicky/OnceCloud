package com.oncecloud.dao;

import java.util.List;

import org.json.JSONArray;

import com.oncecloud.entity.Foreend;

public interface ForeendDAO {
	
	/**
	 * @param foreUuid
	 * @param state
	 *            1可用,0 禁用
	 * @return true 成功,false 失败
	 * @author xpx 2014-7-11
	 */
	public abstract boolean changeForeendStatus(String foreUuid, int state);

	/**
	 * 检查端口的重复性
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @param forePort
	 *            新建端口
	 * @return true 端口可用,false 端口不可用
	 * @author xpx 2014-7-11
	 */
	public abstract boolean checkRepeat(String lbUuid, Integer forePort);

	/**
	 * 创建前端监听
	 * 
	 * @param foreName
	 *            前端监听名称
	 * @param foreProtocol
	 *            监听协议"TCP"/"HTTP"
	 * @param forePort
	 *            监听端口
	 * @param forePolicy
	 *            均衡策略
	 * @param lbUuid
	 *            对应负载均衡对象id
	 * @return 更新后的Foreend前端对象
	 * @author xpx 2014-7-11
	 */
	public abstract Foreend createForeend(String foreUuid, String foreName,
			String foreProtocol, Integer forePort, Integer forePolicy,
			String lbUuid);

	public abstract boolean deleteForeend(String foreUuid);

	/**
	 * 根据负载均衡的id获取前端监听器的列表
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @return 监听器列表
	 * @author xpx 2014-7-11
	 */
	public abstract List<Foreend> getFEListByLB(String lbUuid);

	/**
	 * 根据负载均衡的id 组成修改应用的json串
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @return 监听器列表
	 * @author xpx 2014-7-11
	 */
	public abstract List<Foreend> getSimpleFEListByLB(String lbUuid);

	/**
	 * 更新前端监听
	 * 
	 * @param foreUuid
	 * @param foreName
	 * @param forePolicy
	 * @return
	 * @author xpx 2014-7-11
	 */
	public abstract boolean updateForeend(String foreUuid, String foreName,
			Integer forePolicy);
}
