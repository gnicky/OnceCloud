package com.oncecloud.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class HostManagerTest {

	private HostManager hostManager;

	public HostManager getHostManager() {
		return hostManager;
	}

	@Autowired
	public void setHostManager(HostManager hostManager) {
		this.hostManager = hostManager;
	}
	
	@Test
	public void test() {
//		this.getHostManager().recover(1, "133.133.135.16", "root", "onceas", "", "");
	}
	
}
