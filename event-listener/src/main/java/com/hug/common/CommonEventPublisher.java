package com.hug.common;

import com.hug.listeners.events.MsgEventEnum;
import com.hug.listeners.events.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommonEventPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publish(Object source, MsgEventEnum msgEventEnum) {
        OrderEvent orderEvent = new OrderEvent(source, msgEventEnum.getEventName());
        log.info("publish：{}", source);
        publisher.publishEvent(orderEvent);
        log.info("1==========publish success：{}", source);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("2==========publish success：{}", source);
    }
}
