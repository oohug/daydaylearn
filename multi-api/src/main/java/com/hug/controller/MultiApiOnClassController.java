package com.hug.controller;

import com.hug.common.BaseResponse;
import com.hug.common.OrderBaseReq;
import com.hug.multiapi.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/class")
@RestController
@ApiVersion("1.1")
@Api(value = "MultiApiOnClassController", tags = "MultiApiOnClassController API", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MultiApiOnClassController {

//    @ApiVersion("1.2")
    @ApiOperation(value = "hello", notes = "Hello Api Test(v1.1)", response = String.class)
    @GetMapping("hello")
    public Object hello1() {
        return "v1.1";
    }

//    @ApiVersion("1.2")
    @ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
    @PostMapping(value = "/post/json")
    public BaseResponse postJsonV1(@RequestBody OrderBaseReq orderBaseReq) {
        long start = System.currentTimeMillis();
        log.info("start {}", start);
        return new BaseResponse("v1.1");
    }
}