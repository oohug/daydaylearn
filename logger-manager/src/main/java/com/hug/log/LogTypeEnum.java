package com.hug.log;


import com.hug.log.service.Log4j2LoggerServiceImpl;
import com.hug.log.service.Log4jLoggerServiceImpl;
import com.hug.log.service.LogbackLoggerServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * 日志框架类型
 */
public enum LogTypeEnum {
    @Deprecated
    LOG4J("LOG4J", "org.slf4j.impl.Log4jLoggerFactory", Log4jLoggerServiceImpl.class),
    @Deprecated
    LOG4J2("LOG4J2", "org.apache.logging.slf4j.Log4jLoggerFactory", Log4j2LoggerServiceImpl.class),

    LOGBACK("LOGBACK", "ch.qos.logback.classic.util.ContextSelectorStaticBinder", LogbackLoggerServiceImpl.class),
    UNKNOWN(null, null, null);

    private String type;

    private String logClassStr;

    private Class<?> className;

    LogTypeEnum(String type, String logClassStr, Class<?> className) {
        this.type = type;
        this.logClassStr = logClassStr;
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public String getLogClassStr() {
        return logClassStr;
    }

    public Class<?> getClassName() {
        return className;
    }


    public static LogTypeEnum getELogFrameworkType(String logClassStr) {
        for (LogTypeEnum type : LogTypeEnum.values()) {
            if (StringUtils.isNotBlank(logClassStr) && type.getLogClassStr().equalsIgnoreCase(logClassStr)) {
                return type;
            }
        }
        return null;
    }
}
