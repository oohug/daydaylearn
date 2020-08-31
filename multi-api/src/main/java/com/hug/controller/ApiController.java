package com.hug.controller;

import com.hug.multiapi.ApiVersion;
import com.hug.common.BaseResponse;
import com.hug.common.OrderBaseReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@RestController
@Api(value = "ApiController", tags = "ApiController API", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiController {

    @ApiOperation(value = "hello", notes = "Hello Api Test(v1)", response = String.class)
    @GetMapping("hello")
    @ApiVersion("1.1")
    public Object hello() {
        return "version 1.1";
    }

    @ApiOperation(value = "hello", notes = "Hello Api Test(v2)", response = String.class)
    @GetMapping("hello")
    @ApiVersion("2.1")
    public Object hello2() {
        return "version 2.1";
    }

    @ApiOperation(value = "hello", notes = "Hello Api Test(v3)", response = String.class)
    @GetMapping("hello")
    @ApiVersion("10.1")
    public Object hello5() {
        return "version 10.1";
    }

    @ApiVersion("1.1")
    @ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
    @PostMapping(value = "/post/json")
    public BaseResponse postJsonV1(@RequestBody OrderBaseReq orderBaseReq) {
        long start = System.currentTimeMillis();
        log.info("start {}", start);
        return new BaseResponse("v1.1");
    }

    @ApiVersion("2.1")
    @ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
    @PostMapping(value = "/post/json")
    public BaseResponse postJsonV2(@RequestBody OrderBaseReq orderBaseReq) {
        long start = System.currentTimeMillis();
        log.info("start {}", start);
        return new BaseResponse("v2.1");
    }

    @ApiVersion("4.1")
    @ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
    @PostMapping(value = "/post/json")
    public BaseResponse postJsonV3(@RequestBody OrderBaseReq orderBaseReq, HttpRequest httpRequest) {
        long start = System.currentTimeMillis();
        log.info("start {}", start);
        return new BaseResponse("v4.1");
    }
}