package com.hug.listeners.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgEventEnum {
    order_create(1, "待支付", "order_create"),
    order_paid(2, "已支付", "order_paid"),
    accept(3, "已接单", "accept"),
    ;

    private int index;
    private String name;
    private String eventName;
}
