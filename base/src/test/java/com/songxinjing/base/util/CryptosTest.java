package com.songxinjing.base.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class CryptosTest {

	protected static final Logger logger = LogManager.getLogger(CryptosTest.class);

	@Test
	public void mac() {
		String input = "foo message";

		// key可为任意字符串
		// byte[] key = "a foo key".getBytes();
		byte[] key = Cryptos.generateHmacSha1Key();
		Assert.assertEquals(20, key.length);

		byte[] macResult = Cryptos.hmacSha1(input.getBytes(), key);
		logger.info("hmac-sha1 key in hex      :" + Encodes.encodeHex(key));
		logger.info("hmac-sha1 in hex result   :" + Encodes.encodeHex(macResult));

		Assert.assertTrue(Cryptos.isMacValid(macResult, input.getBytes(), key));
	}

	@Test
	public void aes() {
		byte[] key = Cryptos.generateAesKey();
		Assert.assertEquals(16, key.length);
		String input = "foo message";

		byte[] encryptResult = Cryptos.aesEncrypt(input.getBytes(), key);
		String descryptResult = Cryptos.aesDecrypt(encryptResult, key);

		logger.info("aes key in hex            :" + Encodes.encodeHex(key));
		logger.info("aes encrypt in hex result :" + Encodes.encodeHex(encryptResult));
		Assert.assertEquals(input, descryptResult);
	}

	@Test
	public void aesWithIV() {
		byte[] key = Cryptos.generateAesKey();
		byte[] iv = Cryptos.generateIV();
		Assert.assertEquals(16, key.length);
		Assert.assertEquals(16, iv.length);
		String input = "foo message";

		byte[] encryptResult = Cryptos.aesEncrypt(input.getBytes(), key, iv);
		String descryptResult = Cryptos.aesDecrypt(encryptResult, key, iv);

		logger.info("aes key in hex            :" + Encodes.encodeHex(key));
		logger.info("iv in hex                 :" + Encodes.encodeHex(iv));
		logger.info("aes encrypt in hex result :" + Encodes.encodeHex(encryptResult));

		Assert.assertEquals(input, descryptResult);
	}
}
