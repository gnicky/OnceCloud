package com.oncecloud.test;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.manager.SRManager;
import com.oncecloud.manager.VMManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class VMManagerTest {
	private VMManager vmManager;
	private SRManager srManager;
	
	public VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	public void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}
	
	public SRManager getSrManager() {
		return srManager;
	}

	@Autowired
	public void setSrManager(SRManager srManager) {
		this.srManager = srManager;
	}

	@Test
	public void testList() {
		JSONArray ja = this.getVmManager().getISOList("ee35fa2e-0916-4f85-95ed-2f665df1d479");
		System.out.println(ja);
	}
	
	@Test
	public void testSRList() {
		JSONArray ja = this.getSrManager().getRealSRList("ee35fa2e-0916-4f85-95ed-2f665df1d479");
//		System.out.println(ja);
	}
	
	@Test
	public void createTest() {
		String vmUuid = "3620ffa7-94c8-54b4-475c-7b95742d41f7";
		String isoUuid = "0ed7f8eb-0fc6-fe9b-1a96-bd51c1b95c9e";
		String srUuid = "a41a7317-5f25-4a8a-a63a-ee572e654dab";
		String poolUuid = "ee35fa2e-0916-4f85-95ed-2f665df1d479";
		this.getVmManager().createVMByISO(vmUuid, isoUuid, srUuid, "i-" + vmUuid.substring(0, 8), 1, 1024, 10, poolUuid, 1);
	}
}
