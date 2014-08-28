package com.oncecloud.main;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.oncecloud.thread.UpdateFeeThread;

/**
 * @author hehai
 * @version 2014/06/26
 */
public class OnceServlet extends HttpServlet {
	private static final long serialVersionUID = 8748334845182951985L;

	public void init() throws ServletException {
		UpdateFeeThread uft = new UpdateFeeThread(null);
		uft.start();
		// RecvVMSyncThread rvst = new RecvVMSyncThread();
		// rvst.start();
		// PreallocateThread agent = new PreallocateThread();
		// Thread thread = new Thread(agent);
		// thread.start();
	}
}
