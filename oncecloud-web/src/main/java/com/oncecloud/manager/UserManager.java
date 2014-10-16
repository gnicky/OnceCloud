package com.oncecloud.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.LogDAO;
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
import com.oncecloud.message.MessagePush;

/**
 * @author hehai tangzhen
 * @version 2014/08/25
 */
@Component
public class UserManager {
	private static Logger logger = Logger.getLogger(UserManager.class);

	private UserDAO userDAO;
	private LogDAO logDAO;
	private QuotaDAO quotaDAO;
	private MessagePush messagePush;
    private VMDAO vmDAO;
    private VolumeDAO volumeDAO;
    private EIPDAO eipDAO;
    
	private HashHelper hashHelper;

	
	
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

	public VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	public void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private HashHelper getHashHelper() {
		return hashHelper;
	}

	@Autowired
	private void setHashHelper(HashHelper hashHelper) {
		this.hashHelper = hashHelper;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

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

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
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

	public void initPlatform() {
		this.userRegister("admin", "onceas", "admin@beyondcent.com",
				"12345678901", "BeyondCent", "0", "0");
	}

	/**
	 * 获取用户余额
	 * 
	 * @param userId
	 * @return
	 */
	public JSONObject getBalance(int userId) {
		JSONObject jo = new JSONObject();
		User user = userDAO.getUser(userId);
		jo.put("balance", user.getUserBalance());
		return jo;
	}

	public User userRegister(String userName, String userPassword,
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
			this.getUserDAO().insertUser(userName, userPassword, userEmail,
					userTelephone, userCompany, userLevel, date, poolUuid);
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

	public JSONArray doRegister(String userName, String userPassword,
			String userEmail, String userTelephone)
			throws UnsupportedEncodingException {
		JSONArray ja = new JSONArray();
		User result = this.userRegister(userName, userPassword, userEmail,
				userTelephone, "", "2", "0");
		if (result != null) {
			JSONObject tObj = new JSONObject();
			tObj.put("username",
					URLEncoder.encode(result.getUserName(), "utf-8"));
			tObj.put("userid", result.getUserId());
			tObj.put("usercom",
					URLEncoder.encode(result.getUserCompany(), "utf-8"));
			tObj.put("userdate", Utilities.formatTime(result.getUserDate()));
			tObj.put("userlevel", result.getUserLevel());
			tObj.put("usermail", result.getUserMail());
			tObj.put("userphone", result.getUserPhone());
			ja.put(tObj);
		}
		return ja;
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

	public JSONObject doGetUserQuota(int userId) {
		Quota quotaU = this.getQuotaDAO().getQuotaUsed(userId);
		Quota quotaT = this.getQuotaDAO().getQuotaTotal(userId);
		JSONObject obj = new JSONObject();
		obj.put("quotaU", new JSONObject(quotaU));
		obj.put("quotaT", new JSONObject(quotaT));
		return obj;
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

	public void doLogout(HttpServletResponse response, String basePath,
			HttpSession session, User user) throws IOException {
		if (user != null) {
			session.removeAttribute("user");
			int level = user.getUserLevel();
			if (level == 0) {
				response.sendRedirect(basePath + "account/backdoor.jsp");
			} else {
				response.sendRedirect(basePath + "account/login.jsp");
			}
		}
	}

	public void doAdminLogin(HttpServletRequest request,
			HttpServletResponse response, String basePath, HttpSession session)
			throws IOException {
		int result = -1;
		String ver1 = request.getParameter("vercode");
		String ver2 = (String) session.getAttribute("rand");
		// 会话存在过期现象
		if (ver1 == null || ver2 == null) {
			response.sendRedirect(basePath + "account/backdoor.jsp");
		} else {
			if (ver1.toLowerCase().equals(ver2.toLowerCase())) {
				session.setAttribute("rand", null);
				String username = request.getParameter("user");
				String password = request.getParameter("pw");
				if (username != null) {
					result = this.checkLogin(username, password);
				}
				if (result == 0) {
					User userlogin = this.getUserDAO().getUser(username);
					int level = userlogin.getUserLevel();
					if (level == 0) {
						session.setAttribute("user", userlogin);
						response.sendRedirect(basePath + "admin/dashboard.jsp");
					} else {
						response.sendRedirect(basePath + "account/backdoor.jsp");
					}
				} else {
					response.sendRedirect(basePath + "account/backdoor.jsp");
				}
			} else {
				response.sendRedirect(basePath + "account/backdoor.jsp");
			}
		}
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
}
