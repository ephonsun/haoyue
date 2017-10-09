package com.haoyue.websocket;

/**
 * Created by LiJia on 2017/9/6.
 * 聊天室  测试
 */

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ChattingController {


    // /chatting的处理函数
    @MessageMapping("/chatting")
    //将结果发送到/chat/message
    @SendTo("/chat/message")
    //这里传进的参数是Message对象，对应的键是content，
    public Message chatting(Message message) throws Exception {
        //停1秒，让后台有时间去处理消息
        Thread.sleep(1000);
        //实例化 Message 对象
        Message message1=new Message();
        message1.setContent(message.getContent());
        message1.setDateTime(new Date().toLocaleString());
        message1.setFromer(message.getFromer());
        message1.setReceiver(message.getReceiver());
        //返回Message的json形式
        return message1;
    }

}