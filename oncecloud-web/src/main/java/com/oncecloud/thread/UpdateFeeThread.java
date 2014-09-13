package com.oncecloud.thread;

import java.util.Date;

import com.oncecloud.dao.FeeDAO;

/**
 * @author yly
 * @version 2014/04/04
 */
public class UpdateFeeThread extends Thread {

	private FeeDAO feeDAO;

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	public UpdateFeeThread(FeeDAO feeDAO) {
		this.setFeeDAO(feeDAO);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(60 * 1000);
				Date nowDate = new Date();
				this.getFeeDAO().updateAliveVmEndDate(nowDate);
				this.getFeeDAO().updateAliveVolumeEndDate(nowDate);
				this.getFeeDAO().updateAliveSnapshotEndDate(nowDate);
				this.getFeeDAO().updateAliveImageEndDate(nowDate);
				this.getFeeDAO().updateAliveEipEndDate(nowDate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
