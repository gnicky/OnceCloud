package com.oncecloud.thread;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

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

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
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
