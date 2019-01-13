package com.lyh.cache.auto.aspect;

import java.lang.reflect.Method;
import org.aopalliance.intercept.Interceptor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lyh.cache.annotation.CacheEvicted;
import com.lyh.cache.annotation.Cached;
import com.lyh.cache.auto.process.CacheEvictedProcess;
import com.lyh.cache.auto.process.CachedProcess;

@Aspect
@Component
public class MethodAspectInteceptor implements Interceptor {
  @Autowired
  private CacheEvictedProcess jedisEvictedProcess;
  @Autowired
  private CachedProcess jedisProcess;

  @Around("@annotation(com.lyh.cache.annotation.Cached)")
  public Object invoke_jedis_cache(ProceedingJoinPoint pjp) throws Throwable {
    ProxyProceedingJoinPoint point = new ProxyProceedingJoinPoint(pjp);
    Cached jedisCacheKey = getJedisCache(point);
    return jedisProcess.proceed(point, jedisCacheKey);
  }

  private Cached getJedisCache(ProxyProceedingJoinPoint point)
      throws NoSuchMethodException, SecurityException {
    Method method = point.getMethod();
    Cached jedisCache = method.getAnnotation(Cached.class);
    if (jedisCache == null) {
      method = point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
      jedisCache = method.getAnnotation(Cached.class);
    }
    return jedisCache;
  }

  private CacheEvicted getJedisCacheEvicted(ProxyProceedingJoinPoint point)
      throws NoSuchMethodException, SecurityException {
    Method method = point.getMethod();
    CacheEvicted cacheEvicted = method.getAnnotation(CacheEvicted.class);
    if (cacheEvicted == null) {
      method = point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
      cacheEvicted = method.getAnnotation(CacheEvicted.class);
    }
    return cacheEvicted;
  }

  @Before(value = "@annotation(com.lyh.cache.annotation.CacheEvicted)")
  public void before(JoinPoint jp) throws Throwable {
    ProxyProceedingJoinPoint point = new ProxyProceedingJoinPoint(jp);
    CacheEvicted cacheEvicted = getJedisCacheEvicted(point);
    jedisEvictedProcess.proceed(point, cacheEvicted);
  }
}
