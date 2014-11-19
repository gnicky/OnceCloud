package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.oncecloud.entity.EIP;

public interface EIPDAO {
//	public abstract boolean abandonEip(String eipIp, int userId);
//
//	public abstract boolean addEIP(String prefix, int start, int end,
//			Date date, int eiptype, String eipInterface);
//
//	public abstract EIP applyEip(String eipName, int userId, int eipBandwidth,
//			Date createDate, String eipUuid);
//
//	public abstract boolean bindEip(String eipIp, String dependencyUuid,
//			int type);
//
//	public abstract boolean changeBandwidth(int userId, EIP eipObj, int size);
//
//	public abstract int countAllEipList(int userId, String search);
//
//	/**
//	 * @author hty
//	 * @param search
//	 * @param uid
//	 * @return
//	 */
//	public abstract int countAllEipListAlarm(String search, int eipUID);
//
//	public abstract int countAllEipListNoUserid(String searchStr);
//
//	public abstract boolean deleteEIP(String ip, String uuid);
//
//	// 获取可用公网IP
//	public abstract List<EIP> getableeips(int uid);
//
//	/**
//	 * @author hty
//	 * @param alarmUuid
//	 * @param uid
//	 * @return
//	 */
//	public abstract List<EIP> getAllListAlarm(int eipUID, String alarmUuid);
//
//	public abstract EIP getEip(String eipIp);
//
//	public abstract String getEipId(String eip);

	public abstract String getEipIp(String dependencyUuid);

//	public abstract List<EIP> getOnePageEipList(int userId, int page,
//			int limit, String search);
//
//	/**
//	 * @author hty
//	 * @param page
//	 * @param limit
//	 * @param search
//	 * @param uid
//	 * @return
//	 */
//	public abstract List<EIP> getOnePageEipListAlarm(int page, int limit,
//			String search, int eipUID);
//
//	public abstract List<EIP> getOnePageEIPListNoUserid(int page, int limit,
//			String searchStr);
//
//	public abstract boolean ipExist(Session session, String eIp);
//
//	/**
//	 * @author hty
//	 * @param alarmUuid
//	 * @return
//	 */
//	public abstract boolean isNotExistAlarm(String alarmUuid);
//
//	public abstract boolean unBindEip(String eipIp);
//
//	/**
//	 * @author hty
//	 * @param eipip
//	 * @param alarmUuid
//	 */
//	public abstract boolean updateAlarm(String eipUuid, String alarmUuid);
//
//	/**
//	 * @param eipuuid
//	 * @param newName
//	 * @param description
//	 * @author xpx 2014-7-8
//	 */
//	public abstract boolean updateName(String eipip, String newName,
//			String description);
}
