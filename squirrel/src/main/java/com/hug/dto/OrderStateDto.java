package com.hug.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class OrderStateDto {

    private long id;
    private String orderCode;
    private String partnerId;
    private int orderState;
    private int deliveryState;
    private int suspendState;
    private int orderSubState;
    // 未支付， 支付中，已支付，已退款
    private int payState;
}
