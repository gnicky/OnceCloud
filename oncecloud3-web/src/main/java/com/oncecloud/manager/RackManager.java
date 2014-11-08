package com.oncecloud.manager;

import org.json.JSONArray;

public interface RackManager {

	public abstract JSONArray createRack(String rackName, String dcid,
			String rackDesc, int userid);

	public abstract JSONArray getRackList(int page, int limit, String search);

	public abstract void deleteRack(String rackId, String rackName, int userid);

//	public abstract JSONArray bind(String rackId, String dcId, int userid);
//
//	public abstract JSONArray unbind(String rackId, int userid);

	public abstract void update(String rackId, String rackName,
			String rackDesc, String dcid, int userid);

//	public abstract JSONArray getRackAllList();

}
