package com.hug.controller;

import com.google.common.collect.ImmutableMap;
import com.hug.common.CommonEventPublisher;
import com.hug.listeners.events.MsgEventEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
@Api(value = "HealthController", tags = "HealthController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HealthController {

    @Resource
    Environment evn;

    @Resource
    private CommonEventPublisher commonEventPublisher;

    @GetMapping(value = "status")
    @ApiOperation(value = "health", notes = "health")
    public String health() {
        log.info("-{}", evn.getProperty("catalina.base"));

        log.debug("im debug");
        log.info("im info");
        log.warn("im warn");
        log.error("im error");
        System.out.println("");
        return "ok";
    }

    @GetMapping(value = "test")
    @ApiOperation(value = "test", notes = "test")
    public String test() {
        log.info("-{}", evn.getProperty("catalina.base"));

        Map source = ImmutableMap.of("order_id", "1", "order_state", "i'm create!");
        commonEventPublisher.publish(source, MsgEventEnum.order_create);

        return "ok";
    }
}
