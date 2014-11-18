package com.oncecloud.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.entity.Quota;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class QuotaDAOTest {

	private QuotaDAO quotaDAO;

	public QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	public void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}
	
	@Test
	public void quotaTest() {
//		Quota quota = this.getQuotaDAO().getQuotaTotal(1);
//		System.out.println(quota.getQuotaCpu());
	}
	
}
