package com.oncecloud.ui.model;

import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;

public class UserQuotaModel {
    private User user;
    private Quota quota;
    
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Quota getQuota() {
		return quota;
	}
	public void setQuota(Quota quota) {
		this.quota = quota;
	}
    
    
}
