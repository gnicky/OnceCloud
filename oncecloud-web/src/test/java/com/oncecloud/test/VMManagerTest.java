package com.oncecloud.test;

import java.util.List;

import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.dao.ForwardPortDAO;
import com.oncecloud.entity.ForwardPort;
import com.oncecloud.manager.ImageManager;
import com.oncecloud.manager.SRManager;
import com.oncecloud.manager.VMManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class VMManagerTest {
	private VMManager vmManager;
	private SRManager srManager;
	private ForwardPortDAO rForwardPortDAO;
	private ImageManager imageManager;

	public ImageManager getImageManager() {
		return imageManager;
	}

	@Autowired
	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	public ForwardPortDAO getrForwardPortDAO() {
		return rForwardPortDAO;
	}

	@Autowired
	public void setrForwardPortDAO(ForwardPortDAO rForwardPortDAO) {
		this.rForwardPortDAO = rForwardPortDAO;
	}

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
	public void testImage() {
//		this.getVmManager().templateToVM("bb335e71-7996-3fb5-1be5-050256028e93");
//		this.getVmManager().vmToTemplate("bb335e71-7996-3fb5-1be5-050256028e93");
//		this.getVmManager().templateToVM("e9e2c221-1633-1905-815a-e9497f8f4b7a");
//		this.getVmManager().vmToTemplate("e9e2c221-1633-1905-815a-e9497f8f4b7a");
//		System.out.println(this.getVmManager().checkImage("e9e2c221-1633-1905-815a-e9497f8f4b7a"));
	}

	/*@Test
	public void test() {
		// this.getVmManager().saveToDataBase("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
		// "iii", 36, 1, "oo","192.168.1.1");
		// this.getVmManager().addMac("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
		// "instance", "ovs1", "6");
//		 this.getVmManager().getMacs("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
//		 "instance");
//		this.getVmManager().modifyVnet("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
//				"instance", "7", "98824f2b-2e0c-7f63-f534-335197eb3c42");
//		this.getVmManager().deleteMac("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
//				"instance", "e6af192b-cbef-2b7c-0d58-c7314953f257");
//		this.getVmManager().modifyPhysical("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
//				"instance", "ovs1", "98824f2b-2e0c-7f63-f534-335197eb3c42");
//		this.getVmManager().getNets("fc6a1d4d-aa7a-46e2-afb5-0740839e4e72",
//				"instance");
	}*/

	// @Test
	// public void testList() {
	// JSONArray ja =
	// this.getVmManager().getISOList("ee35fa2e-0916-4f85-95ed-2f665df1d479");
	// System.out.println(ja);
	// String uuidString = UUID.randomUUID().toString();
	// ForwardPort pf = new ForwardPort();
	// pf.setPfUuid("5e0ded46-3ab5-4a60-a65d-5ccc9f436485");
	// boolean result = this.getrForwardPortDAO().deletePF(pf);
	// List<ForwardPort> list =
	// this.getrForwardPortDAO().getpfListByRouter("742c6bd4-6bde-41bb-88c7-0ac773e30264");
	/*
	 * pf.setPfUuid(uuidString); pf.setPfName("8080"); pf.setPfProtocal("TCP");
	 * pf.setPfSourcePort(Integer.parseInt("8080"));
	 * pf.setPfInteranlIP("192.168.1.2");
	 * pf.setPfInternalPort(Integer.parseInt("8080"));
	 * pf.setRouterUuid("21df858d-6d8b-442c-9760-30c76133dabf"); boolean result
	 * = this.getrForwardPortDAO().addPF(pf);
	 */
	// System.out.println(list.size());
	// }

	// @Test
	// public void testSRList() {
	// JSONArray ja =
	// this.getSrManager().getRealSRList("ee35fa2e-0916-4f85-95ed-2f665df1d479");
	// // System.out.println(ja);
	// }
	//
	// @Test
	// public void createTest() {
	// String vmUuid = "3620ffa7-94c8-54b4-475c-7b95742d41f7";
	// String isoUuid = "0ed7f8eb-0fc6-fe9b-1a96-bd51c1b95c9e";
	// String srUuid = "a41a7317-5f25-4a8a-a63a-ee572e654dab";
	// String poolUuid = "ee35fa2e-0916-4f85-95ed-2f665df1d479";
	// // this.getVmManager().createVMByISO(vmUuid, isoUuid, srUuid, "i-" +
	// vmUuid.substring(0, 8), 1, 1024, 10, poolUuid);
	// this.getVmManager().createVMByISO(vmUuid, isoUuid, srUuid, "i-" +
	// vmUuid.substring(0, 8), 1, 1024, 10, poolUuid, 1);
	// }
}
