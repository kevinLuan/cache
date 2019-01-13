package com.lyh.cache.auto.cache;

import com.lyh.cache.auto.gener.KeyGener;

public enum RedisOption {
  SET {
    @Override
    public Object getValue(KeyGener key) {
      return key.getRedisCache().getObject(key.getKey());
    }

    @Override
    public void setValue(KeyGener keyGener, Object value) {
      CacheType type = keyGener.getType();
      String key = keyGener.getKey();
      if (type.getRedisExpireSecond() > 0) {
        keyGener.getRedisCache().set(key, value, type.getRedisExpireSecond());
      } else {
        keyGener.getRedisCache().set(key, value);
      }
    }

    @Override
    public long delete(KeyGener key) {
      return key.getRedisCache().delete(key.getKey());
    }

    @Override
    public boolean isExists(KeyGener keyGener) {
      String key = keyGener.getKey();
      if (keyGener.hasField()) {
        return keyGener.getRedisCache().hExists(key, keyGener.getField());
      } else {
        return keyGener.getRedisCache().exists(key);
      }
    }
  },
  HSET {

    @Override
    public Object getValue(KeyGener key) {
      String field = key.getField();
      return key.getRedisCache().hGet(key.getKey(), field);
    }

    @Override
    public void setValue(KeyGener keyGener, Object value) {
      CacheType type = keyGener.getType();
      String field = keyGener.getField();
      String key = keyGener.getKey();
      keyGener.getRedisCache().hSet(key, field, value);
      if (type.getRedisExpireSecond() > 0) {
        keyGener.getRedisCache().setExpire(key, type.getRedisExpireSecond());
      }
    }

    @Override
    public long delete(KeyGener key) {
      if (key.hasField()) {
        String field = key.getField();
        return key.getRedisCache().hDel(key.getKey(), field);
      } else {
        // String fileds[] = key.getRedisCache().hKeys(key.getKey());
        // try {
        // if (fileds.length > 0) {
        // return key.getRedisCache().hDel(key.getKey(), fileds);
        // } else {
        // return 0;
        // }
        // } finally {
        // 直接删除掉key
        return key.getRedisCache().delete(key.getKey());
        // }
      }
    }

    @Override
    public boolean isExists(KeyGener keyGener) {
      return keyGener.getRedisCache().exists(keyGener.getKey());
    }
  };
  /**
   * 获取redis value
   */
  public abstract Object getValue(KeyGener key);

  /**
   * 设置value to redis
   */
  public abstract void setValue(KeyGener key, Object value);

  /**
   * 删除缓存
   */
  public abstract long delete(KeyGener key);

  public abstract boolean isExists(KeyGener key);
}
