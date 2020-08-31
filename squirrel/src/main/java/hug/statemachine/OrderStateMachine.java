package hug.statemachine;

import com.alibaba.fastjson.JSON;
import hug.common.OrderStatus;
import hug.common.OrderStatusEventEnum;
import hug.common.OrderSuspendStatus;
import hug.dto.OrderStateDto;
import lombok.extern.slf4j.Slf4j;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

@Slf4j
public class OrderStateMachine extends AbstractStateMachine<OrderStateMachine, OrderStatus, OrderStatusEventEnum, OrderStateDto> {

    @Override
    protected void beforeTransitionBegin(OrderStatus fromState, OrderStatusEventEnum event, OrderStateDto context) {
//        super.beforeTransitionBegin(fromState, event, context);
        // todo
        // 1. 判断订单挂起状态，比如有些状态不校验状态机；提前一步可校验 "挂起状态"
        OrderSuspendStatus orderSuspendStatus = OrderSuspendStatus.getByCode(context.getSuspendState());
        // 无需校验场景
        if (OrderStatusEventEnum.not_check.contains(event)) {
            return;
        }
        // 逆向当并且可以退款
        if (OrderStatusEventEnum.suspend_pass.contains(event) && orderSuspendStatus.getIsCanRefund()) {
            return;
        }
        if (orderSuspendStatus.getCode() > OrderSuspendStatus.normal.getCode()) {
            throw new RuntimeException("挂起状态");
        }

    }

    @Override
    protected void afterTransitionDeclined(OrderStatus fromState, OrderStatusEventEnum event, OrderStateDto context) {
        super.afterTransitionDeclined(fromState, event, context);
        log.info("状态机拒绝转变 fromState={} event={} context={}", fromState, event, JSON.toJSONString(context));
        // 抛异常 ?
//        throw new S
    }
}
