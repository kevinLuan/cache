package com.lyh.cache.auto.cache;

import java.util.Map;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;

public class CacheHelper {
  private static Map<CacheType, Cache<Object, Object>> CACHE_INSTANCES = Maps.newConcurrentMap();

  /**
   * 获取单例Cache实例
   * 
   * @return
   */
  public static Cache<Object, Object> getCache(CacheType type) {
    if (CACHE_INSTANCES.containsKey(type)) {
      return CACHE_INSTANCES.get(type);
    }
    synchronized (CACHE_INSTANCES) {
      if (!CACHE_INSTANCES.containsKey(type)) {
        CACHE_INSTANCES.put(type, type.createCache());
      }
      return CACHE_INSTANCES.get(type);
    }
  }
}
