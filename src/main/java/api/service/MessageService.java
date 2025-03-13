package api.service;

import api.event.Publisher;
import api.message.GewechatBaseMessage;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService {
    public void callBack(JSONObject jsonMessage) {
        log.info("message:{}", jsonMessage);
        GewechatBaseMessage.toEvent(jsonMessage).ifPresent(Publisher::publish);
    }
}
