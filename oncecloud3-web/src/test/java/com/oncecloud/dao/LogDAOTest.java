package com.oncecloud.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.entity.OCLog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class LogDAOTest {

	private LogDAO logDAO;

	public LogDAO getLogDAO() {
		return logDAO;
	}

	@Autowired
	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}
	
	@Test
	public void logTest() {
		List<OCLog> list = this.getLogDAO().getLogList(1, 0, 1, 10);
		for (OCLog ocLog : list) {
			System.out.println(ocLog.getLogInfo());
		}
	}
}
