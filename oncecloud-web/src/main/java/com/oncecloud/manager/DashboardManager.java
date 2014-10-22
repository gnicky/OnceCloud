package com.oncecloud.manager;

import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.HostDAO;
import com.oncecloud.dao.PositionDAO;
import com.oncecloud.dao.ServerPortDAO;
import com.oncecloud.dao.StorageDAO;
import com.oncecloud.dao.SwPortDAO;
import com.oncecloud.dao.SwitchDAO;
import com.oncecloud.dao.VlanDAO;
import com.oncecloud.entity.OCHost;
import com.oncecloud.entity.Position;
import com.oncecloud.entity.ServerPort;
import com.oncecloud.entity.Storage;
import com.oncecloud.entity.SwPort;
import com.oncecloud.entity.Switch;
import com.oncecloud.entity.Vlan;

/**
 * @author cyh
 * @version 2014/04/04
 */
@Component
public class DashboardManager {

	private SwitchDAO switchDAO;
	private VlanDAO vlanDAO;
	private SwPortDAO swPortDAO;
	private ServerPortDAO serverPortDAO;
	private HostDAO hostDAO;
	private PositionDAO positionDAO;
	private StorageDAO storageDAO;

	private SwitchDAO getSwitchDAO() {
		return switchDAO;
	}

	@Autowired
	private void setSwitchDAO(SwitchDAO switchDAO) {
		this.switchDAO = switchDAO;
	}

	private VlanDAO getVlanDAO() {
		return vlanDAO;
	}

	@Autowired
	private void setVlanDAO(VlanDAO vlanDAO) {
		this.vlanDAO = vlanDAO;
	}

	private SwPortDAO getSwPortDAO() {
		return swPortDAO;
	}

	@Autowired
	private void setSwPortDAO(SwPortDAO swPortDAO) {
		this.swPortDAO = swPortDAO;
	}

	private ServerPortDAO getServerPortDAO() {
		return serverPortDAO;
	}

	@Autowired
	private void setServerPortDAO(ServerPortDAO serverPortDAO) {
		this.serverPortDAO = serverPortDAO;
	}

	private HostDAO getHostDAO() {
		return hostDAO;
	}

	@Autowired
	private void setHostDAO(HostDAO hostDAO) {
		this.hostDAO = hostDAO;
	}

	private PositionDAO getPositionDAO() {
		return positionDAO;
	}

	@Autowired
	private void setPositionDAO(PositionDAO positionDAO) {
		this.positionDAO = positionDAO;
	}

	private StorageDAO getStorageDAO() {
		return storageDAO;
	}

	@Autowired
	private void setStorageDAO(StorageDAO storageDAO) {
		this.storageDAO = storageDAO;
	}

	public JSONArray getSwitchList(String rackid) {
		JSONArray jsonSwitchArray = new JSONArray();
		List<Switch> switchs = this.getSwitchDAO().getSwitchOfRack(rackid);
		for (Switch sw : switchs) {
			JSONObject swobject = new JSONObject();
			swobject.put("SwitchObj", sw.toJsonString());
			JSONArray jsonVlanArray = new JSONArray();
			List<Vlan> vlans = this.getVlanDAO()
					.getVlanOfSwitch(sw.getSwUuid());
			for (Vlan vlan : vlans) {
				JSONObject vlanobject = new JSONObject();
				vlanobject.put("vlanObj", vlan.toJsonString());
				JSONArray jsonPortArray = new JSONArray();
				List<SwPort> SwPorts = this.getSwPortDAO().getSwPortOfVlan(
						vlan.getVlanUuid());
				for (SwPort swport : SwPorts) {
					JSONObject portobject = new JSONObject();
					portobject.put("portObj", swport.toJsonString());
					if (swport.getPortUsing() == 1) {
						List<ServerPort> serverport = this.getServerPortDAO()
								.getServerPortOfSwPort(
										swport.getServerportUuid());
						portobject.put("serverportObj", serverport.get(0)
								.toJsonString());
						portobject
								.put("hostObj",
										this.getHostDAO()
												.getHost(
														serverport.get(0)
																.getHostUuid())
												.toJsonString());
					} else {
						portobject.put("serverportObj", "");
						portobject.put("hostObj", "");
					}
					jsonPortArray.put(portobject);
				}
				vlanobject.put("portlist", jsonPortArray);
				jsonVlanArray.put(vlanobject);
			}
			swobject.put("vlanlist", jsonVlanArray);
			jsonSwitchArray.put(swobject);
		}
		return jsonSwitchArray;
	}

	public JSONArray getSwitch(String switchid) {
		JSONArray jsonSwitchArray = new JSONArray();
		List<Switch> switchs = this.getSwitchDAO().getSwitch(switchid);
		System.out.print(switchid);
		System.out.print(switchs.size());
		for (Switch sw : switchs) {
			JSONObject swobject = new JSONObject();
			swobject.put("SwitchObj", sw.toJsonString());
			JSONArray jsonVlanArray = new JSONArray();
			List<Vlan> vlans = this.getVlanDAO()
					.getVlanOfSwitch(sw.getSwUuid());
			for (Vlan vlan : vlans) {
				JSONObject vlanobject = new JSONObject();
				vlanobject.put("vlanObj", vlan.toJsonString());
				JSONArray jsonPortArray = new JSONArray();
				List<SwPort> SwPorts = this.getSwPortDAO().getSwPortOfVlan(
						vlan.getVlanUuid());
				for (SwPort swport : SwPorts) {
					JSONObject portobject = new JSONObject();
					portobject.put("portObj", swport.toJsonString());
					if (swport.getPortUsing() == 1) {
						List<ServerPort> serverport = this.getServerPortDAO()
								.getServerPortOfSwPort(
										swport.getServerportUuid());
						portobject.put("serverportObj", serverport.get(0)
								.toJsonString());
						portobject
								.put("hostObj",
										this.getHostDAO()
												.getHost(
														serverport.get(0)
																.getHostUuid())
												.toJsonString());
					} else {
						portobject.put("serverportObj", "");
						portobject.put("hostObj", "");
					}
					jsonPortArray.put(portobject);
				}
				vlanobject.put("portlist", jsonPortArray);
				jsonVlanArray.put(vlanobject);
			}
			swobject.put("vlanlist", jsonVlanArray);
			jsonSwitchArray.put(swobject);
		}
		return jsonSwitchArray;
	}

	public JSONArray getTuoputu(String rackid) {
		Random rand = new Random();
		JSONArray jsonrackArray = new JSONArray();
		JSONObject rackobject = new JSONObject();
		JSONArray jsonSwitchArray = new JSONArray();
		List<Switch> switchs = this.getSwitchDAO().getSwitchOfRack(rackid);
		JSONArray jsonConnectArray = new JSONArray();
		JSONArray jsonHostArray = new JSONArray();
		JSONArray jsonStorageArray = new JSONArray();
		for (Switch sw : switchs) {
			JSONObject swobject = new JSONObject();
			swobject.put("SwitchObj", sw.toJsonString());
			Position positin = this.getPositionDAO()
					.getPosition(sw.getSwUuid());
			swobject.put("SwitchX", positin != null ? positin.getPositionX()
					: rand.nextInt(700));
			swobject.put("SwitchY", positin != null ? positin.getPositionY()
					: rand.nextInt(450));
			jsonSwitchArray.put(swobject);
			List<Vlan> vlans = this.getVlanDAO()
					.getVlanOfSwitch(sw.getSwUuid());
			for (Vlan vlan : vlans) {
				List<SwPort> SwPorts = this.getSwPortDAO().getSwPortOfVlan(
						vlan.getVlanUuid());
				for (SwPort swport : SwPorts) {
					if (swport.getPortUsing() == 1) {
						JSONObject connectbject = new JSONObject();
						connectbject.put("startObj", sw.getSwUuid());
						connectbject.put("portObj", swport.toJsonString());
						List<ServerPort> serverport = this.getServerPortDAO()
								.getServerPortOfSwPort(
										swport.getServerportUuid());
						connectbject.put("serverportObj", serverport.get(0)
								.toJsonString());
						connectbject
								.put("endObj",
										this.getHostDAO()
												.getHost(
														serverport.get(0)
																.getHostUuid())
												.getHostUuid());
						jsonConnectArray.put(connectbject);
					}
				}
			}
		}
		List<Storage> srlist = this.getStorageDAO()
				.getStorageListOfRack(rackid);
		if (srlist != null) {
			for (Storage sr : srlist) {
				JSONObject srobj = new JSONObject();
				Position positin = this.getPositionDAO().getPosition(
						sr.getSrUuid());
				srobj.put("srobj", sr.toJsonString());
				srobj.put("srX", positin != null ? positin.getPositionX()
						: rand.nextInt(700));
				srobj.put("srY", positin != null ? positin.getPositionY()
						: rand.nextInt(450));
				jsonStorageArray.put(srobj);
			}
		}
		List<OCHost> serverList = this.getHostDAO().getHostListOfRack(rackid);
		for (OCHost host : serverList) {
			JSONObject thost = new JSONObject();
			Position positin = this.getPositionDAO().getPosition(
					host.getHostUuid());
			thost.put("hostobj", host.toJsonString());
			thost.put(
					"hostX",
					positin != null ? positin.getPositionX() : rand
							.nextInt(700));
			thost.put(
					"hostY",
					positin != null ? positin.getPositionY() : rand
							.nextInt(450));
			jsonHostArray.put(thost);
		}
		rackobject.put("switchlist", jsonSwitchArray);
		rackobject.put("connectlist", jsonConnectArray);
		rackobject.put("hostlist", jsonHostArray);
		rackobject.put("storagelist", jsonStorageArray);
		jsonrackArray.put(rackobject);
		return jsonrackArray;
	}
}
