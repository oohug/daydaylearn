package com.hug.log.service;

import com.alibaba.fastjson.JSON;
import com.hug.log.LogConstant;
import com.hug.log.LogTypeEnum;
import com.hug.log.LoggerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LogbackLoggerServiceImpl extends AbstractLoggerService {
    private Logger log = LoggerFactory.getLogger(LogbackLoggerServiceImpl.class);

    /**
     * 初始化日志列表
     */
    public LogbackLoggerServiceImpl() {
        getAllLoggerAppender(false);
    }

    @Override
    public Map<String, Object> getAllLoggerAppender(boolean useRootLevel) {
        logTypeEnum = LogTypeEnum.LOGBACK;
        loggerMap.clear();

        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()) {
//            //若无等级则设置 ROOT节点权限
            if (logger.getLevel() == null) {
                logger.error("{} {}", logger.getName(), logger.getLevel());
////                若无等级则设置默认 info
//                if (useRootLevel) {
//                    ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(defaultLevel);
//                    targetLevel = ch.qos.logback.classic.Level.toLevel(rootLogger.getLevel().levelInt);
//                    logger.setLevel(targetLevel);
//                }
            }
            loggerMap.put(logger.getName(), logger);
        }
//        loggerMap.put(rootLogger.getName(), rootLogger);

        return loggerMap;
    }

    @Override
    public void setGlobalLogLevel(String logLevel) {
        logLevel = super.checkAllLoggerStatus(logLevel);
        Set<Map.Entry<String, Object>> entries = loggerMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object logger = entry.getValue();
            // 如果为NULL就是默认等级
            if (null == logger) {
                throw new RuntimeException(LogConstant.LOGGER_NOT_EXSIT);
            }
            ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) logger;
            ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(logLevel);

//            if (targetLogger.getLevel() != null) {
//                Level level = Level.valueOf(logLevel);
//                if (level.toInt() <= targetLevel.levelInt) {
//                    continue;
//                }
//            }
            targetLogger.setLevel(targetLevel);
        }
    }

    @Override
    public void setLogLevel(List<LoggerLevel> loggerLevels) {
        log.info("setLogLevel start loggerLevels ：{}", JSON.toJSONString(loggerLevels));
        int count = 0;
        for (LoggerLevel loggerLevel : loggerLevels) {
            super.checkLoggerStatus(loggerLevel);
            Object logger = loggerMap.get(loggerLevel.getName());
            if (null == logger) {
                throw new RuntimeException(LogConstant.LOGGER_NOT_EXSIT);
            }
            ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) logger;
            ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(loggerLevel.getLevel());
            targetLogger.setLevel(targetLevel);
            count++;
        }
        log.info("setLogLevel end total={},deal={}", loggerLevels.size(), count);
    }

    @Override
    public void setLogLevelLikePrefix(LoggerLevel loggerLevel) {
        log.info("setLogLevelLikePrefix start set loggerLevels {} where name like {}", loggerLevel.getLevel(), loggerLevel.getName());
        super.checkLoggerStatus(loggerLevel);
        Set<Map.Entry<String, Object>> entries = loggerMap.entrySet();
        int count = 0;
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            if (key.contains(loggerLevel.getName())) {
                LoggerLevel targetLevel = new LoggerLevel(key, loggerLevel.getLevel());
                super.setLogLevel(targetLevel);
                count++;
            }
        }
        log.info("setLogLevelLikePrefix count:{},level:{}", count, loggerLevel.getLevel());
    }
}
