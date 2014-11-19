package com.oncecloud.manager;

import org.json.JSONArray;

public interface RouterManager {
	
	public static int[] capacity = { 250, 500, 1000 };
	public final static int POWER_HALTED = 0;
	public final static int POWER_RUNNING = 1;
	public final static int POWER_CREATE = 2;
	public final static int POWER_DESTROY = 3;
	public final static int POWER_BOOT = 4;
	public final static int POWER_SHUTDOWN = 5;
	
	public abstract JSONArray getAdminRouterList(int page, int limit, String host,
			int importance, String type);
	
	public abstract void routerAdminStartUp(String uuid, int userId);
	
	public abstract void routerAdminShutDown(String uuid, String force, int userId);
	
	public abstract void updateImportance(String uuid, int importance);
	
}
