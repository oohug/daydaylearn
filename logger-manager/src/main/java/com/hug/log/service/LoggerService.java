package com.hug.log.service;

import com.hug.log.LoggerLevel;

import java.util.List;
import java.util.Map;

/**
 * 日志拓展服务类
 */
public interface LoggerService {

    /**
     * 获取所有Logger
     *
     * @param useRootLevel logger.level=null，是否set useRootLevel ；是-set root level，否-set null
     * @return Map key=logger.name ，value=Logger {@link ch.qos.logback.classic.Logger}
     */
    Map<String, Object> getAllLoggerAppender(boolean useRootLevel);

    /**
     * 设置默认等级
     * 什么地方会用到此属性
     *
     * @param defaultLevel
     */
    void setDefaultLevel(String defaultLevel);

    /**
     * 设置全局日志等级（此方法会覆盖所有logger）
     *
     * @param logLevel
     */
    void setGlobalLogLevel(String logLevel);

    /**
     * 指定logger日志等级
     *
     * @param loggerLevel
     */
    void setLogLevel(LoggerLevel loggerLevel);

    /**
     * 指定logger日志等级
     * exm：如果 2、3 logger.level=null ，设置1 com 时 会覆盖2/3级别；  如果2、3 logger.level!=null 不覆盖
     * 1 com
     * 2 com.freemud
     * 3 com.freemud.product
     *
     * @param loggerLevels
     */
    void setLogLevel(List<LoggerLevel> loggerLevels);

    /**
     * 指定logger日志等级(前缀匹配)
     * exm：如果 2、3 logger.level=null ，设置1 com 时 会覆盖2/3级别；  如果2、3 logger.level!=null 不覆盖
     * 1 com
     * 2 com.freemud
     * 3 com.freemud.product
     * @param loggerLevel
     */
    void setLogLevelLikePrefix(LoggerLevel loggerLevel);

}
