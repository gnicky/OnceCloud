package com.oncecloud.daemon.event;

import org.springframework.context.ApplicationEvent;

public class ServiceDownEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6424246112254759218L;

	public ServiceDownEvent(Object source) {
		super(source);
	}

}
