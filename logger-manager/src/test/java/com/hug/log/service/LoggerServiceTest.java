package com.hug.log.service;

import com.hug.log.LoggerChangeFactory;
import com.hug.log.LoggerLevel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LoggerServiceTest {
    Logger log = LoggerFactory.getLogger(LoggerServiceTest.class);
    Logger log2 = LoggerFactory.getLogger("TEST");

    @Test
    public void loggerUtil() {
        printLog();

        AbstractLoggerService loggerService = LoggerChangeFactory.getLoggerService();

        loggerService.setGlobalLogLevel(Level.INFO.toString());
        printLog();

        loggerService.setGlobalLogLevel(Level.DEBUG.toString());
        printLog();

        LoggerLevel loggerLevel = new LoggerLevel(LoggerServiceTest.class, Level.INFO.toString());
        loggerService.setLogLevel(loggerLevel);
        printLog();
    }

    public void printLog() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
        log.error("this is error");
        log.info("this is info");
        log.debug("this is debug1");

        log2.error(" log2 this is error");
        log2.info(" log2 this is info");
        log2.debug("log2 this is debug1");
    }
}