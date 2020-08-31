package hug.controller;

import hug.common.DefaultConsumer;
import hug.common.SpringContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/")
@Api(value = "HealthController", tags = "HealthController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HealthController {

    @Resource
    Environment evn;

    @GetMapping(value = "status")
    @ApiOperation(value = "health", notes = "health")
    public String health() {
        log.info("-{}", evn.getProperty("catalina.base"));

        return "ok";
    }


    @GetMapping(value = "test")
    @ApiOperation(value = "test", notes = "test")
    public String test() {
        log.info("-{}", evn.getProperty("catalina.base"));
        DefaultConsumer defaultConsumer = new DefaultConsumer();
        DefaultConsumer o1 = (DefaultConsumer) SpringContextHolder.getApplicationContext().getBean("defaultConsumer");
        SpringContextHolder.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(defaultConsumer);
        DefaultConsumer o2 = (DefaultConsumer) SpringContextHolder.getApplicationContext().getBean("defaultConsumer");
        log.info("o1:{}", o1);
        log.info("o2:{}", o2);
        return "ok";
    }
}
