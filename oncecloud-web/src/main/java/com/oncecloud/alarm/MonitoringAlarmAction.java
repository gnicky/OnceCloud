package com.oncecloud.alarm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.manager.AlarmLogManager;

@Component
public class MonitoringAlarmAction {
  
	
	private AlarmLogManager alarmLogManager;
	
    
	
	////监控告警进程
	@Autowired
	public MonitoringAlarmAction(AlarmLogManager alarmLogManager1)
	{
		this.alarmLogManager= alarmLogManager1;
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new Thread(new Runnable(){
			public void run()
			{
				
				while(true)
				{
					try {
						alarmLogManager.checkCPU();
					    alarmLogManager.checkMem();
						alarmLogManager.checkIO();
					
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}).start();
	}
}
