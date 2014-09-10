package com.oncecloud.daemon.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.oncecloud.daemon.event.ServiceDownEvent;

@Component
public class ServiceDownListener implements
		ApplicationListener<ServiceDownEvent> {

	@Override
	public void onApplicationEvent(ServiceDownEvent event) {
		System.out.println("Service down: " + event.getSource());
	}

}
