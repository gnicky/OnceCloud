package com.oncecloud.ui.action;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.Power;
import com.oncecloud.manager.PowerManager;


@RequestMapping("/PowerAction")
@Controller
public class PowerAction {
   private PowerManager powerManager;

	public PowerManager getPowerManager() {
		return powerManager;
	}
	
	@Autowired
	public void setPowerManager(PowerManager powerManager) {
		this.powerManager = powerManager;
	}
   
   
	@RequestMapping(value = "/PowerHost", method = { RequestMethod.POST })
	@ResponseBody
	public Power PowerHost(HttpServletRequest request, String hostid) {
		Power power = this.getPowerManager().getPower(hostid);
		if(power==null)
		{
			power =new Power();
			power.setHostUuid(hostid);
			power.setPowerPort(8088);
			power.setPowerUuid(UUID.randomUUID().toString());
			power.setPowerValid(0);
		}
		return power;
	}
	
	@RequestMapping(value = "/PowerStatus", method = { RequestMethod.POST })
	@ResponseBody
	public int PowerStatus(HttpServletRequest request, String powerid,String hostid,String hostip, int hostport,String hostusername,String hostpsw) {
		int powerstatus = this.getPowerManager().getStatusOfPower(hostip, hostport, hostusername, hostpsw);
		this.getPowerManager().savePower(powerid, hostid, hostip, hostport, hostusername, hostpsw, powerstatus>0?1:0);
		return powerstatus;
	}
	
	@RequestMapping(value = "/StartPower", method = { RequestMethod.POST })
	@ResponseBody
	public boolean StartPower(HttpServletRequest request, String hostip, int hostport,String hostusername,String hostpsw) {
		return this.getPowerManager().startPower( hostip, hostport, hostusername, hostpsw);
	}
	
	@RequestMapping(value = "/ShutDownPower", method = { RequestMethod.POST })
	@ResponseBody
	public boolean ShutDownPower(HttpServletRequest request, String hostip, int hostport,String hostusername,String hostpsw) {
		return this.getPowerManager().stopPower( hostip, hostport, hostusername, hostpsw);
	}

}
