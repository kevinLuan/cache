package com.lyh.cache.auto.gener;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import com.lyh.cache.auto.cache.CacheFactory;
import com.lyh.cache.auto.cache.CacheType;
import com.lyh.cache.auto.cache.RedisOption;
import com.lyh.cache.auto.cache.CacheFactory.Func;
import com.lyh.cache.auto.client.JedisClientApi;

public abstract class KeyGener {
  protected CacheType type;
  protected String globalPartKey;
  protected String partKey;
  protected String _requestParam;
  protected Object request;
  protected CacheFactory cacheFactory;

  KeyGener(CacheType type) {
    Objects.requireNonNull(type);
    this.type = type;
  }

  protected KeyGener setGlobalPartKey(String globalPartKey) {
    this.globalPartKey = globalPartKey;
    return this;
  }

  public String getGroup() {
    if (StringUtils.isNotBlank(globalPartKey) && StringUtils.isNotBlank(partKey)) {
      return globalPartKey + partKey;
    } else if (StringUtils.isNotBlank(globalPartKey)) {
      return globalPartKey;
    } else if (StringUtils.isNotBlank(partKey)) {
      return partKey;
    }
    return "";
  }

  public KeyGener setGroup(Object userId) {
    if (userId == null) {
      throw new IllegalArgumentException("参数不能为空");
    }
    this.partKey = String.valueOf(userId);
    return this;
  }

  /**
   * 设置请求参数
   * 
   * @param requestParam
   * @return
   */
  KeyGener setRequest(Object requestParam) {
    this.request = requestParam;
    if (requestParam != null) {
      this._requestParam = type.encode(requestParam);
    } else {
      this._requestParam = "";
    }
    return this;
  }

  public boolean hasField() {
    return this.request != null;
  }

  public String getField() {
    return _requestParam;
  }

  /**
   * 获取本地内存key
   * 
   * @return
   */
  public final String getLocalCacheKey() {
    StringBuilder builder = new StringBuilder();
    String group = getGroup();
    if (group.length() > 0) {
      builder.append(group);
    }
    if (builder.length() > 0) {
      builder.append(":");
    }
    builder.append(getField());
    return builder.toString();
  }

  /**
   * 获取redis key
   * 
   * @return
   */
  public abstract String getKey();

  /**
   * 获取基础Key
   * 
   * @param cacheFactory
   * @param type
   * @param param
   * @return
   */
  public static KeyGener make(CacheFactory cacheFactory, CacheType type, Object param) {
    Objects.requireNonNull(cacheFactory);
    Objects.requireNonNull(type);
    KeyGener generator;
    if (type.getRedisOption() == RedisOption.HSET) {
      generator = HSetKeyGenerator.of(type);
    } else {
      generator = SetKeyGenerator.of(type);
    }
    if (cacheFactory.hasGlobalInterceptor()) {
      String partKey = cacheFactory.getGlobalInterceptor().getDynamicPartKey(generator);
      generator.setGlobalPartKey(partKey);
    }
    generator.setRequest(param);
    generator.cacheFactory = cacheFactory;
    return generator;
  }

  public JedisClientApi getRedisCache() {
    return cacheFactory.getRedis();
  }

  public CacheType getType() {
    return type;
  }

  public <T> T doExecute(Func func) {
    return this.cacheFactory.doExecute(this, func);
  }

  /**
   * 删除缓存
   * 
   * @return
   */
  public long delCache() {
    return this.cacheFactory.delCache(this);
  }

  public boolean isExists() {
    return this.getType().getRedisOption().isExists(this);
  }
}
