package api.message;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信原始消息对象 gewechat_message
 *
 * @author Lion Li
 * @date 2024-11-08
 */
@Data
@TableName("gewechat_message")
public class GewechatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息类型名称AddMsg 新消息
     */
    private String typeName;

    /**
     * 设备ID
     */
    private String appid;

    /**
     * 所属微信的wxid
     */
    private String wxid;

    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 消息发送人的wxid wxid_phyyedw9xap22
     */
    private String fromUserName;

    /**
     * 消息接收人的wxid wxid_0xsqb3o0tsvz22
     */
    private String toUserName;

    /**
     * // 消息类型
     * // 1: 文本消息
     * // 3: 图片消息
     * // 34: 语音消息
     * // 43: 视频消息
     * // 47: 表情消息
     * // 49: 链接消息、小程序消息、转账消息
     * // 10002: 系统消息（如撤回消息、拍一拍、被移除群聊通知、解散群聊通知）
     */
    private Integer msgType;

    /**
     * 消息内容 123
     */
    private String content;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 图片状态 1
     */
    private Integer imgStatus;

    /**
     * 图片大小
     */
    private Long imgBufILen;

    /**
     * 缩略图的base64
     */
    private String imgBufBuffer;

    /**
     * 消息发送时间
     */
    private Date msgCreateTime;

    /**
     * 消息源
     */
    private String msgSource;

    /**
     * 消息推送内容
     */
    private String pushContent;

    /**
     * 新的消息ID
     */
    private Long newMsgId;

    /**
     * 消息序列号
     */
    private Long msgSeq;

    /**
     * 原始数据
     */
    private String data;

    /**
     * 记录创建时间
     */
    private Date createTime;


}
