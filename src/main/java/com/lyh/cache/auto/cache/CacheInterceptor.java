package com.lyh.cache.auto.cache;

import com.lyh.cache.auto.gener.KeyGener;

public interface CacheInterceptor {
  /**
   * 获取动态参数例如：${userId}
   * 
   * @param type
   * @param param
   * @return
   */
  String getDynamicPartKey(KeyGener key);
}
