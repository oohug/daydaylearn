package hug.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum OrderStatusEventEnum {
    order_create(1, "待支付", "order_create"),
    order_paid(2, "已支付", "order_paid"),
    accept(3, "已接单", "accept"),

    user_cancel(9, "用户取消", "user_cancel"),
    partner_reject(10, "商家拒单", "partner_reject"),
    partner_cancel(11, "商家取消", "partner_cancel"),
    system_cancel(12, "系统取消", "system_cancel"),
    unpaid_timeout_close(13, "未支付超时关单", "unpaid_timeout_close"),
    accept_timeout_close(14, "商户接单超时关单", "accept_timeout_close");

    private int index;
    private String name;
    private String eventName;

    // 订单挂起时，放过event操作（常见于逆向操作）
    public static final EnumSet<OrderStatusEventEnum> suspend_pass =
            EnumSet.of(user_cancel, partner_cancel, partner_reject, system_cancel);

    // 订单不校验放过event操作
    public static final EnumSet<OrderStatusEventEnum> not_check =
            EnumSet.of(order_paid, unpaid_timeout_close, accept_timeout_close);

    public static OrderStatusEventEnum getByIndex(int index){
        return Arrays.stream(OrderStatusEventEnum.values()).filter(e -> e.index == index).findFirst().orElseThrow(() -> new RuntimeException());
    }
}
