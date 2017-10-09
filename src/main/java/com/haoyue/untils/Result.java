package com.haoyue.untils;

/**
 * Created by LiJia on 2017/8/21.
 * 返回结果集封装
 */

public class Result {
    private boolean error=false;
    //信息提示
    private String message;
    //数据集
    private Object data;
    //token
    private String token;

    public Result(boolean error, String message, Object data,String token) {
        this.message = message;
        this.data = data;
        this.error=error;
        this.token=token;
    }

    public Result(boolean error, String message,String token) {
        this.error=error;
        this.message = message;
        this.token=token;
    }
    public Result(Object data,String token) {
        this.token=token;
        this.data = data;
    }
    public Result(Object data) {
        this.data = data;
    }

    public Result(String message,boolean error) {
        this.error=error;
        this.message=message;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
