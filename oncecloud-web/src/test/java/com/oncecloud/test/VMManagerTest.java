package com.oncecloud.test;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oncecloud.manager.VMManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/oncecloud/config/application-context.xml")
public class VMManagerTest {
	private VMManager vmManager;

	public VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	public void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}
	
	@Test
	public void testList() {
		JSONArray ja = this.getVmManager().getISOList("ee35fa2e-0916-4f85-95ed-2f665df1d479");
		System.out.println(ja);
	}
}
