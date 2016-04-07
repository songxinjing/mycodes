package com.songxinjing.base.util;

import org.junit.Assert;
import org.junit.Test;

import com.songxinjing.base.util.ConfigPropertyUtil;

public class ConfigPropertyUtilTest {
	@Test
	public void testGetValue() {
		String user = ConfigPropertyUtil.getValue("/dbconfig.properties", "db.user");
		Assert.assertEquals("xjsong",user);
	}

}
