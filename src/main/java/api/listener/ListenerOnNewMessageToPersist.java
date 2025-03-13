package api.listener;

import api.event.EventByNewMessage;
import api.mapper.GewechatMessageMapper;
import api.message.GewechatNewMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
@Slf4j
public class ListenerOnNewMessageToPersist {

    @Value("${gewechat.listenerWxIds:ALL}")
    private List<String> listenerWxIds;

    @Resource
    private GewechatMessageMapper gewechatMessageMapper;


    @EventListener(EventByNewMessage.class)
    @Async
    public void persistAndForwardAgent(EventByNewMessage event) {
        GewechatNewMsg message = event.getSource();
        // 监听用户的消息需要存储
        if (listenerWxIds.contains("ALL") || listenerWxIds.contains(message.getToUserName())) {
            log.info("开始持久化存储:{}", message);
            gewechatMessageMapper.insertIgnore(message.toPO());
        }

    }

}
