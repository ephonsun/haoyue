
 package com.haoyue.websocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
//
// //聊天室  测试
//@Configuration
//@EnableWebSocketMessageBroker
////通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
//public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) { //endPoint 注册协议节点,并映射指定的URl
//
//        //注册一个名字为"endpointChat" 的endpoint,并指定 SockJS协议。   点对点-用
//        registry.addEndpoint("/websocket").addInterceptors(new HandshakeInterceptor[]{}).setAllowedOrigins("Access-Control-Allow-Origin","*").withSockJS();
//
//    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(message broker)
//        //namespace 返回数据时加/chat
//        registry.enableSimpleBroker("/chat");
//        //namespace 经过controller的方法的路径都要加/app
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//}
