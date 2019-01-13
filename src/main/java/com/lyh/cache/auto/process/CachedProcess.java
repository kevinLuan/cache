package com.lyh.cache.auto.process;

import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lyh.cache.annotation.Cached;
import com.lyh.cache.annotation.Constants;
import com.lyh.cache.auto.client.JedisClientApi;
import com.lyh.cache.auto.concurrent.CacheHolder;
import com.lyh.cache.auto.concurrent.ConcurrentLock;
import com.lyh.cache.auto.concurrent.Execute;
import com.lyh.cache.utils.CacheLogger;
import com.lyh.cache.utils.GeneratedKey;

@Component
public class CachedProcess {
  /**
   * 注入需要使用的CACHE Api
   */
  @Autowired
  protected JedisClientApi client;
  @Autowired
  private ConcurrentLock concurrentLock;
  @Autowired
  private GeneratedKey generatedKey;

  public Object proceed(ProceedingJoinPoint pjp, Cached cache) throws Throwable {
    String key = generatedKey.buildKey(pjp, cache);
    return doCallProcess(pjp, cache, key, new AtomicInteger(0));
  }

  private Object doCallProcess(ProceedingJoinPoint pjp, Cached cache, String key,
      AtomicInteger counter) throws Throwable {
    CacheHolder value = getCache(key);
    if (value.isExistsCache()) {
      return value.getRawValue();
    } else if (value.isExistsError() || !cache.enablePenetration()) {
      return doInvoke(pjp, cache, key);
    } else {
      if (counter.incrementAndGet() > 3) {
        CacheLogger.getLogger().error("prefix:cache_penetration|concurrent.call:{}|cache_key:{}",
            pjp.toLongString(), key);
        throw new RuntimeException("concurrent cache key timeout");
      }
      return concurrentLock.getExecute(key).run(new Execute<Object>() {
        @Override
        public Object acquire() throws Throwable {
          return doInvoke(pjp, cache, key);
        }

        @Override
        public Object noAcquire() throws Throwable {
          return doCallProcess(pjp, cache, key, counter);
        }
      });
    }
  }

  private Object doInvoke(ProceedingJoinPoint pjp, Cached cache, String key) throws Throwable {
    Object data = pjp.proceed();
    setCache(key, data, cache);
    return data;
  }

  private CacheHolder getCache(String key) {
    try {
      return CacheHolder.newResult(client.getObject(key));
    } catch (Exception ex) {
      CacheLogger.error("getCache->" + key, ex);
      return CacheHolder.newError(ex);
    }
  }

  private void setCache(String key, Object value, Cached cache) {
    if (cache.expire() > 0) {
      if (value == null) {
        value = Constants.NULL;
      }
      try {
        client.set(key, value, cache.expire());
      } catch (Exception ex) {
        CacheLogger.getLogger().error("setCache->" + key + " Error", ex);
      }
    } else {
      throw new IllegalArgumentException("cacheKey:`" + cache.key() + "` expire:`" + cache.expire()
          + "` must be greater than zero");
    }

  }
}
