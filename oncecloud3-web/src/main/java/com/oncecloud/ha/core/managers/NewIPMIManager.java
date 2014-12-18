/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Host.Record;
import com.oncecloud.ha.core.utils.EntityUtils;
import com.oncecloud.ha.core.utils.VMUtils;

/**
 * @author henry
 * @date   2014年8月25日
 *
 */
public class NewIPMIManager {

	private final Logger m_logger = Logger.getLogger(NewIPMIManager.class);
	
	private Map<String,String> map = new HashMap<String,String>();
	
	public NewIPMIManager(Connection conn) {
		super();
		try {
			Set<Host> hosts = Host.getAll(conn);
			for(Host host : hosts) {
				Record record = host.getRecord(conn);
				add(record.address, record.uuid);
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
		
	}

	public String get(String key) {
		return map.get(key);
	}

	public void add(String key, String ipmi) {
		this.map.put(key, ipmi);
	}

	public static void main(String[] args) {
		Connection conn = VMUtils.createXenConnection(EntityUtils
				.createXenEntity("133.133.135.12"));
		NewIPMIManager mgr = new NewIPMIManager(conn);
		System.out.println(mgr.get("133.133.135.12"));
		System.out.println(mgr.get("133.133.135.16"));
	}

}
