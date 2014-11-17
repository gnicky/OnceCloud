package com.oncecloud.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.oncecloud.dao.DHCPDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.LogDAO;
import com.oncecloud.dao.OverViewDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.DHCP;
import com.oncecloud.entity.EIP;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCLog;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.log.LogConstant;
import com.oncecloud.main.Constant;
import com.oncecloud.main.Utilities;
import com.oncecloud.manager.AddressManager;
import com.oncecloud.message.MessagePush;

@Component("AddressManager")
public class AddressManagerImpl implements AddressManager {
	private DHCPDAO dhcpDAO;
	private LogDAO logDAO;
	private VMDAO vmDAO;
	private UserDAO userDAO;
	private LBDAO lbDAO;
	private RouterDAO routerDAO;
	private EIPDAO eipDAO;
	private OverViewDAO overViewDAO;
	
	private MessagePush messagePush;
	
	private Constant constant;

	private DHCPDAO getDhcpDAO() {
		return dhcpDAO;
	}

	@Autowired
	private void setDhcpDAO(DHCPDAO dhcpDAO) {
		this.dhcpDAO = dhcpDAO;
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

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	private void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	public Constant getConstant() {
		return constant;
	}

	@Autowired
	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	public JSONArray addDHCPPool(int userId, String prefix, int start, int end) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		boolean result = false;
		List<DHCP> dhcpList = new ArrayList<DHCP>();
		JSONObject total = new JSONObject();
		JSONArray ipMacArray = new JSONArray();
		try {
			for (int i = start; i <= end; i++) {
				String currentIp = prefix + i;
				boolean check = this.getDhcpDAO().ipExist(currentIp);
				if (check == false) {
					DHCP dhcp = new DHCP();
					String mac = Utilities.randomMac();
					dhcp.setDhcpMac(mac);
					dhcp.setDhcpIp(currentIp);
					dhcp.setCreateDate(new Date());
					JSONObject ipMacObj = new JSONObject();
					ipMacObj.put("ipAddress", currentIp);
					ipMacObj.put("hardwareAddress", mac);
					ipMacArray.put(ipMacObj);
					dhcpList.add(dhcp);
				}
			}
			total.put("hosts", ipMacArray);
			Connection connection = this.getConstant().getConnection(userId);
			boolean bindResult = Host.bindIpMac(connection, total.toString());
			if (bindResult) {
				for (DHCP dhcp : dhcpList) {
					this.getDhcpDAO().saveDHCP(dhcp);
					this.getOverViewDAO().updateOverViewfield("viewDhcp", true);
				}
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo("起始", prefix + start));
		infoArray.put(Utilities.createLogInfo("结束", prefix + end));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.地址池.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.地址池.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray getDHCPList(int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getDhcpDAO().countAllDHCP(search);
		ja.put(totalNum);
		List<DHCP> dcList = this.getDhcpDAO().getOnePageDHCPList(page, limit,
				search);
		if (dcList != null) {
			for (DHCP dc : dcList) {
				JSONObject jo = new JSONObject();
				jo.put("dhcpip", dc.getDhcpIp());
				jo.put("dhcpmac", dc.getDhcpMac());
				jo.put("createdate", Utilities.formatTime(dc.getCreateDate()));
				String tenantUuid = dc.getTenantUuid();
				if (tenantUuid == null) {
					jo.put("tenantuuid", "");
					jo.put("depenType", -1);
					jo.put("tenantuser", "");
					jo.put("showid", "");
				} else {
					String tuser = "";
					jo.put("tenantuuid", tenantUuid);
					int depentype = dc.getDepenType();
					jo.put("depenType", depentype);
					boolean notExist = false;
					switch (depentype) {
					case 0:
						OCVM vm = this.getVmDAO().getAliveVM(tenantUuid);
						if (vm == null) {
							this.getDhcpDAO().returnDHCP(dc.getDhcpMac());
							notExist = true;
						} else {
							jo.put("showid", "i-" + tenantUuid.substring(0, 8));
							tuser = this.getUserDAO().getUser(vm.getVmUID())
									.getUserName();
						}
						break;
					case 1:
						LB lb = this.getLbDAO().getAliveLB(tenantUuid);
						if (lb == null) {
							this.getDhcpDAO().returnDHCP(dc.getDhcpMac());
							notExist = true;
						} else {
							jo.put("showid", "lb-" + tenantUuid.substring(0, 8));
							tuser = this.getUserDAO().getUser(lb.getLbUID())
									.getUserName();
						}
						break;
					/*case 2:
						Database db = this.getDatabaseDAO().getAliveDatabase(
								tenantUuid);
						if (db == null) {
							this.getDhcpDAO().returnDHCP(dc.getDhcpMac());
							notExist = true;
						} else {
							jo.put("showid", "db-" + tenantUuid.substring(0, 8));
							tuser = this.getUserDAO()
									.getUser(db.getDatabaseUID()).getUserName();
						}
						break;*/
					case 3:
						Router rt = this.getRouterDAO().getAliveRouter(
								tenantUuid);
						if (rt == null) {
							this.getDhcpDAO().returnDHCP(dc.getDhcpMac());
							notExist = true;
						} else {
							jo.put("showid", "rt-" + tenantUuid.substring(0, 8));
							tuser = this.getUserDAO()
									.getUser(rt.getRouterUID()).getUserName();
						}
						break;
					default:
						break;
					}
					if (notExist == false) {
						jo.put("tenantuser", tuser);
					} else {
						jo.put("tenantuuid", "");
						jo.put("depenType", -1);
						jo.put("tenantuser", "");
						jo.put("showid", "");
					}
				}
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray addPublicIP(int userId, String prefix, int start, int end,
			int eiptype, String eipInterface) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		boolean result = false;
		for (int i = start; i <= end; i++) {
			String currentIp = prefix + i;
			boolean check = this.getEipDAO().ipExist(currentIp);
			if (check == false) {
				EIP eip = new EIP();
				eip.setEipIp(currentIp);
				eip.setEipType(eiptype);
				eip.setEipUuid(UUID.randomUUID().toString());
				eip.setEipInterface(eipInterface);
				eip.setCreateDate(new Date());
				result = this.getEipDAO().saveEIP(eip);
				result = this.getOverViewDAO().updateOverViewfield("viewOutip", true);
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo("起始", prefix + start));
		infoArray.put(Utilities.createLogInfo("结束", prefix + end));
		if (result == true) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.添加.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray getPublicIPList(int page, int limit, String search) {
		JSONArray ja = new JSONArray();
		int totalNum = this.getEipDAO().countAllEipListNoUserid(search);
		ja.put(totalNum);
		List<EIP> eipList = this.getEipDAO().getOnePageEIPListNoUserid(page,
				limit, search);
		if (eipList != null) {
			for (EIP eip : eipList) {
				JSONObject jo = new JSONObject();
				jo.put("eip", eip.getEipIp());
				jo.put("euuid", eip.getEipUuid());
				jo.put("createdate", Utilities.formatTime(eip.getCreateDate()));
				jo.put("ename",
						eip.getEipName() == null ? "" : Utilities
								.encodeText(eip.getEipName()));
				jo.put("eipDependency", eip.getEipDependency() == null ? ""
						: Utilities.encodeText(eip.getEipDependency()));
				jo.put("depenType",
						eip.getDepenType() == null ? -1 : eip.getDepenType());
				jo.put("eipBandwidth", eip.getEipBandwidth());
				jo.put("eipDescription", eip.getEipDescription() == null ? ""
						: Utilities.encodeText(eip.getEipDescription()));
				jo.put("eipType", Utilities.encodeText(Constant.EIPType
						.values()[eip.getEipType()].toString()));
				jo.put("euid", eip.getEipUID());
				jo.put("eif", eip.getEipInterface());
				if (eip.getEipUID() != null && eip.getEipUID() > 0) {
					int eipuid = eip.getEipUID();
					jo.put("euername", this.getUserDAO().getUser(eipuid)
							.getUserName());
				} else {
					jo.put("euername", "");
				}
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray deleteDHCP(int userId, String ip, String mac) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		boolean result = this.getDhcpDAO().deleteDHCP(ip, mac);
		result = this.getOverViewDAO().updateOverViewfield("viewDhcp", false);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.地址池.toString(), ip));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.地址池.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.地址池.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}

	public JSONArray deletePublicIP(int userId, String ip, String uuid) {
		JSONArray ja = new JSONArray();
		Date startTime = new Date();
		boolean result = this.getEipDAO().deleteEIP(ip, uuid);
		result = this.getOverViewDAO().updateOverViewfield("viewOutip", false);
		JSONObject jo = new JSONObject();
		jo.put("result", result);
		ja.put(jo);
		// write log and push message
		Date endTime = new Date();
		int elapse = Utilities.timeElapse(startTime, endTime);
		JSONArray infoArray = new JSONArray();
		infoArray.put(Utilities.createLogInfo(
				LogConstant.logObject.公网IP.toString(), ip));
		if (result) {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.成功.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToSuccess(log.toString()));
		} else {
			OCLog log = this.getLogDAO().insertLog(userId,
					LogConstant.logObject.公网IP.ordinal(),
					LogConstant.logAction.删除.ordinal(),
					LogConstant.logStatus.失败.ordinal(), infoArray.toString(),
					startTime, elapse);
			this.getMessagePush().pushMessage(userId,
					Utilities.stickyToError(log.toString()));
		}
		return ja;
	}
}
