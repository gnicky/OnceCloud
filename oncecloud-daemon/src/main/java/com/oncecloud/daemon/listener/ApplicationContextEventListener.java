package com.oncecloud.daemon.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.oncecloud.daemon.monitor.ServiceMonitor;

@Component
public class ApplicationContextEventListener implements
		ApplicationListener<ApplicationContextEvent> {

	private ApplicationContext applicationContext;

	private ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Autowired
	private void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextStartedEvent) {
			Map<String, ServiceMonitor> monitors = this.getApplicationContext()
					.getBeansOfType(ServiceMonitor.class);
			for (final ServiceMonitor monitor : monitors.values()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						monitor.start();
					}
				}).run();
			}
			return;
		}
		if (event instanceof ContextStoppedEvent) {
			Map<String, ServiceMonitor> monitors = this.getApplicationContext()
					.getBeansOfType(ServiceMonitor.class);
			for (final ServiceMonitor monitor : monitors.values()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						monitor.stop();
					}
				}).run();
			}
			return;
		}
	}

}
