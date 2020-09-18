package com.hug.statemachine;

import com.hug.common.OrderStatus;
import com.hug.common.OrderStatusEventEnum;
import com.hug.dto.OrderStateDto;
import org.springframework.context.annotation.Configuration;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;

@Configuration
public class OrderStateMachineConfig {

    public StateMachineBuilder<OrderStateMachine, OrderStatus, OrderStatusEventEnum, OrderStateDto> OrderStateMachineBuilder() {
        StateMachineBuilder<OrderStateMachine, OrderStatus, OrderStatusEventEnum, OrderStateDto> builder =
                StateMachineBuilderFactory.create(
                        OrderStateMachine.class, OrderStatus.class, OrderStatusEventEnum.class, OrderStateDto.class);

        // 待支付-》enent：支付-》下单
        builder.externalTransition().from(OrderStatus.pending_pay).to(OrderStatus.place_an_order).on(OrderStatusEventEnum.order_paid);

        // 下单-》event:接单-》已接单

        return builder;
    }
}
