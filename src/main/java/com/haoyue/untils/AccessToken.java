package com.haoyue.untils;

/**
 * Created by Lijia on 2018/4/16.
 */
public class AccessToken {

    private String sellerId;
    private String token;
    private long timestamp;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
