package com.oncecloud.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oncecloud.dao.AlarmDAO;
import com.oncecloud.entity.Alarm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/oncecloud/config/application-context.xml")
public class AlarmDAOTest {
	private AlarmDAO alarmDAO;

	private AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	@Autowired
	private void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	@Test
	public void testList() {
		List<Alarm> alarms = this.getAlarmDAO().getOnePageList(1, 100, 14, "");
		for (Alarm alarm : alarms) {
			System.out.println(alarm.getAlarmUuid());
		}
	}
}
