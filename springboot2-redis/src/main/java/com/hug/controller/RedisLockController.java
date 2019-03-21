package com.hug.controller;

import com.hug.service.CommonRedisHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
@Api(value = "RedisLockController", tags = "RedisLockController", description = "RedisLockController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ApiResponses({@ApiResponse(code = 200, message = "调用成功"),
        @ApiResponse(code = 400, message = "业务异常"),
        @ApiResponse(code = 401, message = "您没有登录"),
        @ApiResponse(code = 403, message = "权限不够,不允许访问"),
        @ApiResponse(code = 405, message = "请求的方法不支持"),
        @ApiResponse(code = 500, message = "系统内部错误"),
        @ApiResponse(code = 502, message = "网关超时"),
        @ApiResponse(code = 503, message = "服务不可达")})
public class RedisLockController {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisLockController.class);

    @Resource
    private CommonRedisHelper commonRedisHelper;

    @ApiOperation(value = "redis-lock", notes = "redis锁")
    @PostMapping(value = "/tryLock")
    @ResponseBody
    public String tryLock(@RequestParam(defaultValue = "100001") String _key) {
        String key = "DAY_LEARN_LOCK_" + _key;
        String ret = "Fail";

        try {

            if (commonRedisHelper.lock(key)) {
                LOGGER.info("key={},执行业务");
                ret = "success";
                commonRedisHelper.delete(key);
            } else {

                // 设置失败次数计数器, 当到达(max)次时, 返回失败
                int failCount = 1;
                while (failCount <= 3) {
                    // 等待200ms重试
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (commonRedisHelper.lock(key)) {
                        // 执行逻辑操作
                        ret = "success";
                        LOGGER.info("key={},自旋执行业务");
                        commonRedisHelper.delete(key);
                        return ret;
                    } else {
                        failCount++;
                    }
                }
            }

        } catch (Exception e) {
            throw e;
        }

        return "success";
    }
}
