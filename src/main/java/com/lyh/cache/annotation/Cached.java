package com.lyh.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {
  /**
   * 生成CACHE key的参数，每个参数之间使用“,”分割
   * <p>
   * 例如：“0,1.id,2.name”
   * </p>
   */
  String params() default "";

  /**
   * 如果设置Key为空，默认使用:“类名”
   */
  String key() default "";

  /**
   * 过期时间（单位：秒）
   */
  int expire() default 60;

  /**
   * 是否Cache NULL value
   */
  boolean cacheNull() default false;

  /**
   * 是否激活缓存穿透处理
   * 
   * @return
   */
  boolean enablePenetration() default true;
}
