package com.hug.log;

import com.hug.log.service.AbstractLoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

/**
 *
 */
public class LoggerChangeFactory {
    private static Logger log = LoggerFactory.getLogger(LoggerChangeFactory.class);

    /**
     * 获取当前系统使用的日志框架
     *
     * @return
     */
    public static AbstractLoggerService getLoggerService() {

        String type = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        LogTypeEnum logType = LogTypeEnum.getELogFrameworkType(type);
        log.info("日志框架：{}，日志类路径：{}，日志实现类：{}", logType, logType.getLogClassStr(), logType.getClassName().getName());
        AbstractLoggerService loggerService = null;
        try {
            loggerService = (AbstractLoggerService) Class.forName(logType.getClassName().getName()).newInstance();
        } catch (Exception e) {
            log.error("create AbstractLoggerService error ", e);
        }
        return loggerService;
    }
}
