package com.oncecloud.ha.core;

import java.util.HashMap;
import java.util.Map;

import com.oncecloud.ha.core.configs.MySQLConfigSingleton;
import com.oncecloud.ha.core.configs.NoVNCConfigSingleton;
import com.oncecloud.ha.core.refs.PoolRef;
import com.oncecloud.ha.core.utils.EntityUtils;
import com.oncecloud.ha.core.utils.VMUtils;

public class HaManager {

	private static Map<String,HAPoolWorker> haMaps=new HashMap<String,HAPoolWorker>();
	
	
	public static void stopPoolHA(String poolUUID) {
			
	    HAPoolWorker worker;
		if(haMaps.size()>0 && haMaps.containsKey(poolUUID))
		{
			worker= haMaps.get(poolUUID);
			worker.stop();
		}
	
		/*while(thread.isAlive()) {
			Thread.sleep(3000);
		}*/
	}

	
	public static void startHAPool(String poolUUID,String haPath,String masterIP) {
	
		stopPoolHA(poolUUID);
		HAPoolWorker worker = init(poolUUID,haPath,masterIP);
		haMaps.put(poolUUID, worker);
		
		Thread thread = new Thread(worker);
		thread.start();
	}

	private static HAPoolWorker init(String poolUUID,String haPath,String masterIP) {
		PoolRef poolRef = new PoolRef();
		poolRef.setPoolUUID(poolUUID);
		
		poolRef.setHAPath(haPath);
		
		NoVNCConfigSingleton novnc = NoVNCConfigSingleton.instance("com/oncecloud/config/haconfig/novnc.conf");
		poolRef.setNovncServer(novnc.getServer());
		MySQLConfigSingleton mysql = MySQLConfigSingleton.instance("com/oncecloud/config/haconfig/conf/db.conf");
		poolRef.setMysqlConn(mysql.getConn());
		
		poolRef.setMaster(masterIP);
		poolRef.setXenConn(VMUtils.createXenConnection(EntityUtils
				.createXenEntity(masterIP)));
		
		HAPoolWorker worker = new HAPoolWorker(poolRef);
		return worker;
	}
	
	
	
}
