package com.oncecloud.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.entity.OverView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class OverviewDAOTest {

	private OverViewDAO overViewDAO;

	public OverViewDAO getOverViewDAO() {
		return overViewDAO;
	}

	@Autowired
	public void setOverViewDAO(OverViewDAO overViewDAO) {
		this.overViewDAO = overViewDAO;
	}
	
	@Test
	public void testOverview() {
		OverView oView = this.getOverViewDAO().getOverViewTotal();
		System.out.println(oView.getViewDc());
	}
}
