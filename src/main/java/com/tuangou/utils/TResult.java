package com.tuangou.utils;

/**
 * Created by LiJia on 2017/11/2.
 */
public class TResult {

    private boolean error=false;
    private String message;
    private Object data;

    public TResult(boolean error, String message, Object data) {
        this.error = error;
        this.message = message;
        this.data = data;
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
