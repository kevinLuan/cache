package com.lyh.cache.auto.cache;

import java.util.concurrent.TimeUnit;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lyh.cache.auto.cache.CacheType;
import com.lyh.cache.auto.cache.RedisOption;

public enum SimpleCacheType implements CacheType {
  TEST_HASH_SET {
    @Override
    public Cache<Object, Object> createCache() {
      Cache<Object, Object> cache = CacheBuilder.newBuilder()//
          .maximumSize(100)//
          .expireAfterAccess(4, TimeUnit.SECONDS)//
          .expireAfterWrite(4, TimeUnit.SECONDS)//
          .recordStats().build();
      return cache;
    }

    @Override
    public String defineErrorTip() {
      return "获取用户优惠券列表失败";
    }
  },
  TEST_SET {
    public Cache<Object, Object> createCache() {
      Cache<Object, Object> cache = CacheBuilder.newBuilder()//
          .maximumSize(2000)//
          .expireAfterAccess(4, TimeUnit.SECONDS)//
          .expireAfterWrite(4, TimeUnit.SECONDS)//
          .recordStats().build();
      return cache;
    }

    @Override
    public RedisOption getRedisOption() {
      return RedisOption.SET;
    }

    @Override
    public String defineErrorTip() {
      return "获取促销优惠失败";
    }
  };

  @Override
  public String getSystemName() {
    return "sys_name";
  }

  @Override
  public String defineUniquePrefix() {
    return this.name().toLowerCase();
  }
}
