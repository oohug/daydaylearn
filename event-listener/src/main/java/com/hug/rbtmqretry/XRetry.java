package com.hug.rbtmqretry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface XRetry {

    int retryTimes() default 0;

    /**
     * 重试间隔（单位：秒; 默认 60秒）
     * @return
     */
    int[] step() default {};

    // 过期时间，单位：秒，距离创建时间超过多久不进行重试
    long expire() default 0;
}