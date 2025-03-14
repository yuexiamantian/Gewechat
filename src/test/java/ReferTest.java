import api.base.MessageApi;
import cn.hutool.core.util.XmlUtil;

import java.util.HashMap;
import java.util.Map;

public class ReferTest {
    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        map.put("title", "收到4");
        map.put("type", 57);
        map.put("refermsg", new HashMap<String, Object>() {
            {
                put("chatusr", "qq461412499");
                put("svrid", "490008439980287254");
                put("fromusr", "52843526447@chatroom");
                put("type", 1);
            }
        });

        MessageApi.postAppMsg("wx_eysbTlWuT45GJHmBxInqs","52843526447@chatroom", XmlUtil.mapToXmlStr(map, "appmsg"));

    }

}
