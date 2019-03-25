package com.hug.common;


public enum CodeEnum {

    成功("0", "成功"),
    失败("999999", "系统异常"),
    系统异常("999999", "系统异常"),
    缺少请求参数("E000001", "参数不合法，参数验证失败"),
    参数解析失败("E000001", "参数不合法，参数验证失败"),
    参数验证失败("E000001", "参数不合法，参数验证失败"),
    数据绑定失败("E000001", "参数不合法，绑定失败"),
    不支持当前请求方法("E000001", "参数不合法，绑定失败"),

    pay_fail("100001", "支付失败!"),
    pay_process("100002", "支付处理中!"),
    pay_repeat_success("100003", "重复支付，已经成功!"),
    pay_repeat_fail("100004", "重复的流水号!"),
    pay_pwd_fail_tips("100005", "支付密码有误，您还可以输入%s次，%s次错误后您的密码将被锁定3小时。"),

    err_server("E9001", "暂时不能为你服务,请稍后再试");

    private String code;
    private String msg;

    CodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



}
