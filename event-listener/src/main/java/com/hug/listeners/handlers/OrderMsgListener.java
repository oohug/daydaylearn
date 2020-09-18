package com.hug.listeners.handlers;

import com.hug.listeners.events.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OrderMsgListener {

//    @Async
    @EventListener(condition = "#event.eventName.equals('order_create')")
    public void orderCreate(OrderEvent event) {
        Map map = (Map) event.getSource();
        log.info("listener: order_create event of {}", map);
//        System.out.println(1/0);
    }

//    @TransactionalEventListener
    @EventListener(condition = "#event.eventName.equals('order_paid')")
    public void orderPaid(OrderEvent event) {
        Map map = (Map) event.getSource();
        log.info("order_paid event of {}", map);
    }
}
