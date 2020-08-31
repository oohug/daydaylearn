package com.hug.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "日志管理")
public class LogController {

    @ApiOperation(value = "change")
    @GetMapping("change")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootLevel", value = "root,全局级别:ALL,TRACE,DEBUG,INFO,WARN,ERROR,OFF", required = false),
            @ApiImplicitParam(name = "singleLevel", value = "单独设置类日志级别:ALL,TRACE,DEBUG,INFO,WARN,ERROR,OFF", required = false),
            @ApiImplicitParam(name = "singlePath", value = "单独类路径:com.hug.controller.HealthController", required = false)
    })
    public String changeLevel(String rootLevel, String singleLevel, String singlePath) {
        log.warn("set log rootLevel:{}, singleLevel:{}, singlePath:{}, current.level:{}", rootLevel, singleLevel, singlePath, log.isDebugEnabled());

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // 设置全局日志级别
        if (!StringUtils.isEmpty(rootLevel)) {
            ch.qos.logback.classic.Logger logger = loggerContext.getLogger("root");
            logger.setLevel(Level.toLevel(rootLevel));
        }

        // 设置某个类或者日志级别-可以实现定向日志级别调整
        if (!StringUtils.isEmpty(singleLevel)) {
            ch.qos.logback.classic.Logger vLogger = loggerContext.getLogger(singlePath);
            if (vLogger != null) {
                vLogger.setLevel(Level.toLevel(singleLevel));
            }
        }

        return "success";
    }
}
