package com.oncecloud.manager;

import com.oncecloud.entity.Power;

public interface PowerManager {
	
	public abstract boolean savePower(String powerUuid, String hostUuid, String motherboardIP,
				int powerPort, String powerUsername, String powerPassword,
				Integer powerValid);
	
    public abstract int getStatusOfPower(String powerIP,int powerPort,String userName,String passWord); 
    
    public abstract boolean startPower(String powerIP,int powerPort,String userName,String passWord);
    
    
    public abstract boolean stopPower(String powerIP,int powerPort,String userName,String passWord);
    
    
    public abstract boolean deletePower(String hostUuid);
    
    public abstract Power getPower(String hostUuid);
    
    
}
