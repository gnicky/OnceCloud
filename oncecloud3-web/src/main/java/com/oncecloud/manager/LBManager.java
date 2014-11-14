package com.oncecloud.manager;

import org.json.JSONArray;

public interface LBManager {
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;
	
	public abstract void lbAdminShutUp(String uuid, int userId);
	
	public abstract void lbAdminShutDown(String uuid, String force, int userId);
	
	public abstract void updateImportance(String uuid, int importance);
	
	public abstract JSONArray getAdminLBList(int page, int limit, String host,
			int importance, String type);
}
