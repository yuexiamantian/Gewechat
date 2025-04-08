package api.listener;

import api.base.ContactApi;
import api.base.MessageApi;
import api.event.EventByNewMessage;
import api.mapper.GewechatMessageMapper;
import api.message.GewechatMessage;
import api.message.GewechatNewMsg;
import api.message.MsgTypeEnum;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
@Slf4j
public class ListenerOnNewMessageToEcho {

    @Value("${gewechat.listenerWxIds:ALL}")
    private List<String> listenerWxIds;

    boolean isVip=true;
    @Resource
    private GewechatMessageMapper gewechatMessageMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @EventListener(EventByNewMessage.class)
    @Async
    public void persistAndForwardAgent(EventByNewMessage event) {
        GewechatNewMsg message = event.getSource();
        if (/*关注人的需要存储*/(listenerWxIds.contains("ALL") || listenerWxIds.contains(message.getToUserName()))) {

            String fromUserName = message.getFromUserName();
            String fromWho = ContactApi.getDetailInfo(message.getAppid(), Collections.singletonList(fromUserName)).getJSONArray("data").getJSONObject(0).getString("nickName");
            if (StringUtils.isNotBlank(fromWho)) {
                stringRedisTemplate.opsForValue().setIfAbsent(fromUserName, fromWho);
            }
            String toWho = ContactApi.getDetailInfo(message.getAppid(), Collections.singletonList(message.getToUserName())).getJSONArray("data").getJSONObject(0).getString("nickName");
            if (StringUtils.isNotBlank(toWho)) {
                stringRedisTemplate.opsForValue().setIfAbsent(message.getToUserName(), toWho);
            }


            // 数据存储
            GewechatMessage po = message.toPO();
            po.setData(JSONObject.toJSONString(message.getData()));
            gewechatMessageMapper.insertIgnore(po);

            String toUserName=fromUserName;
            MsgTypeEnum msgTypeEnum = MsgTypeEnum.from(message.getMsgType());
            if (Objects.nonNull(msgTypeEnum)) {
                String content = message.getContent();
                switch (msgTypeEnum) {
                    case TEXT:
                        MessageApi.postText(message.getAppid(), toUserName, content, "");
                        break;
                    case IMAGE:
                        MessageApi.forwardImage(message.getAppid(), toUserName, content);
                        break;
                    case COMPON:
                        cn.hutool.json.JSONObject jsonObject = JSONUtil.xmlToJson(content);
                        Integer referType = jsonObject.getByPath("msg.appmsg.finderFeed.mediaList.media.mediaType", Integer.class);
                        if (new Integer(4).equals(referType)) {
                            if (isVip) {
                                // 视频号
                                MessageApi.postText(message.getAppid(), toUserName,
                                        jsonObject.getByPath("msg.appmsg.finderFeed.mediaList.media.url", String.class), "");
                                isVip=!isVip;
                            }else{
                                MessageApi.postText(message.getAppid(), toUserName, "余额不足请点击充值。https://vtool.pro/my.html","");
                                isVip=!isVip;
                            }


                        } else {
                            // 引用的非文本消息
                            MessageApi.postText(message.getAppid(), toUserName,
                                    jsonObject.getByPath("msg.appmsg.title", String.class), "notify@all");
                        }
                        break;
                    default: // do nothing
                }
            }
        }
    }
}
