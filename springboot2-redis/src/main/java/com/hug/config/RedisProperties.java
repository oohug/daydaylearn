package com.hug.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:redis.properties"})
public class RedisProperties {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisProperties.class);

    @Value("${rest.connection.request.timeout}")
    private String connectionRequestTimeOut;
    @Value("${rest.connect.timeout}")
    private String connectionTimeOut;
    @Value("${rest.read.timeout}")
    private String readTimeOut;

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;

    @Value("${redis.max.idle}")
    private int maxIdle;
    @Value("${redis.max.total}")
    private int maxTotal;
    @Value("${redis.max.wait}")
    private int maxWait;

    @Value("${redis.database}")
    private int dataBase;

    @Value("${redis.testOnBorrow}")
    private Boolean testOnBorrow;
    @Value("${redis.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${redis.testOnReturn}")
    private Boolean testOnReturn;
    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    public String getConnectionRequestTimeOut() {
        return connectionRequestTimeOut;
    }

    public String getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public String getReadTimeOut() {
        return readTimeOut;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public int getDataBase() {
        return dataBase;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }
}
