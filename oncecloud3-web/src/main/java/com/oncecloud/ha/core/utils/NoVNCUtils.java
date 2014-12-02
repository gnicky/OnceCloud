/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.once.xenapi.Connection;
import com.once.xenapi.Types;
import com.once.xenapi.VM;

/**
 * @author henry
 * @date 2014年9月18日
 *
 */
public class NoVNCUtils {

	private final static Logger m_logger = Logger.getLogger(NoVNCUtils.class); 
	
	public static boolean updateToken(String noVNC, String token, String host,
			int port) {
		deleteToken(noVNC, token);
		return createToken(noVNC, token, host, port);
	}
	/**
	 * 创建NoVNC的密钥
	 *
	 * @param token
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean createToken(String noVNC, String token, String host,
			int port) {
		try {
			StringBuffer urlbuffer = new StringBuffer();
			urlbuffer.append(noVNC + "VncAction?action=create&host=");
			urlbuffer.append(host);
			urlbuffer.append("&port=");
			urlbuffer.append(port);
			urlbuffer.append("&token=");
			urlbuffer.append(token);
			URL url = new URL(urlbuffer.toString());
			URLConnection connection = url.openConnection();
			int i = connection.getContentLength();
			if (i > 0) {
				InputStream is = connection.getInputStream();
				StringBuffer result = new StringBuffer(2);
				int a;
				while ((a = is.read()) != -1) {
					result.append((char) a);
				}
				return result.toString().equals("1");
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除NoVNC的密钥
	 *
	 * @param token
	 * @return
	 */
	public static boolean deleteToken(String noVNC, String token) {
		try {
			// url should
			URL url = new URL(noVNC + "VncAction?action=delete&token=" + token);
			URLConnection connection = url.openConnection();
			int i = connection.getContentLength();
			if (i > 0) {
				StringBuffer result = new StringBuffer(2);
				InputStream is = connection.getInputStream();
				int a;
				while ((a = is.read()) != -1) {
					result.append((char) a);
				}
				return result.toString().equals("1");
			} else {
				return false;
			}
		} catch (Exception e) {
			m_logger.error(e.getMessage());
			return false;
		}
	}
	
	public static int getVNCPort(String uuid, Connection conn) {
		int port = 0;
		try {
			VM vm = Types.toVM(uuid);
			String location = vm.getVNCLocation(conn);
			port = 5900;
			int len = location.length();
			if (len > 5 && location.charAt(len - 5) == ':') {
				port = Integer.parseInt(location.substring(len - 4));
			}
		} catch (Exception e) {
		}
		return port;
	}
}
