package com.oncecloud.manager;

import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.AlarmDAO;
import com.oncecloud.dao.AlarmRuleDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.entity.Alarm;
import com.oncecloud.entity.AlarmRule;
import com.oncecloud.entity.EIP;
import com.oncecloud.entity.LB;
import com.oncecloud.entity.OCVM;
import com.oncecloud.entity.Router;
import com.oncecloud.main.Utilities;

/**
 * @author hty
 * @version 2014/08/25
 */
@Component
public class AlarmManager {
	private AlarmDAO alarmDAO;
	private AlarmRuleDAO alarmRuleDAO;
	private VMDAO vmDAO;
	private EIPDAO eipDAO;
	private RouterDAO routerDAO;
	private LBDAO lbDAO;

	private AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	@Autowired
	private void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	private AlarmRuleDAO getAlarmRuleDAO() {
		return alarmRuleDAO;
	}

	@Autowired
	private void setAlarmRuleDAO(AlarmRuleDAO alarmRuleDAO) {
		this.alarmRuleDAO = alarmRuleDAO;
	}

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
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

	private LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	private void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	public boolean saveAlarm(String alarmUuid, String alarmName, int alarmType,
			int alarmIsalarm, int alarmTouch, int alarmPeriod,
			String alarmRules, String alarmThreshold, int userId) {
		boolean result = this.getAlarmDAO().addAlarm(alarmUuid, alarmName,
				alarmType, alarmIsalarm, alarmTouch, alarmPeriod, userId);
		String[] rulea = alarmRules.split(",");
		String[] thresholda = alarmThreshold.split(",");
		int length = rulea.length;
		for (int i = 0; i < length; i++) {
			result = this.getAlarmRuleDAO().addRule(
					UUID.randomUUID().toString(), Integer.parseInt(rulea[i]),
					Integer.parseInt(thresholda[i]), alarmUuid, 1);
		}
		return result;
	}

	public boolean deleteAlarm(String uuid) {
		Alarm alarm = this.getAlarmDAO().getAlarm(uuid);
		int type = alarm.getAlarmType();
		boolean result = false;
		if (type == 0) {
			result = this.getVmDAO().isNotExistAlarm(uuid);
		} else if (type == 1) {
			result = this.getEipDAO().isNotExistAlarm(uuid);
		} else if (type == 2) {
			result = this.getRouterDAO().isNotExistAlarm(uuid);
		} else {
			result = this.getLbDAO().isLNotExistAlarm(uuid);
		}

		if (result) {
			result = this.getAlarmDAO().removeAlarm(alarm);
			result = this.getAlarmRuleDAO().removeRules(uuid);
		}

		return result;
	}

	public void removeRS(String rsUuid, int type) {
		if (type == 0) {
			this.getVmDAO().updateAlarm(rsUuid, null);
		} else if (type == 1) {
			this.getEipDAO().updateAlarm(rsUuid, null);
		} else if (type == 2) {
			this.getRouterDAO().updateAlarm(rsUuid, null);
		} else {
			this.getLbDAO().updateAlarm(rsUuid, null);
		}
	}

	@SuppressWarnings("rawtypes")
	public JSONArray getResourceList(int page, int limit, String search,
			int type, int uid) {
		List list = null;
		int totalNum = 0;
		JSONArray ja = new JSONArray();
		if (type == 0) {
			list = this.getVmDAO().getOnePageVMsWithoutAlarm(page, limit, search,
					uid);
			totalNum = this.getVmDAO().countVMsWithoutAlarm(search, uid);
			ja.put(totalNum);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject jo = new JSONObject();
					OCVM ocvm = (OCVM) list.get(i);
					jo.put("resourceName",
							Utilities.encodeText(ocvm.getVmName()));
					jo.put("resourceUuid", ocvm.getVmUuid() + "i");
					jo.put("resourceUuidshow", "i-"
							+ ocvm.getVmUuid().substring(0, 8));
					ja.put(jo);
				}
			}
		} else if (type == 1) {
			list = this.getEipDAO().getOnePageEipListAlarm(page, limit, search,
					uid);
			totalNum = this.getEipDAO().countAllEipListAlarm(search, uid);
			ja.put(totalNum);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject jo = new JSONObject();
					EIP eip = (EIP) list.get(i);
					jo.put("resourceName",
							Utilities.encodeText(eip.getEipName()));
					jo.put("resourceUuid", eip.getEipUuid() + "p");
					jo.put("resourceUuidshow", "ip-"
							+ eip.getEipUuid().substring(0, 8));
					ja.put(jo);
				}
			}
		} else if (type == 2) {
			list = this.getRouterDAO().getOnePageRoutersWithoutAlarm(page,
					limit, search, uid);
			totalNum = this.getRouterDAO().countRoutersWithoutAlarm(search,
					uid);
			ja.put(totalNum);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject jo = new JSONObject();
					Router router = (Router) list.get(i);
					jo.put("resourceName",
							Utilities.encodeText(router.getRouterName()));
					jo.put("resourceUuid", router.getRouterUuid() + "r");
					jo.put("resourceUuidshow", "rt-"
							+ router.getRouterUuid().substring(0, 8));
					ja.put(jo);
				}
			}
		} else {
			list = this.getLbDAO().getOnePageLBListAlarm(page, limit, search,
					uid);
			totalNum = this.getLbDAO().countAllLBListAlarm(search, uid);
			ja.put(totalNum);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject jo = new JSONObject();
					LB lb = (LB) list.get(i);
					jo.put("resourceName", Utilities.encodeText(lb.getLbName()));
					jo.put("resourceUuid", lb.getLbUuid() + "l");
					jo.put("resourceUuidshow", "lb-"
							+ lb.getLbUuid().substring(0, 8));
					ja.put(jo);
				}
			}
		}
		return ja;
	}

	public boolean addResourceToAlarm(int userId, String rsuuidStr,
			String alarmUuid) {
		boolean result = false;
		try {
			JSONArray jArray = new JSONArray(rsuuidStr);
			for (int i = 0; i < jArray.length(); i++) {
				String rsUuid = jArray.getString(i);
				String str = rsUuid.substring(rsUuid.length() - 1,
						rsUuid.length());// i主机
											// p公网
											// r路由
											// l负载
				rsUuid = rsUuid.substring(0, rsUuid.length() - 1);
				if (str.equals("i")) {
					this.getVmDAO().updateAlarm(rsUuid, alarmUuid);
				} else if (str.equals("p")) {
					this.getEipDAO().updateAlarm(rsUuid, alarmUuid);
				} else if (str.equals("r")) {
					this.getRouterDAO().updateAlarm(rsUuid, alarmUuid);
				} else if (str.equals("l")) {
					this.getLbDAO().updateAlarm(rsUuid, alarmUuid);
				} else {
					result = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public JSONObject getBasicInfo(String alarmUuid, int uid) {
		Alarm alarm = this.getAlarmDAO().getAlarm(alarmUuid);
		int type = alarm.getAlarmType();
		JSONObject jo = new JSONObject();
		jo.put("alarmName", Utilities.encodeText(alarm.getAlarmName()));
		jo.put("alarmStatus", alarm.getAlarmStatus());
		jo.put("alarmType", alarm.getAlarmType());
		jo.put("alarmPeriod", alarm.getAlarmPeriod());
		jo.put("alarmDate", alarm.getAlarmDate());
		jo.put("alarmDesc", (null == alarm.getAlarmDesc()) ? "&nbsp;"
				: Utilities.encodeText(alarm.getAlarmDesc()));
		JSONArray ja = new JSONArray();
		if (type == 0) {
			for (OCVM ocvm : this.getVmDAO().getVMsOfAlarm(uid, alarmUuid)) {
				JSONObject js = new JSONObject();
				js.put("rsName", Utilities.encodeText(ocvm.getVmName()));
				js.put("rsUuid", ocvm.getVmUuid());
				ja.put(js);
			}
		} else if (type == 1) {
			for (EIP eip : this.getEipDAO().getAllListAlarm(uid, alarmUuid)) {
				JSONObject js = new JSONObject();
				js.put("rsName", Utilities.encodeText(eip.getEipName()));
				js.put("rsUuid", eip.getEipUuid());
				ja.put(js);
			}
		} else if (type == 2) {
			for (Router router : this.getRouterDAO().getRoutersOfAlarm(uid,
					alarmUuid)) {
				JSONObject js = new JSONObject();
				js.put("rsName", Utilities.encodeText(router.getRouterName()));
				js.put("rsUuid", router.getRouterUuid());
				ja.put(js);
			}
		} else {
			for (LB lb : this.getLbDAO().getAllListAlarm(uid, alarmUuid)) {
				JSONObject js = new JSONObject();
				js.put("rsName", Utilities.encodeText(lb.getLbName()));
				js.put("rsUuid", lb.getLbUuid());
				ja.put(js);
			}
		}
		jo.put("alarmResource", ja);
		jo.put("alarmTouch", alarm.getAlarmTouch());
		jo.put("alarmIsalarm", alarm.getAlarmIsalarm());
		return jo;
	}

	public JSONArray getRuleList(String alarmUuid, int page, int limit) {
		int totalNum = this.getAlarmRuleDAO().countAlarmList(alarmUuid);
		List<AlarmRule> list = this.getAlarmRuleDAO().getOnePageList(page,
				limit, alarmUuid);
		JSONArray ja = new JSONArray();
		ja.put(totalNum);
		for (AlarmRule ar : list) {
			JSONObject jo = new JSONObject();
			jo.put("ruleUuid", ar.getRuleAUuid());
			jo.put("rulePeriod", ar.getRuleAPeriod());
			jo.put("ruleThreshold", ar.getRuleAThreshold());
			jo.put("ruleType", ar.getRuleAType());
			ja.put(jo);
		}
		return ja;
	}

	/**
	 * 获取监控警告列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public JSONArray getAlarmList(int userId, int page, int limit, String search) {
		int totalNum = this.getAlarmDAO().countAlarmList(userId, search);
		List<Alarm> alarmList = this.getAlarmDAO().getOnePageList(userId, page,
				limit, search);
		JSONArray ja = new JSONArray();
		ja.put(totalNum);
		if (alarmList != null) {
			for (int i = 0; i < alarmList.size(); i++) {
				JSONObject jo = new JSONObject();
				Alarm alarm = alarmList.get(i);
				jo.put("alarmUuid", alarm.getAlarmUuid());
				jo.put("alarmName", Utilities.encodeText(alarm.getAlarmName()));
				jo.put("alarmStatus", alarm.getAlarmStatus());
				jo.put("alarmType", alarm.getAlarmType());
				jo.put("alarmPeriod", alarm.getAlarmPeriod());
				jo.put("alarmModify", alarm.getAlarmModify());
				String timeUsed = Utilities.encodeText(alarm.getAlarmDate()
						.toString());
				jo.put("alarmDate", timeUsed);
				ja.put(jo);
			}
		}
		return ja;
	}
}
