package com.hug.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "通用返回")
public class BaseResponse<T> {

    @ApiModelProperty(value = "返回码")
    String code;
    @ApiModelProperty(value = "返回消息")
    String message;
    @ApiModelProperty(value = "版本号")
    String ver;
    @ApiModelProperty(value = "返回数据")
    T data;

    public BaseResponse() {
    }

    public BaseResponse(T data) {
        this("100", "成功", data);
    }

    public BaseResponse(String code, String message) {
        this(code, message, null);
    }

    public BaseResponse(String message, T data) {
        this("100", message, data);
    }

    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
