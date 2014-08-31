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
	public void testGetAlarm() {
		System.out.println(this.getAlarmDAO()
				.getAlarm("32530a9a-0b13-4e17-8b81-2240aa8e214e")
				.getAlarmName());
	}

	@Test
	public void testGetOnePageList() {
		List<Alarm> alarms = this.getAlarmDAO().getOnePageList(14, 1, 100, "");
		for (Alarm alarm : alarms) {
			System.out.println(alarm.getAlarmUuid());
		}
	}

	@Test
	public void testCountAlarmList() {
		System.out.println(this.getAlarmDAO().countAlarmList(14, ""));
	}
}
