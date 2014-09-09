package com.oncecloud.daemon.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceDownListener implements
		ApplicationListener<ServiceDownEvent> {

	@Override
	public void onApplicationEvent(ServiceDownEvent event) {
		System.out.println(event.getSource());
	}

}
