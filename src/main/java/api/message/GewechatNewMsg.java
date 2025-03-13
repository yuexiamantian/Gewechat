package api.message;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.Objects;

@Data
public class GewechatNewMsg extends GewechatBaseMessage {

    // 消息ID 1040356095
    private Long MsgId;

    // 消息发送人的wxid wxid_phyyedw9xap22
    private String FromUserName;

    // 消息接收人的wxid wxid_0xsqb3o0tsvz22
    private String ToUserName;

    // 消息类型
    // 1: 文本消息
    // 3: 图片消息
    // 34: 语音消息
    // 43: 视频消息
    // 47: 表情消息
    // 49: 链接消息、小程序消息、转账消息
    // 10002: 系统消息（如撤回消息、拍一拍、被移除群聊通知、解散群聊通知）
    private Integer MsgType;

    // 消息内容 123
    private String Content;

    // 消息状态
    private Integer Status;

    // 图片状态 1
    private Integer ImgStatus;

    // 图片大小
    private Long ImgBufILen;

    //缩略图的base64
    private String ImgBufBuffer;

    // 消息发送时间(秒) 1705043418
    private Long CreateTime;

    // 消息源
    //<msgsource>\n\t<alnode>\n\t\t<fr>1</fr>\n\t</alnode>\n\t<signature>v1_volHXhv4</signature>\n\t<tmp_node>\n\t\t<publisher-id></publisher-id>\n\t</tmp_node>\n</msgsource>\n
    private String MsgSource;

    // 消息推送内容 朝夕。 : 123
    private String PushContent;

    // 新的消息ID 7773749793478223190
    private Long NewMsgId;

    // 消息序列号 640356095
    private Long MsgSeq;

    public static GewechatNewMsg from(JSONObject json) {
        GewechatNewMsg gegechatNewMsg = new GewechatNewMsg();
        gegechatNewMsg.setTypeName(json.getString("TypeName"));
        gegechatNewMsg.setAppid(json.getString("Appid"));
        gegechatNewMsg.setWxid(json.getString("Wxid"));
        JSONObject data = json.getJSONObject("Data");
        gegechatNewMsg.setData(data);
        gegechatNewMsg.setMsgId(data.getLong("MsgId"));
        gegechatNewMsg.setFromUserName(Objects.nonNull(data.getJSONObject("FromUserName")) ? data.getJSONObject("FromUserName").getString("string") : null);
        gegechatNewMsg.setToUserName(Objects.nonNull(data.getJSONObject("ToUserName")) ? data.getJSONObject("ToUserName").getString("string") : null);
        gegechatNewMsg.setMsgType(data.getInteger("MsgType"));
        gegechatNewMsg.setContent(Objects.nonNull(data.getJSONObject("Content")) ? data.getJSONObject("Content").getString("string") : null);
        gegechatNewMsg.setStatus(data.getInteger("Status"));
        gegechatNewMsg.setImgStatus(data.getInteger("ImgStatus"));
        gegechatNewMsg.setImgBufILen(Objects.nonNull(data.getJSONObject("ImgBuf")) ? data.getJSONObject("ImgBuf").getLong("iLen") : null);
        gegechatNewMsg.setImgBufBuffer(Objects.nonNull(data.getJSONObject("ImgBuf")) ? data.getJSONObject("ImgBuf").getString("buffer") : null);
        gegechatNewMsg.setCreateTime(data.getLong("CreateTime"));
        gegechatNewMsg.setMsgSource(data.getString("MsgSource"));
        gegechatNewMsg.setPushContent(data.getString("PushContent"));
        gegechatNewMsg.setNewMsgId(data.getLong("NewMsgId"));
        gegechatNewMsg.setMsgSeq(data.getLong("MsgSeq"));
        return gegechatNewMsg;
    }

    public GewechatMessage toPO() {
        GewechatMessage gewechatMessage = new GewechatMessage();
        gewechatMessage.setTypeName(this.getTypeName());
        gewechatMessage.setAppid(this.getAppid());
        gewechatMessage.setWxid(this.getWxid());
        gewechatMessage.setMsgId(this.getMsgId());
        gewechatMessage.setFromUserName(this.getFromUserName());
        gewechatMessage.setToUserName(this.getToUserName());
        gewechatMessage.setMsgType(this.getMsgType());
        gewechatMessage.setContent(this.getContent());
        gewechatMessage.setStatus(this.getStatus());
        gewechatMessage.setImgStatus(this.getImgStatus());
        gewechatMessage.setImgBufILen(this.getImgBufILen());
        gewechatMessage.setImgBufBuffer(this.getImgBufBuffer());
        gewechatMessage.setMsgCreateTime(DateUtil.date(this.getCreateTime() * 1000));
        gewechatMessage.setMsgSource(this.getMsgSource());
        gewechatMessage.setPushContent(this.getPushContent());
        gewechatMessage.setNewMsgId(this.getNewMsgId());
        gewechatMessage.setMsgSeq(this.getMsgSeq());
        gewechatMessage.setContent(this.getContent());
        gewechatMessage.setData(JSONObject.toJSONString(this));
        return gewechatMessage;
    }
}
