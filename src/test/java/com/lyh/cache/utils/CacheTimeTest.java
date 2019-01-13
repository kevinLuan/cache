package com.lyh.cache.utils;

import org.junit.Assert;
import org.junit.Test;
import com.lyh.cache.utils.CacheTime;

public class CacheTimeTest {
  @Test
  public void test_seconds() {
    Assert.assertEquals(1, CacheTime.SECONDS.one());
    Assert.assertEquals(10, CacheTime.SECONDS.ten());
    Assert.assertEquals(30, CacheTime.SECONDS.thirty());

    Assert.assertEquals(60, CacheTime.MINUTES.one());
    Assert.assertEquals(600, CacheTime.MINUTES.ten());
    Assert.assertEquals(1800, CacheTime.MINUTES.thirty());

    Assert.assertEquals(60 * 60, CacheTime.HOURS.one());
    Assert.assertEquals(600 * 60, CacheTime.HOURS.ten());
    Assert.assertEquals(1800 * 60, CacheTime.HOURS.thirty());

    Assert.assertEquals(60 * 60 * 24, CacheTime.DAYS.one());
    Assert.assertEquals(600 * 60 * 24, CacheTime.DAYS.ten());
    Assert.assertEquals(1800 * 60 * 24, CacheTime.DAYS.thirty());

    Assert.assertEquals(60 * 60 * 24 * 30, CacheTime.MONTH.one());
    Assert.assertEquals(600 * 60 * 24 * 30, CacheTime.MONTH.ten());
    Assert.assertEquals(1800 * 60 * 24 * 30, CacheTime.MONTH.thirty());

    Assert.assertEquals(60 * 60 * 24 * 30 * 12, CacheTime.YEAR.one());
    Assert.assertEquals(600 * 60 * 24 * 30 * 12, CacheTime.YEAR.ten());
    Assert.assertEquals(1800 * 60 * 24 * 30 * 12, CacheTime.YEAR.thirty());
  }
}
