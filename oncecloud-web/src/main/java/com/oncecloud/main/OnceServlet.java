package com.oncecloud.main;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Component;

/**
 * @author hehai
 * @version 2014/06/26
 */
@Component
public class OnceServlet extends HttpServlet {
	private static final long serialVersionUID = 8748334845182951985L;

	public void init() throws ServletException {
//		UpdateFeeThread uft = new UpdateFeeThread();
//		uft.start();
		// PreallocateThread agent = new PreallocateThread();
		// Thread thread = new Thread(agent);
		// thread.start();
	}
}
