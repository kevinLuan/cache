package com.lyh.cache.auto.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import com.lyh.cache.utils.CacheLogger;

@Component
public class ConcurrentLock {
  private final static ConcurrentHashMap<String, Object> dataLock = new ConcurrentHashMap<>();

  /**
   * 获取并发执行器
   * 
   * @param key
   * @return
   */
  public ConcurrentExeucte getExecute(String key) {
    Object lock = new Object();
    Object oldLock = dataLock.putIfAbsent(key, lock);
    if (oldLock == null) {
      return new ConcurrentExeucte(key, lock, true, this);
    } else {
      return new ConcurrentExeucte(key, oldLock, false, this);
    }
  }

  /**
   * 阻塞当前请求
   * 
   * @param key
   */
  void waitRequest(Object lock) {
    synchronized (lock) {
      try {
        lock.wait(50);
      } catch (InterruptedException e) {
      }
    }
  }

  // 释放锁
  void releaseLock(String key) {
    try {
      dataLock.remove(key);
    } catch (Throwable t) {
      CacheLogger.getLogger().error("releaseLock(" + key + ")", key);
    }
  }
}
