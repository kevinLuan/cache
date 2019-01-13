package com.lyh.cache.auto.gener;

import com.lyh.cache.auto.cache.CacheType;

/**
 * REDIS HSET KEY 生成器
 * 
 * @author SHOUSHEN LUAN
 *
 */
public class HSetKeyGenerator extends KeyGener {

  public HSetKeyGenerator(CacheType type) {
    super(type);
  }

  public static HSetKeyGenerator of(CacheType type) {
    return new HSetKeyGenerator(type);
  }

  public String getKey() {
    StringBuilder builder = new StringBuilder();
    builder.append(type.defineUniquePrefix())//
        .append(".").append(type.getSystemName());
    String group = getGroup();
    if (group.length() > 0) {
      builder.append(":").append(group);
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return getKey();
  }
}
