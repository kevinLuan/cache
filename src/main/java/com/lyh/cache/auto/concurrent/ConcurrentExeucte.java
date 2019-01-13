package com.lyh.cache.auto.concurrent;

public final class ConcurrentExeucte {
	private final boolean lockSelf;
	private final Object lock;
	private final String key;
	private final ConcurrentLock concurrentLock;

	/***
	 * Lock 执行器
	 * 
	 * @param key
	 *            锁定key
	 * @param lock
	 * @param lockSelf
	 * @param concurrentLock
	 */
	public ConcurrentExeucte(String key, Object lock, boolean lockSelf, ConcurrentLock concurrentLock) {
		this.lock = lock;
		this.key = key;
		this.lockSelf = lockSelf;
		this.concurrentLock = concurrentLock;
	}

	public boolean isLock() {
		return lockSelf;
	}

	public <T> T run(Execute<T> execute) throws Throwable {
		if (this.isLock()) {
			try {
				return execute.acquire();
			} finally {
				this.releaseLock();
			}
		} else {
			this.waitRequest();
			return execute.noAcquire();
		}
	}

	/**
	 * 释放锁
	 */
	private void releaseLock() {
		synchronized (lock) {
			lock.notifyAll();
		}
		concurrentLock.releaseLock(key);
	}

	/**
	 * 阻塞当前请求
	 * 
	 * @param key
	 */
	private void waitRequest() {
		synchronized (lock) {
			try {
				lock.wait(100);
			} catch (InterruptedException e) {
			}
		}
	}
}
