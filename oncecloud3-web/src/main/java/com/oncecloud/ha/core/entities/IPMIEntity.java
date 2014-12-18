/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.entities;

/**
 * @author henry
 * @email  wuheng09@gmail.com
 * @date   2014年8月21日
 *
 */
public class IPMIEntity {

	private String url;
	
	private int port;
	
	private String user;
	
	private String pwd;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
