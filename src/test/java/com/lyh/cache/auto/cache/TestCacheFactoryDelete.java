package com.lyh.cache.auto.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.lyh.cache.auto.cache.CacheFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cache.xml"})
public class TestCacheFactoryDelete {
  @Autowired
  private CacheFactory cacheFactory;

  @Before
  public void init() {}

  @Test
  public void test() {
    cacheFactory.delRedisAllCache(SimpleCacheType.TEST_HASH_SET);
    for (int i = 0; i < 10; i++) {
      final int num = i;
      int value = cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, i, () -> num * 10);
      Assert.assertEquals(num * 10, value);
    }
    for (int i = 0; i < 10; i++) {
      final int num = i;
      int value = cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, i, () -> null);
      Assert.assertEquals(num * 10, value);
    }
    cacheFactory.delRedisAllCache(SimpleCacheType.TEST_HASH_SET);
    cacheFactory.delLocalAllCache(SimpleCacheType.TEST_HASH_SET);
    for (int i = 0; i < 10; i++) {
      final int num = i;
      int value = cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, i, () -> num + 1);
      Assert.assertEquals(num + 1, value);
    }

    String value = cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, null, () -> null);
    Assert.assertNull(value);
    value = cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, null, () -> "NO");
    Assert.assertEquals("NO", value);
    value = cacheFactory.doExecute(SimpleCacheType.TEST_HASH_SET, "abc", () -> null);
    Assert.assertNull(value);
  }
}
