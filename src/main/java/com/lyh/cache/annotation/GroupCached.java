package com.lyh.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 待实现
 * 
 * @author SHOUSHEN LUAN
 *
 */
@Deprecated
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupCached {
  String CLASS_NAME = "${Class.Name}";

  /**
   * 动态参数格式：${}，参数下标.属性名称例如：`0.id`,多个参数用`,`分隔. 例如:XXX${0.id,0.name,1.status}xxx
   * 
   * @return
   */
  // "XXXX${0.id}"
  String key() default CLASS_NAME;

  // 生成HashKey的部分,例如：xxx:${0.type,0.pageNo,1.pageSize}
  String hashKey() default "";
}
