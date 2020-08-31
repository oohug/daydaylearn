package com.hug.multiapi;

import java.lang.annotation.*;

/**
 * For multi api version control
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ApiVersion {

    /**
     * 版本号 ([1-9]\d*\.?\d*)
     */
    String value();

    /**
     * 匹配不到指定版本时规则
     * 1. 小于等于0（默认） 返回404
     * 2. 大于0时 选择最接近的版本执行
     */
    int missRule() default 10;
}