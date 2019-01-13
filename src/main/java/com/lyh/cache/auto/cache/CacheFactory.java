package com.lyh.cache.auto.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.lyh.cache.auto.client.JedisClientApi;
import com.lyh.cache.auto.gener.KeyGener;
import com.lyh.cache.error.NotSupportException;
import com.lyh.cache.utils.CacheLogger;

// @Scope("prototype")
@Component
public class CacheFactory {

  @Autowired
  protected JedisClientApi jedisClientApi;
  static volatile CacheInterceptor globalInterceptor;

  public boolean hasGlobalInterceptor() {
    return globalInterceptor != null;
  }

  public CacheInterceptor getGlobalInterceptor() {
    return globalInterceptor;
  }

  /**
   * 设置全局拦截器
   * 
   * @param cacheInterceptor
   * @return
   */
  public CacheFactory setGlobalInterceptor(CacheInterceptor cacheInterceptor) {
    CacheFactory.globalInterceptor = cacheInterceptor;
    return this;
  }

  public <T> T getCache(CacheType type, Object param) {
    KeyGener keyGener = KeyGener.make(this, type, param);
    return this.getCache(keyGener);
  }

  @SuppressWarnings("unchecked")
  public <T> T getCache(KeyGener keyGener) {
    final String cacheKey = keyGener.getLocalCacheKey();
    CacheType type = keyGener.getType();
    try {
      return (T) type.getCache().get(cacheKey, () -> {
        if (type.supportRedis()) {
          Object value = type.getRedisOption().getValue(keyGener);
          return value;
        }
        return null;
      });
    } catch (Exception e) {
      if (isRtnNull(e)) {
        return null;
      }
      CacheLogger.error("加载type:`" + type + "`,cacheKey:`" + cacheKey + "`数据出错", e);
      throw new RuntimeException(type.defineErrorTip());
    }
  }

  private Object parseRawParam(Object... args) {
    if (args == null || args.length == 0) {
      return null;
    } else if (args.length == 1) {
      return args[0];
    } else {
      return args;
    }
  }

  /**
   * 创建缓存key
   * 
   * @param args
   * @return
   */
  public KeyGener createKey(CacheType type, Object... args) {
    Object param = parseRawParam(args);
    return KeyGener.make(this, type, param);
  }

  public <T> T doExecute(CacheType type, Object param, Func func) {
    KeyGener keyGenerator = KeyGener.make(this, type, param);
    return this.doExecute(keyGenerator, func);
  }

  @SuppressWarnings("unchecked")
  public <T> T doExecute(KeyGener keyGener, Func func) {
    final String cacheKey = keyGener.getLocalCacheKey();
    try {
      CacheType type = keyGener.getType();
      return (T) type.getCache().get(cacheKey, () -> {
        if (type.supportRedis()) {
          Object value = type.getRedisOption().getValue(keyGener);
          if (value != null) {
            return value;
          }
          value = func.load();
          if (value != null) {
            type.getRedisOption().setValue(keyGener, value);
          }
          return value;
        } else {
          return func.load();
        }
      });
    } catch (Exception e) {
      if (isRtnNull(e)) {
        return null;
      }
      CacheLogger.error("加载type:`" + keyGener.getType() + "`,cacheKey:`" + cacheKey + "`数据出错", e);
      throw new RuntimeException(keyGener.getType().defineErrorTip());
    }
  }


  public long delCache(CacheType type, Object param) {
    return delCache(KeyGener.make(this, type, param));
  }

  public long delCache(KeyGener keyGener) {
    CacheType type = keyGener.getType();
    if (keyGener.hasField()) {
      String localKey = keyGener.getLocalCacheKey();
      type.getCache().invalidate(localKey);
    } else {
      type.getCache().invalidateAll();
    }
    return type.getRedisOption().delete(keyGener);
  }

  public void delLocalAllCache(CacheType type) {
    type.getCache().invalidateAll();
  }

  public long delRedisAllCache(CacheType type) {
    if (type.getRedisOption() == RedisOption.HSET) {
      return type.getRedisOption().delete(KeyGener.make(this, type, null));
    } else {
      throw new NotSupportException("当前CacheType:`" + type + "`不支持该操作");
    }
  }

  private boolean isRtnNull(Exception e) {
    if (e instanceof CacheLoader.InvalidCacheLoadException) {
      CacheLoader.InvalidCacheLoadException loadException = (InvalidCacheLoadException) e;
      if (loadException.getMessage() != null) {
        if (loadException.getMessage().startsWith("CacheLoader returned null for key")) {
          return true;
        }
      }
    }
    return false;
  }

  public JedisClientApi getRedis() {
    return this.jedisClientApi;
  }

  public static interface Func {
    /**
     * 加载数据
     * 
     * @return
     */
    Object load();
  }

}
