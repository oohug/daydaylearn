package com.hug.log.enhances;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * restTemplate Default Interceptor （optional）,由引入方决定是否使用；
 * 例：@ImportAutoConfiguration(value = {DefaultRestInterceptor.class})
 */
@Slf4j
public class DefaultRestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution execution) throws IOException {

        if (StringUtils.isEmpty(httpRequest.getHeaders().getFirst(LogCont.X_TRANSACTION_ID))) {
            httpRequest.getHeaders().add(LogCont.X_TRANSACTION_ID, MDCUtil.createTno());
            log.debug("x-tid:{}", httpRequest.getHeaders().get(LogCont.X_TRANSACTION_ID));
        }

        return execution.execute(httpRequest, bytes);
    }
}