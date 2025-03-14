package api.listener;

import api.base.MessageApi;
import api.event.EventByNewMessage;
import api.mapper.GewechatMessageMapper;
import api.message.*;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Slf4j
public class ListenerOnNewMessageToCallSource {
    @Value("${gewechat.agentWxId:57835595162@chatroom}")
    private String agentWxId;

    @Resource
    private GewechatMessageMapper gewechatMessageMapper;

    @EventListener(EventByNewMessage.class)
    @Async
    public void callSouce(EventByNewMessage event) {
        GewechatNewMsg message = event.getSource();

        if (message.getFromUserName().equals(agentWxId) || message.getFromUserName().equals(message.getWxid())) {
            // 解析引用消息ID
            JSONObject jsonObject = JSONUtil.xmlToJson(message.getContent());
            String referMsgContent = jsonObject.getByPath("msg.appmsg.refermsg.content", String.class);
            if (StringUtils.isBlank(referMsgContent)) {
                return;
            }
            String[] split = referMsgContent.split(":");
            String sourceMessageId = split[0].replaceAll("\\s+", "");
            if (StringUtils.isBlank(sourceMessageId)) {
                return;
            }

            // 查找引用消息
            GewechatMessage gewechatMessage = gewechatMessageMapper.selectOne(Wrappers.lambdaQuery(GewechatMessage.class)
                    .eq(GewechatMessage::getMsgId, Long.valueOf(sourceMessageId)));
            if (gewechatMessage == null) {
                return;
            }


            // 组装请求参数
            Map<String, Object> map = new HashMap<>();
            map.put("title", jsonObject.getByPath("msg.appmsg.title", String.class));
            map.put("type", 57);
            map.put("refermsg", new HashMap<String, Object>() {
                {
                    put("fromusr", gewechatMessage.getFromUserName());
                    put("chatusr", "wxid_k83tqco1jng522");
                    put("svrid", gewechatMessage.getNewMsgId());
                    put("type", 1);
                }
            });
            // 转发回原问题群

            String appmsg = XmlUtil.mapToXmlStr(map, "appmsg");
            MessageApi.postAppMsg(message.getAppid(), gewechatMessage.getFromUserName(), appmsg);
        }
    }
}
