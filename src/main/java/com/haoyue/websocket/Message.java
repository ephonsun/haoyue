package com.haoyue.websocket;

/**
 * Created by LiJia on 2017/9/6.
 * 聊天信息实体
 */
public class Message {

    private String content;
    private String fromer;
    private String receiver;
    private String dateTime;

    public Message(String content) {
        this.content = content;
    }

    public Message(String content, String fromer, String receiver, String dateTime) {
        this.content = content;
        this.fromer = fromer;
        this.receiver = receiver;
        this.dateTime = dateTime;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromer() {
        return fromer;
    }

    public void setFromer(String fromer) {
        this.fromer = fromer;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
