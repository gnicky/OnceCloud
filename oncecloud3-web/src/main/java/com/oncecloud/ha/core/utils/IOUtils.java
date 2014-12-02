/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;

import org.apache.log4j.Logger;

import com.oncecloud.ha.core.Constants;

/**
 * @author henry
 * @date 2014年9月19日
 *
 * 和IO相关的所有操作
 */
public class IOUtils {

	private final static Logger m_logger = Logger.getLogger(IOUtils.class);
	
	public static FileInputStream openFileAsStream(String name) {
		return openFileAsStream(new File(name));
	}

	public synchronized static FileInputStream openFileAsStream(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			m_logger.info("Open file inputstream " + file.getAbsolutePath());
			return fis;
		} catch (Exception e) {
			m_logger.error(e.getMessage());
			return Constants.NULL_FILESTREAM;
		}
	}
	
	public synchronized static void close(FileInputStream fis) {
		m_logger.info("Close file inputstream " + fis);
		if(fis != null) {
			try {
				fis.close();
			} catch (Exception e) {
				// this is ok..
			}
		}
	}
	
}
