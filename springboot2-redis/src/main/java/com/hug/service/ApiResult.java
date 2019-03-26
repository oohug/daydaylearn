package com.hug.service;

public class ApiResult {

    public ApiResult(String message,String code) {
        this.code = code;
        this.message = message;
    }

    private String message;
    private String code;

    public static ApiResult failed(String message){
        return new ApiResult(message,"0");
    }

    public static ApiResult success(String message){
        return new ApiResult(message,"100001");
    }


}
