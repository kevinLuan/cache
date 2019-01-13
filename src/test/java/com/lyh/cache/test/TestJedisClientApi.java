package com.lyh.cache.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.lyh.cache.auto.client.JedisClientApi;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cache.xml"})
public class TestJedisClientApi {
  @Autowired
  private JedisClientApi jedisClientApi;

  @Test
  public void hash_test() {
    String key = "kevin-luan";
    for (int i = 0; i < 100; i++) {
      String field = "f:" + i;
      String value = "v:测试:" + i;
      if (i % 2 == 0) {
        Assert.assertTrue(jedisClientApi.hSetNX(key, field, value));
      } else {
        Assert.assertTrue(jedisClientApi.hSet(key, field, value));
      }
      Assert.assertTrue(jedisClientApi.hExists(key, field));
      Assert.assertEquals(value, jedisClientApi.hGet(key, field));
    }
    Assert.assertEquals(100L, jedisClientApi.hLen(key).longValue());
    // 验证删除
    String del_fields[] = new String[100];
    for (int i = 0; i < 100; i++) {
      String field = "f:" + i;
      del_fields[i] = field;
    }
    Assert.assertEquals(100L, jedisClientApi.hDel(key, del_fields).longValue());
    // 验证null
    for (int i = 0; i < 100; i++) {
      String field = "f:" + i;
      Assert.assertNull(jedisClientApi.hGet(key, field));
    }
  }
}
