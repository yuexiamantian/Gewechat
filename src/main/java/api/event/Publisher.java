package api.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Publisher {

    static ApplicationEventPublisher applicationEventPublisher;

    public static void publish(ApplicationEvent event){
        applicationEventPublisher.publishEvent(event);
    }

    @Resource
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        Publisher.applicationEventPublisher = applicationEventPublisher;
    }
}
