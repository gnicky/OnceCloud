package com.oncecloud.manager.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.PerformanceDAO;
import com.oncecloud.entity.performance.Cpu;
import com.oncecloud.entity.performance.Memory;
import com.oncecloud.entity.performance.Pif;
import com.oncecloud.entity.performance.Vbd;
import com.oncecloud.entity.performance.Vif;
import com.oncecloud.manager.PerformanceManger;

@Component("PerformanceManger")
public class PerformanceMangerImpl implements PerformanceManger {
	private PerformanceDAO performanceDAO;

	private PerformanceDAO getPerformanceDAO() {
		return performanceDAO;
	}

	@Autowired
	private void setPerformanceDAO(PerformanceDAO performanceDAO) {
		this.performanceDAO = performanceDAO;
	}

	public JSONObject getCpu(String uuid, String type) {
		List<Cpu> cpuList = null;
		if (type.equals("sixhours")) {
			cpuList = this.getPerformanceDAO().getCpuList4Sixhours(uuid);
		} else if (type.equals("oneday")) {
			cpuList = this.getPerformanceDAO().getCpuList4Oneday(uuid);
		} else if (type.equals("twoweeks")) {
			cpuList = this.getPerformanceDAO().getCpuList4Twoweeks(uuid);
		} else if (type.equals("onemonth")) {
			cpuList = this.getPerformanceDAO().getCpuList4Onemonth(uuid);
		} else if (type.equals("thirtymin")) {
			cpuList = this.getPerformanceDAO().getCpuList(uuid);
		}
		JSONObject cpuPicData = new JSONObject();
		Set<Long> timeset = new HashSet<Long>();
		if (cpuList != null) {
			for (Cpu cpu : cpuList) {
				String cpuId = cpu.getCpuId().toString();
				JSONArray ja = null;
				long cpuTime = cpu.getTime();
				double cpuUsage = cpu.getUsage();
				if (timeset.contains(cpuTime)) {
				} else {
					timeset.add(cpuTime);
				}
				JSONObject jo = new JSONObject();
				jo.put("times", cpuTime);
				jo.put("usage", cpuUsage * 100);
				if (cpuPicData.has(cpuId)) {
					ja = cpuPicData.getJSONArray(cpuId);
					ja.put(jo);
				} else {
					ja = new JSONArray();
					ja.put(jo);
					cpuPicData.put(cpuId, ja);
				}
			}
		}
		return cpuPicData;
	}

	public JSONArray getMemory(String uuid, String type) {
		List<Memory> memoryList = null;
		if (type.equals("sixhours")) {
			memoryList = this.getPerformanceDAO().getMemoryList4Sixhours(uuid);
		} else if (type.equals("oneday")) {
			memoryList = this.getPerformanceDAO().getMemoryList4Oneday(uuid);
		} else if (type.equals("twoweeks")) {
			memoryList = this.getPerformanceDAO().getMemoryList4Twoweeks(uuid);
		} else if (type.equals("onemonth")) {
			memoryList = this.getPerformanceDAO().getMemoryList4Onemonth(uuid);
		} else if (type.equals("thirtymin")) {
			memoryList = this.getPerformanceDAO().getMemoryList(uuid);
		}
		JSONArray memoryPicData = new JSONArray();
		if (memoryList != null) {
			for (Memory memory : memoryList) {
				Double totalMemory = memory.getTotalSize() / 1024 / 1024;
				Double freeMemory = memory.getFreeSize() / 1024 / 1024;
				Double usedMemory = totalMemory - freeMemory;
				long memoryTime = memory.getTime();
				JSONObject jo = new JSONObject();
				jo.put("times", memoryTime);
				jo.put("total", totalMemory);
				jo.put("used", usedMemory);
				memoryPicData.put(jo);
			}
		}
		return memoryPicData;
	}

	public JSONObject getVbd(String uuid, String type) {
		List<Vbd> vbdList = null;
		if (type.equals("sixhours")) {
			vbdList = this.getPerformanceDAO().getVbdList4Sixhours(uuid);
		} else if (type.equals("oneday")) {
			vbdList = this.getPerformanceDAO().getVbdList4Oneday(uuid);
		} else if (type.equals("twoweeks")) {
			vbdList = this.getPerformanceDAO().getVbdList4Twoweeks(uuid);
		} else if (type.equals("onemonth")) {
			vbdList = this.getPerformanceDAO().getVbdList4Onemonth(uuid);
		} else if (type.equals("thirtymin")) {
			vbdList = this.getPerformanceDAO().getVbdList(uuid);
		}
		JSONObject vbdPicData = new JSONObject();
		if (vbdList != null) {
			for (Vbd vbd : vbdList) {
				String vbdId = vbd.getVbdId();
				long vbdTime = vbd.getTime();
				Double vbdRead = vbd.getRead();
				Double vbdWrite = vbd.getWrite();
				JSONArray ja = null;
				JSONObject jo = new JSONObject();
				jo.put("times", vbdTime);
				jo.put("read", vbdRead);
				jo.put("write", vbdWrite);
				if (vbdPicData.has(vbdId)) {
					ja = vbdPicData.getJSONArray(vbdId);
					ja.put(jo);
				} else {
					ja = new JSONArray();
					ja.put(jo);
					vbdPicData.put(vbdId, ja);
				}
			}
		}
		return vbdPicData;
	}

	public JSONObject getVif(String uuid, String type) {
		List<Vif> vifList = null;
		if (type.equals("sixhours")) {
			vifList = this.getPerformanceDAO().getVifList4Sixhours(uuid);
		} else if (type.equals("oneday")) {
			vifList = this.getPerformanceDAO().getVifList4Oneday(uuid);
		} else if (type.equals("twoweeks")) {
			vifList = this.getPerformanceDAO().getVifList4Twoweeks(uuid);
		} else if (type.equals("onemonth")) {
			vifList = this.getPerformanceDAO().getVifList4Onemonth(uuid);
		} else if (type.equals("thirtymin")) {
			vifList = this.getPerformanceDAO().getVifList(uuid);
		}
		JSONObject vifPicData = new JSONObject();
		if (vifList != null) {
			for (Vif vif : vifList) {
				String vifId = vif.getVifId().toString();
				long vifTime = vif.getTime();
				Double vifTx = vif.getTxd();
				Double vifRx = vif.getRxd();
				JSONArray ja = null;
				JSONObject jo = new JSONObject();
				jo.put("times", vifTime);
				jo.put("rx", vifRx);
				jo.put("tx", vifTx);
				if (vifPicData.has(vifId)) {
					ja = vifPicData.getJSONArray(vifId);
					ja.put(jo);
				} else {
					ja = new JSONArray();
					ja.put(jo);
					vifPicData.put(vifId, ja);
				}
			}
		}
		return vifPicData;
	}

	public JSONObject getPif(String uuid, String type) {
		List<Pif> pifList = null;
		if (type.equals("sixhours")) {
			pifList = this.getPerformanceDAO().getPifList4Sixhours(uuid);
		} else if (type.equals("oneday")) {
			pifList = this.getPerformanceDAO().getPifList4Oneday(uuid);
		} else if (type.equals("twoweeks")) {
			pifList = this.getPerformanceDAO().getPifList4Twoweeks(uuid);
		} else if (type.equals("onemonth")) {
			pifList = this.getPerformanceDAO().getPifList4Oneday(uuid);
		} else if (type.equals("thirtymin")) {
			pifList = this.getPerformanceDAO().getPifList(uuid);
		}
		JSONObject vifPicData = new JSONObject();
		if (pifList != null) {
			for (Pif pif : pifList) {
				String pifId = pif.getPifId().toString();
				long pifTime = pif.getTime();
				Double pifTx = pif.getTxd();
				Double pifRx = pif.getRxd();
				JSONArray ja = null;
				JSONObject jo = new JSONObject();
				jo.put("times", pifTime);
				jo.put("rx", pifRx);
				jo.put("tx", pifTx);
				if (vifPicData.has(pifId)) {
					ja = vifPicData.getJSONArray(pifId);
					ja.put(jo);
				} else {
					ja = new JSONArray();
					ja.put(jo);
					vifPicData.put(pifId, ja);
				}
			}
		}
		return vifPicData;
	}
}
