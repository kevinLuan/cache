package com.lyh.cache.auto.aspect;

import java.util.Arrays;
import org.aopalliance.intercept.Interceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.lyh.cache.utils.CacheConfig;
import com.lyh.cache.utils.CacheLogger;

@Aspect
@Component
class ProxyJedisClientApi implements Interceptor {

  @Around("execution(* com.lyh.cache.auto.client.JedisClientApi.*(..))")
  public Object proxyRedisLog(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    ProxyProceedingJoinPoint point = new ProxyProceedingJoinPoint(pjp);
    Object result = null;
    try {
      result = pjp.proceed(pjp.getArgs());
      return result;
    } finally {
      long useTime = System.currentTimeMillis() - start;
      String args = toString(point.getArgs());
      if (CacheLogger.getLogger().isDebugEnabled()) {
        CacheLogger.getLogger().debug("prefix:redis|api:{}|args:{}|useTime:{}",
            point.getShortName(), args, useTime);
      } else {
        if (useTime >= CacheConfig.slow_times) {
          CacheLogger.getLogger().info("prefix:redis|api:{}|args:{}|useTime:{}",
              point.getShortName(), args, useTime);
        }
      }
    }
  }

  private static String toString(Object[] args) {
    StringBuilder builder = new StringBuilder(512);
    builder.append("[");
    for (int i = 0; i < args.length; i++) {
      String val;
      if (args[i] == null) {
        val = "null";
      } else if (args[i] != null && args[i].getClass() == String[].class) {
        val = Arrays.toString((String[])args[i]);
      } else {
        val = args[i].toString();
      }
      if (val.indexOf("\n") > 0) {
        val = val.replace("\n", ",");
        if (val.endsWith(",")) {
          val = val.substring(0, val.length() - 1);
        }
        builder.append("{" + val + "}");
      } else {
        builder.append(val);
      }
      if (i != args.length - 1) {
        builder.append(",");
      }
    }
    builder.append("]");
    return builder.toString();
  }
}
