package com.oncecloud.manager;

import org.json.JSONArray;

public interface AddressManager {

	public abstract JSONArray addDHCPPool(int userId, String prefix, int start,
			int end);

	public abstract JSONArray getDHCPList(int page, int limit, String search);

	public abstract JSONArray addPublicIP(int userId, String prefix, int start,
			int end, int eiptype, String eipInterface);

	public abstract JSONArray getPublicIPList(int page, int limit, String search);

	public abstract JSONArray deleteDHCP(int userId, String ip, String mac);

	public abstract JSONArray deletePublicIP(int userId, String ip, String uuid);

}
