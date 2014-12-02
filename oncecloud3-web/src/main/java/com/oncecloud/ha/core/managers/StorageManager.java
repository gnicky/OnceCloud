/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.managers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author henry
 * @date   2014年8月25日
 *
 */
public class StorageManager {

	
	private static StorageManager mgr =null;
	
	private Map<String,String> map = new HashMap<String,String>();
	
	private StorageManager() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String get(String key) {
		return map.get(key);
	}

	public void add(String key, String store) {
		this.map.put(key, store);
	}

	public static StorageManager createStorageManager() {
		if(mgr == null) {
			mgr = new StorageManager();
		}
		return mgr;
	}
}
