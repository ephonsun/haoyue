package com.haoyue.websocket;
//
///**
// * Created by LiJia on 2017/9/29.
// *
// * http://www.chakd.com/index.php?action=search&start=北京&end=上海&weight=1&Submit=哈哈
// */
//
//import java.io.IOException;
//import java.util.concurrent.CopyOnWriteArraySet;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//
////该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
//@ServerEndpoint("/websocket")
//public class MyWebSocket {
//    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
//    private static int onlineCount = 0;
//
//    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
//    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
//
//    //与某个客户端的连接会话，需要通过它来给客户端发送数据
//    private Session session;
//
//    /**
//     * 连接建立成功调用的方法
//     *
//     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        this.session = session;
//        webSocketSet.add(this);     //加入set中
//        addOnlineCount();           //在线数加1
//        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        webSocketSet.remove(this);  //从set中删除
//        subOnlineCount();           //在线数减1
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     * @param session 可选的参数
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("来自客户端的消息:" + message);
//
//        //群发消息
//        for (MyWebSocket item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
//    }
//
//    /**
//     * 发生错误时调用
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        System.out.println("发生错误");
//        error.printStackTrace();
//    }
//
//    /**
//     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
//     *
//     * @param message
//     * @throws IOException
//     */
//    public void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
//        //this.session.getAsyncRemote().sendText(message);
//    }
//
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount() {
//        MyWebSocket.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount() {
//        MyWebSocket.onlineCount--;
//    }
//}
//

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket", configurator = SpringConfigurator.class)
public class MyWebSocket {
    private static int onlineCount = 0;

    public MyWebSocket() {

    }

    @Autowired
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //private static Logger logger = Logger.getLogger(MyWebSocket.class);
    private Session session;

    /**
     * 链接建立成功调用的方法
     *
     *  @param session 可选的参数，session为与某个客户端连接的会话，需要通过它来给客户发送数据
     */
    @OnOpen
    public void onOpen(Session session, String message) {
        this.session = session;
//        session.getAttributes();

        //JSONObject s = new JSONObject(session);
        String str = session.getQueryString();
//        String[] strs = str.split("&");
//        int slen = strs.length;
//        for (String quey : strs) {
//            String[] q = quey.split("=");
//            System.out.println("sing:" + q[0] + " value:" + q[1]);
//
//        }

        System.out.println("session 中的数据："+str);

        webSocketSet.add(this);//加入set中
        addOnlineCount();//在线人数加1
        System.out.println("有新链接加入！当前在线人数为：" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);//从set中删除
        subOnlineCount();
        System.out.println("有一个连接关闭！当前在线人数为：" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息：" + message);
        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                if (item.session == session) {
//                    item.sendMessage("<p><span style='color:#ff0000;'>我说："+message+"</span>  <span style='color:#bbbaba;'>当前在线人数："+getOnlineCount()+"</span></p>");
                    item.sendMessage(message);
                } else {
                    item.sendMessage("<p>" + message + "  当前在线人数：" + getOnlineCount() + "</p>");
                }

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样，没有用注解，根据自己需要添加的方法
     *
     * @param message
     */
    private void sendMessage(String message) throws IOException {
        //保存到数据库
//        Content content = new Content();
//        content.setContent(message);
//        content.setCreateDate(StringUtilZ.getTimestampOfNow("yyyy-MM-dd HH:mm:ss"));


        this.session.getBasicRemote().sendText(message);

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
