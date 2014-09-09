package com.oncecloud.daemon.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath*:/com/oncecloud/daemon/test/config/application-context.xml" })
public class HomeControllerTest {
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	private WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	@Autowired
	private void setWebApplicationContext(
			WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

	private MockMvc getMockMvc() {
		return mockMvc;
	}

	private void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Before
	public void setUp() {
		this.setMockMvc(MockMvcBuilders.webAppContextSetup(
				this.getWebApplicationContext()).build());
	}

	@Test
	public void testIndex() throws Exception {
		MvcResult result = this.getMockMvc()
				.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.content().string("It works!"))
				.andReturn();

		Assert.assertEquals(200, result.getResponse().getStatus());
	}
}
