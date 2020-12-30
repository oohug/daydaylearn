package com.hug.log.service;

import com.hug.log.LogConstant;
import com.hug.log.LogTypeEnum;
import com.hug.log.LoggerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLoggerService implements LoggerService {
    private Logger log = LoggerFactory.getLogger(AbstractLoggerService.class);

    /**
     * 存放所有logger信息，未设置的logger使用父节点 level
     * key=logger.name ，value=Logger {@link ch.qos.logback.classic.Logger}
     */
    protected static final ConcurrentHashMap<String, Object> loggerMap = new ConcurrentHashMap<>();

    /**
     * 默认日志等级
     */
    protected String defaultLevel = Level.ERROR.toString();

    /**
     * 日志框架类型
     */
    public static LogTypeEnum logTypeEnum;

    public AbstractLoggerService() {
    }

    @Override
    public void setDefaultLevel(String defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    /**
     * 检查所有日志状态
     *
     * @param logLevel
     * @return
     */
    public String checkAllLoggerStatus(String logLevel) {
        LoggerLevel loggerLevel = new LoggerLevel(LogConstant.LOGGER_ALL_NAME, logLevel);
        return checkLoggerStatus(loggerLevel);
    }

    /**
     * 检查日志状态
     *
     * @param loggerLevel
     * @return
     */
    public String checkLoggerStatus(LoggerLevel loggerLevel) {
        // 如果为NULL就是默认等级
        if (null == loggerLevel.getLevel()) {
            loggerLevel.setLevel(defaultLevel);
        }
        log.info("Check {} of {} set level:{}", logTypeEnum, loggerLevel.getName(), loggerLevel.getLevel());
        if (null == loggerMap || loggerMap.isEmpty()) {
            log.warn(" checkLoggerStatus not found logger of {}", loggerLevel.getName());
        }
        return loggerLevel.getLevel();
    }

    /**
     * 检查日志列表状态
     *
     * @param loggerLevels
     */
    public void checkLoggerListStatus(List<LoggerLevel> loggerLevels) {
        if (loggerLevels == null || loggerLevels.isEmpty()) {
            throw new RuntimeException(LogConstant.LOGGER_LIST_IS_NULL);
        }
        for (LoggerLevel loggerLevel : loggerLevels) {
            checkLoggerStatus(loggerLevel);
        }
    }

    @Override
    public void setLogLevel(LoggerLevel loggerLevel) {
        List<LoggerLevel> loggerLevels = new ArrayList<>();
        loggerLevels.add(loggerLevel);
        this.setLogLevel(loggerLevels);
    }
}
