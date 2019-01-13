package com.lyh.cache.auto.cache;

import com.google.common.cache.Cache;
import com.lyh.cache.encoder.JsonSerialize;

public interface CacheType {
  /**
   * 创建CACHE实例
   * 
   * @return
   */
  Cache<Object, Object> createCache();

  /**
   * 获取单例Cache实例 <br/>
   * <strong>通常子类不需要重写该方法，如需重写，请确保返回的cache实例是单例 </strong>
   * 
   * @return
   */
  default Cache<Object, Object> getCache() {
    return CacheHelper.getCache(this);
  }

  /**
   * 定义redis缓存key前缀（确保业务唯一性，避免冲突）
   * 
   * @return
   */
  String defineUniquePrefix();

  /**
   * 定义错误提示语
   * 
   * @return
   */
  String defineErrorTip();

  /**
   * 获取系统名称
   * 
   * @return
   */
  String getSystemName();

  default boolean supportRedis() {
    return true;
  }

  default RedisOption getRedisOption() {
    return RedisOption.HSET;
  }

  /**
   * 获取过期时间（单位:秒）
   * 
   * @return
   */
  default int getRedisExpireSecond() {
    return 60;
  }

  default String encode(Object param) {
    final String cacheKey;
    if (param == null) {
      cacheKey = "null";
    } else {
      cacheKey = JsonSerialize.encode(param);
    }
    return cacheKey;
  }
}
