package com.oncecloud.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
	
	private DHCPDAO dhcpdao;
	
	public DHCPDAO getDhcpdao() {
		return dhcpdao;
	}

	@Autowired
	public void setDhcpdao(DHCPDAO dhcpdao) {
		this.dhcpdao = dhcpdao;
	}

	@Test
	public void logTest() {
//		System.out.println(this.getDhcpdao().macExist("00:16:3e:4e:87:ee"));
	}
}
