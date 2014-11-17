package com.oncecloud.manager.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Network;
import com.once.xenapi.SR;
import com.once.xenapi.Types;
import com.once.xenapi.Types.BadServerResponse;
import com.once.xenapi.Types.XenAPIException;
import com.once.xenapi.VDI;
import com.once.xenapi.VIF;
import com.once.xenapi.VM;
import com.once.xenapi.VMUtil;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.dao.VolumeDAO;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.Image;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.User;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.NoVNC;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.VMManager;
import com.oncecloud.message.MessagePush;

@Component("VMManager")
public class VMManagerImpl implements VMManager {
	private final static Logger logger = Logger.getLogger(VMManager.class);
	private final static long MB = 1024 * 1024;
	
	private VMDAO vmDAO;
	private VnetDAO vnetDAO;
	private UserDAO userDAO;
	private HostDAO hostDAO;
	private LogDAO logDAO;
	private DHCPDAO dhcpDAO;
	private EIPDAO eipDAO;
	private VolumeDAO volumeDAO;
	private FirewallDAO firewallDAO;
	private RouterDAO routerDAO;
	private LBDAO lbDAO;
	private ImageDAO imageDAO;
	
	private MessagePush messagePush;
	
	private Constant constant;
	
	private VnetDAO getVnetDAO() {
		return vnetDAO;
	}

	@Autowired
	private void setVnetDAO(VnetDAO vnetDAO) {
		this.vnetDAO = vnetDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}
	
	private LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	private void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}
	
	private DHCPDAO getDhcpDAO() {
		return dhcpDAO;
	}

	@Autowired
	private void setDhcpDAO(DHCPDAO dhcpDAO) {
		this.dhcpDAO = dhcpDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}
	
	private VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	private void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}
	
	public RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	public void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	public LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	public void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}
	
	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}
	
	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}
	
	private String getHostAddress(String hostUuid) {
		String hostIP = null;
		OCHost host = this.getHostDAO().getHost(hostUuid);
		if (host != null) {
			hostIP = host.getHostIP();
		}
		return hostIP;
	}

	private int getVNCPort(String uuid, String poolUuid) {
		int port = 0;
		try {
			VM vm = Types.toVM(uuid);
			String location = vm.getVNCLocation(this.getConstant()
					.getConnectionFromPool(poolUuid));
			port = 5900;
			int len = location.length();
			if (len > 5 && location.charAt(len - 5) == ':') {
				port = Integer.parseInt(location.substring(len - 4));
			}
		} catch (Exception e) {
		}
		return port;
	}
	
	//如果vlan为null，则绑定到基础网络，否则绑定到对应到私有网络
	private int bindVlan(String uuid, String vlan, Connection c) {
		int vlanId = -1;
		if (vlan != null) {
			try {
				vlanId = this.getVnetDAO().getVnet(vlan).getVnetID();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setVlan(uuid, vlanId, c);
		return vlanId;
	}

	private boolean setVlan(String uuid, int vlanId, Connection c) {
		try {
			VM vm = VM.getByUuid(c, uuid);
			vm.setTag(c, vm.getVIFs(c).iterator().next(),
					String.valueOf(vlanId));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private String getAllocateHost(Connection conn, int memory) {
		String host = null;
		try {
			Map<Host, Host.Record> hostMap = Host.getAllRecords(conn);
			long maxFree = 0;
			for (Host thisHost : hostMap.keySet()) {
				Host.Record hostRecord = hostMap.get(thisHost);
				long memoryFree = hostRecord.memoryFree;
				if (memoryFree > maxFree) {
					maxFree = memoryFree;
					host = thisHost.toWireString();
				}
			}
			if ((int) (maxFree / MB) >= memory) {
				return host;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public void adminDeleteVM(int userId, String uuid) {
		OCVM ocvm = this.getVmDAO().getVM(uuid);
		String hostUuid = ocvm.getHostUuid();
		String poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		this.deleteVM(userId, uuid, poolUuid);
	}

	private void deleteVM(int userId, String uuid, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doDeleteVM(userId, uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.主机.toString(),
				"i-" + uuid.substring(0, 8)));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().deleteRow(userId, uuid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.销毁.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doDeleteVM(int userId, String uuid, String poolUuid) {
		boolean result = false;
		Connection c = null;
		try {
			c = this.getConstant().getConnectionFromPool(poolUuid);
			boolean preDeleteVM = this.getVmDAO().updatePowerStatus(uuid,
					VMManager.POWER_DESTROY);
			if (preDeleteVM == true) {
				VM thisVM = VM.getByUuid(c, uuid);
				thisVM.hardShutdown(c);
				thisVM.destroy(c, true);
				OCVM currentVM = this.getVmDAO().getVM(uuid);
				String ip = currentVM.getVmIP();
				String mac = currentVM.getVmMac();
				if (ip != null) {
					this.getDhcpDAO().returnDHCP(mac);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
//				管理员无此操作
//				if (c != null) {
//					emptyAttachedVolume(c, uuid);
//				}
//				String publicip = this.getEipDAO().getEipIp(uuid);
//				if (publicip != null) {
//					unbindElasticIp(c, uuid,publicip, userId);
//				}
//				Date endDate = new Date();
//				管理员不用改费用
//				this.getFeeDAO().destoryVM(endDate, uuid);
				NoVNC.deleteToken(uuid.substring(0, 8));
				this.getVmDAO().removeVM(userId, uuid);
//				管理员不用改配额
//				this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
//						"quotaVM", 1, false);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
//	管理员无此操作，用户该操作还需要再做考虑，如何模块化
//	private void unbindElasticIp(Connection c, String uuid, String eipIp, int userId) {
//		try {
//			String ip = null;
//			String eif = this.getEipDAO().getEip(eipIp).getEipInterface();
//			OCVM vm = this.getVmDAO().getVM(uuid);
//			ip = vm.getVmIP();
//			boolean deActiveResult = deActiveFirewall(c, userId, ip);
//			if (deActiveResult) {
//				if (Host.unbindOuterIp(c, ip, eipIp, eif)) {
//					this.getEipDAO().unBindEip(eipIp);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private boolean deActiveFirewall(Connection c, int userId, String ip) {
//		boolean result = false;
//		try {
//			JSONObject total = new JSONObject();
//			JSONArray ipArray = new JSONArray();
//			ipArray.put(ip);
//			JSONArray ruleArray = new JSONArray();
//			total.put("IP", ipArray);
//			total.put("rules", ruleArray);
//			result = Host.firewallApplyRule(c, total.toString(), null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	private void emptyAttachedVolume(Connection c, String uuid) {
//		try {
//			List<String> volumeList = this.getVolumeDAO().getVolumesOfVM(uuid);
//			for (String volume : volumeList) {
//				try {
//					VM.deleteDataVBD(c, uuid, volume);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				this.getVolumeDAO().emptyDependency(volume);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void doAdminStartVm(int userId, String uuid) {
		OCVM ocvm = this.getVmDAO().getVM(uuid);
		String hostUuid = ocvm.getHostUuid();
		String poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		this.startVM(userId, uuid, poolUuid);
	}

	public void doAdminShutDown(int userId, String uuid, String force) {
		OCVM ocvm = this.getVmDAO().getVM(uuid);
		String hostUuid = ocvm.getHostUuid();
		String poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		this.shutdownVM(userId, uuid, force, poolUuid);
	}

	private void startVM(int userId, String uuid, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doStartVM(uuid, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.主机.toString(),
				"i-" + uuid.substring(0, 8)));
		if (result == true) {
			this.getMessagePush()
					.editRowStatus(userId, uuid, "running", "正常运行");
			this.getMessagePush().editRowConsole(userId, uuid, "add");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			this.getMessagePush().editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.启动.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doStartVM(String uuid, String poolUuid) {
		boolean result = false;
		String hostUuid = null;
		String powerState = null;
		OCVM currentVM = null;
		Connection c = null;
		try {
			currentVM = this.getVmDAO().getVM(uuid);
			if (currentVM != null) {
				boolean preStartVM = this.getVmDAO().updatePowerStatus(uuid,
						VMManager.POWER_BOOT);
				if (preStartVM == true) {
					c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					if (!powerState.equals("Running")) {
						hostUuid = getAllocateHost(c,
								currentVM.getVmMem());
						Host allocateHost = Types.toHost(hostUuid);
						thisVM.startOn(c, allocateHost, false, true);
					} else {
						hostUuid = thisVM.getResidentOn(c).toWireString();
					}
					this.getVmDAO().updateHostUuid(uuid, hostUuid);
					this.getVmDAO().updatePowerStatus(uuid,
							VMManager.POWER_RUNNING);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getVmDAO().updatePowerStatus(uuid,
							VMManager.POWER_RUNNING);
				} else {
					this.getVmDAO().updatePowerStatus(uuid,
							VMManager.POWER_HALTED);
				}
			} else {
				this.getVmDAO().updatePowerStatus(uuid, VMManager.POWER_HALTED);
			}
		} finally {
			if (result = true) {
				int vlan = bindVlan(uuid, currentVM.getVmVlan(), c);
				logger.debug("Bind Vlan: VM [i-" + uuid.substring(0, 8)
						+ "] Result [" + vlan + "]");
				String hostAddress = getHostAddress(hostUuid);
				int port = getVNCPort(uuid, poolUuid);
				logger.debug("Override Token: Token [" + uuid.substring(0, 8)
						+ "] Host Address [" + hostAddress + "] Port [" + port
						+ "]");
				NoVNC.createToken(uuid.substring(0, 8), hostAddress, port);
			}
		}
		return result;
	}

	private void shutdownVM(int userId, String uuid, String force,
			String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doShutdownVM(uuid, force, poolUuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.主机.toString(),
				"i-" + uuid.substring(0, 8)));
		if (result == true) {
			this.getMessagePush().editRowStatus(userId, uuid, "stopped", "已关机");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			this.getMessagePush()
					.editRowStatus(userId, uuid, "running", "正常运行");
			this.getMessagePush().editRowConsole(userId, uuid, "add");
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.关闭.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doShutdownVM(String uuid, String force, String poolUuid) {
		boolean result = false;
		String powerState = null;
		String hostUuid = null;
		try {
			OCVM currentVM = this.getVmDAO().getVM(uuid);
			if (currentVM != null) {
				boolean preShutdownVM = this.getVmDAO().updatePowerStatus(uuid,
						VMManager.POWER_SHUTDOWN);
				if (preShutdownVM == true) {
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
					this.getVmDAO().updateHostUuid(uuid, hostUuid);
					this.getVmDAO().updatePowerStatus(uuid,
							VMManager.POWER_HALTED);
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (powerState != null) {
				if (powerState.equals("Running")) {
					this.getVmDAO().updatePowerStatus(uuid,
							VMManager.POWER_RUNNING);
				} else {
					this.getVmDAO().updatePowerStatus(uuid,
							VMManager.POWER_HALTED);
				}
			} else {
				this.getVmDAO()
						.updatePowerStatus(uuid, VMManager.POWER_RUNNING);
			}
		} finally {
			if (result == true) {
				NoVNC.deleteToken(uuid.substring(0, 8));
			}
		}
		return result;
	}

	public JSONArray getAdminVMList(int page, int limit, String host,
			int importance, String type) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getVmDAO().countVMsOfAdmin(host, importance);
		List<OCVM> vmList = this.getVmDAO().getOnePageVMsOfAdmin(page, limit,
				host, importance);
		ja.put(totalNum);
		if (vmList != null) {
			for (int i = 0; i < vmList.size(); i++) {
				JSONObject jo = new JSONObject();
				OCVM ocvm = vmList.get(i);
				jo.put("vmid", ocvm.getVmUuid());
				jo.put("vmname", Utilities.encodeText(ocvm.getVmName()));
				jo.put("state", ocvm.getVmPower());
				jo.put("cpu", ocvm.getVmCpu());
				jo.put("memory", ocvm.getVmMem());
				String ip = ocvm.getVmIP();
				if (ip == null) {
					jo.put("ip", "null");
				} else {
					jo.put("ip", ip);
				}
				String vlan = ocvm.getVmVlan();
				if (vlan == null) {
					jo.put("vlan", "null");
				} else {
					String name = this.getVnetDAO().getVnetName(vlan);
					jo.put("vlan", name);
				}
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed(ocvm.getCreateDate()));
				jo.put("createdate", timeUsed);
				jo.put("importance", ocvm.getVmImportance());
				User user = this.getUserDAO().getUser(ocvm.getVmUID());
				jo.put("userName", Utilities.encodeText(user.getUserName()));
				jo.put("level", user.getUserLevel());
				ja.put(jo);
			}
		}
		return ja;
	}
	
	public void updateImportance(String uuid, int importance) {
		this.getVmDAO().updateVMImportance(uuid, importance);
	}

	public JSONArray getISOList(String poolUuid) {
		JSONArray ja = new JSONArray();
		Connection conn = null;
		try {
			conn = this.getConstant().getConnectionFromPool(poolUuid);
			Set<VDI> vdis = VMUtil.getISOs(conn);
			for (VDI vdi : vdis) {
				String nameLabel = vdi.getNameLabel(conn);
				if (nameLabel.contains(".iso")) {
					// String location = vdi.getLocation(conn);
					String uuid = vdi.getUuid(conn);
					JSONObject jo = new JSONObject();
					jo.put("name", nameLabel);
					// jo.put("location", location);
					jo.put("uuid", uuid);
					ja.put(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}

	public void createVMByISO(String vmUuid, String isoUuid, String srUuid,
			String name, int cpu, int memory, int volumeSize, String poolUuid,
			int userId) {
		int memoryCapacity = (int) (memory * 1024);
		Date startTime = new Date();
		boolean result = doCreateVMByISO(vmUuid, isoUuid, srUuid, name, cpu,
				memoryCapacity, volumeSize, poolUuid, userId);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.主机.toString(),
				"i-" + vmUuid.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo("配置", cpu + " 核， " + memory
				+ " GB"));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().editRowStatus(userId, vmUuid, "running",
					"正常运行");
			this.getMessagePush().editRowConsole(userId, vmUuid, "add");
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().deleteRow(userId, vmUuid);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doCreateVMByISO(String vmUuid, String isoUuid,
			String srUuid, String name, int cpu, int memory, int volumeSize,
			String poolUuid, int userId) {
		Connection conn = null;
		boolean result = false;
		boolean preCreate = false;
		boolean dbRollback = true;
		try {
			preCreate = this.getVmDAO().preCreateVM(vmUuid, null, userId, name, 1,
					null, memory, cpu, VMManager.POWER_CREATE, 1, new Date());
//			管理员不需要
//			this.getQuotaDAO().updateQuotaFieldNoTransaction(vmUID, "quotaVM",
//					1, true);
			if (preCreate) {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				String hostUuid = this.getAllocateHost(conn, memory);
				// Get Disk
				SR sr = SR.getByUuid(conn, srUuid);
				String type = sr.getType(conn);
				// Create VM By ISO
				VM.Record record = VM.createVMFromISO(vmUuid,
						"i-" + vmUuid.substring(0, 8), cpu, memory, conn,
						hostUuid, isoUuid, volumeSize, srUuid, type);
				if (record != null) {
//					管理员不需要
//					String firewallId = null;
//					if (userId != 1) {
//						firewallId = this.getFirewallDAO()
//								.getDefaultFirewall(userId).getFirewallId();
//					}
					result = this.getVmDAO().updateVM(null, vmUuid, "onceas",
							VMManager.POWER_RUNNING, hostUuid, null);
					String hostAddress = getHostAddress(hostUuid);
					int port = getVNCPort(vmUuid, poolUuid);
					NoVNC.createToken(vmUuid.substring(0, 8), hostAddress, port);
					dbRollback = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbRollback) {
				this.getVmDAO().removeVM(userId, vmUuid);
			}
		}
		return result;
	}

	private String getPoolUuid(String uuid, String type) {
		String poolUuid = "";
		if (type.equals("instance")) {
			OCVM ocvm = this.getVmDAO().getVM(uuid);
			String hostUuid = ocvm.getHostUuid();
			poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		} else if (type.equals("router")) {
			Router router = this.getRouterDAO().getRouter(uuid);
			String hostUuid = router.getHostUuid();
			poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		} else if (type.equals("loadbalance")) {
			LB lb = this.getLbDAO().getLB(uuid);
			String hostUuid = lb.getHostUuid();
			poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
		}
		return poolUuid;
	}

	public boolean addMac(String uuid, String type, String physical,
			String vnetid) {
		String poolUuid = this.getPoolUuid(uuid, type);
		if (!poolUuid.equals("")) {
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				String mac = Utilities.randomMac();
				VIF vif = VIF.createBindToPhysicalNetwork(conn, vm, physical,
						mac);
				vm.setTag(conn, vif, vnetid);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean modifyVnet(String uuid, String type, String vnetid,
			String vifUuid) {
		String poolUuid = this.getPoolUuid(uuid, type);
		if (!poolUuid.equals("")) {
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				VIF vif = VIF.getByUuid(conn, vifUuid);
				vm.setTag(conn, vif, vnetid);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean modifyPhysical(String uuid, String type, String physical,
			String vifUuid) {
		String poolUuid = this.getPoolUuid(uuid, type);
		if (!poolUuid.equals("")) {
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				VIF vif = VIF.getByUuid(conn, vifUuid);
				vif.set_physical_network(conn, vm, physical);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteMac(String uuid, String type, String vifUuid) {
		String poolUuid = this.getPoolUuid(uuid, type);
		if (!poolUuid.equals("")) {
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				VIF vif = VIF.getByUuid(conn, vifUuid);
				vm.destroyVIF(conn, vif);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public JSONArray getMacs(String uuid, String type) {
		String poolUuid = this.getPoolUuid(uuid, type);
		JSONArray ja = new JSONArray();
		if (!poolUuid.equals("")) {
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				Set<VIF> vifset = vm.getVIFs(conn);
				for (VIF vif : vifset) {
					JSONObject jo = new JSONObject();
					VIF.Record record = vm.getVIFRecord(conn, vif);
					String tag = vm.getTag(conn, vif);
					String physical = record.network.getNameLabel(conn);
					jo.put("vifuuid", record.uuid);
					jo.put("device", record.device);
					jo.put("mac", record.MAC);
					jo.put("tag", tag);
					jo.put("physical", physical);
					ja.put(jo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ja;
	}

	public JSONArray getNets(String uuid, String type) {
		JSONArray ja = new JSONArray();
		String poolUuid = this.getPoolUuid(uuid, type);
		if (!poolUuid.equals("")) {
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				Map<Network, Network.Record> map = Network.getAllRecords(conn);
				Collection<Network.Record> collection = map.values();
				Iterator<Network.Record> it = collection.iterator();
				while (it.hasNext()) {
					JSONObject jo = new JSONObject();
					jo.put("nets", it.next().nameLabel);
					ja.put(jo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ja;
	}

	public void saveToDataBase(String vmUuid, String vmPWD, int vmUID,
			int vmPlatform, String vmName, String vmIP) {
		Connection conn = null;
		try {
			conn = this.getConstant().getConnection(vmUID);
			VM vm = VM.getByUuid(conn, vmUuid);
			VM.Record record = vm.getRecord(conn);
			String vmMac = null;
			for (String temMac : record.MAC) {
				vmMac = temMac;
				break;
			}
			OCVM ocvm = new OCVM();
			ocvm.setVmUuid(vmUuid);
			ocvm.setVmPWD(vmPWD);
			ocvm.setVmUID(vmUID);
			ocvm.setVmName(vmName);
			ocvm.setVmPlatform(vmPlatform);
			ocvm.setVmIP(vmIP);
			ocvm.setVmMac(vmMac);
			ocvm.setVmMem((int) (record.memoryStaticMax / (1024 * 1024)));
			ocvm.setVmCpu((record.VCPUsMax).intValue());
			ocvm.setVmPower(1);
			ocvm.setVmStatus(1);
			ocvm.setHostUuid(record.residentOn.toWireString());
			ocvm.setCreateDate(new Date());
			this.getVmDAO().saveVM(ocvm);
			DHCP dhcp = new DHCP();
			dhcp.setDhcpMac(vmMac);
			dhcp.setDhcpIp(vmIP);
			dhcp.setTenantUuid(vmUuid);
			dhcp.setDepenType(0);
			dhcp.setCreateDate(new Date());
			this.getDhcpDAO().saveDHCP(dhcp);
		} catch (BadServerResponse e) {
			e.printStackTrace();
		} catch (XenAPIException e) {
			e.printStackTrace();
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	}

	public boolean templateToVM(String uuid) {
		Image image = this.getImageDAO().getImage(uuid);
		String poolUuid = image.getPoolUuid();
		Connection conn = null;
		boolean result = false;
		try {
			conn = this.getConstant().getConnectionFromPool(poolUuid);
			VM vm = VM.getByUuid(conn, uuid);
			VM.Record record = vm.getRecord(conn);
			vm.setIsATemplate(conn, false);
			String vmMac = null;
			for (String temMac : record.MAC) {
				vmMac = temMac;
				break;
			}
			image.setImageStatus(-1);
			this.getImageDAO().updateImage(image);
			OCVM ocvm = new OCVM(uuid, image.getImagePwd(),
					image.getImageUID(), image.getImageName(),
					image.getImagePlatform(), vmMac,
					(int) (record.memoryStaticMax / (1024 * 1024)),
					(record.VCPUsMax).intValue(), 0, 1, image.getCreateDate());
			String hostUuid = record.residentOn.toWireString();
			ocvm.setHostUuid(hostUuid);
			this.getVmDAO().saveVM(ocvm);
			String hostAddress = getHostAddress(hostUuid);
			int port = getVNCPort(uuid, poolUuid);
			NoVNC.createToken(uuid.substring(0, 8), hostAddress, port);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean vmToTemplate(String uuid) {
		boolean result = false;
		if (this.getImageDAO().checkImage(uuid)) {
			OCVM ocvm = this.getVmDAO().getVM(uuid);
			Image image = this.getImageDAO().getImage(uuid);
			String poolUuid = image.getPoolUuid();
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				vm.setIsATemplate(conn, true);
				image.setImageStatus(1);
				this.getImageDAO().updateImage(image);
				this.getVmDAO().deleteVM(ocvm);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		} else {
			OCVM ocvm = this.getVmDAO().getVM(uuid);
			String hostUuid = ocvm.getHostUuid();
			String poolUuid = this.getHostDAO().getHost(hostUuid).getPoolUuid();
			Connection conn = null;
			try {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				VM vm = VM.getByUuid(conn, uuid);
				vm.setIsATemplate(conn, true);
				Image image = new Image(ocvm.getVmUuid(), ocvm.getVmName(),
						ocvm.getVmPWD(), ocvm.getVmUID(), 20,
						ocvm.getVmPlatform(), 1, poolUuid, ocvm.getVmDesc(),
						ocvm.getCreateDate(), null);
				this.getImageDAO().save(image);
				this.getVmDAO().deleteVM(ocvm);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}
	
}
