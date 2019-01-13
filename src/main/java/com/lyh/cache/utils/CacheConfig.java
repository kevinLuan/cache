package com.lyh.cache.utils;

public class CacheConfig {
	/**
	 * redis调用慢处理时间定义（单位毫秒）
	 */
	public static volatile int slow_times = 0;//10
	/**
	 * 分布式锁慢处理时间定义（单位毫秒）
	 */
	public static volatile int slow_lock_times = 0;//50
	/***
	 * 分布式Lock选择redis集群节点数量，建议为奇数
	 */
	private static volatile LockLevel lockLevel = LockLevel.high;

	public static LockLevel getLockLevel() {
		return lockLevel;
	}

	public static void setLockLevel(LockLevel value) {
		if (value != null) {
			lockLevel = value;
		}
	}

	/**
	 * 计算分布式lock最小写入成功数量
	 * 
	 * @param totalClusterNodes
	 * @return
	 */
	public static int calcMinSuccessNum(int totalClusterNodes) {
		if (totalClusterNodes < 1) {
			throw new IllegalArgumentException("totalClusterNodes必须大于等于1");
		}
		int minSuccess = (lockLevel.getSelectNodes(totalClusterNodes) / 2) + 1;
		if (minSuccess > totalClusterNodes) {
			return totalClusterNodes;
		} else if (minSuccess < 1) {
			return 1;
		} else {
			return minSuccess;
		}
	}
}
