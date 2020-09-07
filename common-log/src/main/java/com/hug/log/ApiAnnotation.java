package com.hug.log;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ApiAnnotation {
    String logMessage() default "";
}
