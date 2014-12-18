/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.configs;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oncecloud.ha.core.Config;
import com.oncecloud.ha.core.HAException;
import com.oncecloud.ha.core.refs.PoolRef;
import com.oncecloud.ha.core.utils.IOUtils;
import com.oncecloud.ha.core.utils.ObjectUtils;

/**
 * @author henry
 * @date 2014年9月19日
 *
 */
public class NoVNCConfig extends Config {

	private final static Logger m_logger = Logger.getLogger(NoVNCConfig.class);
	
	final static String URL = "com.oncecloud.noVNC.url";

	public NoVNCConfig(PoolRef pool) {
		super(pool);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.oncecloud.ha.core.HAConfig#config(java.lang.String)
	 */
	@Override
	public void config(String name) throws HAException {
		if (ObjectUtils.invalid(pool) || !fileExist(name)) {
			m_logger.error("PoolRef is null, or configuration is not exist");
			return;
		}

		FileInputStream fis = IOUtils.openFileAsStream(name);
		try {
			Properties props = new Properties();
			props.load(fis);
			pool.setNovncServer(props.getProperty(URL));
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		} finally {
			IOUtils.close(fis);
		}
	}

	private boolean fileExist(String name) {
		return name == null ? false : new File(name).exists();
	}
}
