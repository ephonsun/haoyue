//package com.haoyue.sokcet;
//
///**
// * Created by LiJia on 2017/10/16.
// */
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
///**
// * 此类用来获取登录用户信息并交由websocket管理
// */
//public class MyWebSocketInterceptor implements HandshakeInterceptor {
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse arg1, WebSocketHandler arg2,
//                                   Map<String, Object> arg3) throws Exception {
//        // 将ServerHttpRequest转换成request请求相关的类，用来获取request域中的用户信息
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest httpRequest = servletRequest.getServletRequest();
//            Map<String,String[]> map=httpRequest.getParameterMap();
//            for(String key:arg3.keySet()){
//                System.out.println("key="+key);
//                System.out.println("value="+arg3.get(key));
//            }
//        }
//        System.out.println("连接到我了");
//
//        return true;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
//        // TODO Auto-generated method stub
//
//    }
//
//}
