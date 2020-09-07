package com.hug.log.enhances;

public class LogCont {
    // 原有代码字段
    public static final String X_TRANSACTION_ID = "x-transaction-id";

    /**
     * log:mdc:trackingNo的简写，配合日志 tno:%X{tno} 使用
     */
    public static final String X_TNO = "tno";
}
