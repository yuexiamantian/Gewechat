package api.message;

import lombok.Getter;

@Getter
public enum MsgTypeEnum {

    TEXT(1, "文本消息"),
    IMAGE(3, "图片消息"),
    VOICE(34, "语音消息"),
    VIDEO(43, "视频消息"),
    EMOTION(47, "表情消息"),
    //引用消息 判断此类消息的逻辑：$.Data.MsgType=49 并且 解析$.Data.Content.string中的xml msg.appmsg.type=57
    //$.Data.MsgType=49 并且 解析$.Data.Content.string中的xml msg.appmsg.type=2001
    //更多消息结构详见 https://apifox.com/apidoc/shared-69ba62ca-cb7d-437e-85e4-6f3d3df271b1/doc-4801171
    COMPON(49, "组合消息"),
    REFER(57, "引用消息"),
    SYSTEM(10002, "系统消息");
    private final Integer code;
    private final String desc;

    MsgTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MsgTypeEnum from(Integer code) {
        for (MsgTypeEnum msgTypeEnum : MsgTypeEnum.values()) {
            if (msgTypeEnum.getCode().equals(code)) {
                return msgTypeEnum;
            }
        }
        return null;
    }
}
