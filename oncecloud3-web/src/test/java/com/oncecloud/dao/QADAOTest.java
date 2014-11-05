package com.oncecloud.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.dao.impl.QADAOImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class QADAOTest {

	private QADAO qadao;

	public QADAO getQadao() {
		return qadao;
	}

	@Autowired
	public void setQadao(QADAO qadao) {
		this.qadao = qadao;
	}

	@Test
	public void qadaotest() {
		int count = this.getQadao().insertQuestion(1, "test", "test-hty",
				new Date());
		System.out.println(count);
	}
}
