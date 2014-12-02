/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.configs;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oncecloud.ha.core.HAException;
import com.oncecloud.ha.core.utils.IOUtils;

/**
 * @author henry
 * @date 2014年9月19日
 *
 *  得到MySQL数据的配置
 */
public class MySQLConfigSingleton {

	private final static Logger m_logger = Logger.getLogger(MySQLConfigSingleton.class);

	final static String DRIVER = "com.mysql.jdbc.Driver";

	final static String URL = "com.oncecloud.mysql.url";

	final static String PORT = "com.oncecloud.mysql.port";

	final static String USER = "com.oncecloud.mysql.user";

	final static String PWD = "com.oncecloud.mysql.pwd";

	final static String DB = "com.oncecloud.mysql.db";

	private static MySQLConfigSingleton mysql = null;
	
	private static Connection conn; 
	
	private String config; 
	
	private MySQLConfigSingleton(String config) {
		super();
		this.config = config;
	}
	
	public void config() throws HAException {
		if (!fileExist(config)) {
			m_logger.error("PoolRef is null, or configuration is not exist");
			return;
		}

		FileInputStream fis = IOUtils.openFileAsStream(config);
		try {
			Properties props = new Properties();
			props.load(fis);
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(getMysqlUrl(props),
					props.getProperty(USER), props.getProperty(PWD));
		} catch (Exception e) {
			m_logger.error(e.getMessage());
		} finally {
			IOUtils.close(fis);
		}
	}

	private String getMysqlUrl(Properties props) {
		return "jdbc:mysql://" + props.getProperty(URL) + ":" + props.getProperty(PORT) + "/" + props.getProperty(DB)
				+ "?useUnicode=true&characterEncoding=utf-8";
	}

	private boolean fileExist(String name) {
		return name == null ? false : new File(name).exists();
	}
	
	public static MySQLConfigSingleton instance(String config) {
		if(mysql == null) {
			mysql = new MySQLConfigSingleton(config);
			mysql.config();
		}
		return mysql;
	}

	public Connection getConn() {
		return conn;
	}
	
}
