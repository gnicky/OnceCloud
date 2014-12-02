/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.configs;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oncecloud.ha.core.HAException;
import com.oncecloud.ha.core.utils.IOUtils;

/**
 * @author henry
 * @date 2014年9月19日
 * 
 */
public class NoVNCConfigSingleton {

	private final static Logger m_logger = Logger
			.getLogger(NoVNCConfigSingleton.class);

	private static NoVNCConfigSingleton novnc = null;

	final static String URL = "com.oncecloud.noVNC.url";

	private String config;

	private static String server = null;

	private NoVNCConfigSingleton(String config) {
		super();
		this.config = config;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.oncecloud.ha.core.HAConfig#config(java.lang.String)
	 */
	public void config() throws HAException {
		if (!fileExist(config)) {
			m_logger.error("PoolRef is null, or configuration is not exist");
			return;
		}

		FileInputStream fis = IOUtils.openFileAsStream(config);
		try {
			Properties props = new Properties();
			props.load(fis);
			server = props.getProperty(URL);
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		} finally {
			IOUtils.close(fis);
		}
	}


	private boolean fileExist(String name) {
		return name == null ? false : new File(name).exists();
	}
	
	public static NoVNCConfigSingleton instance(String config) {
		if(novnc == null) {
			novnc = new NoVNCConfigSingleton(config);
			novnc.config();
		}
		return novnc;
	}

	public String getServer() {
		return server;
	}
	
}
