package com.lyh.cache.auto.process;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lyh.cache.annotation.CacheEvicted;
import com.lyh.cache.auto.client.JedisClientApi;
import com.lyh.cache.utils.CacheLogger;
import com.lyh.cache.utils.GeneratedKey;

/**
 * 清除缓存注解处理器
 */
@Component
public class CacheEvictedProcess {
  /**
   * 注入需要使用的CACHE Api
   */
  @Autowired
  protected JedisClientApi client;
  @Autowired
  private GeneratedKey generatedKey;

  public void proceed(ProceedingJoinPoint jp, CacheEvicted evicted) throws Throwable {
    assertParam(evicted, jp);
    try {
      String[] keys = evicted.key();
      String[] params = evicted.params();
      String del_keys[] = new String[keys.length];
      for (int i = 0; i < keys.length; i++) {
        del_keys[i] = generatedKey.buildKey(jp, keys[i], params[i]);
      }
      long res = client.delete(del_keys);
      if (CacheLogger.getLogger().isDebugEnabled()) {
        CacheLogger.getLogger().debug("delete key:{},res:{}", Arrays.toString(del_keys), res);
      }
    } catch (Throwable e) {
      CacheLogger.getLogger().error("clearRedisData", e);
    }
  }

  private void assertParam(CacheEvicted evicted, ProceedingJoinPoint jp) {
    String[] keys = evicted.key();
    String[] params = evicted.params();
    if (keys.length != params.length) {
      throw new IllegalArgumentException(
          jp.toLongString() + "jp @CacheEvicted(...) key.length 必须与params.length相同");
    }
  }
}
