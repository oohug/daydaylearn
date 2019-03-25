package com.hug.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=IdcardValidator.class)
public @interface IDCardNo {
	String message() default "身份证格式不正确";
	Class<?>[] groups() default {};
	
	//负载 
    Class<? extends Payload>[] payload() default { };
}
