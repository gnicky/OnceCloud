package com.oncecloud.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.entity.Quota;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class QuotaManager {
	private QuotaDAO quotaDAO;

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	public Quota getQuotaUsed(int quotaUID) {
		try {
			Quota quota = this.getQuotaDAO().getQuotaUsed(quotaUID);
			return quota;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Quota getQuotaTotal(int quotaUID) {
		try {
			Quota quota = this.getQuotaDAO().getQuotaTotal(quotaUID);
			return quota;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
