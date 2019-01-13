package com.lyh.cache.utils;

public enum LockLevel {
	high(Integer.MAX_VALUE), middle(5), low(3);
	private int selectNodes;

	private LockLevel(int selectNodes) {
		this.selectNodes = selectNodes;
	}

	public int getSelectNodes(int totalClusterNodes) {
		if (totalClusterNodes > this.selectNodes) {
			return this.selectNodes;
		} else {
			return totalClusterNodes;
		}
	}

	public int getValue() {
		return selectNodes;
	}
}
