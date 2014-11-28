package com.oncecloud.dao;

import java.util.List;

import org.hibernate.Session;

import com.oncecloud.entity.Backend;

public interface BackendDAO {

	public abstract boolean changeBackendStatus(String backUuid, int state);

	public abstract boolean checkRepeat(String beuuid, int port);

	public abstract Backend createBackend(String backUuid, String backName,
			String vmUuid, String vmIp, Integer vmPort, Integer backWeight,
			String foreUuid);

	public abstract boolean deleteBackend(String backUuid);

	public abstract boolean deleteBackendByFrontend(String foreUuid);

	public abstract List<Backend> doGetBackendListByFrontend(String foreUuid, int forbid);
}
