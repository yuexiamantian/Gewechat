package api.event;

import api.message.GewechatNewMsg;
import org.springframework.context.ApplicationEvent;


public class EventByNewMessage extends ApplicationEvent {

    private final GewechatNewMsg source;

    public EventByNewMessage(GewechatNewMsg source) {
        super(source);
        this.source = source;
    }

    public GewechatNewMsg getSource() {
        return source;
    }
}
