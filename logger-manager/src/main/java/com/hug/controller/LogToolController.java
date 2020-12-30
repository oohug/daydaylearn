package com.freemud.product.controller;

import com.freemud.common.BaseResponse;
import com.freemud.product.utils.log.AbstractLoggerService;
import com.freemud.product.utils.log.LogConstant;
import com.freemud.product.utils.log.LoggerChangeFactory;
import com.freemud.product.utils.log.LoggerLevel;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/Log")
@Slf4j
public class LogToolController {


    @RequestMapping(value = "/getAllLogger", method = {RequestMethod.GET}, produces = "application/json")
    @ApiOperation(value = "查询所有日志信息", notes = "查询所有日志信息 path:包、类路径")
    public BaseResponse getAllLogger(@RequestParam(required = false, defaultValue = "") String path) {
        Map<String, Object> loggerMap = AbstractLoggerService.getLoggerMap();
        if (CollectionUtils.isEmpty(loggerMap)) {
            loggerMap = LoggerChangeFactory.getLoggerService().getAllLoggerAppender(true);
        }
        if (StringUtils.isEmpty(path)) {
            return new BaseResponse(LogConstant.LOGGER_ALL_NAME, loggerMap);
        }

        Map<String, Object> result = new HashMap<>();
        Set<Map.Entry<String, Object>> entries = loggerMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            if (key.contains(path)) {
                result.put(key, loggerMap.get(key));
            }
        }
        return new BaseResponse(path, result);
    }

    @RequestMapping(value = "/changeRootLevel", method = {RequestMethod.GET}, produces = "application/json")
    @ApiImplicitParam(name = "level", value = "日志级别", required = true, dataType = "String", allowableValues = "ERROR,WARN,INFO,DEBUG,TRACE")
    public BaseResponse chgRootLevel(String level) {
        LoggerChangeFactory.getLoggerService().setGlobalLogLevel(level);
        return new BaseResponse("done", "");
    }

    @RequestMapping(value = "/changeLevel", method = {RequestMethod.POST}, produces = "application/json")
    public BaseResponse changeLevel(LoggerLevel loggerLevel) {
        LoggerChangeFactory.getLoggerService().setLogLevel(loggerLevel);
        return new BaseResponse("done", "");
    }

    @RequestMapping(value = "/changeLevelLikePrefix", method = {RequestMethod.POST}, produces = "application/json")
    public BaseResponse changeLevelLikePrefix(LoggerLevel loggerLevel) {
        LoggerChangeFactory.getLoggerService().setLogLevelLikePrefix(loggerLevel);
        return new BaseResponse("done", "");
    }


    @RequestMapping(value = "/batchChangeLevel", method = {RequestMethod.POST}, produces = "application/json")
    public BaseResponse batchChangeLevel(List<LoggerLevel> loggerLevel) {
        LoggerChangeFactory.getLoggerService().setLogLevel(loggerLevel);
        return new BaseResponse("done", "");
    }

}
