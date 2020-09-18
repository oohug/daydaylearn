package com.hug.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    pending_pay(1, "待支付"),
    place_an_order(2, "下单"),
    accepted(3, "已接单"),
    produced(4, "制作完成（代发货）"),
    delivering(5, "配送中"),
    complete(6, "已完成"),
    closed(7, "已关闭");

    private int code;
    private String name;

    public static final EnumSet<OrderStatus> forward_operation =
            EnumSet.of(pending_pay, place_an_order, accepted, produced, delivering, complete);

    public static OrderStatus getByCode(int code) {
        return Arrays.stream(OrderStatus.values()).filter(e -> e.code == code).findFirst().orElseThrow(() -> new RuntimeException());
    }
}
