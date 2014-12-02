/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.oncecloud.ha.core.entities.SSHEntity;
import com.oncecloud.ha.core.entities.XenEntity;

/**
 * @author henry
 * @date 2014年9月23日
 *
 */
public class EntityUtils {

	private final static Logger m_logger = Logger.getLogger(EntityUtils.class);

	public synchronized static SSHEntity createSSHEntity(String IP) {
		return createSSHEntity(IP, "root", "onceas", 22, 3000);
	}

	public synchronized static SSHEntity createSSHEntity(String IP, int port) {
		return createSSHEntity(IP, "root", "onceas", port, 3000);
	}

	public synchronized static SSHEntity createSSHEntity(String IP, String usr,
			String pwd, int port) {
		return createSSHEntity(IP, usr, pwd, port, 3000);
	}

	public synchronized static XenEntity createXenEntity(String IP) {
		return createXenEntity(IP, 9363);
	}

	public synchronized static XenEntity createXenEntity(String IP, int port) {
		return createXenEntity(IP, "root", "onceas", port);
	}

	public synchronized static XenEntity createXenEntity(String IP, String usr,
			String pwd, int port) {
		XenEntity newConf = new XenEntity();
		String url = "http://" + IP + ":" + port;
		try {
			newConf.setUrl(new URL(url));
		} catch (MalformedURLException e) {
		}
		newConf.setUser(usr);
		newConf.setPwd(pwd);
		return newConf;
	}

	public synchronized static SSHEntity createSSHEntity(String IP, String usr,
			String pwd, int port, int timeout) {
		m_logger.info("");
		SSHEntity ssh = new SSHEntity();
		ssh.setUrl(IP);
		ssh.setUser(usr);
		ssh.setPwd(pwd);
		ssh.setPort(port);
		ssh.setTimeout(timeout);
		return ssh;
	}
}
