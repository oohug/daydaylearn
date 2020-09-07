package com.hug.log.enhances;

import com.hug.log.LogIdInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WebMvcConfigurerAdapter config （optional）,由引入方决定是否使用；
 * 例：@ImportAutoConfiguration(value = {DefaultLogInterceptorConfig.class})
 */
//@Configuration
public class DefaultLogInterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new LogIdInterceptor()).addPathPatterns("/**");

    }

}
