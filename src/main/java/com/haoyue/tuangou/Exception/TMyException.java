package com.haoyue.tuangou.Exception;

/**
 * Created by LiJia on 2017/8/25.
 */
public class TMyException extends Exception {

    private Integer errcode;

    public TMyException(String message) {
        super(message);
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public TMyException(String message, Throwable cause, Integer errcode) {
        super(message, cause);
        this.errcode = errcode;
    }
}
