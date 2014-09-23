package com.oncecloud.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oncecloud.manager.ImageManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:./com/oncecloud/config/application-context.xml")
@WebAppConfiguration
public class ImageManagerTest {
	private ImageManager imageManager;
	private String[] sa = {"e9e2c221-1633-1905-815a-e9497f8f4b7a","de6026f4-79a5-2374-b448-a23d11fca994"};
	public ImageManager getImageManager() {
		return imageManager;
	}

	@Autowired
	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	@Test
	public void testList() {
		this.getImageManager().getShareImageList("ee35fa2e-0916-4f85-95ed-2f665df1d479", sa);
	}
	
}
