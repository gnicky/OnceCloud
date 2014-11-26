package com.oncecloud.dao;

import java.util.List;

import com.oncecloud.entity.PPTPUser;

public interface PPTPUserDAO {
	public abstract boolean save(PPTPUser pptpUser);

	public abstract boolean delete(PPTPUser pptpUser);

	public abstract boolean update(PPTPUser pptpUser);

	public abstract List<PPTPUser> getList(String routerUuid);

	public abstract PPTPUser getPPTPUser(int pptpid);
}
