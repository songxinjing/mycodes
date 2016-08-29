package com.songxinjing.base.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.songxinjing.base.domain.Config;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ConfigServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	ConfigService configService;

	@Test
	public void saveTest() {
		Config config = new Config();
		config.setName("key");
		config.setValue("value");
		config.setDescp("descp");
		config.setEnable(true);
		String key = (String) configService.save(config);
		Assert.assertEquals("key", key);
	}

}
