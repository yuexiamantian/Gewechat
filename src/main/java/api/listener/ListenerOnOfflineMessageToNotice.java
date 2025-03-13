package api.listener;

import api.event.EventByOffline;
import api.util.OkhttpUtil;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;


@Component
public class ListenerOnOfflineMessageToNotice {
    @EventListener(EventByOffline.class)
    @Async
    public void persist(EventByOffline event) throws IOException {

        OkhttpUtil.post("https://oapi.dingtalk.com/robot/send?access_token=a739e0b3d79421f4b07895161bdcf5d287ef83df2e30c5391c06f264d4d32918",
                new HashMap<String, Object>() {{
                    put("Content-Type", "application/json");
                }}, "{\"msgtype\":\"text\",\"at\":{\"atMobiles\":[15536685992]},\"text\":{\"content\":\"❌ 微信机器人已掉线\"}}", OkhttpUtil.okHttpClient());
    }
}
