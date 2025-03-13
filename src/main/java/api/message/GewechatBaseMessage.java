package api.message;

import api.event.EventByNewMessage;
import api.event.EventByOffline;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Data
public class GewechatBaseMessage<T> {
    /**
     * 消息类型名称
     * AddMsg 新消息
     * ModContacts 好友通过验证及好友资料变更的通知 /群信息变更通知
     * DelContacts 删除好友通知
     * Offline  掉线通知
     **/
    private String TypeName;
    /**
     * 设备ID
     */
    private String Appid;
    /**
     * 所属微信的wxid
     */
    private String Wxid;

    private T Data;

    public static Optional<ApplicationEvent> toEvent(JSONObject jsonMessage) {
        String typeName = jsonMessage.getString("TypeName");
        if (StringUtils.isEmpty(typeName)) {
            return Optional.empty();
        }
        switch (typeName) {
            case "AddMsg":
                return Optional.of(new EventByNewMessage(GewechatNewMsg.from(jsonMessage)));
//            case "ModContacts":
//                return GewechatModContactsNotifyMsg.fromJson(jsonMessage);
//            case "DelContacts":
//                return GegechatDelContactsNotifyMsg.fromJson(jsonMessage);
            case "Offline":
                return Optional.of(new EventByOffline(jsonMessage));
        }
        return Optional.empty();
    }
}
