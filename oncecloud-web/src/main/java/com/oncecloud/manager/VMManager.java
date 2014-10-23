package com.oncecloud.manager;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import com.once.xenapi.VM.Record;
import com.once.xenapi.VMUtil;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FeeDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
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
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.NoVNC;
import com.oncecloud.main.Utilities;
import com.oncecloud.message.MessagePush;

/**
 * @author hehai
 * @version 2014/08/22
 */
@Component
public class VMManager {
	private final static Logger logger = Logger.getLogger(VMManager.class);
	private final static long MB = 1024 * 1024;
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;
	public final static int POWER_RESTART = 6;

	private HostDAO hostDAO;
	private VnetDAO vnetDAO;
	private VMDAO vmDAO;
	private LogDAO logDAO;
	private DHCPDAO dhcpDAO;
	private EIPDAO eipDAO;
	private FeeDAO feeDAO;
	private VolumeDAO volumeDAO;
	private ImageDAO imageDAO;
	private FirewallDAO firewallDAO;
	private QuotaDAO quotaDAO;
	private UserDAO userDAO;
	private RouterDAO routerDAO;
	private LBDAO lbDAO;

	private MessagePush messagePush;

	private EIPManager eipManager;

	private Constant constant;

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

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

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	private VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	private void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	private void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
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

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private EIPManager getEipManager() {
		return eipManager;
	}

	@Autowired
	private void setEipManager(EIPManager eipManager) {
		this.eipManager = eipManager;
	}

	private Constant getConstant() {
		return constant;
	}

	@Autowired
	private void setConstant(Constant constant) {
		this.constant = constant;
	}

	public Record createVMOnHost(Connection c, String vmUuid, String tplUuid,
			String loginName, String loginPwd, long cpuCore,
			long memoryCapacity, String mac, String ip, String OS,
			String hostUuid, String imagePwd, String vmName, boolean ping) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cpuNumber", cpuCore);
		map.put("memoryValue", memoryCapacity);
		map.put("newUuid", vmUuid);
		map.put("MAC", mac);
		map.put("IP", ip);
		map.put("type", OS);
		map.put("passwd", loginPwd);
		map.put("origin_passwd", imagePwd);
		Host host = Types.toHost(hostUuid);
		Record vmrecord = VM.createOnFromTemplate(c, host, tplUuid, vmName,
				map, ping);
		return vmrecord;
	}

	public Record createVMFromVDI(Connection c, String vmUuid, String vdiUuid,
			String tplUuid, String loginName, String loginPwd, long cpuCore,
			long memoryCapacity, String mac, String ip, String OS,
			String hostUuid, String imagePwd, String vmName, boolean ping) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cpuNumber", cpuCore);
		map.put("memoryValue", memoryCapacity);
		map.put("newUuid", vmUuid);
		map.put("vdiUuid", vdiUuid);
		map.put("MAC", mac);
		map.put("IP", ip);
		map.put("type", OS);
		map.put("passwd", loginPwd);
		map.put("origin_passwd", imagePwd);
		Host host = Types.toHost(hostUuid);
		Record vmrecord = VM.createWithVDI(c, host, tplUuid, vmName, map, ping);
		return vmrecord;
	}

	public String getHostAddress(String hostUuid) {
		String hostIP = null;
		OCHost host = this.getHostDAO().getHost(hostUuid);
		if (host != null) {
			hostIP = host.getHostIP();
		}
		return hostIP;
	}

	public int getVNCPort(String uuid, String poolUuid) {
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

	/*
	 * 如果vlan为null，则绑定到基础网络，否则绑定到对应到私有网络
	 */
	private int bindVlan(String uuid, String vlan, String poolUuid) {
		int vlanId = -1;
		if (vlan != null) {
			try {
				vlanId = this.getVnetDAO().getVnet(vlan).getVnetID();
			} catch (Exception e) {
			}
		}
		setVlan(uuid, vlanId, poolUuid);
		return vlanId;
	}

	public boolean setVlan(String uuid, int vlanId, String poolUuid) {
		try {
			Connection c = this.getConstant().getConnectionFromPool(poolUuid);
			VM vm = VM.getByUuid(c, uuid);
			vm.setTag(c, vm.getVIFs(c).iterator().next(),
					String.valueOf(vlanId));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean changeBandwidth(Integer userId, String uuid, int size) {
		boolean result = false;
		try {
			Connection c = this.getConstant().getConnection(userId);
			OCVM thisVM = this.getVmDAO().getVM(uuid);
			if (thisVM != null) {
				String ip = thisVM.getVmIP();
				if (ip != null) {
					result = Host.addIOLimit(c, ip, String.valueOf(size));
					logger.info("Change Bandwidth: IP [" + ip + "] Bandwidth ["
							+ size + " Mbps]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAllocateHost(String poolUuid, int memory) {
		String host = null;
		try {
			Connection conn = this.getConstant()
					.getConnectionFromPool(poolUuid);
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

	public void deleteVM(int userId, String uuid, String poolUuid) {
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
				if (c != null) {
					emptyAttachedVolume(c, uuid);
				}
				String publicip = this.getEipDAO().getEipIp(uuid);
				if (publicip != null) {
					this.getEipManager().unbindElasticIp(userId, uuid,
							publicip, "vm");
				}
				Date endDate = new Date();
				this.getFeeDAO().destoryVM(endDate, uuid);
				NoVNC.deleteToken(uuid.substring(0, 8));
				this.getVmDAO().removeVM(userId, uuid);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private void emptyAttachedVolume(Connection c, String uuid) {
		try {
			List<String> volumeList = this.getVolumeDAO().getVolumesOfVM(uuid);
			for (String volume : volumeList) {
				try {
					VM.deleteDataVBD(c, uuid, volume);
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.getVolumeDAO().emptyDependency(volume);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void restartVM(int userId, String uuid, String poolUuid) {
		Date startTime = new Date();
		boolean result = this.doRestartVM(uuid, poolUuid);
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
					LogConstant.logAction.重启.ordinal(),
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
					LogConstant.logAction.重启.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
	}

	private boolean doRestartVM(String uuid, String poolUuid) {
		boolean result = false;
		String hostUuid = null;
		String powerState = null;
		OCVM currentVM = null;
		try {
			currentVM = this.getVmDAO().getVM(uuid);
			if (currentVM != null) {
				boolean preRestartVM = this.getVmDAO().updatePowerStatus(uuid,
						VMManager.POWER_RESTART);
				if (preRestartVM == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					hostUuid = thisVM.getResidentOn(c).toWireString();
					if (powerState.equals("Running")) {
						boolean cleanReboot = thisVM.cleanReboot(c);
						if (cleanReboot == false) {
							thisVM.hardShutdown(c);
							thisVM.start(c, false, true);
						}
					} else {
						thisVM.start(c, false, true);
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
				this.getVmDAO().updatePowerStatus(hostUuid,
						VMManager.POWER_RUNNING);
			}
		} finally {
			if (result == true) {
				int vlan = bindVlan(uuid, currentVM.getVmVlan(), poolUuid);
				logger.debug("Bind Vlan: VM [i-" + uuid.substring(0, 8)
						+ "] Result [" + vlan + "]");
				String hostAddress = this.getHostAddress(hostUuid);
				int port = this.getVNCPort(uuid, poolUuid);
				logger.debug("Override Token: Token [" + uuid.substring(0, 8)
						+ "] Host Address [" + hostAddress + "] Port [" + port
						+ "]");
				NoVNC.createToken(uuid.substring(0, 8), hostAddress, port);
			}
		}
		return result;
	}

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

	public void startVM(int userId, String uuid, String poolUuid) {
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
		try {
			currentVM = this.getVmDAO().getVM(uuid);
			if (currentVM != null) {
				boolean preStartVM = this.getVmDAO().updatePowerStatus(uuid,
						VMManager.POWER_BOOT);
				if (preStartVM == true) {
					Connection c = this.getConstant().getConnectionFromPool(
							poolUuid);
					VM thisVM = VM.getByUuid(c, uuid);
					powerState = thisVM.getPowerState(c).toString();
					if (!powerState.equals("Running")) {
						hostUuid = getAllocateHost(poolUuid,
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
				int vlan = bindVlan(uuid, currentVM.getVmVlan(), poolUuid);
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

	public void shutdownVM(int userId, String uuid, String force,
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

	private JSONObject doCreateVM(String vmUuid, String tplUuid, int userId,
			String vmName, int cpu, int memory, String pwd, String poolUuid,
			String vnetuuid) {
		JSONObject jo = new JSONObject();
		String ip = null;
		String allocateHost = null;
		String mac = null;
		Image image = null;
		String vmBackendName = "i-" + vmUuid.substring(0, 8);
		Date createDate = new Date();
		String OS = null;
		String imagePwd = null;
		Connection c = null;
		if (vnetuuid.equals("0")) {
			try {
				image = this.getImageDAO().getImage(tplUuid);
				if (image.getImagePlatform() == 1) {
					OS = "linux";
				} else {
					OS = "windows";
				}
				imagePwd = image.getImagePwd();
				DHCP dhcp = this.getDhcpDAO().getFreeDHCP(vmUuid, 0);
				ip = dhcp.getDhcpIp();
				mac = dhcp.getDhcpMac();
				c = this.getConstant().getConnectionFromPool(poolUuid);
				allocateHost = getAllocateHost(poolUuid, memory);
				logger.info("VM [" + vmBackendName + "] allocated to Host ["
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
			if (ip == null || allocateHost == null) {
				jo.put("isSuccess", false);
			} else {
				boolean dhcpRollback = false;
				boolean dbRollback = false;
				try {
					boolean preCreate = this.getVmDAO().preCreateVM(vmUuid,
							pwd, userId, vmName, image.getImagePlatform(), mac,
							memory, cpu, VMManager.POWER_CREATE, 1, createDate);
					Date preEndDate = new Date();
					int elapse = Utilities.timeElapse(createDate, preEndDate);
					logger.info("VM [" + vmBackendName + "] Pre Create Time ["
							+ elapse + "]");
					if (preCreate == true) {
						Record vmrecord = null;
						// 如果不能获取该模板的空闲VDI，则直接创建该虚拟机，否则使用该VDI创建虚拟机
						logger.info("VM Config: Template [" + tplUuid
								+ "] CPU [" + cpu + "] Memory [" + memory
								+ "] Mac [" + mac + "] IP [" + ip + "] OS ["
								+ OS + "]");
						vmrecord = createVMOnHost(c, vmUuid, tplUuid, "root",
								pwd, cpu, memory, mac, "", OS, allocateHost,
								imagePwd, vmBackendName, false);
						Thread.sleep(8000);
						Date createEndDate = new Date();
						int elapse1 = Utilities.timeElapse(createDate,
								createEndDate);
						logger.info("VM [" + vmBackendName + "] Create Time ["
								+ elapse1 + "]");
						if (vmrecord != null) {
							String hostuuid = vmrecord.residentOn
									.toWireString();
							if (hostuuid.equals(allocateHost)) {
								if (!vmrecord.setpasswd) {
									pwd = imagePwd;
								}
								jo.put("ip", ip);
								this.getVmDAO().updateVM(userId, vmUuid, pwd,
										VMManager.POWER_RUNNING, hostuuid, ip);
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(createDate);
								calendar.add(Calendar.MINUTE, 60);
								Date endDate = calendar.getTime();
								this.getFeeDAO()
										.insertFeeVM(
												userId,
												createDate,
												endDate,
												(Constant.CPU_PRICE
														* (double) cpu + Constant.MEMORY_PRICE
														* (memory / 1024.0)),
												1, vmUuid, vmName);
								String hostAddress = getHostAddress(hostuuid);
								int port = getVNCPort(vmUuid, poolUuid);
								NoVNC.createToken(vmUuid.substring(0, 8),
										hostAddress, port);
								jo.put("isSuccess", true);
							} else {
								jo.put("error", "主机后台启动位置错误");
								dhcpRollback = true;
								dbRollback = true;
							}
						} else {
							jo.put("error", "主机后台创建失败");
							dhcpRollback = true;
							dbRollback = true;
						}
					} else {
						jo.put("error", "主机预创建失败");
						dhcpRollback = true;
					}
				} catch (Exception e) {
					jo.put("error", "主机创建异常");
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
					this.getVmDAO().removeVM(userId, vmUuid);
					jo.put("isSuccess", false);
				}
			}
		} else {
			image = this.getImageDAO().getImage(tplUuid);
			if (image.getImagePlatform() == 1) {
				OS = "linux";
			} else {
				OS = "windows";
			}
			imagePwd = image.getImagePwd();
			mac = Utilities.randomMac();
			ip = "";
			c = this.getConstant().getConnectionFromPool(poolUuid);
			allocateHost = getAllocateHost(poolUuid, memory);
			logger.info("VM [" + vmBackendName + "] allocated to Host ["
					+ allocateHost + "]");
			try {
				boolean preCreate = this.getVmDAO().preCreateVM(vmUuid, pwd,
						userId, vmName, image.getImagePlatform(), mac, memory,
						cpu, VMManager.POWER_CREATE, 1, createDate);
				Date preEndDate = new Date();
				int elapse = Utilities.timeElapse(createDate, preEndDate);
				logger.info("VM [" + vmBackendName + "] Pre Create Time ["
						+ elapse + "]");
				if (preCreate == true) {
					Record vmrecord = null;
					// 如果不能获取该模板的空闲VDI，则直接创建该虚拟机，否则使用该VDI创建虚拟机
					logger.info("VM Config: Template [" + tplUuid + "] CPU ["
							+ cpu + "] Memory [" + memory + "] Mac [" + mac
							+ "] IP [" + ip + "] OS [" + OS + "]");
					vmrecord = createVMOnHost(c, vmUuid, tplUuid, "root", pwd,
							cpu, memory, mac, "", OS, allocateHost, imagePwd,
							vmBackendName, false);
					OCVM vm = this.getVmDAO().getVM(vmUuid);
					Vnet vnet = this.getVnetDAO().getVnet(vnetuuid);
					jo.put("vname", vnet.getVnetName());
					VM pvm = VM.getByUuid(c, vmUuid);
					pvm.setTag(c, pvm.getVIFs(c).iterator().next(),
							String.valueOf(vnet.getVnetID()));
					vm.setVmVlan(vnetuuid);
					if (vnet.getVnetRouter() != null) {
						String routerIp = this.getRouterDAO()
								.getRouter(vnet.getVnetRouter()).getRouterIP();
						String url = routerIp + ":9090";
						String subnet = "192.168." + vnet.getVnetNet() + ".0";
						ip = Host.assignIpAddress(c, url, mac, subnet);
					}
					Date createEndDate = new Date();
					int elapse1 = Utilities.timeElapse(createDate,
							createEndDate);
					logger.info("VM [" + vmBackendName + "] Create Time ["
							+ elapse1 + "]");
					if (vmrecord != null) {
						String hostuuid = vmrecord.residentOn.toWireString();
						if (hostuuid.equals(allocateHost)) {
							if (!vmrecord.setpasswd) {
								pwd = imagePwd;
							}
							if (ip.equals("")) {
								ip = null;
								jo.put("ip", "");
							} else {
								jo.put("ip", ip);
							}
							vm.setVmUID(userId);
							vm.setVmPWD(pwd);
							vm.setVmPower(VMManager.POWER_RUNNING);
							vm.setHostUuid(hostuuid);
							vm.setVmIP(ip);
							this.getVmDAO().updateVM(vm);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(createDate);
							calendar.add(Calendar.MINUTE, 60);
							Date endDate = calendar.getTime();
							this.getFeeDAO()
									.insertFeeVM(
											userId,
											createDate,
											endDate,
											(Constant.CPU_PRICE * (double) cpu + Constant.MEMORY_PRICE
													* (memory / 1024.0)), 1,
											vmUuid, vmName);
							String hostAddress = getHostAddress(hostuuid);
							int port = getVNCPort(vmUuid, poolUuid);
							NoVNC.createToken(vmUuid.substring(0, 8),
									hostAddress, port);
							jo.put("isSuccess", true);
						}
					}
				}
			} catch (Exception e) {
				this.getVmDAO().removeVM(userId, vmUuid);
				jo.put("isSuccess", false);
			}
		}
		return jo;
	}

	/**
	 * 获取用户主机列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getVMList(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getVmDAO().countVMs(userId, search);
		List<OCVM> VMList = this.getVmDAO().getOnePageVMs(userId, page, limit,
				search);
		ja.put(totalNum);
		if (VMList != null) {
			for (int i = 0; i < VMList.size(); i++) {
				JSONObject jo = new JSONObject();
				OCVM ocvm = VMList.get(i);
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
				String publicip = this.getEipDAO().getEipIp(ocvm.getVmUuid());
				if (publicip == null) {
					jo.put("publicip", "");
				} else {
					jo.put("publicip", publicip);
				}
				if (ocvm.getBackupDate() == null) {
					jo.put("backupdate", "");
				} else {
					String timeUsed = Utilities.encodeText(Utilities
							.dateToUsed(ocvm.getBackupDate()));
					jo.put("backupdate", timeUsed);
				}
				String timeUsed = Utilities.encodeText(Utilities
						.dateToUsed(ocvm.getCreateDate()));
				jo.put("createdate", timeUsed);
				ja.put(jo);
			}
		}
		return ja;
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

	public JSONArray getVMsOfUser(int userId, int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getVmDAO().countVMs(userId, search);
		ja.put(totalNum);
		List<OCVM> vmList = this.getVmDAO().getOnePageVMs(userId, page, limit,
				search);
		if (vmList != null) {
			for (int i = 0; i < vmList.size(); i++) {
				JSONObject jo = new JSONObject();
				OCVM ocvm = vmList.get(i);
				jo.put("vmid", ocvm.getVmUuid());
				jo.put("vmname", Utilities.encodeText(ocvm.getVmName()));
				jo.put("vmip", ocvm.getVmIP());
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray getBasicNetworkList(int userId) {
		JSONArray ja = new JSONArray();
		List<Object[]> vmList = this.getVmDAO().getBasicNetworkList(userId);
		if (vmList != null) {
			for (Object[] item : vmList) {
				JSONObject itemjo = new JSONObject();
				itemjo.put("vmid", item[0]);
				itemjo.put("vmname", Utilities.encodeText((String) item[1]));
				itemjo.put("vmip", item[2]);
				ja.put(itemjo);
			}
		}
		return ja;
	}

	/**
	 * 获取主机详细信息
	 * 
	 * @param vmUuid
	 * @return
	 */
	public JSONObject getVMDetail(String vmUuid) {
		JSONObject jo = new JSONObject();
		OCVM ocvm = this.getVmDAO().getVM(vmUuid);
		if (ocvm != null) {
			jo.put("instanceName", Utilities.encodeText(ocvm.getVmName()));
			jo.put("instanceDesc", (ocvm.getVmDesc() == null) ? "&nbsp;"
					: Utilities.encodeText(ocvm.getVmDesc()));
			jo.put("instanceState", ocvm.getVmPower());
			jo.put("createDate", Utilities.formatTime(ocvm.getCreateDate()));
			String timeUsed = Utilities.encodeText(Utilities.dateToUsed(ocvm
					.getCreateDate()));
			List<String> volList = this.getVolumeDAO().getVolumesOfVM(vmUuid);
			if (volList == null || volList.size() == 0) {
				jo.put("volList", "&nbsp;");
			} else {
				jo.put("volList", volList);
			}
			if (ocvm.getBackupDate() == null) {
				jo.put("backupdate", "");
			} else {
				String tUsed = Utilities.encodeText(Utilities.dateToUsed(ocvm
						.getBackupDate()));
				jo.put("backupdate", tUsed);
			}
			jo.put("useDate", timeUsed);
			jo.put("instanceCPU", ocvm.getVmCpu());
			jo.put("instanceMemory", ocvm.getVmMem());
			jo.put("instancevlan", ocvm.getVmVlan());
			String fw = ocvm.getVmFirewall();
			if (fw == null) {
				jo.put("instanceFirewall", "");
				jo.put("instanceFirewallName", "");
			} else {
				jo.put("instanceFirewall", fw);
				jo.put("instanceFirewallName",
						Utilities.encodeText(this.getFirewallDAO()
								.getFirewall(fw).getFirewallName()));
			}
			String ip = ocvm.getVmIP();
			if (ip == null) {
				jo.put("instanceIP", "null");
			} else {
				jo.put("instanceIP", ip);
			}
			jo.put("instanceMAC", ocvm.getVmMac());
			String eip = this.getEipDAO().getEipIp(vmUuid);
			String eipUuid = this.getEipDAO().getEipId(eip);
			if (eip == null) {
				jo.put("instanceEip", "null");
				jo.put("instanceEipUuid", "null");
			} else {
				jo.put("instanceEip", eip);
				jo.put("instanceEipUuid", eipUuid);
			}
			String vlan = ocvm.getVmVlan();
			if (vlan == null) {
				jo.put("vlan", "null");
			} else {
				Vnet vnet = this.getVnetDAO().getVnet(vlan);
				jo.put("vlan", vnet.getVnetName());
				jo.put("vlanUuid", vnet.getVnetUuid());
				jo.put("routerUuid", vnet.getVnetRouter());
			}
		}
		return jo;
	}

	public String getQuota(int userId, int userLevel, int count) {
		String result = "ok";
		if (userLevel != 0) {
			int free = this.getQuotaDAO().getQuotaTotal(userId).getQuotaVM()
					- this.getQuotaDAO().getQuotaUsed(userId).getQuotaVM();
			if (free < count) {
				result = String.valueOf(free);
			}
		}
		return result;
	}

	public boolean syncAddVMOperate(String hostUuid, String vmUuid, int power) {
		int userId = this.getVmDAO().getVM(vmUuid).getVmUID();
		boolean result = this.getVmDAO()
				.updateVMStatus(vmUuid, hostUuid, power);
		if (result) {
			if (power == 1) {
				String hostAddress = getHostAddress(hostUuid);
				String poolUuid = this.getHostDAO().getHost(hostUuid)
						.getPoolUuid();
				int port = this.getVNCPort(vmUuid, poolUuid);
				NoVNC.createToken(vmUuid.substring(0, 8), hostAddress, port);
				this.getMessagePush().editRowStatus(userId, vmUuid, "running",
						"正常运行");
				this.getMessagePush().editRowConsole(userId, vmUuid, "add");
			} else {
				NoVNC.deleteToken(vmUuid.substring(0, 8));
				this.getMessagePush().editRowStatus(userId, vmUuid, "stopped",
						"已关机");
				this.getMessagePush().editRowConsole(userId, vmUuid, "del");
			}
		}
		return result;
	}

	public void syncDelVMOperate(String vmUuid, String hostUuid) {
		OCVM vm = this.getVmDAO().getVM(vmUuid);
		if (vm != null && vm.getVmStatus() != 0) {
			if (hostUuid.equals(vm.getHostUuid())) {
				this.getVmDAO().removeVM(vm.getVmUID(), vmUuid);
				this.getFeeDAO().destoryVM(new Date(), vmUuid);
				NoVNC.deleteToken(vmUuid.substring(0, 8));
				this.getMessagePush().deleteRow(vm.getVmUID(), vmUuid);
			}
		}
	}

	public void syncUpdateVM(String vmUuid, int powerAttrvalue) {
		OCVM ocvm = this.getVmDAO().getVM(vmUuid);
		if (ocvm != null) {
			int userId = ocvm.getVmUID();
			String poolUuid = this.getUserDAO().getUser(userId)
					.getUserAllocate();
			if (powerAttrvalue == 1 && ocvm.getVmPower() == 0) {
				this.getVmDAO().updatePowerStatus(vmUuid, powerAttrvalue);
				String hostAddress = this.getHostAddress(ocvm.getHostUuid());
				int port = this.getVNCPort(vmUuid, poolUuid);
				NoVNC.createToken(vmUuid.substring(0, 8), hostAddress, port);
				this.getMessagePush().editRowStatus(userId, vmUuid, "running",
						"正常运行");
				this.getMessagePush().editRowConsole(userId, vmUuid, "add");
			} else if (powerAttrvalue == 0 && ocvm.getVmPower() == 1) {
				this.getVmDAO().updatePowerStatus(vmUuid, powerAttrvalue);
				NoVNC.deleteToken(vmUuid.substring(0, 8));
				this.getMessagePush().editRowStatus(userId, vmUuid, "stopped",
						"已关机");
				this.getMessagePush().editRowConsole(userId, vmUuid, "del");
			}
		}
	}

	public void updateImportance(String uuid, int importance) {
		this.getVmDAO().updateVMImportance(uuid, importance);
	}

	public void assginIpAddress(Connection c, String url, String subnet,
			String vnUuid) {
		List<OCVM> vmList = this.getVmDAO().getVMsOfVnet(vnUuid);
		if (vmList != null) {
			try {
				for (OCVM vm : vmList) {
					String mac = vm.getVmMac();
					String vnetIp = Host.assignIpAddress(c, url, mac, subnet);
					vm.setVmIP(vnetIp);
					this.getVmDAO().updateVM(vm);
					this.restartNetwork(c, vm.getVmUuid(), false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean restartNetwork(Connection c, String vmuuid, boolean sync) {
		boolean result = false;
		try {
			VM temVM = VM.getByUuid(c, vmuuid);
			JSONObject temjo = new JSONObject();
			temjo.put("requestType", "Agent.RestartNetwork");
			temVM.sendRequestViaSerial(c, temjo.toString(), sync);
		} catch (BadServerResponse e) {
			e.printStackTrace();
		} catch (XenAPIException e) {
			e.printStackTrace();
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean assginIpAddressToVM(Connection c, String url, String subnet,
			OCVM vm) {
		boolean result = false;
		if (vm != null) {
			try {
				String mac = vm.getVmMac();
				String vnetIp = Host.assignIpAddress(c, url, mac, subnet);
				vm.setVmIP(vnetIp);
				this.getVmDAO().updateVM(vm);
				this.restartNetwork(c, vm.getVmUuid(), false);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void unAssginIpAddress(Connection c, String vnUuid) {
		List<OCVM> vmList = this.getVmDAO().getVMsOfVnet(vnUuid);
		if (vmList != null) {
			try {
				for (OCVM vm : vmList) {
					this.restartNetwork(c, vm.getVmUuid(), true);
					vm.setVmIP(null);
					this.getVmDAO().updateVM(vm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public JSONObject unbindNet(String uuid, User user, String content, String conid) {
		JSONObject jo = new JSONObject();
		OCVM vm = this.getVmDAO().getVM(uuid);
		String vlan = vm.getVmVlan();
		boolean result = false;
		Connection conn = null;
		if (vlan == null) {
			String eip = this.getEipDAO().getEipIp(uuid);
			if (eip != null) {
				JSONObject unbind = this.eipManager.unbindElasticIp(
						user.getUserId(), uuid, eip, "vm");
				if (unbind.getBoolean("result")) {
					result = this.setVlan(uuid, 1, user.getUserAllocate());
				} else {
					result = false;
				}
			} else {
				result = this.setVlan(uuid, 1, user.getUserAllocate());
			}
		} else {
			result = this.setVlan(uuid, 1, user.getUserAllocate());
		}
		if (result) {
			try {
				conn = this.getConstant().getConnectionFromPool(
						user.getUserAllocate());
				this.restartNetwork(conn, uuid, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jo.put("result", result);
		if (result) {
			this.getVmDAO().unbindNet(uuid);
			this.getMessagePush().pushMessageClose(user.getUserId(), content, conid);
			this.getMessagePush().pushMessage(user.getUserId(),
					Utilities.stickyToSuccess("主机解绑网络成功"));
		} else {
			this.getMessagePush().pushMessageClose(user.getUserId(), content, conid);
			this.getMessagePush().pushMessage(user.getUserId(),
					Utilities.stickyToError("主机解绑网络失败"));
		}
		return jo;
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

	public void createVM(String vmUuid, String tplUuid, int userId,
			String vmName, int cpuCore, double memorySize, String pwd,
			String poolUuid, String vnetuuid) {
		Date startTime = new Date();
		int memoryCapacity = (int) (memorySize * 1024);
		JSONObject jo = doCreateVM(vmUuid, tplUuid, userId, vmName, cpuCore,
				memoryCapacity, pwd, poolUuid, vnetuuid);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.主机.toString(),
				"i-" + vmUuid.substring(0, 8)));
		infoArray.put(Utilities.createLogInfo("配置", cpuCore + " 核， "
				+ memorySize + " GB"));
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.映像.toString(),
				"image-" + tplUuid.substring(0, 8)));
		if (jo.getBoolean("isSuccess") == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.主机.ordinal(),
					LogConstant.logAction.创建.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().editRowStatus(userId, vmUuid, "running",
					"正常运行");
			if (vnetuuid.equals("0")) {
				this.getMessagePush().editRowIP(userId, vmUuid, "基础网络",
						jo.getString("ip"));
			} else {
				this.getMessagePush().editRowIP(userId, vmUuid,
						jo.getString("vname"), jo.getString("ip"));
			}
			this.getMessagePush().editRowConsole(userId, vmUuid, "add");
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			infoArray.put(Utilities.createLogInfo("原因", jo.getString("error")));
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

	public void createVMByISO(String vmUuid, String isoUuid, String srUuid,
			String name, int cpu, int memory, int volumeSize, String poolUuid,
			int userId) {
		int memoryCapacity = (int) (memory * 1024);
		Date startTime = new Date();
		boolean result = doCreateVMByISO(vmUuid, isoUuid, srUuid, name, cpu,
				memoryCapacity, volumeSize, poolUuid);
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
			String poolUuid) {
		Connection conn = null;
		boolean result = false;
		boolean preCreate = false;
		boolean dbRollback = true;
		try {
			preCreate = this.getVmDAO().preCreateVM(vmUuid, null, 1, name,
					1, null, memory, cpu, VMManager.POWER_CREATE, 1,
					new Date());
			if (preCreate) {
				conn = this.getConstant().getConnectionFromPool(poolUuid);
				String hostUuid = this.getAllocateHost(poolUuid, memory);
				// Get Disk
				SR sr = SR.getByUuid(conn, srUuid);
				String type = sr.getType(conn);
				// Create VM By ISO
				VM.Record record = VM.createVMFromISO(vmUuid,
						"i-" + vmUuid.substring(0, 8), cpu, memory, conn,
						hostUuid, isoUuid, volumeSize, srUuid, type);
				if (record != null) {
					result = this.getVmDAO().updateVM(1, vmUuid, "onceas",
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
				this.getVmDAO().removeVM(1, vmUuid);
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
					VIF.Record record = vif.getRecord(conn);
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
			this.getDhcpDAO().saveVM(dhcp);
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
	
	public int getCount(int userId) {
		return this.getVmDAO().countVMsOfUser(userId);
	}
}
