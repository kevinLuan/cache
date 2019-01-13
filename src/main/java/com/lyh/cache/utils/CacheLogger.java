package com.lyh.cache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheLogger.class);

	public static Logger getLogger() {
		return LOGGER;
	}

	public static void debug(String format, Object... args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(format, args);
		}
	}

	public static void error(String format, Throwable t) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(format, t);
		}
	}

	public static void info(String format, Object... args) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(format, args);
		}
	}
}
