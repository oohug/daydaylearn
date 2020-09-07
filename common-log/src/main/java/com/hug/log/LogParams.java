package com.hug.log;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.PARAMETER})
@Documented
public @interface LogParams {
}
