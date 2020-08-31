package hug.listeners.events;

import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {

    private String eventName;

    public OrderEvent(Object source, String eventName) {
        super(source);
        setEventName(eventName);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
