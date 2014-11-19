package com.oncecloud.manager;

import com.oncecloud.entity.Quota;

public interface QuotaManager {

	public abstract Quota getQuotaUsed(int quotaUID);

	public abstract Quota getQuotaTotal(int quotaUID);
}
