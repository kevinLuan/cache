package com.lyh.cache.auto.concurrent;

import com.lyh.cache.annotation.Constants;

public final class CacheHolder {
  public Object value;
  public Throwable throwable;

  /**
   * 获取原始数据
   * 
   * @return
   */
  public Object getRawValue() {
    if (value.equals(Constants.NULL)) {
      return null;
    }
    return this.value;
  }

  /**
   * 是否存在缓存数据
   * 
   * @return
   */
  public boolean isExistsCache() {
    return value != null;
  }

  /**
   * 是否存在错误
   * 
   * @return
   */
  public boolean isExistsError() {
    return throwable != null;
  }

  public static CacheHolder newResult(Object value) {
    CacheHolder holder = new CacheHolder();
    holder.value = value;
    return holder;
  }

  public static CacheHolder newError(Throwable throwable) {
    CacheHolder holder = new CacheHolder();
    holder.throwable = throwable;
    return holder;
  }
}
