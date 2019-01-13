package com.lyh.cache.auto.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.lyh.cache.auto.cache.CacheFactory;
import com.lyh.cache.auto.cache.CacheInterceptor;
import com.lyh.cache.auto.client.JedisClientApi;
import com.lyh.cache.auto.gener.KeyGener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cache.xml"})
public class TestCacheFactory {
  @Autowired
  private CacheFactory globalCacheFactory;
  @Autowired
  private CacheFactory cacheFactory;
  @Autowired
  protected JedisClientApi jedisClientApi;

  @Before
  public void init() {
    // 初始化全局拦截器
    globalCacheFactory.setGlobalInterceptor(new CacheInterceptor() {
      public String getDynamicPartKey(KeyGener key) {
        return "Global";
      }
    });
  }

  @Test
  public void testGenerKey_set() {
    for (int i = 0; i < 100; i++) {
      String request = "kevin" + i;
      KeyGener generator = cacheFactory.createKey(SimpleCacheType.TEST_SET, request);
      String key = "test_set.sys_name:Global:\"" + request + "\"";
      Object value = 200;
      Assert.assertEquals(key, generator.getKey());
      Assert.assertNull(globalCacheFactory.getCache(SimpleCacheType.TEST_SET, request));
      jedisClientApi.set(generator.getKey(), value, 1);
      Assert.assertEquals(value, globalCacheFactory.getCache(SimpleCacheType.TEST_SET, request));
      globalCacheFactory.delCache(SimpleCacheType.TEST_SET, request);
      Assert.assertNull(globalCacheFactory.getCache(SimpleCacheType.TEST_SET, request));
    }
  }

  @Test
  public void testGenerKey_hashset() {
    for (int i = 0; i < 100; i++) {
      String request = "hash-kevin" + i;
      KeyGener generator = cacheFactory.createKey(SimpleCacheType.TEST_HASH_SET, request);
      String key = "test_hash_set.sys_name:Global";
      Object value = 200;
      Assert.assertEquals(key, generator.getKey());
      Assert.assertNull(globalCacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, request));
      jedisClientApi.hSet(generator.getKey(), SimpleCacheType.TEST_HASH_SET.encode(request), value);
      Assert.assertEquals(value,
          globalCacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, request));
      globalCacheFactory.delCache(SimpleCacheType.TEST_HASH_SET, request);
      Assert.assertNull(globalCacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, request));
    }
  }

  @Test
  public void test_set() {
    for (int i = 10; i < 100; i++) {
      Assert.assertNull(cacheFactory.doExecute(SimpleCacheType.TEST_SET, "abc" + i, () -> {
        return null;
      }));
      Assert.assertEquals("返回值", cacheFactory.doExecute(SimpleCacheType.TEST_SET, "abc" + i, () -> {
        return "返回值";
      }));
    }
  }

  @Test
  public void test_hash_set() {
    cacheFactory.delRedisAllCache(SimpleCacheType.TEST_HASH_SET);
    for (int i = 0; i < 100; i++) {
      Assert.assertNull(cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, i, () -> {
        return null;
      }));
      Assert.assertEquals("返回值", cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, i, () -> {
        return "返回值";
      }));
    }
  }

  @Test
  public void test_big_hash() {
    for (int i = 2000; i < 3000; i++) {
      String param_key = "testabc" + i;
      String value = "value->" + param_key;
      Assert.assertEquals(value,
          cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, param_key, () -> {
            return value;
          }));
    }
  }

  @Test
  public void test_delete() {
    for (int i = 0; i < 10; i++) {
      String param_key = "mytest" + i;
      String value = "value->" + i;
      Assert.assertEquals(value,
          cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, param_key, () -> {
            return value;
          }));
    }
    cacheFactory.delCache(SimpleCacheType.TEST_HASH_SET, "mytest0");
    Assert.assertNull(cacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, "mytest0"));
    Assert.assertEquals("value->1",
        cacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, "mytest1"));
    // 删除本地所有cache
    cacheFactory.delLocalAllCache(SimpleCacheType.TEST_HASH_SET);
    Assert.assertEquals("value->1",
        cacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, "mytest1"));
    cacheFactory.delRedisAllCache(SimpleCacheType.TEST_HASH_SET);
    Assert.assertNotNull(cacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, "mytest1"));
    cacheFactory.delLocalAllCache(SimpleCacheType.TEST_HASH_SET);
    Assert.assertNull(cacheFactory.getCache(SimpleCacheType.TEST_HASH_SET, "mytest2"));
  }
}
