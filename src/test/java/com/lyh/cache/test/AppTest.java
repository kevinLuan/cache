package com.lyh.cache.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.lyh.cache.service.test.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cache.xml"})
public class AppTest {
  static final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);
  @Autowired
  private TestService testService;

  @Test
  public void test_list() {
    testService.getList("zhangsan", 1, 10);
    testService.getList("zhangsan", 1, 10);
    testService.getList("zhangsan", 11, 20);
    testService.getList("zhangsan", 21, 30);
  }

  @Test
  public void test_cache() throws InterruptedException {
    int value = testService.test_get(5, 20);
    Assert.assertEquals(20, value);
    for (int i = 0; i < 10; i++) {
      value = testService.test_get(5, 30);
      Assert.assertEquals(20, value);
    }
    testService.clear(5);
    value = testService.test_get(5, 10);
    Assert.assertEquals(10, value);
  }
}
