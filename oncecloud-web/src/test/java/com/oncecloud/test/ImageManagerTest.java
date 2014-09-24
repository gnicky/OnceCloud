package com.oncecloud.test;

import java.net.MalformedURLException;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.once.xenapi.Connection;
import com.once.xenapi.Host;
import com.once.xenapi.Types;
import com.once.xenapi.Types.BadServerResponse;
import com.once.xenapi.Types.SessionAuthenticationFailed;
import com.once.xenapi.Types.XenAPIException;
import com.oncecloud.manager.ImageManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class ImageManagerTest {
	private ImageManager imageManager;
	private String[] sa = {"e9e2c221-1633-1905-815a-e9497f8f4b7a"};
	public ImageManager getImageManager() {
		return imageManager;
	}

	@Autowired
	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	@Test
	public void testList() {
		this.getImageManager().shareImages("ee35fa2e-0916-4f85-95ed-2f665df1d479", "df434994-d8e8-46cf-8560-611b8ee80c4c", sa);
		Connection conn = null;
		try {
			conn = new Connection("http://133.133.135.12:9363", "root", "onceas");
			Host.migrateTemplate(conn, Types.toVM("e9e2c221-1633-1905-815a-e9497f8f4b7a"), "e9e2c221-1633-1905-815a-e9497f8f4bbb", "df434994-d8e8-46cf-8560-611b8ee80c4c");
		} catch (BadServerResponse e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SessionAuthenticationFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XenAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
