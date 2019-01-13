package com.lyh.cache.annotation;

public interface Constants {
  String NULL = "<NULL>";
  // cache key前缀，通常使用使用服务名称
  String PREFIX = "redis.cache.prefix";
  String POOL_MAX_TOTAL = "redis.pool.maxTotal";
  String POOL_MAX_IDLE = "redis.pool.maxIdle";
  String POOL_MIN_IDLE = "redis.pool.minIdle";
  String POOL_MAX_WAITMILLS = "redis.pool.maxWaitMills";
  String HOST_PORT = "redis.cache.hostAndPort";
  String PASSWORD = "redis.cache.password";
  String TIMEOUT = "redis.cache.timeout";
}
