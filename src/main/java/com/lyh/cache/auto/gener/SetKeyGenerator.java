package com.lyh.cache.auto.gener;

import com.lyh.cache.auto.cache.CacheType;

/**
 * REDIS SET KEY生成器
 * 
 * @author SHOUSHEN LUAN
 *
 */
public class SetKeyGenerator extends KeyGener {

  public SetKeyGenerator(CacheType type) {
    super(type);
  }

  public static SetKeyGenerator of(CacheType type) {
    return new SetKeyGenerator(type);
  }

  public String getKey() {
    StringBuilder builder = new StringBuilder();
    builder.append(type.defineUniquePrefix())//
        .append(".").append(type.getSystemName());
    String group = getGroup();
    if (group.length() > 0) {
      builder.append(":").append(group);
    }
    if (hasField()) {
      builder.append(":");
      builder.append(getField());
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return getKey();
  }

  public String getField() {
    return _requestParam;
  }
}
