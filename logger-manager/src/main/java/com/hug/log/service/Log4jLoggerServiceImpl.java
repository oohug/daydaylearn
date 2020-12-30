package com.hug.log.service;

import com.hug.log.LogTypeEnum;
import com.hug.log.LoggerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Log4jLoggerServiceImpl extends AbstractLoggerService {
    private Logger log = LoggerFactory.getLogger(Log4jLoggerServiceImpl.class);

    public Log4jLoggerServiceImpl() {
    }

    @Override
    public Map<String, Object> getAllLoggerAppender(boolean useRootLevel) {
        logTypeEnum = LogTypeEnum.LOG4J2.LOG4J2;
        return loggerMap;
    }

    @Override
    public void setGlobalLogLevel(String logLevel) {

    }

    @Override
    public void setLogLevel(List<LoggerLevel> loggerLevels) {

    }

    @Override
    public void setLogLevelLikePrefix(LoggerLevel loggerLevel) {

    }
}
