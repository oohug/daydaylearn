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
     * 1. 小于等于0时（默认），返回404
     * 2. 大于0时，向下兼容取版本；如果依然取不到返回404
     */
    int missRule() default 10;
}