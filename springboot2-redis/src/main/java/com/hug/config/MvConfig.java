package com.hug.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Configuration
@PropertySource({"classpath:rest.properties"})
public class MvConfig implements WebMvcConfigurer {

    @Value("${crf.http.maxTotal}")
    private int httpMaxTotal;
    @Value("${crf.http.maxPerRoute}")
    private int httpMaxPerRoute;
    @Value("${crf.http.connectTimeout}")
    private int httpConnectTimeout;
    @Value("${crf.http.readTimeout}")
    private int httpReadTimeout;
    @Value("${crf.http.connectionRequestTimeout}")
    private int httpConnectionRequestTimeout;

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
        return mapper;
    }

    @Primary
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory());
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        List<HttpMessageConverter<?>> converters2 = new LinkedList<>();
        converters.forEach(c -> {
            if (c instanceof StringHttpMessageConverter) {
                converters2.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
            } else {
                converters2.add(c);
            }
        });
        restTemplate.setMessageConverters(converters2);

        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        pollingConnectionManager.setMaxTotal(httpMaxTotal);
        pollingConnectionManager.setDefaultMaxPerRoute(httpMaxPerRoute);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);
        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(httpConnectTimeout);
        clientHttpRequestFactory.setReadTimeout(httpReadTimeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(httpConnectionRequestTimeout);
        // clientHttpRequestFactory.setBufferRequestBody(false);
        return clientHttpRequestFactory;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}