package com.oncecloud.daemon;

public class ShutdownHandler extends Thread {
	private DaemonServer daemonServer;

	private DaemonServer getDaemonServer() {
		return daemonServer;
	}

	private void setDaemonServer(DaemonServer daemonServer) {
		this.daemonServer = daemonServer;
	}

	public ShutdownHandler(DaemonServer daemonServer) {
		this.setDaemonServer(daemonServer);
	}

	@Override
	public void run() {
		this.getDaemonServer().stop();
	}
}
