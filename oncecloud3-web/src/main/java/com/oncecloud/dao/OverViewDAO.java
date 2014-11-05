package com.oncecloud.dao;

import com.oncecloud.entity.OverView;

public interface OverViewDAO {

	public abstract OverView getOverViewTotal();

	public abstract boolean updateOverViewfield(String filedName, boolean isadd);
}