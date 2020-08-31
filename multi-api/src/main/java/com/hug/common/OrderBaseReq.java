package com.hug.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "订单通用请求")
public class OrderBaseReq {

    @ApiModelProperty(value = "订单号")
    private String oid;

    @ApiModelProperty(value = "请求时间")
    private Date requestTime;
}
