package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.VM;
import com.once.xenapi.VM.Record;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.DatabaseDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.Database;
import com.oncecloud.entity.Image;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author hehai
 * @version 2014/08/25
 */
@Component
public class DatabaseManager {
	private ImageDAO imageDAO;
	private DHCPDAO dhcpDAO;
	private DatabaseDAO databaseDAO;
	private EIPDAO eipDAO;
	private EIPManager eipManager;
	private VMManager vmManager;
	private Constant constant;
	private MessagePush messagePush;

	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private DHCPDAO getDhcpDAO() {
		return dhcpDAO;
	}

	@Autowired
	private void setDhcpDAO(DHCPDAO dhcpDAO) {
		this.dhcpDAO = dhcpDAO;
	}

	private DatabaseDAO getDatabaseDAO() {
		return databaseDAO;
	}

	@Autowired
	private void setDatabaseDAO(DatabaseDAO databaseDAO) {
		this.databaseDAO = databaseDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	private void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	private VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	private void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;

	public boolean createDatabase(String dbUuid, int userId, String dbName,
			String dbUser, String dbPwd, String dbType, int dbPort,
			int dbthroughout, String hostUuid) {
		Connection c = null;
		String allocateHost = null;
		String ip = null;
		String mac = null;
		boolean result = false;
		String imagePwd = null;
		String tplUuid = null;
		Date createDate = new Date();
		try {
			Image dbImage = this.getImageDAO().getDBImage(dbType, dbthroughout);
			imagePwd = dbImage.getImagePwd();
			tplUuid = dbImage.getImageUuid();
			DHCP dhcp = this.getDhcpDAO().getFreeDHCP(dbUuid, 2);
			ip = dhcp.getDhcpIp();
			mac = dhcp.getDhcpMac();
			c = this.getConstant().getConnection(userId);
			allocateHost = this.getVmManager().getAllocateHost(hostUuid, 1024);
		} catch (Exception e) {
			e.printStackTrace();
			if (mac != null) {
				try {
					this.getDhcpDAO().returnDHCP(mac);
				} catch (Exception e1) {
					e.printStackTrace();
				}
			}
			return false;
		}
		if (allocateHost == null) {
			return false;
		} else {
			boolean dhcpRollback = false;
			boolean dbRollback = false;
			try {
				boolean preCreate = this.getDatabaseDAO().preCreateDatabase(
						dbUuid, dbName, userId, dbUser, dbPwd, dbType,
						dbthroughout, mac, ip, dbPort,
						DatabaseManager.POWER_CREATE, 1, createDate);
				if (preCreate == true) {
					String os = "linux";
					String backendName = "db-" + dbUuid.substring(0, 8);
					Record lbrecord = this.getVmManager().createVMOnHost(c,
							dbUuid, tplUuid, "root", "onceas", 1, 1024, mac,
							ip, os, allocateHost, imagePwd, backendName, true);
					if (lbrecord != null) {
						String hostuuid = lbrecord.residentOn.toWireString();
						if (hostuuid.equals(allocateHost)) {
							this.getDatabaseDAO().updateDatabase(dbUuid,
									DatabaseManager.POWER_RUNNING, hostuuid);
							result = true;
						} else {
							dhcpRollback = true;
							dbRollback = true;
						}
					} else {
						dhcpRollback = true;
						dbRollback = true;
					}
				} else {
					dhcpRollback = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				dhcpRollback = true;
				dbRollback = true;
			}
			if (dhcpRollback == true) {
				try {
					this.getDhcpDAO().returnDHCP(mac);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dbRollback == true) {
				this.getDatabaseDAO().removeDatabase(dbUuid);
			}
		}
		return result;
	}

	public boolean deleteDB(int userId, String uuid) {
		boolean result = false;
		try {
			Database toDelete = this.getDatabaseDAO().getDatabase(uuid);
			int powerStatus = toDelete.getDatabasePower();
			boolean preDeleteDB = this.getDatabaseDAO().setDBPowerStatus(uuid,
					DatabaseManager.POWER_DESTROY);
			if (preDeleteDB == true) {
				Connection c = this.getConstant().getConnection(userId);
				VM thisDB = VM.getByUuid(c, uuid);
				if (powerStatus == DatabaseManager.POWER_RUNNING) {
					thisDB.hardShutdown(c);
				}
				thisDB.destroy(c, true);
				Database db = this.getDatabaseDAO().getDatabase(uuid);
				String ip = db.getDatabaseIp();
				String mac = db.getDatabaseMac();
				String publicip = this.getEipDAO().getEipIp(uuid);
				if (publicip != null) {
					this.getEipManager().unbindElasticIp(userId, uuid,
							publicip, "db");
				}
				if (ip != null) {
					this.getDhcpDAO().returnDHCP(mac);
				}
				this.getDatabaseDAO().removeDatabase(uuid);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean startDB(int userId, String uuid) {
		boolean result = false;
		try {
			boolean preStartDB = this.getDatabaseDAO().setDBPowerStatus(uuid,
					DatabaseManager.POWER_BOOT);
			if (preStartDB == true) {
				Connection c = this.getConstant().getConnection(userId);
				VM thisVM = VM.getByUuid(c, uuid);
				thisVM.start(c, false, false);
				this.getDatabaseDAO().setDBPowerStatus(uuid,
						DatabaseManager.POWER_RUNNING);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.getDatabaseDAO().setDBPowerStatus(uuid,
					DatabaseManager.POWER_HALTED);
		}
		return result;
	}

	public boolean shutdownDB(int userId, String uuid, String force) {
		boolean result = false;
		try {
			boolean preShutdownDB = this.getDatabaseDAO().setDBPowerStatus(
					uuid, DatabaseManager.POWER_SHUTDOWN);
			if (preShutdownDB == true) {
				Connection c = this.getConstant().getConnection(userId);
				VM thisVM = VM.getByUuid(c, uuid);
				if (force.equals("true")) {
					thisVM.hardShutdown(c);
				} else {
					if (thisVM.cleanShutdown(c)) {
					} else {
						thisVM.hardShutdown(c);
					}
				}
				this.getDatabaseDAO().setDBPowerStatus(uuid,
						DatabaseManager.POWER_HALTED);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.getDatabaseDAO().setDBPowerStatus(uuid,
					DatabaseManager.POWER_RUNNING);
		}
		return result;
	}

	public JSONArray getDatabaseList(int userId, int page, int limit,
			String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getDatabaseDAO().countAllDatabaseList(search,
				userId);
		ja.put(totalNum);
		List<Database> databaseList = this.getDatabaseDAO()
				.getOnePageDatabaseList(page, limit, search, userId);
		if (databaseList != null) {
			for (int i = 0; i < databaseList.size(); i++) {
				JSONObject jo = new JSONObject();
				Database db = databaseList.get(i);
				String dbUuid = db.getDatabaseUuid();
				jo.put("dbid", dbUuid);
				jo.put("dbname", Utilities.encodeText(db.getDatabaseName()));
				jo.put("dbstate", db.getDatabasePower());
				jo.put("dbtype", db.getDatabaseType());
				String ip = db.getDatabaseIp();
				if (ip == null) {
					jo.put("dbip", "null");
				} else {
					jo.put("dbip", ip);
				}
				String publicip = this.getEipDAO().getEipIp(dbUuid);
				if (publicip == null) {
					jo.put("dbeip", "");
				} else {
					jo.put("dbeip", publicip);
				}
				jo.put("dbport", db.getDatabasePort());
				jo.put("dbthroughout", db.getDatabaseThroughout());
				String timeUsed = Utilities.encodeText(Utilities.dateToUsed(db
						.getCreateDate()));
				jo.put("createdate", timeUsed);
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray getAbleDBs(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getDatabaseDAO().countAllDatabaseList(search,
				userId);
		ja.put(totalNum);
		List<Database> dbList = this.getDatabaseDAO().getOnePageDatabaseList(
				page, limit, search, userId);
		if (dbList != null) {
			for (int i = 0; i < dbList.size(); i++) {
				JSONObject jo = new JSONObject();
				Database db = dbList.get(i);
				jo.put("vmid", db.getDatabaseUuid());
				jo.put("vmname", Utilities.encodeText(db.getDatabaseName()));
				ja.put(jo);
			}
		}
		return ja;
	}

	public void doCreateDB(int userId, String dbUuid, String dbName,
			String dbUser, String dbPwd, int dbPort, String dbType,
			int dbthroughout, String hostUuid) {
		boolean result = this.createDatabase(dbUuid, userId, dbName, dbUser,
				dbPwd, dbType, dbPort, dbthroughout, hostUuid);
		// push message
		if (result == true) {
			this.getMessagePush()
					.editRowStatus(userId, dbUuid, "running", "活跃");
		} else {
			this.getMessagePush().deleteRow(userId, dbUuid);
		}
	}

	public void doShutdownDB(int userId, String dbUuid, String forse) {
		boolean result = this.shutdownDB(userId, dbUuid, forse);
		// push message
		if (result) {
			this.getMessagePush().editRowStatus(userId, dbUuid, "stopped",
					"已关机");
		} else {
			this.getMessagePush()
					.editRowStatus(userId, dbUuid, "running", "活跃");
		}
	}

	public void doDeleteDB(int userId, String dbUuid) {
		// push message
		boolean result = this.deleteDB(userId, dbUuid);
		if (result) {
			this.getMessagePush().deleteRow(userId, dbUuid);
		}
	}

	public void doStartupDB(int userId, String dbUuid) {
		boolean result = this.startDB(userId, dbUuid);
		// push message
		if (result) {
			this.getMessagePush()
					.editRowStatus(userId, dbUuid, "running", "活跃");
		} else {
			this.getMessagePush().editRowStatus(userId, dbUuid, "stopped",
					"已关机");
		}
	}
}
