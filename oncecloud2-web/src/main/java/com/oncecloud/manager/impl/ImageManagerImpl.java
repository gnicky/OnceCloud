package com.oncecloud.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.VM;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.PoolDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.Image;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCPool;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.User;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.ImageManager;
import com.oncecloud.message.MessagePush;

@Component("ImageManager")
public class ImageManagerImpl implements ImageManager {
	private final static Logger logger = Logger.getLogger(ImageManager.class);
	
	private ImageDAO imageDAO;
	private UserDAO userDAO;
	private PoolDAO poolDAO;
	private LogDAO logDAO;
	private VMDAO vmDAO;
	private HostDAO hostDAO;
	private QuotaDAO quotaDAO;
	private OverViewDAO overViewDAO;
	
	private Constant constant;
	
	private MessagePush messagePush;

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}
	
	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public PoolDAO getPoolDAO() {
		return poolDAO;
	}

	@Autowired
	public void setPoolDAO(PoolDAO poolDAO) {
		this.poolDAO = poolDAO;
	}
	
	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	public QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	public void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

/*	
	private HostManager hostManager;
	
	private HostSRDAO hostSRDAO;

	private HostManager getHostManager() {
		return hostManager;
	}

	@Autowired
	private void setHostManager(HostManager hostManager) {
		this.hostManager = hostManager;
	}


	public HostSRDAO getHostSRDAO() {
		return hostSRDAO;
	}

	@Autowired
	public void setHostSRDAO(HostSRDAO hostSRDAO) {
		this.hostSRDAO = hostSRDAO;
	}

	@SuppressWarnings("deprecation")
	public boolean imageExist(String imageUuid, String poolUuid) {
		boolean result = false;
		try {
			OCHost host = this.getHostManager().getPoolMaster(poolUuid);
			Connection conn = new Connection("http://" + host.getHostIP()
					+ ":9363", HostManager.DEFAULT_USER, host.getHostPwd());
			logger.info("Check Image Exist: Image [image-"
					+ imageUuid.substring(0, 8) + "] URL [http://"
					+ host.getHostIP() + ":9363");
			Map<VM, VM.Record> map = VM.getAllRecords(conn);
			for (VM thisVM : map.keySet()) {
				VM.Record vmRecord = map.get(thisVM);
				if (vmRecord.isATemplate && vmRecord.uuid.equals(imageUuid)) {
					result = true;
					break;
				}
			}
			logger.info("Check Image Exist Result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
*/
	public JSONArray getImageList(int userId, int userLevel, int page,
			int limit, String search, String type) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getImageDAO().countAllImageList(userId, search,
				userLevel, type);
		String poolUuid = this.getUserDAO().getUser(userId)
				.getUserAllocate();
		List<Image> imageList = this.getImageDAO().getOnePageImageList(userId,
				userLevel, page, limit, search, type, poolUuid);
		ja.put(totalNum);
		if (imageList != null) {
			for (Image image : imageList) {
				JSONObject jo = new JSONObject();
				jo.put("imageid", image.getImageUuid());
				jo.put("imagename", Utilities.encodeText(image.getImageName()));
				jo.put("imagesize", image.getImageDisk());
				jo.put("imageplatform", Utilities.encodeText(Constant.Platform
						.values()[image.getImagePlatform()].toString()));
				jo.put("imagestatus", image.getImageStatus());
				User imageUser = this.getUserDAO().getUser(image.getImageUID());
				jo.put("imageuser", imageUser.getUserName());
				jo.put("createDate",
						Utilities.formatTime(image.getCreateDate()));
				OCPool pool = this.getPoolDAO().getPool(image.getPoolUuid());
				jo.put("pooluuid", pool.getPoolUuid());
				jo.put("poolname", pool.getPoolName());
				jo.put("reference", image.getReferenceUuid());
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject cloneImage(int userId, int userLevel, String vmUuid,
			String imageName, String imageDesc) {
		Date startTime = new Date();
		if (userLevel == 0) {
			userId = 1;
		}
		JSONObject result = makeImage(vmUuid, imageName, userId, imageDesc);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				Utilities.encodeText(LogConstant.logObject.映像.toString()),
				Utilities.encodeText(imageName)));
		if (result.getBoolean("result")) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.映像.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.映像.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return result;
	}

	private JSONObject makeImage(String uuid, String newName, int uid,
			String desc) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		OCVM fromVM = this.getVmDAO().getVM(uuid);
		if (fromVM != null) {
			try {
				String poolUuid = this.getHostDAO()
						.getHost(fromVM.getHostUuid()).getPoolUuid();
				Connection c = this.getConstant().getConnection(uid);
				VM thisVM = VM.getByUuid(c, uuid);
				String imageUuid = UUID.randomUUID().toString();
				boolean createResult = thisVM.createImage(c, imageUuid);
				if (createResult == true) {
					Image image = this.getImageDAO().createImage(imageUuid, newName, uid,
							fromVM.getVmPlatform(), poolUuid, desc,
							fromVM.getVmPWD());
					this.getQuotaDAO().updateQuota(image.getImageUID(),
							"quotaImage", 1, true);
					this.getOverViewDAO().updateOverViewfield("viewImage", true);
					result.put("result", true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
/*
	public JSONArray createImage(int userId, int userLevel, String imageUuid,
			String imageName, String imageServer, int imageOs,
			String imageDesc, String imagePwd) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		Image imageExist = this.getImageDAO().getImage(imageUuid);
		boolean isCheck = true;
		if (imageExist != null && imageExist.getImageStatus() == 1) {
			// image exist in database
			isCheck = false;
		}
		if (!this.imageExist(imageUuid, imageServer)) {
			// image not exist
			isCheck = false;
		}
		// imageUID = 1 public
		int imageUID = userId;
		if (userLevel == 0) {
			imageUID = 1;
		}
		// create image
		Image image = null;
		if (isCheck) {
			image = this.getImageDAO().createImage(imageUuid, imageName,
					imageUID, imageOs, imageServer, imageDesc, imagePwd);
		}
		if (image != null) {
			JSONObject jo = new JSONObject();
			jo.put("imageid", image.getImageUuid());
			jo.put("imagename", Utilities.encodeText(image.getImageName()));
			jo.put("imagesize", image.getImageDisk());
			jo.put("imageplatform", Utilities.encodeText(Constant.Platform
					.values()[image.getImagePlatform()].toString()));
			jo.put("imagestatus", image.getImageStatus());
			User imageUser = this.getUserDAO().getUser(imageUID);
			jo.put("imageuser", imageUser.getUserName());
			jo.put("createDate", Utilities.formatTime(image.getCreateDate()));
			String pooluuid = image.getPoolUuid();
			OCPool pool = this.getPoolDAO().getPool(pooluuid);
			jo.put("poolname", pool.getPoolName());
			jo.put("pooluuid", pooluuid);
			jo.put("reference", image.getReferenceUuid());
			
			ja.put(jo);
		}
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.映像.toString(), imageName));
		if (image != null) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.映像.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.映像.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}
*/
	public JSONObject deleteImage(int userId, String imageId, String imageName) {
		JSONObject jo = new JSONObject();
		Date startTime = new Date();
		boolean result = this.getImageDAO().deleteImage(imageId);
		this.getQuotaDAO().updateQuota(userId,
				"quotaImage", 1, false);
		this.getOverViewDAO().updateOverViewfield("viewImage", false);
		jo.put("result", result);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.映像.toString(), imageName));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.映像.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.映像.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return jo;
	}

	public JSONObject getBasciList(String imageUuid) {
		JSONObject jo = new JSONObject();
		Image image = this.getImageDAO().getImage(imageUuid);
		if (image != null) {
			jo.put("imageName", Utilities.encodeText(image.getImageName()));
			jo.put("imageUID", image.getImageUID());
			jo.put("imageDisk", image.getImageDisk());
			jo.put("imagePlatform", Utilities.encodeText(Constant.Platform
					.values()[image.getImagePlatform()].toString()));
			jo.put("imageStatus", image.getImageStatus());
			jo.put("poolUuid", image.getPoolUuid());
			jo.put("imageDesc", (image.getImageDesc() == null) ? "&nbsp;"
					: Utilities.encodeText(image.getImageDesc()));
			User imageUser = this.getUserDAO().getUser(image.getImageUID());
			jo.put("imageUser", Utilities.encodeText(imageUser.getUserName()));
			jo.put("createDate", Utilities.formatTime(image.getCreateDate()));
			String timeUsed = Utilities.encodeText(Utilities.dateToUsed(image
					.getCreateDate()));
			jo.put("useDate", timeUsed);
		}
		return jo;
	}
/*	
	@SuppressWarnings("rawtypes")
	public JSONArray getShareImageList(String poolUuid, String[] imageUuids) {
		JSONArray ja = new JSONArray();
		OCPool pool = this.getPoolDAO().getPool(poolUuid);
		String master = pool.getPoolMaster();
		Set<String> srListSet = new HashSet<String>();
		srListSet = this.getHostSRDAO().getSRList(master);
		if (srListSet.size() > 0) {
			Set<String> hostListSet = new HashSet<String>();
			Iterator iterator = srListSet.iterator();
			hostListSet = this.getHostSRDAO().getHostList(iterator.next().toString());
			hostListSet.remove(master);
			if (hostListSet.size() > 0){
				while (iterator.hasNext()) {
					Set<String> hSet = new HashSet<String>();
					hSet = this.getHostSRDAO().getHostList(iterator.next().toString());
					hostListSet.retainAll(hSet);
				}
			}
			if (hostListSet.size() > 0) {
				boolean result = true;
				for (String hostString : hostListSet) {
					OCPool pl = this.getPoolDAO().getPoolByMaster(hostString);
					for (String uuid : imageUuids) {
						result &= this.getImageDAO().isShared( pl.getPoolUuid(), uuid); 
					}
					if (pl != null && result) {
						JSONObject jo = new JSONObject();
						jo.put("pooluuid", pl.getPoolUuid());
						jo.put("poolname", pl.getPoolName());
						ja.put(jo);
					}
				}
			}
		}
		return ja;
	}
	
	public JSONArray shareImages(String sorpoolUuid, String despoolUuid, String[] imageUuids) {
		JSONArray ja = new JSONArray();
		Connection conn = null;
		conn = this.getConstant().getConnectionFromPool(sorpoolUuid);
		for (String imageString : imageUuids) {
			try {
				Date startTime = new Date();
				String uuid = UUID.randomUUID().toString();
				OCPool pool = this.getPoolDAO().getPool(despoolUuid);
				String masterString = pool.getPoolMaster();
				OCHost host = this.getHostDAO().getHost(masterString);
				String des_ip = host.getHostIP();
				Host.migrateTemplate(conn, Types.toVM(imageString), uuid, des_ip);
				boolean result = this.getImageDAO().shareImage(uuid, imageString, despoolUuid);
				// write log and push message
				Date endTime = new Date();
				int elapse = Utilities.timeElapse(startTime, endTime);
				
				Image image = this.getImageDAO().getImage(imageString);
				JSONObject jo = new JSONObject();
				jo.put("imagename", Utilities.encodeText(image.getImageName()));
				jo.put("imageid", uuid);
				jo.put("imagesize", image.getImageDisk());
				jo.put("imageplatform", Utilities.encodeText(Constant.Platform
						.values()[image.getImagePlatform()].toString()));
				jo.put("createDate", Utilities.formatTime(image.getCreateDate()));
				jo.put("pooluuid", despoolUuid);
				jo.put("poolname", pool.getPoolName());
				jo.put("reference", imageString);
				ja.put(jo);
				
				JSONArray infoArray = new JSONArray();
				infoArray.put(Utilities.createLogInfo(
						LogConstant.logObject.映像.toString(), uuid));
				if (result) {
					OCLog log = this.getLogDAO().insertLog(1,
							LogConstant.logObject.映像.ordinal(),
							LogConstant.logAction.创建.ordinal(),
							LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
							startTime, elapse);
					this.getMessagePush().pushMessage(1,
							Utilities.stickyToSuccess(log.toString()));
				} else {
					OCLog log = this.getLogDAO().insertLog(1,
							LogConstant.logObject.映像.ordinal(),
							LogConstant.logAction.创建.ordinal(),
							LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
							startTime, elapse);
					this.getMessagePush().pushMessage(1,
							Utilities.stickyToError(log.toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ja;
	}
	
	public boolean updateImage(String uuid, String pwd, String desc, int disk, int platform) {
		if((!pwd.equals(""))&&(!desc.equals(""))&& (disk != 0) && (platform != 0)) {
			Image image = this.getImageDAO().getImage(uuid);
			image.setImagePwd(pwd);
			image.setImageDisk(disk);
			image.setImagePlatform(platform);
			image.setImageDesc(desc);
			return this.getImageDAO().updateImage(image);
		} else {
			return true;
		}
	}*/
}
