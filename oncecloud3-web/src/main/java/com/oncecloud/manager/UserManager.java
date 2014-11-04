package com.oncecloud.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.User;

public interface UserManager {

//	public abstract VolumeDAO getVolumeDAO();
//
//	public abstract void setVolumeDAO(VolumeDAO volumeDAO);
//
//	public abstract EIPDAO getEipDAO();
//
//	public abstract void setEipDAO(EIPDAO eipDAO);
//
//	public abstract VMDAO getVmDAO();
//
//	public abstract void setVmDAO(VMDAO vmDAO);

	public abstract int checkLogin(String userName, String userPass);

	public abstract User getUser(String user);
//	public abstract void initPlatform();
//
//	/**
//	 * 获取用户余额
//	 * 
//	 * @param userId
//	 * @return
//	 */
//	public abstract JSONObject getBalance(int userId);
//
//	public abstract User userRegister(String userName, String userPassword,
//			String userEmail, String userTelephone, String userCompany,
//			String uLevel, String poolUuid);
//
//	public abstract JSONArray doRegister(String userName, String userPassword,
//			String userEmail, String userTelephone)
//			throws UnsupportedEncodingException;
//
//	public abstract JSONArray doQueryUser(String userName);
//
//	public abstract JSONArray doCreateUser(String userName,
//			String userPassword, String userEmail, String userTelephone,
//			String userCompany, String userLevel, int userid, String poolUuid);
//
//	public abstract void doQuotaUpdate(int quotaid, int changerId, int eip,
//			int vm, int bk, int img, int vol, int ssh, int fw, int rt,
//			int vlan, int lb, int disk, int bw, int mem, int cpu, int userId);
//
//	public abstract JSONObject doGetUserQuota(int userId);
//
//	public abstract JSONObject doGetOneUser(int userId);
//
//	public abstract JSONArray doGetCompanyList();
//
//	public abstract void doUpdateUser(int userId, int changeId,
//			String userName, String userEmail, String userTel, String userCom,
//			String userLevel);
//
//	public abstract JSONObject doDeleteUser(int userId, int changeId,
//			String userName);
//
//	/**
//	 * 获取用户列表
//	 * 
//	 * @param page
//	 * @param limit
//	 * @param search
//	 * @return
//	 */
//	public abstract JSONArray getUserList(int page, int limit, String search);
//
//	public abstract void doLogout(HttpServletResponse response,
//			String basePath, HttpSession session, User user) throws IOException;
//
//	public abstract void doAdminLogin(HttpServletRequest request,
//			HttpServletResponse response, String basePath, HttpSession session)
//			throws IOException;
//
//	public abstract JSONArray doGetcompanyDetail(int companyuid);
//
//	public abstract JSONArray getUserList();

}