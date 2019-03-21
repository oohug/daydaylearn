package com.hug.controller;

import com.hug.entity.TOrder;
import com.hug.mapper.TOrderMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Api(value = "OrderController", tags = "OrderController", description = "OrderController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ApiResponses({@ApiResponse(code = 200, message = "调用成功"),
        @ApiResponse(code = 400, message = "业务异常"),
        @ApiResponse(code = 401, message = "您没有登录"),
        @ApiResponse(code = 403, message = "权限不够,不允许访问"),
        @ApiResponse(code = 405, message = "请求的方法不支持"),
        @ApiResponse(code = 500, message = "系统内部错误"),
        @ApiResponse(code = 502, message = "网关超时"),
        @ApiResponse(code = 503, message = "服务不可达")})
public class OrderController {
    @Autowired
    private TOrderMapper orderMapper;

    @GetMapping("/add")
    public void addOrder(Long orderId,Long userId) {

        TOrder entity10 = new TOrder();
//        entity10.setOrderId(10000L);
        entity10.setOrderId(orderId);
        entity10.setOrderNo("No1000000");
//        entity10.setUserId(102333001L);
        entity10.setUserId(userId);
        orderMapper.insertSelective(entity10);

//        TOrder entity11 = new TOrder();
//        entity11.setOrderId(10001L);
//        entity11.setOrderNo("No1000000");
//        entity11.setUserId(102333000L);
//        orderMapper.insertSelective(entity11);
    }
}