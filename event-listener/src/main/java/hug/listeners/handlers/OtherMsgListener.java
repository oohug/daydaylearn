package hug.listeners.handlers;

import hug.listeners.events.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OtherMsgListener {

    @EventListener(condition = "#event.eventName.equals('order_create')")
    public void orderCreate(OrderEvent event) {
        Map map = (Map) event.getSource();
        log.info("listener(2): order_create event of {}", map);
    }
}
