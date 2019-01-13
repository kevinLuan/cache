package com.lyh.cache.test;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.lyh.cache.service.test.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cache.xml"})
public class MapParamTest {
  @Autowired
  private TestService testService;

  @Test
  public void test_ok() {
    Map<Object, Object> map = new HashMap<>();
    map.put("a", "A");
    map.put("b", "B");
    map.put(100, "100x");
    Assert.assertEquals("OK", testService.testMapParam(map));
    Assert.assertEquals("OK", testService.testMapParam(map));
    Assert.assertEquals("OK", testService.testMapParam(map));
    Assert.assertEquals("OK", testService.testMapParam(map));
    Assert.assertEquals("OK", testService.testMapParam(map));
  }

  @Test
  public void test_error() {
    Map<Object, Object> map = new HashMap<>();
    try {
      map.put("a", "A");
      testService.testMapParam(map);
      Assert.fail("没有出现预期的错误");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("fieldName is empty or not found:`b`", e.getMessage());
    }
  }
}
