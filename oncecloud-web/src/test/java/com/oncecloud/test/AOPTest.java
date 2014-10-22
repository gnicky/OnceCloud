package com.oncecloud.test;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.dao.VMDAO;
import com.oncecloud.manager.VMManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class AOPTest {

	private VMDAO vmdao;
	private VMManager vmManager;
	
	public VMManager getVmManager() {
		return vmManager;
	}

	@Autowired
	public void setVmManager(VMManager vmManager) {
		this.vmManager = vmManager;
	}

	public VMDAO getVmdao() {
		return vmdao;
	}

	@Autowired
	public void setVmdao(VMDAO vmdao) {
		this.vmdao = vmdao;
	}
	
	@Test
	public void aopTest() {
//		System.out.println(this.getVmdao().countVMsOfUser(34));
//		System.out.println(this.getVmManager().getCount(34));
		String ip = null;
		ip = "";
		if (ip.equals("")) {
			System.out.println("ip");
		} else {
			System.out.println("null");
		}
	}
	
}
