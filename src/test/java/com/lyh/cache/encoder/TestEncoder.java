package com.lyh.cache.encoder;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import com.lyh.cache.encoder.HessianEncode;

public class TestEncoder {
	@Test
	public void test() {
		User user = new User();
		user.bigDecimal = new BigDecimal("1900.12345");
		byte[] data = new HessianEncode().encode(user);
		user = (User) new HessianEncode().decode(data);
		System.out.println(user.toString());
		String expected = "{\"num\":1,\"bigDecimal\":1900.12345,\"bool\":true,\"f\":1.234,\"d\":1.123}";
		Assert.assertEquals(expected, user.toString());
	}
}
