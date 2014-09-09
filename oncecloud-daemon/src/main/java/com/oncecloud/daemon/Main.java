package com.oncecloud.daemon;

public class Main {
	public static void main(String[] args) {
		DaemonServer daemonServer = new DaemonServer(9090);
		Runtime.getRuntime().addShutdownHook(new ShutdownHandler(daemonServer));
		daemonServer.start();
	}

}
