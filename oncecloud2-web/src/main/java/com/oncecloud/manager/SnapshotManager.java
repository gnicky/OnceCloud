package com.oncecloud.manager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface SnapshotManager {

	/**
	 * 获取备份链列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	public abstract JSONArray getSnapshotList(int userId, int page, int limit,
			String search);

	public abstract JSONObject createSnapshot(int userId, String snapshotId,
			String snapshotName, String resourceUuid, String resourceType);

	public abstract JSONObject deleteSnapshotSeries(int userId,
			String resourceUuid, String resourceType);

	public abstract JSONArray getDetailList(String resourceUuid,
			String resourceType);

	public abstract JSONObject rollbackSnapshot(int userId, String snapshotId,
			String resourceUuid, String resourceType);

	public abstract JSONObject deleteSnapshot(int userId, String snapshotId,
			String resourceUuid, String resourceType);

	public abstract JSONObject getBasicList(int userId, String resourceUuid,
			String resourceType);

	public abstract String getQuota(int userId, int count);
}
