package api.listener;

import api.base.MessageApi;
import api.event.EventByNewMessage;
import api.mapper.GewechatMessageMapper;
import api.message.GewechatNewMsg;
import api.message.MsgTypeEnum;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


@Component
@Slf4j
public class ListenerOnNewMessageToPersist {

    @Value("${gewechat.listenerWxIds:ALL}")
    private List<String> listenerWxIds;

    @Value("${gewechat.agentWxId:57835595162@chatroom}")
    private String agentWxId;

    @Resource
    private GewechatMessageMapper gewechatMessageMapper;


    @EventListener(EventByNewMessage.class)
    @Async
    public void persistAndForwardAgent(EventByNewMessage event) {
        GewechatNewMsg message = event.getSource();
        if (/*关注人的需要存储*/(listenerWxIds.contains("ALL") || listenerWxIds.contains(message.getToUserName()))) {
            log.info("持久化存储:{}", message);
            gewechatMessageMapper.insertIgnore(message.toPO());


            log.info("转发代理群:{}", message);
            if (!agentWxId.equals(message.getFromUserName())) {
                MsgTypeEnum msgTypeEnum = MsgTypeEnum.from(message.getMsgType());
                if (Objects.nonNull(msgTypeEnum)) {
                    String content = message.getContent();
                    switch (msgTypeEnum) {
                        case TEXT:
                            MessageApi.postText(message.getAppid(), agentWxId, content, "");
                            break;
                        case IMAGE:
                            MessageApi.forwardImage(message.getAppid(), agentWxId, content);
                            break;
                        case VIDEO:
                            MessageApi.forwardVideo(message.getAppid(), agentWxId, content);
                            break;
                        case COMPON:
                            cn.hutool.json.JSONObject jsonObject = JSONUtil.xmlToJson(content);
                            Integer referType = jsonObject.getByPath("msg.appmsg.refermsg.type", Integer.class);
                            if (MsgTypeEnum.TEXT.getCode().equals(referType)) {
                                // 引用的文本消息
                                MessageApi.postText(message.getAppid(), agentWxId,
                                        "【引用】:"
                                                + jsonObject.getByPath("msg.appmsg.refermsg.content", String.class)
                                                + "\n\n"
                                                + jsonObject.getByPath("msg.appmsg.title", String.class), "");
                            } else {
                                // 引用的非文本消息
                                MessageApi.postText(message.getAppid(), agentWxId,
                                        jsonObject.getByPath("msg.appmsg.title", String.class), "");
                            }

                            break;
                        default: // do nothing
                    }
                }
            }
        }
    }
}
