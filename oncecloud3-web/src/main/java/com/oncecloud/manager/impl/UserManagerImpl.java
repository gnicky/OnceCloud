package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.PoolDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VolumeDAO;
import com.oncecloud.entity.EIP;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;
import com.oncecloud.entity.Volume;
import com.oncecloud.helper.HashHelper;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.UserManager;
import com.oncecloud.message.MessagePush;

@Component("UserManager")
public class UserManagerImpl implements UserManager{
	private static Logger logger = Logger.getLogger(UserManager.class);

	private UserDAO userDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private PoolDAO poolDAO;
	private FirewallDAO firewallDAO;
	private VMDAO vmDAO;
	private VolumeDAO volumeDAO;
	private EIPDAO eipDAO;
	
	private MessagePush messagePush;
	
	private HashHelper hashHelper;
	
	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	public void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public PoolDAO getPoolDAO() {
		return poolDAO;
	}

	@Autowired
	public void setPoolDAO(PoolDAO poolDAO) {
		this.poolDAO = poolDAO;
	}

	public FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	public void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	public VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	public void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	public VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	public void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	public EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	public void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	public MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	public void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private HashHelper getHashHelper() {
		return hashHelper;
	}

	@Autowired
	private void setHashHelper(HashHelper hashHelper) {
		this.hashHelper = hashHelper;
	}

	public int checkLogin(String userName, String userPass) {
		try {
			int result = 1;
			User user = this.getUserDAO().getUser(userName);
			if (user == null) {
				// User does not exist
				result = 1;
			} else {
				if (user.getUserStatus() == 0) {
					result = 1;
				} else {
					String pass = user.getUserPass();
					if (pass.equals(this.getHashHelper().md5Hash(userPass))) {
						// Validated
						result = 0;
					} else {
						// Password is wrong
						result = 1;
					}
				}
			}
			logger.info("Check Login: User [" + userName + "] Result ["
					+ result + "]");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public User getUser(String user) {
		return this.getUserDAO().getUser(user);
	}
	
	/**
	 * 获取用户列表
	 * 
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getUserList(int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getUserDAO().countAllUserList(search);
		ja.put(totalNum);
		List<User> userList = this.getUserDAO().getOnePageUserList(page, limit,
				search);
		if (userList != null) {
			for (User myuser : userList) {
				JSONObject obj = new JSONObject();
				obj.put("username", Utilities.encodeText(myuser.getUserName()));
				obj.put("userid", myuser.getUserId());
				obj.put("usercom",
						Utilities.encodeText(myuser.getUserCompany()));
				obj.put("userdate", Utilities.formatTime(myuser.getUserDate()));
				obj.put("userlevel", myuser.getUserLevel());
				obj.put("usermail", myuser.getUserMail());
				obj.put("userphone", myuser.getUserPhone());
				if (myuser.getUserVoucher() == null) {
					obj.put("uservoucher", 0);
				} else {
					obj.put("uservoucher", myuser.getUserVoucher());
				}
				ja.put(obj);
			}
		}
		return ja;
	}

	public JSONArray doGetCompanyList() {
		JSONArray ja = new JSONArray();
		String searchStr = "";
		List<User> userList = this.getUserDAO().getCompanyUserList(searchStr);
		for (User myuser : userList) {
			JSONObject obj = new JSONObject();
			obj.put("username", Utilities.encodeText(myuser.getUserName()));
			obj.put("userid", myuser.getUserId());
			obj.put("usercom", Utilities.encodeText(myuser.getUserCompany()));
			obj.put("userlevel", myuser.getUserLevel());
			obj.put("usermail", myuser.getUserMail());
			obj.put("userphone", myuser.getUserPhone());
			Quota quota = this.getQuotaDAO().getQuotaUsed(myuser.getUserId());
			obj.put("usedVM", quota.getQuotaVM());
			obj.put("usedDiskN", quota.getQuotaDiskN());
			obj.put("usedDiskS", quota.getQuotaDiskS());
			obj.put("usedIP", quota.getQuotaIP());
			obj.put("usedBandwidth", quota.getQuotaBandwidth());
			obj.put("usedCpu", quota.getQuotaCpu());
			obj.put("usedQuotaMemory", quota.getQuotaMemory());
			if (myuser.getUserVoucher() == null) {
				obj.put("uservoucher", 0);
			} else {
				obj.put("uservoucher", myuser.getUserVoucher());
			}
			ja.put(obj);
		}
		return ja;
	}
	
	public JSONObject doDeleteUser(int userId, int changeId, String userName) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		boolean result = this.getUserDAO().disableUser(changeId);
		jo.put("result", result);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.用户.toString(), userName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.用户.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.用户.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}
	
	public JSONObject doGetOneUser(int userId) {
		User myuser = this.getUserDAO().getUser(userId);
		JSONObject obj = new JSONObject();
		obj.put("username", Utilities.encodeText(myuser.getUserName()));
		obj.put("userid", myuser.getUserId());
		obj.put("usercom", Utilities.encodeText(myuser.getUserCompany()));
		obj.put("userdate", Utilities.formatTime(myuser.getUserDate()));
		obj.put("userlevel", myuser.getUserLevel());
		obj.put("usermail", myuser.getUserMail());
		obj.put("userphone", myuser.getUserPhone());
		obj.put("balance", myuser.getUserBalance());
		return obj;
	}
	
	public JSONObject doGetUserQuota(int userId) {
		Quota quotaU = this.getQuotaDAO().getQuotaUsed(userId);
		Quota quotaT = this.getQuotaDAO().getQuotaTotal(userId);
		JSONObject obj = new JSONObject();
		obj.put("quotaU", new JSONObject(quotaU));
		obj.put("quotaT", new JSONObject(quotaT));
		return obj;
	}
	
	public void doQuotaUpdate(int quotaid, int changerId, int eip, int vm,
			int bk, int img, int vol, int ssh, int fw, int rt, int vlan,
			int lb, int disk, int bw, int mem, int cpu, int userId) {
		Quota newQuota = new Quota(changerId, eip, vm, bk, img, vol, disk, ssh,
				fw, rt, vlan, lb, bw, mem, cpu, 0);
		newQuota.setQuotaID(quotaid);
		boolean result = this.getQuotaDAO().updateQuota(newQuota);
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("资源配额更新成功"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("资源配额更新失败"));
		}
	}
	
	public JSONArray doCreateUser(String userName, String userPassword,
			String userEmail, String userTelephone, String userCompany,
			String userLevel, int userid, String poolUuid) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		User result = this.userRegister(userName, userPassword, userEmail,
				userTelephone, userCompany, userLevel, poolUuid);
		if (result != null) {
			JSONObject tObj = new JSONObject();
			tObj.put("username", Utilities.encodeText(result.getUserName()));
			tObj.put("userid", result.getUserId());
			tObj.put("usercom", Utilities.encodeText(result.getUserCompany()));
			tObj.put("userdate", Utilities.formatTime(result.getUserDate()));
			tObj.put("userlevel", result.getUserLevel());
			tObj.put("usermail", result.getUserMail());
			tObj.put("userphone", result.getUserPhone());
			ja.put(tObj);
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.用户.toString(), userName));
		if (result != null) {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.用户.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userid,
					LogConstant.logObject.用户.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userid,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}
	
	private User userRegister(String userName, String userPassword,
			String userEmail, String userTelephone, String userCompany,
			String uLevel, String poolUuid) {
		try {
			if (userName == null || userPassword == null) {
				logger.error("User Register: User [" + userName
						+ "] Failed: Null Exception");
				return null;
			}
			int userLevel = Integer.valueOf(uLevel);
			Date date = new Date();
			User user = this.getUserDAO().getUser(userName);
			if (user != null) {
				logger.error("User Register: User [" + userName
						+ "] Failed: Exist Yet");
				return null;
			}
			if (poolUuid.equals("0")){
				poolUuid = this.getPoolDAO().getRandomPool();
			}
			int userId = this.getUserDAO().insertUser(userName, userPassword, userEmail,
					userTelephone, userCompany, userLevel, date, poolUuid);
			this.getQuotaDAO().initQuota(userId);
			this.getFirewallDAO().createDefaultFirewall(userId);
			User check = this.getUserDAO().getUser(userName);
			if (check == null) {
				logger.error("User Register: User [" + userName
						+ "] Failed: Check Failed");
				return null;
			}
			logger.info("User Register: User [" + userName + "] Password ["
					+ userPassword + "] Mail [" + userEmail + "] Telephone ["
					+ userTelephone + "] Company [" + userCompany
					+ "] UserLevel [" + userLevel + "] Successful");
			return check;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void doUpdateUser(int userId, int changeId, String userName,
			String userEmail, String userTel, String userCom, String userLevel) {
		boolean result = this.userUpdate(changeId, userName, userEmail,
				userTel, userCom, userLevel);
		if (result) {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess("用户信息修改成功"));
		} else {
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError("用户信息修改失败"));
		}
	}
	
	private boolean userUpdate(Integer userid, String userName,
			String userMail, String userPhone, String userCompany, String uLevel) {
		try {
			if (userName == null || null == userid) {
				logger.error("User Update: User [" + userName
						+ "] Failed: Null Exception");
				return false;
			}
			int userLevel = Integer.valueOf(uLevel);
			User user = this.getUserDAO().getUser(userid);
			if (user == null) {
				logger.error("User Update: User- [" + userid
						+ "] Failed: Not Exist");
				return false;
			}
			boolean result = this.getUserDAO().updateUser(userid, userName,
					userMail, userPhone, userCompany, userLevel);
			if (!result) {
				logger.error("User Update: User- [" + userid
						+ "] Failed: Update Failed");
				return false;
			}
			logger.info("User Update To: User [" + userid + "] UserName ["
					+ userName + "] Mail [" + userMail + "] Telephone ["
					+ userPhone + "] Company [" + userCompany + "] UserLevel ["
					+ userLevel + "] Successful");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public JSONArray doQueryUser(String userName) {
		JSONArray ja = new JSONArray();
		User query = this.getUserDAO().getUser(userName);
		JSONObject tObj = new JSONObject();
		if (query != null) {
			tObj.put("exist", true);
		} else {
			tObj.put("exist", false);
		}
		ja.put(tObj);
		return ja;
	}
	
	public JSONArray doGetcompanyDetail(int companyuid) {
		JSONArray jsonarray =new JSONArray();
		JSONObject jsonobject =new JSONObject();
		// 获取vm列表，硬盘列表，共网Ip列表（包括带宽），然后还有 vm数量变化的信息
		List<OCVM> vmlist =this.getVmDAO().getOnePageVMs(companyuid,1, 100, "");
		JSONArray jsonarrayvm =new JSONArray();
		for(OCVM ocvmobj : vmlist)
		{
			jsonarrayvm.put(ocvmobj.toJsonString());
		}
		jsonobject.put("vmlist", jsonarrayvm);
		
		List<Volume> volumelist =this.getVolumeDAO().getOnePageVolumes(companyuid, 1, 100, ""); ///查询该用户的所有Volume
		JSONArray jsonarrayvolume =new JSONArray();
		for(Volume volumeobj : volumelist)
		{
			jsonarrayvolume.put(volumeobj.toJsonString());
		}
		jsonobject.put("volumelist", jsonarrayvolume);
		
		List<EIP> eiplist = this.getEipDAO().getOnePageEipList(companyuid,1, 100, ""); ///查询该用户的所有EIP
		JSONArray jsonarrayeip =new JSONArray();
		for(EIP eipobj : eiplist)
		{
			jsonarrayeip.put(eipobj.toJsonString());
		}
		jsonobject.put("eiplist", jsonarrayeip);
		
		jsonarray.put(jsonobject);
		
		return jsonarray;
	}
	
	public JSONArray getUserList() {
		JSONArray ja = new JSONArray();
		List<User> list = this.getUserDAO().getUserList();
		for (User user : list) {
			JSONObject jo = new JSONObject();
			jo.put("userid", user.getUserId());
			jo.put("username", user.getUserName());
			ja.put(jo);
		}
		return ja;
	}
	
}
