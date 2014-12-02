package com.oncecloud.manager;

import org.json.JSONObject;

public interface VoucherManager {
//	public abstract JSONObject applyVoucher(int userId, int voucher);

	public abstract JSONObject confirmVoucher(int userId);

	public abstract JSONObject denyVoucher(int userId);
}
