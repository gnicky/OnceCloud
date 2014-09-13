package com.oncecloud.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author cyh
 * @version 2014/06/26
 */
public class UserSessionListener implements HttpSessionListener {
	// /在线人数
	private static int online = 0;

	public static int getOnline() {
		return online;
	}

	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		online++;
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		if (online > 0)
			online--;
	}

}
