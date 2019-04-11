package com.hug.redishelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis 分布式锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisDistributedLock {

    /**
     * redis key 前缀
     */
    String prefix() default "";

    /**
     * redis key value
     */
    String key() default "";

    // 过期时间, 默认1分钟
    long expiration() default 60;

}