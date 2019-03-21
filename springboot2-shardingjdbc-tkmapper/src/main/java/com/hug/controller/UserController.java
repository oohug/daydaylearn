package com.hug.controller;

import com.hug.entity.TUser;
import com.hug.mapper.TUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(value = "UserController", tags = "UserController", description = "UserController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ApiResponses({@ApiResponse(code = 200, message = "调用成功"),
        @ApiResponse(code = 400, message = "业务异常"),
        @ApiResponse(code = 401, message = "您没有登录"),
        @ApiResponse(code = 403, message = "权限不够,不允许访问"),
        @ApiResponse(code = 405, message = "请求的方法不支持"),
        @ApiResponse(code = 500, message = "系统内部错误"),
        @ApiResponse(code = 502, message = "网关超时"),
        @ApiResponse(code = 503, message = "服务不可达")})
public class UserController {
    @Autowired
    private TUserMapper tUserMapper;

    @PostMapping("/add")
    public int add(@RequestBody TUser user) {
        return tUserMapper.insert(user);
    }

    @GetMapping("/query")
    @ResponseBody
    public TUser query(Long userId) {

        TUser obj = new TUser();
        obj.setUserId(userId);
        return tUserMapper.selectOne(obj);
    }
}