package api.listener;

import api.base.MessageApi;
import api.event.EventByNewMessage;
import api.mapper.GewechatMessageMapper;
import api.message.GewechatMessage;
import api.message.GewechatNewMsg;
import api.message.MsgTypeEnum;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ListenerOnNewMessageToRedirect {
    @Resource
    private GewechatMessageMapper gewechatMessageMapper;

    @EventListener(EventByNewMessage.class)
    @Async
    public void redirect(EventByNewMessage event) {
        GewechatNewMsg message = event.getSource();

        // 不处理组合消息
        if (MsgTypeEnum.COMPON != MsgTypeEnum.from(message.getMsgType())) {

            return;
        }

        // 引用的消息不处理：可能是引用并回复
        cn.hutool.json.JSONObject jsonObject = JSONUtil.xmlToJson(message.getContent());
        if (!"57".equals(jsonObject.getByPath("msg.appmsg.type", String.class))) {
            return;
        }

        String referMsgContent= JSONUtil.xmlToJson(message.getContent()).getByPath("msg.appmsg.refermsg.content", String.class);
        // 解析获取消息ID
        Long msgId = getMsgId(referMsgContent);
        if (null == msgId) {
            return;
        }
        // 通过消息ID查询信息
        GewechatMessage gewechatMessage = gewechatMessageMapper.selectById(msgId);
        if (gewechatMessage == null) {
            return;
        }
        // 组装请求参数
        String appmsg = buildParam(gewechatMessage, referMsgContent);

        // 转发回原问题群
//        MessageApi.postAppMsg(message.getAppid(), agentWxId, appmsg);
    }


    public Long getMsgId(String referMsgContent) {

        String msgIdRegex = "\\d+";
        String[] split = referMsgContent.split("\n");
        String msgIdStr = split[0];
        Matcher matcher = Pattern.compile(msgIdRegex).matcher(msgIdStr);
        if (matcher.find()) {
            return Long.valueOf(matcher.group());
        }
        return null;
    }

    public String buildParam(GewechatMessage gewechatMessage, String title) {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("type", 1);
        String[] split = gewechatMessage.getContent().split("\n");
        String nickNameColon = split[0];
        String nickName = "";
        if (nickNameColon.endsWith(":")) {
            nickName = nickNameColon.substring(0, nickNameColon.indexOf(":"));
        } else {
            nickName = gewechatMessage.getFromUserName();
        }
        map2.put("chatusr", nickName);
//        map2.put("chatusr",String.valueOf(redisTemplate.opsForValue().get(nickName)));
        map2.put("content", gewechatMessage.getContent().replaceFirst(".*\n", ""));
        map1.put("title", title);
        map1.put("type", 57);
        map1.put("refermsg", map2);

        return XmlUtil.mapToXmlStr(map1, "appmsg");
    }
}
