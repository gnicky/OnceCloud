/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

import com.once.xenapi.Connection;
import com.oncecloud.ha.core.Config;
import com.oncecloud.ha.core.HAException;
import com.oncecloud.ha.core.entities.IPMIEntity;
import com.oncecloud.ha.core.managers.IPMIManager;
import com.oncecloud.ha.core.managers.StorageManager;
import com.oncecloud.ha.core.refs.PoolRef;
import com.oncecloud.ha.core.utils.ObjectUtils;

/**
 * @author henry
 * @date   2014年9月19日
 *
 */
public class PoolConfig extends Config {

	private final static Logger m_logger = Logger.getLogger(PoolConfig.class);
	
	private IPMIManager m_IPMIMgr = IPMIManager.createIPMIManager();
	
	private StorageManager m_STGMgr = StorageManager.createStorageManager();
	
	public PoolConfig(PoolRef pool) {
		super(pool);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.oncecloud.ha.core.HAConfig#config(java.lang.String)
	 */
	@Override
	public void config(String name) throws HAException {
		// TODO Auto-generated method stub

		if (ObjectUtils.invalid(pool) || !fileExist(name)) {
			m_logger.error("PoolRef is null, or configuration is not exist");
			return;
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(name));
			String line = null;
			while((line = br.readLine()) != null) {
				
				if("".equals(line.trim()) || line.split("=").length != 2) {
					continue;
				}
				
				String str = line.split("=")[1];
				if(line.startsWith("com.bncloud.pool.UUID")) {
					pool.setPoolUUID(str);
				} else if(line.startsWith("com.bncloud.pool.ha.path")) {
					pool.setHAPath(str);
				} else if(line.startsWith("com.bncloud.master.url")) {
					String totalURls = str.substring(1, str.length() - 1);
					String[] urlDetails = totalURls.split(",");
					pool.setMaster(urlDetails[0]);
					Connection conn = new Connection("http://" + urlDetails[0] + ":" + urlDetails[1],urlDetails[2], urlDetails[3]);
					pool.setXenConn(conn);
				} else if(line.startsWith("com.bncloud.mb")) {
					String TotalIPMI = str.substring(1, str.length() - 1);
					String[] IPMIDetail = TotalIPMI.split(",");
					IPMIEntity IPMI = new IPMIEntity();
					IPMI.setUrl(IPMIDetail[1]);
					IPMI.setUser(IPMIDetail[2]);
					IPMI.setPwd(IPMIDetail[3]);
					IPMI.setPort(Integer.parseInt(IPMIDetail[4]));
					m_IPMIMgr.add(IPMIDetail[0], IPMI);
				} else if(line.startsWith("com.bncloud.sg")) {
					String TotalStoarge = str.substring(1, str.length() - 1);
					String[] StorageDetail = TotalStoarge.split(",");
					m_STGMgr.add(StorageDetail[0], StorageDetail[1]);
				}
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		}
	}

	private boolean fileExist(String name) {
		return name == null ? false : new File(name).exists();
	}
}
