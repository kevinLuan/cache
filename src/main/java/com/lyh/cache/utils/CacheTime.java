package com.lyh.cache.utils;

/**
 * 缓存过期时间
 * 
 * @author SHOUSHEN LUAN
 *
 */
public enum CacheTime {
	/**
	 * 缓存过期时间单位-年
	 */
	YEAR {
		@Override
		public int getMultiple() {
			return 12;
		}

		@Override
		public CacheTime getParent() {
			return CacheTime.MONTH;
		}

	},
	/**
	 * 缓存过期时间单位-月，目前是按自然月30天
	 */
	MONTH {
		@Override
		public int getMultiple() {
			return 30;
		}

		@Override
		public CacheTime getParent() {
			return CacheTime.DAYS;
		}
	},
	/**
	 * 缓存过期时间单位-天
	 */
	DAYS {
		@Override
		public int getMultiple() {
			return 24;
		}

		@Override
		public CacheTime getParent() {
			return HOURS;
		}
	},
	/**
	 * 缓存过期时间单位-小时
	 */
	HOURS {
		@Override
		public int getMultiple() {
			return 60;
		}

		@Override
		public CacheTime getParent() {
			return MINUTES;
		}
	},
	/**
	 * 缓存过期时间单位-分
	 */
	MINUTES {
		@Override
		public int getMultiple() {
			return 60;
		}

		@Override
		public CacheTime getParent() {
			return SECONDS;
		}
	},
	/**
	 * 缓存过期时间单位-秒
	 */
	SECONDS {
		@Override
		public CacheTime getParent() {
			return this;
		}

		@Override
		public int getMultiple() {
			return 1;
		}

		@Override
		public int one() {
			return 1;
		}

		@Override
		public int two() {
			return 2;
		}

		@Override
		public int five() {
			return 5;
		}

		@Override
		public int ten() {
			return 10;
		}

		@Override
		public int fifteen() {
			return 15;
		}

		@Override
		public int thirty() {
			return 30;
		}

	};

	public abstract int getMultiple();

	public abstract CacheTime getParent();

	public int one() {
		return getMultiple() * getParent().one();
	}

	public int two() {
		return getMultiple() * getParent().two();
	}

	public int five() {
		return getMultiple() * getParent().five();
	}

	public int ten() {
		return getMultiple() * getParent().ten();
	}

	public int fifteen() {
		return getMultiple() * getParent().fifteen();
	}

	public int thirty() {
		return getMultiple() * getParent().thirty();
	}
}
