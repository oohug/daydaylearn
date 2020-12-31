package com.freemud.product.config;

import com.freemud.product.utils.log.AbstractLoggerService;
import com.freemud.product.utils.log.LoggerChangeFactory;
import com.freemud.product.utils.log.LoggerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
public class LoggerRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * key=packageName,value=log.level
     * exm: {'com.freemud.product.config.LoggerRunner':'debug'}
     */
    @Value("#{${logger.level.map:{'-1':'-1'}}}")
    private Map<String, String> loggerLevelMap;

    /**
     * 远程日志版本
     */
    @Value("${logger.change.version:0}")
    private Integer loggerChangeVersion;

    /**
     * 系统日志版本，系统启动时设置 loggerVersion=loggerChangeVersion
     */
    public static Integer loggerVersion = 0;

    @Override
    public void run(String... args) {

        initLoggerVersion();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Logger-Change");
                t.setDaemon(true);
                return t;
            }
        });

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                logger.info("loggerVersion:{}, loggerChangeVersion:{}", LoggerRunner.loggerVersion, loggerChangeVersion);
                if (!LoggerRunner.loggerVersion.equals(loggerChangeVersion)) {
                    logger.info("The global switch for LoggerChange from {} to {}", LoggerRunner.loggerVersion, loggerChangeVersion);
                    loggerVersion = loggerChangeVersion;

                    Map<String, Object> allLoggerAppenderMap = AbstractLoggerService.getLoggerMap();
                    if (CollectionUtils.isEmpty(allLoggerAppenderMap)) {
                        allLoggerAppenderMap = LoggerChangeFactory.getLoggerService().getAllLoggerAppender();
                    }
                    Set<String> keys = loggerLevelMap.keySet();
                    for (String key : keys) {
                        if (allLoggerAppenderMap.containsKey(key)) {
                            ch.qos.logback.classic.Logger sourceLoggerLevel = (ch.qos.logback.classic.Logger) allLoggerAppenderMap.get(key);
                            logger.info("logger {} change level from (level:{},effectiveLevel:{}) to {}"
                                    , key, sourceLoggerLevel.getLevel(), sourceLoggerLevel.getEffectiveLevel().levelStr, loggerLevelMap.get(key));
                            LoggerLevel targetLevel = new LoggerLevel(key, loggerLevelMap.get(key));
                            LoggerChangeFactory.getLoggerService().setLogLevel(targetLevel);
                        }
                    }
                }
            }
        }, 5L, 5L, TimeUnit.SECONDS);
    }

    /**
     * 系统启动时：设置loggerVersion= 配置版本，当版本一致时 不改变日志级别
     */
    public void initLoggerVersion() {
        logger.info("logger level version {}", loggerChangeVersion);
        LoggerRunner.loggerVersion = loggerChangeVersion;
    }
}
