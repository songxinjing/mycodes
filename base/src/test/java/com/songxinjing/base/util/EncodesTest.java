package com.songxinjing.base.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class EncodesTest {

	protected static final Logger logger = LogManager.getLogger(EncodesTest.class);

	@Test
	public void hexEncode() {
		String input = "haha,i am a very long message";
		String result = Encodes.encodeHex(input.getBytes());
		Assert.assertEquals(input, new String(Encodes.decodeHex(result)));
	}

	@Test
	public void base64Encode() {
		String input = "haha,i am a very long message";
		String result = Encodes.encodeBase64(input.getBytes());
		Assert.assertEquals(input, new String(Encodes.decodeBase64(result)));
	}

	@Test
	public void base64UrlSafeEncode() {
		String input = "haha,i am a very long message";
		String result = Encodes.encodeUrlSafeBase64(input.getBytes());
		Assert.assertEquals(input, new String(Encodes.decodeBase64(result)));
	}

	@Test
	public void urlEncode() {
		String input = "http://locahost/?q=中文&t=1";
		String result = Encodes.urlEncode(input);
		logger.info(result);
		Assert.assertEquals(input, Encodes.urlDecode(result));
	}

	@Test
	public void xmlEncode() {
		String input = "1>2";
		String result = Encodes.escapeXml(input);
		Assert.assertEquals("1&gt;2", result);
		Assert.assertEquals(input, Encodes.unescapeXml(result));
	}

	@Test
	public void html() {
		String input = "1>2";
		String result = Encodes.escapeHtml(input);
		Assert.assertEquals("1&gt;2", result);
		Assert.assertEquals(input, Encodes.unescapeHtml(result));
	}
}
