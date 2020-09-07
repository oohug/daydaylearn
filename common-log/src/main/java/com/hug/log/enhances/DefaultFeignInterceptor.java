package com.hug.log.enhances;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * FeignClient Default Interceptor （optional）,由引入方决定是否使用；
 * 例：@ImportAutoConfiguration(value = {DefaultFeignInterceptor.class})
 */
@Slf4j
public class DefaultFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (CollectionUtils.isEmpty(requestTemplate.headers().get(LogCont.X_TRANSACTION_ID))) {
            requestTemplate.header(LogCont.X_TRANSACTION_ID, MDCUtil.createTno());
        }
        log.debug("x-tid:{}", requestTemplate.headers().get(LogCont.X_TRANSACTION_ID));
    }
}