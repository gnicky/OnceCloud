package com.oncecloud.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oncecloud.dao.AlarmRuleDAO;
import com.oncecloud.entity.AlarmRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/oncecloud/config/application-context.xml")
public class AlarmRuleDAOTest {
	private AlarmRuleDAO alarmRuleDAO;

	private AlarmRuleDAO getAlarmRuleDAO() {
		return alarmRuleDAO;
	}

	@Autowired
	private void setAlarmRuleDAO(AlarmRuleDAO alarmRuleDAO) {
		this.alarmRuleDAO = alarmRuleDAO;
	}

	@Test
	public void testList() {
		List<AlarmRule> alarmRules = this.getAlarmRuleDAO().getOnePageList(1,
				100, "9baef98b-25d4-4678-a596-79310dbbd4b8");
		for (AlarmRule alarmRule : alarmRules) {
			System.out.println(alarmRule.getRuleAUuid());
		}
	}
}
