package com.oncecloud.main;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author hehai
 * @version 2014/04/23
 */
public class NoVNC {

	public static boolean createToken(String token, String host, int port) {
		try {
			StringBuffer urlbuffer = new StringBuffer();
			urlbuffer.append(Constant.noVNCServer + "VncAction?action=create&host=");
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

	public static boolean deleteToken(String token) {
		try {
			URL url = new URL(Constant.noVNCServer + "VncAction?action=delete&token=" + token);
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
			e.printStackTrace();
			return false;
		}
	}
}
