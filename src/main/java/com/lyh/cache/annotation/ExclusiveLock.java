package com.lyh.cache.annotation;
//package com.look.cache.annotation;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * 分布式排他锁
// * 
// * @author SHOUSHEN LUAN
// */
//@Documented
//@Target({ ElementType.METHOD })
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ExclusiveLock {
//	/**
//	 * 如果设置Key为空，默认使用:“类名_方法”
//	 */
//	String key() default "";
//
//	/**
//	 * 生成CACHE key的参数，每个参数之间使用“,”分割
//	 * <p>
//	 * 例如：“0,1.id,2.name”
//	 * </p>
//	 */
//	String param();
//
//	/**
//	 * 默认等待200毫秒重试
//	 * 
//	 * @return
//	 */
//	int retryTimer() default 200;// ms
//
//	/**
//	 * 获得锁失败后是否重试。
//	 * 
//	 * @return
//	 */
//	boolean isRetry() default false;
//
//	/**
//	 * 过期时间（当自动释放锁失败时，会根据这个时间进行过期）
//	 * <p/>
//	 * 单位：秒
//	 * 
//	 * @return
//	 */
//	int expire() default 60 * 10;// 默认自动释放锁的时间为10分钟，单位秒
//
//}
