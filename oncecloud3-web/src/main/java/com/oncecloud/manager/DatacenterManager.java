package com.oncecloud.manager;

import java.util.List;

import org.json.JSONArray;

public interface DatacenterManager {

	public abstract JSONArray createDatacenter(String dcName,
			String dcLocation, String dcDesc, int userid);

	public abstract JSONArray getDatacenterList(int page, int limit,
			String search);

//	public abstract JSONArray getDatacenterAllList();

	public abstract JSONArray deleteDatacenter(String dcId, String dcName,
			int userid);

	public abstract void update(String dcUuid, String dcName,
			String dcLocation, String dcDesc, int userid);

//	public abstract List<Integer> getDCVolume(String dcUuid);
//
//	public abstract List<Integer> getPoolVolume(String poolUuid);
//
	public abstract JSONArray getPoolList(String dcid);

	public abstract JSONArray getRackList(String dcid);

}