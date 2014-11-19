package com.oncecloud.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class QAManagerTest {
	@Test
	public void logTest() {
//		List<OCLog> list = this.getLogDAO().getLogList(1, 0, 1, 10);
//		for (OCLog ocLog : list) {
//			System.out.println(ocLog.getLogInfo());
//		}
	}
}
