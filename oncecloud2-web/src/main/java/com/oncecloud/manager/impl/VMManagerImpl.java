package com.oncecloud.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FeeDAO;
import com.oncecloud.dao.FirewallDAO;
import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VnetDAO;
import com.oncecloud.dao.VolumeDAO;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.Database;
import com.oncecloud.entity.Image;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.entity.Vnet;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.NoVNC;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.VMManager;
import com.oncecloud.message.MessagePush;

@Component
public class VMManagerImpl implements VMManager {
	private final static Logger logger = Logger.getLogger(VMManager.class);
	private final static long MB = 1024 * 1024;
	
	private VMDAO vmDAO;
	private LogDAO logDAO;
	private VnetDAO vnetDAO;
	private EIPDAO eipDAO;
	private ImageDAO imageDAO;
	private DHCPDAO dhcpDAO;
	private QuotaDAO quotaDAO;
	private FirewallDAO firewallDAO;
	private FeeDAO feeDAO;
	private HostDAO hostDAO;
	private RouterDAO routerDAO;
	private VolumeDAO volumeDAO;
	
	private MessagePush messagePush;
	
	private Constant constant;
	
	public VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	public void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	public LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public VnetDAO getVnetDAO() {
		return vnetDAO;
	}

	@Autowired
	public void setVnetDAO(VnetDAO vnetDAO) {
		this.vnetDAO = vnetDAO;
	}

	public EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	public void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	public ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	public DHCPDAO getDhcpDAO() {
		return dhcpDAO;
	}

	@Autowired
	public void setDhcpDAO(DHCPDAO dhcpDAO) {
		this.dhcpDAO = dhcpDAO;
	}

	public QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	public void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public FirewallDAO getFirewallDAO() {
		return firewallDAO;
	}

	@Autowired
	public void setFirewallDAO(FirewallDAO firewallDAO) {
		this.firewallDAO = firewallDAO;
	}

	public FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	public void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	public MessagePush getMessagePush() {
		return messagePush;
	}

	public HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	public void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	public RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	public void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	public VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	public void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	@Autowired
	public void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public Constant getConstant() {
		return constant;
	}

	@Autowired
	public void setConstant(Constant constant) {
		this.constant = constant;
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
	
	private Record createVMOnHost(Connection c, String vmUuid, String tplUuid,
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
	
	public String getAllocateHost(Connection conn, int memory) {
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
				allocateHost = getAllocateHost(c, memory);
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
							memory, cpu  , VMManager.POWER_CREATE, 1, createDate);
					this.getQuotaDAO().updateQuota(userId, "quotaVM", 1, true);
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
								String firewallId = this.getFirewallDAO()
										.getDefaultFirewall(userId).getFirewallId();
								this.getVmDAO().updateVM(userId, vmUuid, pwd,
										VMManager.POWER_RUNNING, hostuuid, ip, firewallId);
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
					this.getQuotaDAO().updateQuota(userId, "quotaVM", 1, false);
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
			allocateHost = getAllocateHost(c, memory);
			logger.info("VM [" + vmBackendName + "] allocated to Host ["
					+ allocateHost + "]");
			try {
				boolean preCreate = this.getVmDAO().preCreateVM(vmUuid, pwd,
						userId, vmName, image.getImagePlatform(), mac, memory,
						cpu, VMManager.POWER_CREATE, 1, createDate);
				this.getQuotaDAO().updateQuota(userId, "quotaVM", 1, true);
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
				this.getQuotaDAO().updateQuota(userId, "quotaVM", 1, false);
				jo.put("isSuccess", false);
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
	
	private int bindVlan(String uuid, String vlan, Connection c) {
		int vlanId = -1;
		if (vlan != null) {
			try {
				vlanId = this.getVnetDAO().getVnet(vlan).getVnetID();
			} catch (Exception e) {
			}
		}
		setVlan(uuid, vlanId, c);
		return vlanId;
	}
	
	public boolean setVlan(String uuid, int vlanId, Connection c) {
		try {
			VM vm = VM.getByUuid(c, uuid);
			vm.setTag(c, vm.getVIFs(c).iterator().next(),
					String.valueOf(vlanId));
			return true;
		} catch (Exception e) {
			return false;
		}
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
		Connection c = null;
		try {
			currentVM = this.getVmDAO().getVM(uuid);
			if (currentVM != null) {
				boolean preRestartVM = this.getVmDAO().updatePowerStatus(uuid,
						VMManager.POWER_RESTART);
				if (preRestartVM == true) {
					c = this.getConstant().getConnectionFromPool(
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
				int vlan = bindVlan(uuid, currentVM.getVmVlan(), c);
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
	
	private JSONObject unbindElasticIp(Connection c, String uuid, String eipIp) {
		JSONObject result = new JSONObject();
		result.put("result", false);
		try {
			String ip = null;
			String eif = this.getEipDAO().getEip(eipIp).getEipInterface();
			OCVM vm = this.getVmDAO().getVM(uuid);
			ip = vm.getVmIP();
			boolean deActiveResult =  deActiveFirewall(c, ip);
			if (deActiveResult) {
				if (Host.unbindOuterIp(c, ip, eipIp, eif)) {
					this.getEipDAO().unBindEip(eipIp);
					result.put("result", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean deActiveFirewall(Connection c, String ip) {
		boolean result = false;
		try {
			JSONObject total = new JSONObject();
			JSONArray ipArray = new JSONArray();
			ipArray.put(ip);
			JSONArray ruleArray = new JSONArray();
			total.put("IP", ipArray);
			total.put("rules", ruleArray);
			result = Host.firewallApplyRule(c, total.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
					unbindElasticIp(c, uuid, publicip);
				}
				Date endDate = new Date();
				this.getFeeDAO().destoryVM(endDate, uuid);
				NoVNC.deleteToken(uuid.substring(0, 8));
				this.getVmDAO().removeVM(userId, uuid);
				this.getQuotaDAO().updateQuota(userId, "quotaVM", 1, false);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
