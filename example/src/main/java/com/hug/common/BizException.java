package com.hug.common;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    //请求太频繁
    public static final String REQ_TOO_TIMES = "-2";

    private String code = "-1";
    private Object data;


    public BizException() {
        super();
    }

    public BizException(CodeEnum CodeEnum) {
        super(CodeEnum.getMsg());
        this.code = CodeEnum.getCode();
    }

    public BizException(CodeEnum CodeEnum, Object data) {
        super(CodeEnum.getMsg());
        this.code = CodeEnum.getCode();
        this.data = data;
    }

    public BizException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BizException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
