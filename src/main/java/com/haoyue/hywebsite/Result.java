package com.haoyue.hywebsite;

/**
 * Created by LiJia on 2018/1/12.
 *
 * 皓月官网 封装返回信息实体对象
 *
 *
 */
public class Result {

    private boolean error=false;
    private String message;
    private Object data;

    public Result(boolean error, String message, Object data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean getError() {
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
