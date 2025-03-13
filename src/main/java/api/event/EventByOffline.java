package api.event;

import api.message.GewechatBaseMessage;
import org.springframework.context.ApplicationEvent;


public class EventByOffline extends ApplicationEvent {

    public EventByOffline(Object obj) {
        super(obj);
    }
}
