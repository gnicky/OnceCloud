package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.ForwardPort;

public interface ForwardPortDAO {

	public abstract boolean addPF(ForwardPort pf);

	public abstract boolean deletePF(ForwardPort pf);

	public abstract List<ForwardPort> getpfListByRouter(String routerUuid);
}
