package api.controller;

import com.alibaba.fastjson2.JSONObject;
import api.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gewechat/message")
public class GeWechatMessageController {
    @Resource
    MessageService messageService;


    /**
     * 消息回调
     */
    @PostMapping("/callback")
    public Boolean callback(@RequestBody JSONObject message) {
        messageService.callBack(message);
        return true;
    }
}



