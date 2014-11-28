package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.ChargeRecord;

public interface ChargeDAO {
	public abstract int countChargeRecord(int userid);

	public abstract ChargeRecord createChargeRecord(String recordId,
			Double recordBill, Integer recordType, Date recordDate,
			Integer recordUID, Integer state);

	public abstract ChargeRecord getChargeRecord(String recordId);

	public abstract List<ChargeRecord> getOnePageChargeRecord(int page,
			int limit, int userid);

	public abstract boolean updateChargeRecord(String recordId,
			Integer recordstate);
}
