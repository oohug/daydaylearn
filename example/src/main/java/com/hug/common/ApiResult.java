package com.hug.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "接口响应结果", description = "接口响应结果")
public class ApiResult {
    public static final String CODE_UNKNOWN = "UNKNOWN";    // 未知结果，受理中
    public static final String CODE_SUCCESS = "SUCCESS";    // 0  表示成功
    public static final String CODE_FAILED = "FAIL";        // -1 表示未知原因的失败

    @ApiModelProperty(value = "SUCCESS-成功,FAIL-失败,UNKNOWN-未知或者处理中", required = true)
    private String code;
    @ApiModelProperty(value = "", required = true)
    private Object data;
    @ApiModelProperty(value = "响应结果描述")
    private String message;

    private ApiResult(String code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

}
