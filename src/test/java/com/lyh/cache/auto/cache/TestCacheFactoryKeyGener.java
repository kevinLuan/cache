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
import com.lyh.cache.auto.gener.KeyGener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cache.xml"})
public class TestCacheFactoryKeyGener {
  @Autowired
  private CacheFactory globalCacheFactory;
  @Autowired
  private CacheFactory interCacheFactory;
  @Autowired
  private CacheFactory cacheFactory3;

  @Before
  public void init() {
    globalCacheFactory.setGlobalInterceptor(new CacheInterceptor() {
      public String getDynamicPartKey(KeyGener key) {
        return "GLOBAL";
      }
    });
  }

  @Test
  public void testScope() {
    Assert.assertTrue(globalCacheFactory.hasGlobalInterceptor());
    //
    Assert.assertTrue(interCacheFactory.hasGlobalInterceptor());
    //
    Assert.assertTrue(cacheFactory3.hasGlobalInterceptor());
  }

  @Test
  public void test_key() {
    KeyGener keyGenerator = globalCacheFactory.createKey(SimpleCacheType.TEST_SET, 1, 2);
    Assert.assertEquals("test_set.sys_name:GLOBAL:[1,2]", keyGenerator.getKey());

    keyGenerator = globalCacheFactory.createKey(SimpleCacheType.TEST_HASH_SET, 1, true);
    Assert.assertEquals("test_hash_set.sys_name:GLOBAL", keyGenerator.getKey());
    Assert.assertEquals("[1,true]", keyGenerator.getField());
    keyGenerator =
        interCacheFactory.createKey(SimpleCacheType.TEST_HASH_SET, true).setGroup("Hello");
    Assert.assertEquals("test_hash_set.sys_name:GLOBALHello", keyGenerator.getKey());
    Assert.assertEquals("true", keyGenerator.getField());
    // 未设置局部拦截器
    keyGenerator = cacheFactory3.createKey(SimpleCacheType.TEST_SET, "hello world");
    Assert.assertEquals("test_set.sys_name:GLOBAL:\"hello world\"", keyGenerator.getKey());
    Assert.assertEquals("\"hello world\"", keyGenerator.getField());
  }

  @Test
  public void test() {
    cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET).setGroup(1234).delCache();
    for (int i = 0; i < 100; i++) {
      int param = i;
      String value = cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET, param)//
          .setGroup(1234).doExecute(() -> {
            return "数值：" + param;
          });
      Assert.assertEquals("数值：" + param, value);
      // 删除
      long delRow = cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET, param)//
          .setGroup(1234).delCache();
      Assert.assertTrue(delRow == 1);
      System.out.println("成功删除" + delRow + "行");
      String delVal = cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET, param)//
          .setGroup(1234).doExecute(() -> {
            return "TEST";
          });
      Assert.assertEquals("TEST", delVal);
    }
    Assert.assertTrue(
        cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET).setGroup(1234).delCache() == 1);
    Assert.assertFalse(
        cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET).setGroup(1234).isExists());
    // 无法命中缓存
    for (int i = 0; i < 100; i++) {
      int num = i;
      String value = cacheFactory3.createKey(SimpleCacheType.TEST_HASH_SET, num)//
          .setGroup(1234).doExecute(() -> {
            return "ABC" + num;
          });
      Assert.assertEquals("ABC" + num, value);
    }

  }
}
