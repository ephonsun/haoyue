//package com.haoyue.sokcet;
//
//import com.aliyun.oss.common.comm.ResponseMessage;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
///**
// * Created by LiJia on 2017/10/18.
// */
//@Controller
//public class WsController {
//
//    @MessageMapping("/welcome")
//    @SendTo("/topic/getResponse")
//    public String say(String message) {
//        System.out.println(message);
//        return "welcome," + message + " !";
//    }
//
//}