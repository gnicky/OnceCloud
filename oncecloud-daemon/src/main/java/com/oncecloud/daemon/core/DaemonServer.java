package com.oncecloud.daemon.core;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class DaemonServer {
	private Server server;
	private int port;

	private Server getServer() {
		return server;
	}

	private void setServer(Server server) {
		this.server = server;
	}

	private int getPort() {
		return port;
	}

	private void setPort(int port) {
		this.port = port;
	}

	public DaemonServer(int port) {
		this.setPort(port);
	}

	private String getWebXmlLocation() {
		return Thread.currentThread().getContextClassLoader()
				.getResource("META-INF/webapp/WEB-INF/web.xml").toString();
	}

	private String getResourceBase() {
		return Thread.currentThread().getContextClassLoader()
				.getResource("META-INF/webapp/").toString();
	}

	public boolean start() {
		try {
			this.setServer(new Server(this.getPort()));
			WebAppContext webAppContext = new WebAppContext();
			webAppContext.setContextPath("/");
			webAppContext.setDescriptor(this.getWebXmlLocation());
			webAppContext.setResourceBase(this.getResourceBase());
			this.getServer().setHandler(webAppContext);
			this.getServer().start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean stop() {
		try {
			if (this.getServer() != null) {
				this.getServer().stop();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.setServer(null);
		}
	}
}
