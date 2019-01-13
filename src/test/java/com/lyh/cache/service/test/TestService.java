package com.lyh.cache.service.test;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.lyh.cache.annotation.CacheEvicted;
import com.lyh.cache.annotation.Cached;

@Service
public class TestService {
  static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

  @Cached(params = "0", expire = 5)
  public int test_get(int id, int def) {
    LOGGER.error("执行方法 test_get:" + id + "," + def);
    return def;
  }

  @Cached(key = "getList", params = "0,1,2", expire = 10)
  public Map<String, Object> getList(String name, int start, int endIndex) {
    LOGGER.error("执行方法getList----name:" + name + "|start:" + start + "|endIndex:" + endIndex);
    Map<String, Object> data = new HashMap<>();
    data.put("a", "aa");
    data.put("b", "sf");
    data.put("name", name);
    data.put("start", start);
    data.put("endIndex", endIndex);
    return data;
  }

  @CacheEvicted(params = {"0"})
  public boolean clear(int i) {
    System.out.println("执行Clear(" + i + ")");
    return true;
  }

  @Cached(key = "test_map_param", params = "0.a,0.b,0.100")
  public Object testMapParam(Map<Object, Object> map) {
    System.out.println("--->>>>执行testMapParam调用.......");
    return "OK";
  }
}
