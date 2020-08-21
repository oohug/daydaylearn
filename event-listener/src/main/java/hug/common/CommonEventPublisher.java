package hug.common;

import hug.listeners.events.MsgEventEnum;
import hug.listeners.events.OrderEvent;
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
    }
}
