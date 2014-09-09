package com.oncecloud.daemon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.oncecloud.daemon.event.ServiceDownEvent;

@Component
public class WebConsoleMonitor implements ServiceMonitor {
	private ApplicationContext applicationContext;

	private ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Autowired
	private void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void publish() {
		this.getApplicationContext().publishEvent(
				new ServiceDownEvent("Web Console"));
	}
}
