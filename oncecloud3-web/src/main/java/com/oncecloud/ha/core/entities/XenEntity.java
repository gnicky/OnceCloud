/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.entities;

import java.net.URL;

/**
 * @author henry
 * @email  wuheng09@gmail.com
 * @date   2014年8月21日
 *
 */
public class XenEntity {

	private URL url;
	
	private String user;
	
	private String pwd;
	
	private String version;
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
