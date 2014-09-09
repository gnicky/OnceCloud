package com.oncecloud.daemon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.oncecloud.daemon.event.ServiceDownEvent;

@Component
public class WebConsoleMonitor implements ServiceMonitor {
	private ApplicationContext applicationContext;
	private Thread monitorThread;
	private boolean running;

	private ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Autowired
	private void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private Thread getMonitorThread() {
		return monitorThread;
	}

	private void setMonitorThread(Thread monitorThread) {
		this.monitorThread = monitorThread;
	}

	private boolean isRunning() {
		return running;
	}

	private void setRunning(boolean running) {
		this.running = running;
	}

	private boolean isAlive() {
		return false;
	}

	@Override
	public boolean start() {
		this.setRunning(true);
		this.setMonitorThread(new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning()) {
					try {
						boolean alive = isAlive();
						if (!alive) {
							getApplicationContext().publishEvent(
									new ServiceDownEvent("Web Console"));
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}));
		this.getMonitorThread().run();
		System.out.println("Web Console Monitor Started");
		return true;
	}

	@Override
	public boolean stop() {
		try {
			this.setRunning(false);
			this.getMonitorThread().join();
			System.out.println("Web Console Monitor Stopped");
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
}
