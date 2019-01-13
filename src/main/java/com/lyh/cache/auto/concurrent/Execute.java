package com.lyh.cache.auto.concurrent;

public interface Execute<V> {
	/**
	 * 获得执行许可
	 * 
	 * @return
	 * @throws Throwable
	 */
	public V acquire() throws Throwable;

	public V noAcquire() throws Throwable;
}