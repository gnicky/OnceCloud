/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.managers;

import java.util.HashMap;
import java.util.Map;

import com.oncecloud.ha.core.entities.IPMIEntity;

/**
 * @author henry
 * @date   2014年8月25日
 *
 */
public class IPMIManager {

	
	private static IPMIManager mgr =null;
	
	private Map<String,IPMIEntity> map = new HashMap<String,IPMIEntity>();
	
	private IPMIManager() {
		super();
		// TODO Auto-generated constructor stub
	}


	public IPMIEntity get(String key) {
		return map.get(key);
	}

	public void add(String key, IPMIEntity ipmi) {
		this.map.put(key, ipmi);
	}

	public static IPMIManager createIPMIManager() {
		if(mgr == null) {
			mgr = new IPMIManager();
		}
		return mgr;
	}
}
