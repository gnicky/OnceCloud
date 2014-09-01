package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Types;
import com.once.xenapi.VM;
import com.once.xenapi.VM.Record;
import com.oncecloud.dao.BackendDAO;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.ForeendDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VDIDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.Backend;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.Foreend;
import com.oncecloud.entity.Image;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVDI;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class LBManager {
	private final static Logger logger = Logger.getLogger(LBManager.class);
	public static int[] capacity = { 5000, 20000, 40000, 100000 };
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;

	private ImageDAO imageDAO;
	private DHCPDAO dhcpDAO;
	private LBDAO lbDAO;
	private VDIDAO vdiDAO;
	private EIPDAO eipDAO;
	private RouterDAO routerDAO;
	private ForeendDAO foreendDAO;
	private LogDAO logDAO;
	private BackendDAO backendDAO;
	private QuotaDAO quotaDAO;
	private EIPManager eipManager;
	private VMManager vmManager;
	private Constant constant;
	private HostDAO hostDAO;
	private UserDAO userDAO;
	
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

	private LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	private void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	private VDIDAO getVdiDAO() {
		return vdiDAO;
	}

	@Autowired
	private void setVdiDAO(VDIDAO vdiDAO) {
		this.vdiDAO = vdiDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private ForeendDAO getForeendDAO() {
		return foreendDAO;
	}

	@Autowired
	private void setForeendDAO(ForeendDAO foreendDAO) {
		this.foreendDAO = foreendDAO;
	}

	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	private BackendDAO getBackendDAO() {
		return backendDAO;
	}

	@Autowired
	private void setBackendDAO(BackendDAO backendDAO) {
		this.backendDAO = backendDAO;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
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

	public HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	public void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public JSONObject createLB(String uuid, int userId, String name,
			int capacity, String poolUuid) {
		Connection c = null;
		String allocateHost = null;
		String ip = null;
		String mac = null;
		JSONObject jo = new JSONObject();
		String OS = null;
		String imagePwd = null;
		String pwd = "onceas";
		String tplUuid = null;
		String backendName = "lb-" + uuid.substring(0, 8);
		Date createDate = new Date();
		try {
			Image lbImage = this.getImageDAO().getLBImage(userId);
			OS = "linux";
			imagePwd = lbImage.getImagePwd();
			tplUuid = lbImage.getImageUuid();
			DHCP dhcp = this.getDhcpDAO().getFreeDHCP(uuid, 1);
			ip = dhcp.getDhcpIp();
			mac = dhcp.getDhcpMac();
			c = this.getConstant().getConnectionFromPool(poolUuid);
			allocateHost = this.getVmManager().getAllocateHost(poolUuid, 1024);
			logger.info("LB [" + backendName + "] allocated to Host ["
					+ allocateHost + "]");
		} catch (Exception e) {
			e.printStackTrace();
			if (mac != null) {
				try {
					this.getDhcpDAO().returnDHCP(mac);
				} catch (Exception e1) {
					e.printStackTrace();
				}
				jo.put("isSuccess", false);
			}
			return jo;
		}
		if (allocateHost == null) {
			jo.put("isSuccess", false);
		} else {
			boolean dhcpRollback = false;
			boolean dbRollback = false;
			try {
				boolean preCreate = this.getLbDAO().preCreateLB(uuid, pwd,
						userId, name, mac, capacity, LBManager.POWER_CREATE, 1,
						createDate);
				Date preEndDate = new Date();
				int elapse = Utilities.timeElapse(createDate, preEndDate);
				logger.info("LB [" + backendName + "] Pre Create Time ["
						+ elapse + "]");
				if (preCreate == true) {
					OCVDI freeVDI = this.getVdiDAO().getFreeVDI(tplUuid);
					Record lbrecord = null;
					// 如果不能获取该模板的空闲VDI，则直接创建该负载均衡，否则使用该VDI创建负载均衡
					if (freeVDI == null) {
						lbrecord = this.getVmManager().createVMOnHost(c, uuid,
								tplUuid, "root", pwd, 1, 1024, mac, ip, OS,
								allocateHost, imagePwd, backendName);
						Date createEndDate = new Date();
						int elapse1 = Utilities.timeElapse(createDate,
								createEndDate);
						logger.info("LB [" + backendName + "] Create Time ["
								+ elapse1 + "]");
					} else {
						String vdiUuid = freeVDI.getVdiUuid();
						this.getVdiDAO().deleteVDI(freeVDI);
						lbrecord = this.getVmManager().createVMFromVDI(c, uuid,
								vdiUuid, tplUuid, "root", pwd, 1, 1024, mac,
								ip, OS, allocateHost, imagePwd, backendName);
						Date createEndDate = new Date();
						int elapse1 = Utilities.timeElapse(createDate,
								createEndDate);
						logger.info("LB [" + backendName
								+ "] Create From VDI Time [" + elapse1 + "]");
					}
					if (lbrecord != null) {
						String hostuuid = lbrecord.residentOn.toWireString();
						if (hostuuid.equals(allocateHost)) {
							if (!lbrecord.setpasswd) {
								pwd = imagePwd;
							}
							jo.put("ip", ip);
							this.getLbDAO().updateLB(userId, uuid, pwd,
									LBManager.POWER_RUNNING, hostuuid, ip);
							jo.put("isSuccess", true);
						} else {
							jo.put("error", "负载均衡后台启动位置错误");
							dhcpRollback = true;
							dbRollback = true;
						}
					} else {
						jo.put("error", "负载均衡后台创建失败");
						dhcpRollback = true;
						dbRollback = true;
					}
				} else {
					jo.put("error", "负载均衡预创建失败");
					dhcpRollback = true;
				}
			} catch (Exception e) {
				jo.put("error", "负载均衡创建异常");
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
				jo.put("isSuccess", false);
			}
			if (dbRollback == true) {
				this.getLbDAO().removeLB(userId, uuid);
				jo.put("isSuccess", false);
			}
		}
		return jo;
	}

	public boolean deleteLB(int userId, String uuid) {
		boolean result = false;
		Connection c = null;
		try {
			c = this.getConstant().getConnection(userId);
			boolean preDeleteLB = this.getLbDAO().setLBPowerStatus(uuid,
					LBManager.POWER_DESTROY);
			if (preDeleteLB == true) {
				VM thisLB = VM.getByUuid(c, uuid);
				thisLB.hardShutdown(c);
				thisLB.destroy(c, true);
				LB lb = this.getLbDAO().getLB(uuid);
				String ip = lb.getLbIP();
				String mac = lb.getLbMac();
				if (ip != null) {
					this.getDhcpDAO().returnDHCP(mac);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				String publicip = this.getEipDAO().getEipIp(uuid);
				if (publicip != null) {
					this.getEipManager().unbindElasticIp(userId, uuid,
							publicip, "lb");
				}
				this.getLbDAO().removeLB(userId, uuid);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean startLB(String uuid, String poolUuid) {
		boolean result = false;
		String hostUuid = null;
		String powerState = null;
		try {
			LB currentLB = this.getLbDAO().getLB(uuid);
			if (currentLB != null) {
				boolean preStartLB = this.getLbDAO().setLBPowerStatus(uuid,
						LBManager.POWER_BOOT);
				if (preStartLB == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					if (!powerState.equals("Running")) {
						hostUuid = this.getVmManager().getAllocateHost(
								poolUuid, 1024);
						Host allocateHost = Types.toHost(hostUuid);
						thisVM.startOn(c, allocateHost, false, true);
					} else {
						hostUuid = thisVM.getResidentOn(c).toWireString();
					}
					this.getLbDAO().setLBHostUuid(uuid, hostUuid);
					this.getLbDAO().setLBPowerStatus(uuid,
							LBManager.POWER_RUNNING);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getLbDAO().setLBPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getLbDAO().setLBPowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getLbDAO().setLBPowerStatus(uuid,
						RouterManager.POWER_HALTED);
			}
		}
		return result;
	}

	public boolean shutdownLB(String uuid, String force, String poolUuid) {
		boolean result = false;
		String powerState = null;
		String hostUuid = null;
		try {
			LB currentLB = this.getLbDAO().getLB(uuid);
			if (currentLB != null) {
				boolean preShutdownLB = this.getLbDAO().setLBPowerStatus(uuid,
						LBManager.POWER_SHUTDOWN);
				if (preShutdownLB == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					hostUuid = thisVM.getResidentOn(c).toWireString();
					if (powerState.equals("Running")) {
						if (force.equals("true")) {
							thisVM.hardShutdown(c);
						} else {
							if (thisVM.cleanShutdown(c)) {
							} else {
								thisVM.hardShutdown(c);
							}
						}
					}
					this.getRouterDAO().setRouterHostUuid(uuid, hostUuid);
					this.getLbDAO().setLBPowerStatus(uuid,
							LBManager.POWER_HALTED);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getLbDAO().setLBPowerStatus(uuid,
							RouterManager.POWER_RUNNING);
				} else {
					this.getLbDAO().setLBPowerStatus(uuid,
							RouterManager.POWER_HALTED);
				}
			} else {
				this.getLbDAO().setLBPowerStatus(uuid,
						RouterManager.POWER_RUNNING);
			}
		}
		return result;
	}

	public boolean applyLB(int userId, String lbUuid) {
		boolean result = false;
		try {
			Date startTime = new Date();
			LB lb = this.getLbDAO().getLB(lbUuid);
			JSONArray feArray = this.getForeendDAO()
					.getSimpleFEListByLB(lbUuid);
			JSONObject jo = new JSONObject();
			jo.put("listeners", feArray);
			jo.put("workerProcesses", 5);
			jo.put("workerConnections", 1000);
			String url = lb.getLbIP() + ":9090";
			Connection c = this.getConstant().getConnection(userId);
			result = Host.setLoadBalancer(c, url, jo.toString());
			if (result == true) {
				this.getLbDAO().setLBStatus(lbUuid, 1);
			}
			// write log and push message
			Date endTime = new Date();
			int elapse = Utilities.timeElapse(startTime, endTime);
			JSONArray infoArray = new JSONArray();
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.负载均衡.toString(),
					"lb-" + lbUuid.substring(0, 8)));
			infoArray.put(Utilities.createLogInfo(
					LogConstant.logObject.地址.toString(), lb.getLbIP()));
			if (result == true) {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.负载均衡.ordinal(),
						LogConstant.logAction.更新.ordinal(),
						LogConstant.logStatus.成功.ordinal(),
						infoArray.toString(), startTime, elapse);
				MessagePush.pushMessage(userId,
						Utilities.stickyToSuccess(log.toString()));
			} else {
				OCLog log = this.getLogDAO().insertLog(userId,
						LogConstant.logObject.负载均衡.ordinal(),
						LogConstant.logAction.更新.ordinal(),
						LogConstant.logStatus.失败.ordinal(),
						infoArray.toString(), startTime, elapse);
				MessagePush.pushMessage(userId,
						Utilities.stickyToError(log.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getLBList(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int total = this.getLbDAO().countAllLBList(search, userId);
		List<LB> lbList = this.getLbDAO().getOnePageLBList(userId, page, limit,
				search);
		ja.put(total);
		if (lbList != null) {
			for (LB lb : lbList) {
				JSONObject jo = new JSONObject();
				String lbUuid = lb.getLbUuid();
				jo.put("uuid", lbUuid);
				jo.put("name", Utilities.encodeText(lb.getLbName()));
				jo.put("power", lb.getLbPower());
				jo.put("ip", null == lb.getLbIP() ? "null" : lb.getLbIP());
				jo.put("capacity", lb.getLbCapacity());
				String eip = this.getEipDAO().getEipIp(lbUuid);
				if (eip == null) {
					jo.put("eip", "");
				} else {
					jo.put("eip", eip);
				}
				String timeUsed = Utilities.encodeText(Utilities.dateToUsed(lb
						.getCreateDate()));
				jo.put("createdate", timeUsed);
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray getAbleLBs(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getLbDAO().countAllLBList(search, userId);
		ja.put(totalNum);
		List<LB> lbList = this.getLbDAO().getOnePageLBList(userId, page, limit,
				search);
		if (lbList != null) {
			for (int i = 0; i < lbList.size(); i++) {
				JSONObject jo = new JSONObject();
				LB lb = lbList.get(i);
				jo.put("vmid", lb.getLbUuid());
				jo.put("vmname", Utilities.encodeText(lb.getLbName()));
				ja.put(jo);
			}
		}
		return ja;
	}

	public void lbCreate(String name, String uuid, int capacity, int userId,
			String poolUuid) {
		Date startTime = new Date();
		JSONObject result = this.createLB(uuid, userId, name, capacity,
				poolUuid);
		// push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.负载均衡.toString(), name));
		infoArray
				.put(Utilities.createLogInfo("最大连接数", String.valueOf(capacity)));
		if (result.getBoolean("isSuccess") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.editRowStatus(userId, uuid, "running", "活跃");
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			infoArray.put(Utilities.createLogInfo("原因",
					result.getString("error")));
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.deleteRow(userId, uuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void lbCreateFore(String name, String foreuuid, String lbuuid,
			String protocol, int port, int policy, int userId) {
		Foreend fe = this.getForeendDAO().createForeend(foreuuid, name,
				protocol, port, policy, lbuuid);
		this.getLbDAO().setLBStatus(lbuuid, 2);
		// push message
		if (null != fe) {
			String info = "监听器创建成功";
			MessagePush.editRowStatus(userId, foreuuid, "running", "活跃");
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			String info = "监听器创建失败";
			MessagePush.deleteRow(userId, foreuuid);
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
	}

	public void lbCreateBack(String name, String lbuuid, String backuuid,
			String vmuuid, String vmip, int port, int weight, String feuuid,
			int userId) {
		Backend be = this.getBackendDAO().createBackend(backuuid, name, vmuuid,
				vmip, port, weight, feuuid);
		this.getLbDAO().setLBStatus(lbuuid, 2);
		// push message
		if (null != be) {
			String info = "后端创建成功";
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			String info = "后端创建失败";
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
	}

	public JSONObject lbCheckBack(String beuuid, int port) {
		boolean result = this.getBackendDAO().checkRepeat(beuuid, port);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		return jo;
	}

	public JSONObject lbDeletefore(String foreuuid, String lbuuid, int userId) {
		JSONObject jo = new JSONObject();
		boolean result = this.getForeendDAO().deleteForeend(foreuuid);
		jo.put("result", result);
		// push message
		if (result) {
			this.getLbDAO().setLBStatus(lbuuid, 2);
			String info = "监听器删除成功";
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			String info = "监听器删除失败";
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
		return jo;
	}

	public JSONObject lbDeleteBack(String backuuid, String lbuuid, int userId) {
		boolean result = this.getBackendDAO().deleteBackend(backuuid);
		this.getLbDAO().setLBStatus(lbuuid, 2);
		// push message
		if (result) {
			String info = "后端删除成功";
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			String info = "后端删除失败";
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		return jo;
	}

	public JSONObject lbForbidfore(String foreUuid, int state, String lbuuid,
			int userId) {
		JSONObject jo = new JSONObject();
		boolean result = this.getForeendDAO().changeForeendStatus(foreUuid,
				state);
		jo.put("result", result);
		this.getLbDAO().setLBStatus(lbuuid, 2);
		// push message
		String info = "";
		if (state == 0) {
			if (result == true) {
				info = "监听器禁用成功";
			} else {
				info = "监听器禁用失败";
			}
		} else {
			if (result == true) {
				info = "监听器启用成功";
			} else {
				info = "监听器启动失败";
			}
		}
		if (result == true) {
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
		return jo;
	}

	public JSONObject lbForbidback(String backuuid, int state, String lbuuid,
			int userId) {
		boolean result = this.getBackendDAO().changeBackendStatus(backuuid,
				state);
		this.getLbDAO().setLBStatus(lbuuid, 2);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		// push message
		String info = "";
		if (state == 0) {
			if (result == true) {
				info = "后端禁用成功";
			} else {
				info = "后端禁用失败";
			}
		} else {
			if (result == true) {
				info = "后端启用成功";
			} else {
				info = "后端启动失败";
			}
		}
		if (result == true) {
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
		return jo;
	}

	public void lbUpdatefore(String foreName, String foreUuid, int forePolicy,
			String lbuuid, int userId) {
		boolean result = this.getForeendDAO().updateForeend(foreUuid, foreName,
				forePolicy);
		this.getLbDAO().setLBStatus(lbuuid, 2);
		// push message
		if (result) {
			String info = "监听器修改成功";
			MessagePush.pushMessage(userId, Utilities.stickyToSuccess(info));
		} else {
			String info = "监听器修改失败";
			MessagePush.pushMessage(userId, Utilities.stickyToError(info));
		}
	}

	public JSONObject lbApplylb(String lbuuid, int userId) {
		boolean result = this.applyLB(userId, lbuuid);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		return jo;
	}

	public void lbAdminShutUp(String uuid, int userId) {
		LB lb = this.getLbDAO().getLB(uuid);
		String poolUuid = this.getHostDAO().getHost(lb.getHostUuid()).getPoolUuid();
		this.lbShutup(uuid, userId, poolUuid);
	}
	
	public void lbShutup(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.startLB(uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.负载均衡.toString(),
				"lb-" + uuid.substring(0, 8)));
		if (result == true) {
			MessagePush.editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			MessagePush.editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void lbAdminShutDown(String uuid, String force, int userId) {
		LB lb = this.getLbDAO().getLB(uuid);
		String poolUuid = this.getHostDAO().getHost(lb.getHostUuid()).getPoolUuid();
		this.lbShutdown(uuid, force, userId, poolUuid);
	}
	
	public void lbShutdown(String uuid, String force, int userId,
			String poolUuid) {
		Date startTime = new Date();
		boolean result = this.shutdownLB(uuid, force, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.负载均衡.toString(),
				"lb-" + uuid.substring(0, 8)));
		if (result == true) {
			MessagePush.editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			MessagePush.editRowStatus(userId, uuid, "running", "活跃");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public void lbDelete(String uuid, int userId, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.deleteLB(userId, uuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.负载均衡.toString(),
				"lb-" + uuid.substring(0, 8)));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.deleteRow(userId, uuid);
			MessagePush.pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.负载均衡.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			MessagePush.pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	public JSONArray lbQuota(int userId) {
		JSONArray ja = new JSONArray();
		int free = this.getQuotaDAO().getQuotaTotal(userId)
				.getQuotaLoadBalance()
				- this.getQuotaDAO().getQuotaUsed(userId).getQuotaLoadBalance();
		JSONObject jo = new JSONObject();
		jo.put("free", free);
		if (free < 1) {
			jo.put("result", false);
		} else {
			jo.put("result", true);
		}
		ja.put(jo);
		return ja;
	}

	public JSONObject getLBDetail(String lbUuid) {
		JSONObject jo = new JSONObject();
		LB lb = this.getLbDAO().getLB(lbUuid);
		if (lb != null) {
			jo.put("lbName", Utilities.encodeText(lb.getLbName()));
			jo.put("lbDesc",
					(lb.getLbDesc() == null) ? "&nbsp;" : Utilities
							.encodeText(lb.getLbDesc()));
			jo.put("lbIp", lb.getLbIP());
			String eip = this.getEipDAO().getEipIp(lbUuid);
			if (eip == null) {
				jo.put("eip", "&nbsp;");
			} else {
				jo.put("eip", eip);
			}
			jo.put("lbUID", lb.getLbUID());
			jo.put("lbMac", lb.getLbMac());
			jo.put("lbStatus", lb.getLbStatus());
			jo.put("lbCapacity", lb.getLbCapacity());
			jo.put("lbPower", lb.getLbPower());
			jo.put("lbFirewall",
					(lb.getFirewallUuid() == null) ? "&nbsp;" : lb
							.getFirewallUuid());
			jo.put("createDate", Utilities.formatTime(lb.getCreateDate()));
			String timeUsed = Utilities.encodeText(Utilities.dateToUsed(lb
					.getCreateDate()));
			jo.put("useDate", timeUsed);
		}
		return jo;
	}

	public JSONArray getAdminLBList(int page, int limit, String host,
			int importance, String type) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getLbDAO().countAllAdminVMList(host, importance);
		List<LB> lbList = this.getLbDAO().getOnePageAdminVmList(page, limit,
				host, importance);
		ja.put(totalNum);
		if (lbList != null) {
			for (int i = 0; i < lbList.size(); i++) {
				JSONObject jo = new JSONObject();
				LB lb = lbList.get(i);
				jo.put("vmid", lb.getLbUuid());
				jo.put("vmname", Utilities.encodeText(lb.getLbName()));
				jo.put("state", lb.getLbPower());
				jo.put("cpu", "1");
				jo.put("memory", "1024");
				String ip = lb.getLbIP();
				if (ip == null) {
					jo.put("ip", "null");
				} else {
					jo.put("ip", ip);
				}
				String timeUsed = Utilities.encodeText(Utilities.dateToUsed(lb
						.getCreateDate()));
				jo.put("createdate", timeUsed);
				jo.put("importance", lb.getLbImportance());
				jo.put("userName", Utilities.encodeText(this.getUserDAO().getUser(lb.getLbUID()).getUserName()));
				ja.put(jo);
			}
		}
		return ja;
	}
}
