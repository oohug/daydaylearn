package com.hug.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
@Api(value = "HealthController", tags = "HealthController", description = "HealthController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ApiResponses({@ApiResponse(code = 200, message = "调用成功"),
        @ApiResponse(code = 400, message = "业务异常"),
        @ApiResponse(code = 401, message = "您没有登录"),
        @ApiResponse(code = 403, message = "权限不够,不允许访问"),
        @ApiResponse(code = 405, message = "请求的方法不支持"),
        @ApiResponse(code = 500, message = "系统内部错误"),
        @ApiResponse(code = 502, message = "网关超时"),
        @ApiResponse(code = 503, message = "服务不可达")})
public class HealthController {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    Environment evn;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "status")
    @ApiOperation(value = "health", notes = "health")
    public String health() {
        LOGGER.debug("logging.config={},catalina.base={}", evn.getProperty("logging.config"), evn.getProperty("catalina.base"));
        return "ok";
    }

    @ApiOperation(value = "redis.get", notes = "redis.get")
    @GetMapping(value = "redis/get/{key}")
    @ResponseBody
    public String redis(@PathVariable String key) {

        if (StringUtils.isEmpty(key)) {
            return key;
        }

        String rateCount = stringRedisTemplate.opsForValue().get(key);

        return rateCount;
    }

    @GetMapping(value = "redis/del/{key}")
    @ApiOperation(value = "redis.del", notes = "redis.del")
    @ResponseBody
    public String redisDel(@PathVariable String key) {

        if (StringUtils.isEmpty(key)) {
            return key;
        }

        Boolean result = stringRedisTemplate.delete(key);

        return String.valueOf(result);
    }

}
