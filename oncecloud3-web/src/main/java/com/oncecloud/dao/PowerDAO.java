package com.oncecloud.dao;

import com.oncecloud.entity.Power;

public interface PowerDAO {
	
    public abstract boolean addPower(Power power);
    
    public abstract boolean editPower(Power power);
    
    public abstract boolean removePower(Power power);
    
    public abstract Power getPowerByHostID(String hostUuid);
}
